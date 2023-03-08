package io.apicurio.tenantmanager.logging.audit;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import io.apicurio.common.apps.logging.audit.AuditMetaDataExtractor;
import io.apicurio.tenantmanager.api.datamodel.NewApicurioTenantRequest;

@ApplicationScoped
public class NewApicurioTenantRequestExtractor implements AuditMetaDataExtractor {

    @Override
    public boolean accept(Object parameterValue) {
        return parameterValue != null && parameterValue instanceof NewApicurioTenantRequest;
    }

    @Override
    public void extractMetaDataInto(Object parameterValue, Map<String, String> metaData) {
        NewApicurioTenantRequest tenant = (NewApicurioTenantRequest) parameterValue;
        metaData.put("tenantId", tenant.getTenantId());
        metaData.put("orgId", tenant.getOrganizationId());
        metaData.put("name", tenant.getName());
        metaData.put("createdBy", tenant.getCreatedBy());
    }

}
