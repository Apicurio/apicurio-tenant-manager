/*
 * Copyright 2021 Red Hat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apicurio.tenantmanager;

import io.apicurio.tenantmanager.api.datamodel.TenantStatusValue;
import io.apicurio.tenantmanager.storage.ApicurioTenantStorage;
import io.apicurio.tenantmanager.storage.dto.ApicurioTenantDto;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;

@ApplicationScoped
public class TenantReaper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    ApicurioTenantStorage storage;

    @ConfigProperty(name = "tenant-manager.tenant-reaper.max-tenants-reaped.count", defaultValue = "1000")
    int maxTenantsReaped;

    @ConfigProperty(name = "tenant-manager.tenant-reaper.period.seconds", defaultValue = "10800") // 3 * 60 * 60 = 3 hours
    int reaperPeriodSeconds;

    private Duration reaperPeriod;

    private Instant next;

    private Duration getReaperPeriod() {
        if (reaperPeriod == null) {
            reaperPeriod = Duration.ofSeconds(reaperPeriodSeconds);
        }
        return reaperPeriod;
    }

    @PostConstruct
    void init() {
        var stagger = Duration.ZERO;
        // Only stagger if the reaper period is at least 1 minute (testing support).
        if (getReaperPeriod().compareTo(Duration.ofMinutes(1)) >= 0) {
            // Start with a random stagger, 1-30 minutes, inclusive.
            stagger = Duration.ofMinutes(new Random().nextInt(30) + 1L); // TODO Reuse RNG in multiple places
            log.info("Staggering tenant manager reaper job by {}", stagger);
        }
        next = Instant.now().plus(stagger);
    }

    @Scheduled(concurrentExecution = SKIP, every = "{tenant-manager.tenant-reaper.check-period.duration-expr}")
    void run() {
        final var now = Instant.now();
        if (now.isAfter(next)) {
            try {
                log.info("Running tenant manager reaper job at {}", now);
                reap();
                log.info("Tenant manager reaper job finished successfully");
            } catch (Exception ex) {
                log.error("Exception thrown when running tenant manager reaper job", ex);
            } finally {
                next = now.plus(getReaperPeriod());
                log.info("Tenant manager reaper job finished in {}", Duration.between(Instant.now(), now));
                log.info("Running next tenant reaper job at around {}", next);
            }
        }
    }

    private synchronized void reap() {
        var deletedCount = 0;
        var processedCount = 0; // This also counts failed deletions
        List<ApicurioTenantDto> page;
        do {
            log.info("Getting a page of tenants to delete");
            page = storage.getTenantsByStatus(TenantStatusValue.DELETED, 10);
            log.info("A page of tenants to delete: {}", page);
            for (ApicurioTenantDto tenant : page) {
                try {
                    log.info("Deleting tenant {}", tenant.getTenantId());
                    storage.delete(tenant.getTenantId());
                    deletedCount++;
                } catch (Exception ex) {
                    log.error("Tenant manager reaper could not delete tenant " + tenant, ex);
                }
            }
            processedCount += page.size();
        } while (!page.isEmpty() && processedCount < maxTenantsReaped);
        log.info("Tenant manager reaper deleted {} tenants marked with DELETED status.", deletedCount);
    }
}
