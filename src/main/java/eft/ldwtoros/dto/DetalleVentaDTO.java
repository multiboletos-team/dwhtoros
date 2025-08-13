package eft.ldwtoros.dto;

import java.math.BigDecimal;

public class DetalleVentaDTO {

	private Long id;
	private Long ventaId;     // referencia, no el objeto Venta
    private Long sku;

	private String typeProduct;
    private String zone;
    private BigDecimal discountAplied;
    private BigDecimal priceExTax;
    private BigDecimal priceIncTax;

    private String secction;
    private String row;
    private Long seat;
    private Boolean cancel;

    public DetalleVentaDTO() {}

    public DetalleVentaDTO(Long id, Long ventaId, Long sku, String typeProduct,
    		BigDecimal priceExTax, BigDecimal priceIncTax, String secction, String row, Long seat, String zone, Boolean cancel, BigDecimal discountAplied) {
        this.id = id;
        this.ventaId = ventaId;
        this.sku = sku;
        this.typeProduct = typeProduct;
        this.priceExTax = priceExTax;
        this.priceIncTax = priceIncTax;
        this.secction = secction;
        this.row = row;
        this.seat = seat;
        this.zone = zone;
        this.cancel = cancel;
        this.discountAplied = discountAplied;
    }

    // getters y setters...
    public Long getVentaId() {
		return ventaId;
	}

	public void setVentaId(Long ventaId) {
		this.ventaId = ventaId;
	}

	public Long getSku() {
		return sku;
	}

	public void setSku(Long sku) {
		this.sku = sku;
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

	public Long getId() {
		return id;
	}
    public String getTypeProduct() {
		return typeProduct;
	}

	public void setTypeProduct(String typeProduct) {
		this.typeProduct = typeProduct;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public BigDecimal getDiscountAplied() {
		return discountAplied;
	}

	public void setDiscountAplied(BigDecimal discountAplied) {
		this.discountAplied = discountAplied;
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

	public Boolean getCancel() {
		return cancel;
	}

	public void setCancel(Boolean cancel) {
		this.cancel = cancel;
	}
	
    public void setId(Long id) {
		this.id = id;
	}
}