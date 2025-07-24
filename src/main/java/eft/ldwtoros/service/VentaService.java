package eft.ldwtoros.service;

import eft.ldwtoros.dto.VentaRequestDTO;
import eft.ldwtoros.dto.ProductoDTO;
import eft.ldwtoros.entity.DetalleVenta;
import eft.ldwtoros.entity.Venta;
import eft.ldwtoros.repository.DetalleVentaRepository;
import eft.ldwtoros.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Transactional
    public void insertarVenta(VentaRequestDTO dto) {
        Venta venta = new Venta();
        venta.setOrderId(dto.getOrderId());
        venta.setEventId(dto.getEventId());
        venta.setEventDescription(dto.getEventDescription());
        venta.setSeriesGame(dto.getSeriesGame());
        venta.setTotalExTax(dto.getTotalExTax());
        venta.setTotalIncTax(dto.getTotalIncTax());
        venta.setTotalDiscount(dto.getTotalDiscount());
        venta.setPaymentMethod(dto.getPaymentMethod());
        venta.setPaymentCompleted(dto.isPaymentCompleted());
        venta.setCurrencyCode(dto.getCurrencyCode());
        venta.setOrderSource(dto.getOrderSource());
        venta.setStoreCreditAmount(dto.getStoreCreditAmount());

        // Parseo de fecha en formato: "2025-07-21 15:33:54"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        venta.setFechaVenta(LocalDateTime.parse(dto.getFechaVenta(), formatter));

        venta.setEmailCliente(dto.getCustomer().getCustomerEmail());
        venta.setTelefonoCliente(dto.getCustomer().getCustomerPhone());

        // Guardar venta primero (para obtener ID)
        venta = ventaRepository.save(venta);

        // Guardar detalles
        for (ProductoDTO producto : dto.getProducts()) {
            DetalleVenta detalle = new DetalleVenta();

            detalle.setSku(producto.getSku());
            detalle.setTipoProducto(producto.getTypeProduct());
            detalle.setPrecioSinIva(producto.getPriceExTax());
            detalle.setPrecioConIva(producto.getPriceIncTax());

            if (!producto.getAppliedDiscounts().isEmpty()) {
                var descuento = producto.getAppliedDiscounts().get(0);
                detalle.setDescuentoAplicado(BigDecimal.valueOf(descuento.getAmount()));
                detalle.setPorcentajeDescuento(BigDecimal.valueOf(descuento.getPorcent()));
            }

            var desc = producto.getDescription();
            detalle.setZona(desc.getZona());
            detalle.setSeccion(desc.getSeccion());
            detalle.setFila(desc.getFila());
            detalle.setAsiento(desc.getAsiento());

            var info = producto.getDetails();
            detalle.setNombreComprador(info.getName());
            detalle.setDireccion(info.getSeasonPurchaseAddress());

            detalle.setVenta(venta);
            detalleVentaRepository.save(detalle);
        }
    }
}
