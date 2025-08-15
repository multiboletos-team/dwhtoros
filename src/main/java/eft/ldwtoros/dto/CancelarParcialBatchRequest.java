package eft.ldwtoros.dto;

import java.math.BigDecimal;
import java.util.List;

public class CancelarParcialBatchRequest {
    private Long orderId;
    private List<CancelarParcialItem> items;

    // opcionales por si quieres actualizar montos al final
    private BigDecimal totalExTax;
    private BigDecimal totalIncTax;
    private Integer itemsTotal;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public List<CancelarParcialItem> getItems() { return items; }
    public void setItems(List<CancelarParcialItem> items) { this.items = items; }

    public BigDecimal getTotalExTax() { return totalExTax; }
    public void setTotalExTax(BigDecimal totalExTax) { this.totalExTax = totalExTax; }

    public BigDecimal getTotalIncTax() { return totalIncTax; }
    public void setTotalIncTax(BigDecimal totalIncTax) { this.totalIncTax = totalIncTax; }

    public Integer getItemsTotal() { return itemsTotal; }
    public void setItemsTotal(Integer itemsTotal) { this.itemsTotal = itemsTotal; }
}
