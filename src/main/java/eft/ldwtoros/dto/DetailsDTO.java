
package eft.ldwtoros.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetailsDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("season_purchase_address")
    private String seasonPurchaseAddress;

    public String getName() { return name; }
    public String getSeasonPurchaseAddress() { return seasonPurchaseAddress; }
}
