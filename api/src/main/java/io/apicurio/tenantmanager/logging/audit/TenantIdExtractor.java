package io.apicurio.tenantmanager.logging.audit;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import io.apicurio.common.apps.logging.audit.AuditMetaDataExtractor;

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
