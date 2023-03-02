package io.apicurio.tenantmanager.logging.audit;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import io.apicurio.common.apps.logging.audit.AuditMetaDataExtractor;
import io.apicurio.tenantmanager.api.datamodel.UpdateApicurioTenantRequest;

@ApplicationScoped
public class UpdateApicurioTenantRequestExtractor implements AuditMetaDataExtractor {

    @Override
    public boolean accept(Object parameterValue) {
        return parameterValue != null && parameterValue instanceof UpdateApicurioTenantRequest;
    }

    @Override
    public void extractMetaDataInto(Object parameterValue, Map<String, String> metaData) {
        UpdateApicurioTenantRequest tenant = (UpdateApicurioTenantRequest) parameterValue;
        if (tenant.getStatus() != null) {
            metaData.put("tenantStatus", tenant.getStatus().value());
        }
        if (tenant.getName() != null) {
            metaData.put("name", tenant.getName());
        }
    }

}
