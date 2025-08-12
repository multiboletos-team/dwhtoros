package eft.ldwtoros.dto;

//DTO para request de cancelación parcial
public class CancelarItemRequest {
	 private Long orderId;
	 private String secction;
	 private String row;
	 private String seat;
	
	 public Long getOrderId() { return orderId; }
	 public void setOrderId(Long orderId) { this.orderId = orderId; }
	 public String getSecction() { return secction; }
	 public void setSecction(String secction) { this.secction = secction; }
	 public String getRow() { return row; }
	 public void setRow(String row) { this.row = row; }
	 public String getSeat() { return seat; }
	 public void setSeat(String seat) { this.seat = seat; }
}