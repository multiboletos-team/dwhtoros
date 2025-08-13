
package eft.ldwtoros.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    private String typeProduct;
	private Long sku;
    private String zone;
	private String secction;
	private String row;
	private Long seat;
	private String nameClient;
	private String address;
	private BigDecimal priceExTax;
    private BigDecimal priceIncTax;
    private BigDecimal discountAplied;
	private BigDecimal porcentDiscount;
	private boolean cancel = false;

	// Getters y setters
    public Venta getVenta() {
		return venta;
	}
	public void setVenta(Venta venta) {
		this.venta = venta;
	}
    public String getTypeProduct() {
		return typeProduct;
	}
	public void setTypeProduct(String typeProduct) {
		this.typeProduct = typeProduct;
	}
	public Long getSku() {
		return sku;
	}
	public void setSku(Long sku) {
		this.sku = sku;
	}
    public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
    public String getSecction() {
		return secction;
	}
	public void setSecction(String secction) {
		this.secction = secction;
	}
    public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
    public Long getSeat() {
		return seat;
	}
	public void setSeat(Long seat) {
		this.seat = seat;
	}
    public String getNameClient() {
		return nameClient;
	}
	public void setNameClient(String nameClient) {
		this.nameClient = nameClient;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BigDecimal getPriceExTax() {
		return priceExTax;
	}
	public void setPriceExTax(BigDecimal priceExTax) {
		this.priceExTax = priceExTax;
	}
	public BigDecimal getPriceIncTax() {
		return priceIncTax;
	}
	public void setPriceIncTax(BigDecimal priceIncTax) {
		this.priceIncTax = priceIncTax;
	}
    public BigDecimal getDiscountAplied() {
		return discountAplied;
	}
	public void setDiscountAplied(BigDecimal discountAplied) {
		this.discountAplied = discountAplied;
	}
    public BigDecimal getPorcentDiscount() {
		return porcentDiscount;
	}
	public void setPorcentDiscount(BigDecimal porcentDiscount) {
		this.porcentDiscount = porcentDiscount;
	}
	public boolean isCancel() {
		return cancel;
	}
	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
    public Long getId() {
		return id;
	}
}
