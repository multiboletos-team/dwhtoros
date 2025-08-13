
package eft.ldwtoros.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public class ProductoDTO {

    @JsonProperty("sku")
    private Long sku;

    @JsonProperty("type_product")
    private String typeProduct;

    @JsonProperty("price_ex_tax")
    private BigDecimal priceExTax;

    @JsonProperty("price_inc_tax")
    private BigDecimal priceIncTax;

    @JsonProperty("description")
    private DescriptionDTO description;

    @JsonProperty("details")
    private DetailsDTO details;

    @JsonProperty("applied_discounts")
    private List<DiscountDTO> appliedDiscounts;

    // Getters
    public Long getSku() { return sku; }
    public String getTypeProduct() { return typeProduct; }
    public BigDecimal getPriceExTax() { return priceExTax; }
    public BigDecimal getPriceIncTax() { return priceIncTax; }
    public DescriptionDTO getDescription() { return description; }
    public DetailsDTO getDetails() { return details; }
    public List<DiscountDTO> getAppliedDiscounts() { return appliedDiscounts; }
}
