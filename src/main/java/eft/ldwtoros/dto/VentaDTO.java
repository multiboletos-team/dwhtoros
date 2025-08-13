package eft.ldwtoros.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaDTO {

	private Long id;
	private Long orderId;
    private Integer eventId;
    private Integer itemsTotal;
    private String eventDescription;
    private String seriesGame;
    private BigDecimal totalExTax;
    private BigDecimal totalIncTax;
    private BigDecimal totalDiscount;
    private String paymentMethod;
    private Boolean paymentCompleted;
    private String currencyCode;
    private String orderSource;
    private BigDecimal storeCreditAmount;
    private LocalDateTime fechaVenta;
    private String emailCliente;
    private String telefonoCliente;
    private Boolean cancel;

    public VentaDTO() {}

    public VentaDTO(
            Long id, Long orderId, Integer eventId, Integer itemsTotal,
            String eventDescription, String seriesGame,
            BigDecimal totalExTax, BigDecimal totalIncTax, BigDecimal totalDiscount,
            String paymentMethod, Boolean paymentCompleted,
            String currencyCode, String orderSource, BigDecimal storeCreditAmount,
            LocalDateTime fechaVenta, String emailCliente, String telefonoCliente,
            Boolean cancel
    ) {
        this.id = id;
        this.orderId = orderId;
        this.eventId = eventId;
        this.itemsTotal = itemsTotal;
        this.eventDescription = eventDescription;
        this.seriesGame = seriesGame;
        this.totalExTax = totalExTax;
        this.totalIncTax = totalIncTax;
        this.totalDiscount = totalDiscount;
        this.paymentMethod = paymentMethod;
        this.paymentCompleted = paymentCompleted;
        this.currencyCode = currencyCode;
        this.orderSource = orderSource;
        this.storeCreditAmount = storeCreditAmount;
        this.fechaVenta = fechaVenta;
        this.emailCliente = emailCliente;
        this.telefonoCliente = telefonoCliente;
        this.cancel = cancel;
    }

    // getters y setters...
    
    public void setId(Long id) {
		this.id = id;
	}
    
    public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public Integer getItemsTotal() {
		return itemsTotal;
	}

	public void setItemsTotal(Integer itemsTotal) {
		this.itemsTotal = itemsTotal;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getSeriesGame() {
		return seriesGame;
	}

	public void setSeriesGame(String seriesGame) {
		this.seriesGame = seriesGame;
	}

	public BigDecimal getTotalExTax() {
		return totalExTax;
	}

	public void setTotalExTax(BigDecimal totalExTax) {
		this.totalExTax = totalExTax;
	}

	public BigDecimal getTotalIncTax() {
		return totalIncTax;
	}

	public void setTotalIncTax(BigDecimal totalIncTax) {
		this.totalIncTax = totalIncTax;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Boolean getPaymentCompleted() {
		return paymentCompleted;
	}

	public void setPaymentCompleted(Boolean paymentCompleted) {
		this.paymentCompleted = paymentCompleted;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public BigDecimal getStoreCreditAmount() {
		return storeCreditAmount;
	}

	public void setStoreCreditAmount(BigDecimal storeCreditAmount) {
		this.storeCreditAmount = storeCreditAmount;
	}

	public LocalDateTime getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(LocalDateTime fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	public String getEmailCliente() {
		return emailCliente;
	}

	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	public String getTelefonoCliente() {
		return telefonoCliente;
	}

	public void setTelefonoCliente(String telefonoCliente) {
		this.telefonoCliente = telefonoCliente;
	}

	public Boolean getCancel() {
		return cancel;
	}

	public void setCancel(Boolean cancel) {
		this.cancel = cancel;
	}

	public Long getId() {
		return id;
	}
}
