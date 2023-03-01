package io.apicurio.tenantmanager.api;

import io.apicurio.tenantmanager.api.datamodel.NewApicurioTenantRequest;
import io.apicurio.tenantmanager.api.datamodel.ApicurioTenant;
import io.apicurio.tenantmanager.api.datamodel.ApicurioTenantList;
import io.apicurio.tenantmanager.api.datamodel.SortBy;
import io.apicurio.tenantmanager.api.datamodel.SortOrder;
import io.apicurio.tenantmanager.api.datamodel.UpdateApicurioTenantRequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

/**
 * A JAX-RS interface.  An implementation of this interface must be provided.
 */
@Path("/api/v1/tenants")
public interface TenantsResource {
  /**
   * Gets the details of a single instance of a `Tenant`.
   */
  @Path("/{tenantId}")
  @GET
  @Produces("application/json")
  ApicurioTenant getTenant(@PathParam("tenantId") String tenantId);

  /**
   * Updates the name, description, and resources for a tenant.
   */
  @Path("/{tenantId}")
  @PUT
  @Consumes("application/json")
  void updateTenant(@PathParam("tenantId") String tenantId, UpdateApicurioTenantRequest data);

  /**
   * Marks an existing `Tenant` to be deleted.
   */
  @Path("/{tenantId}")
  @DELETE
  void deleteTenant(@PathParam("tenantId") String tenantId);

  /**
   * Gets a list of `ApicurioTenant` entities according to the query parameters set.
   */
  @GET
  @Produces("application/json")
  ApicurioTenantList getTenants(@QueryParam("status") String status,
      @QueryParam("offset") @Min(0) Integer offset, @QueryParam("limit") @Min(1) @Max(500) Integer limit,
      @QueryParam("order") SortOrder order, @QueryParam("orderby") SortBy orderby);

  /**
   * Creates a new instance of a `Tenant`.
   */
  @POST
  @Produces("application/json")
  @Consumes("application/json")
  Response createTenant(NewApicurioTenantRequest data);
}
