package eft.ldwtoros.dto;

import java.math.BigDecimal;

//DTO para request de cancelación parcial
public class CancelarItemRequest {
	 private Long orderId;
	 private String secction;
	 private String row;
	 private Long seat;
	 
	    // NUEVOS (opcionales)
	    private BigDecimal totalExTax;
	    private BigDecimal totalIncTax;
	
	 public Long getOrderId() { return orderId; }
	 public void setOrderId(Long orderId) { this.orderId = orderId; }
	 public String getSecction() { return secction; }
	 public void setSecction(String secction) { this.secction = secction; }
	 public String getRow() { return row; }
	 public void setRow(String row) { this.row = row; }
	 public Long getSeat() { return seat; }
	 public void setSeat(Long seat) { this.seat = seat; }
	 public BigDecimal getTotalExTax() { return totalExTax; }
	 public void setTotalExTax(BigDecimal totalExTax) { this.totalExTax = totalExTax; }
	 public BigDecimal getTotalIncTax() { return totalIncTax; }
	 public void setTotalIncTax(BigDecimal totalIncTax) { this.totalIncTax = totalIncTax; }
}