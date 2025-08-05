
package eft.ldwtoros.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public class VentaRequestDTO {

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("event_id")
    private int eventId;

    @JsonProperty("event_description")
    private String eventDescription;
    
    @JsonProperty("items_total")
    private int itemsTotal;

	@JsonProperty("series_game")
    private String seriesGame;

    @JsonProperty("total_ex_tax")
    private BigDecimal totalExTax;

    @JsonProperty("total_inc_tax")
    private BigDecimal totalIncTax;

    @JsonProperty("total_discount")
    private BigDecimal totalDiscount;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("payment_completed")
    private boolean paymentCompleted;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("date_created")
    private String fechaVenta;

    @JsonProperty("order_source")
    private String orderSource;

    @JsonProperty("store_credit_amount")
    private BigDecimal storeCreditAmount;

    @JsonProperty("customer")
    private CustomerDTO customer;

    @JsonProperty("products")
    private List<ProductoDTO> products;

    // Getters y setters omitidos por brevedad

    public static class CustomerDTO {

		@JsonProperty("customer_email")
        private String customerEmail;

        @JsonProperty("customer_phone")
        private String customerPhone;

        // Getters y setters
        public String getCustomerEmail() {
			return customerEmail;
		}

		public void setCustomerEmail(String customerEmail) {
			this.customerEmail = customerEmail;
		}

		public String getCustomerPhone() {
			return customerPhone;
		}

		public void setCustomerPhone(String customerPhone) {
			this.customerPhone = customerPhone;
		}
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public List<ProductoDTO> getProducts() {
        return products;
    }

    public int getOrderId() { return orderId; }
    public int getEventId() { return eventId; }
    public String getEventDescription() { return eventDescription; }
    public String getSeriesGame() { return seriesGame; }
    public BigDecimal getTotalExTax() { return totalExTax; }
    public BigDecimal getTotalIncTax() { return totalIncTax; }
    public BigDecimal getTotalDiscount() { return totalDiscount; }
    public String getPaymentMethod() { return paymentMethod; }
    public boolean isPaymentCompleted() { return paymentCompleted; }
    public String getCurrencyCode() { return currencyCode; }
    public String getFechaVenta() { return fechaVenta; }
    public String getOrderSource() { return orderSource; }
    public BigDecimal getStoreCreditAmount() { return storeCreditAmount; }
    public int getItemsTotal() { return itemsTotal; }
    public void setItemsTotal(int itemsTotal) { this.itemsTotal = itemsTotal; }
}
