package io.apicurio.tenantmanager;

import io.quarkus.scheduler.Scheduled;
import io.sentry.Sentry;
import org.slf4j.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * TODO: Remove me, or even better, move me to io.apicurio.common.apps.logging.sentry.AbstractSentryConfiguration
 * <p>
 * Emits a Sentry event on startup, to ensure the GlitchTip integration is working.
 */
@ApplicationScoped
public class TemporaryOnReadySentryEventEmitter {

    @Inject
    Logger log;

    @Scheduled(every = "P1000D", delayed = "45s")
    void onReady() {
        var message = "Sentry enabled for Tenant Manager";
        log.info("Trying to send a Sentry event: {}", message);
        Sentry.capture(message);
    }
}
