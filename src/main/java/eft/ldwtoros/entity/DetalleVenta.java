
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

    private String tipoProducto;
    private int sku;
    private String zona;
    private String seccion;
    private String fila;
    private String asiento;
    private String nombreComprador;
    private String direccion;
    private BigDecimal precioSinIva;
    private BigDecimal precioConIva;
    private BigDecimal descuentoAplicado;
    private BigDecimal porcentajeDescuento;

    // Getters y setters
    public Venta getVenta() {
		return venta;
	}
	public void setVenta(Venta venta) {
		this.venta = venta;
	}
	public String getTipoProducto() {
		return tipoProducto;
	}
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	public int getSku() {
		return sku;
	}
	public void setSku(int sku) {
		this.sku = sku;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public String getSeccion() {
		return seccion;
	}
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	public String getFila() {
		return fila;
	}
	public void setFila(String fila) {
		this.fila = fila;
	}
	public String getAsiento() {
		return asiento;
	}
	public void setAsiento(String asiento) {
		this.asiento = asiento;
	}
	public String getNombreComprador() {
		return nombreComprador;
	}
	public void setNombreComprador(String nombreComprador) {
		this.nombreComprador = nombreComprador;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public BigDecimal getPrecioSinIva() {
		return precioSinIva;
	}
	public void setPrecioSinIva(BigDecimal precioSinIva) {
		this.precioSinIva = precioSinIva;
	}
	public BigDecimal getPrecioConIva() {
		return precioConIva;
	}
	public void setPrecioConIva(BigDecimal precioConIva) {
		this.precioConIva = precioConIva;
	}
	public BigDecimal getDescuentoAplicado() {
		return descuentoAplicado;
	}
	public void setDescuentoAplicado(BigDecimal descuentoAplicado) {
		this.descuentoAplicado = descuentoAplicado;
	}
	public BigDecimal getPorcentajeDescuento() {
		return porcentajeDescuento;
	}
	public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}
	public Long getId() {
		return id;
	}
}
