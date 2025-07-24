
package eft.ldwtoros.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountDTO {

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("porcent")
    private double porcent;

    public double getAmount() { return amount; }
    public double getPorcent() { return porcent; }
}
