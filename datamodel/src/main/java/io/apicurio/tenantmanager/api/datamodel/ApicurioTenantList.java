
package io.apicurio.tenantmanager.api.datamodel;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Describes the response received when searching for tenants.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "items",
    "count"
})
@Generated("jsonschema2pojo")
public class ApicurioTenantList {

    public ApicurioTenantList() {}

    /**
     * The ApicurioTenants returned in the result set.
     * (Required)
     * 
     */
    @JsonProperty("items")
    @JsonPropertyDescription("The ApicurioTenants returned in the result set.")
    private List<ApicurioTenant> items = new ArrayList<ApicurioTenant>();
    /**
     * The total number of items that matched the query that produced the result set (may be 
     * more than the number of items in the result set).
     * (Required)
     * 
     */
    @JsonProperty("count")
    @JsonPropertyDescription("The total number of items that matched the query that produced the result set (may be \nmore than the number of items in the result set).")
    private Integer count;

    /**
     * The ApicurioTenants returned in the result set.
     * (Required)
     * 
     */
    @JsonProperty("items")
    public List<ApicurioTenant> getItems() {
        return items;
    }

    /**
     * The ApicurioTenants returned in the result set.
     * (Required)
     * 
     */
    @JsonProperty("items")
    public void setItems(List<ApicurioTenant> items) {
        this.items = items;
    }

    /**
     * The total number of items that matched the query that produced the result set (may be 
     * more than the number of items in the result set).
     * (Required)
     * 
     */
    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    /**
     * The total number of items that matched the query that produced the result set (may be 
     * more than the number of items in the result set).
     * (Required)
     * 
     */
    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

}
