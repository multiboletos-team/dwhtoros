
package eft.ldwtoros.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private int orderId;
    private int eventId;
    private String eventDescription;
    private String seriesGame;
    private BigDecimal totalExTax;
    private BigDecimal totalIncTax;
    private BigDecimal totalDiscount;
    private String paymentMethod;
    private boolean paymentCompleted;
    private String currencyCode;
    private String orderSource;
    private BigDecimal storeCreditAmount;
    private LocalDateTime fechaVenta;
    private String emailCliente;
    private String telefonoCliente;
    private boolean cancelada = false;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles;

    // Getters y setters
    public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
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

	public boolean isPaymentCompleted() {
		return paymentCompleted;
	}

	public void setPaymentCompleted(boolean paymentCompleted) {
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

	public boolean isCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}

	public List<DetalleVenta> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleVenta> detalles) {
		this.detalles = detalles;
	}

	public Long getId() {
		return id;
	}
}
