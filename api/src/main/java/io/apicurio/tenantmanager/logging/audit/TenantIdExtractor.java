package io.apicurio.tenantmanager.logging.audit;

import java.util.Map;


import io.apicurio.common.apps.logging.audit.AuditMetaDataExtractor;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TenantIdExtractor implements AuditMetaDataExtractor {

    @Override
    public boolean accept(Object parameterValue) {
        return parameterValue != null && parameterValue instanceof String;
    }

    @Override
    public void extractMetaDataInto(Object parameterValue, Map<String, String> metaData) {
        metaData.put("tenantId", (String) parameterValue);
    }

}
