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
package io.apicurio.tenantmanager.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.apicurio.tenantmanager.api.datamodel.TenantStatusValue;
import io.apicurio.tenantmanager.storage.dto.ApicurioTenantDto;
import io.apicurio.tenantmanager.storage.hibernate.ApicurioTenantPanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

/**
 * @author Fabian Martinez
 */
@ApplicationScoped
public class ApicurioTenantStorageImpl implements ApicurioTenantStorage {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    ApicurioTenantPanacheRepository repo;

    @Override
    @Transactional
    public void save(ApicurioTenantDto dto) {
        try {
            repo.persistAndFlush(dto);
        } catch (PersistenceException e) {
            logger.warn("Error saving tenant", e);
            if (e.getCause() instanceof ConstraintViolationException) {
                TenantAlreadyExistsException ex = TenantAlreadyExistsException.create(dto.getTenantId());
                ex.addSuppressed(e);
                throw ex;
            } else {
                throw e;
            }
        }
    }

    @Override
    public Optional<ApicurioTenantDto> findByTenantId(String tenantId) {
        return repo.find("tenantId", tenantId).singleResultOptional();
    }

    @Override
    @Transactional
    public void delete(String tenantId) {
        ApicurioTenantDto dto = findByTenantId(tenantId)
            .orElseThrow(() -> TenantNotFoundException.create(tenantId));
        repo.delete(dto);
    }

    @Override
    public List<ApicurioTenantDto> queryTenants(String query, Sort sort, Parameters parameters,
            Integer offset, Integer returnLimit) {
        PanacheQuery<ApicurioTenantDto> pq = null;
        if (query == null || query.isEmpty()) {
            pq = repo.findAll(sort);
        } else {
            pq = repo.find(query, sort, parameters);
        }
        return pq.range(offset, offset + (returnLimit - 1))
                .list();
    }

    @Override
    public long count(String query, Parameters parameters) {
        return repo.count(query, parameters);
    }

    /**
     * @see io.apicurio.tenantmanager.storage.ApicurioTenantStorage#getTenantsCountByStatus()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Long> getTenantsCountByStatus() {
        var res = new HashMap<String, Long>();
        List<Object[]> queryRes = this.repo.getEntityManager()
                .createQuery("select r.status, count(r) from ApicurioTenantDto r group by r.status")
                .getResultList();
        for (Object[] qr : queryRes) {
            if (qr.length != 2)
                throw new IllegalStateException("Unexpected number of columns in the result row: " + qr.length);
            res.put((String) qr[0], ((Number) qr[1]).longValue());
        }
        return res;
    }

    public List<ApicurioTenantDto> getTenantsByStatus(TenantStatusValue status, int limit) {
        return repo.find("status", Sort.ascending("createdOn"), status.value())
                .page(0, limit)
                .list();
    }

}
