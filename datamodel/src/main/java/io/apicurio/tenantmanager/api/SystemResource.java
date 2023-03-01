package io.apicurio.tenantmanager.api;

import io.apicurio.tenantmanager.api.datamodel.SystemInfo;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

/**
 * A JAX-RS interface.  An implementation of this interface must be provided.
 */
@Path("/api/v1/system")
public interface SystemResource {
  /**
   * This operation retrieves information about the running system, such as the version
   * of the software and when it was built.
   */
  @Path("/info")
  @GET
  @Produces("application/json")
  SystemInfo getSystemInfo();
}
