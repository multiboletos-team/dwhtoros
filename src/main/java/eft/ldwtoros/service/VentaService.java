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
import java.util.List;

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
        venta.setItemsTotal(dto.getItemsTotal());
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
            detalle.setTypeProduct(producto.getTypeProduct());
            detalle.setPriceExTax(producto.getPriceExTax());
            detalle.setPriceIncTax(producto.getPriceIncTax());

            if (!producto.getAppliedDiscounts().isEmpty()) {
                var descuento = producto.getAppliedDiscounts().get(0);
                detalle.setDiscountAplied(BigDecimal.valueOf(descuento.getAmount()));
                detalle.setPorcentDiscount(BigDecimal.valueOf(descuento.getPorcent()));
            }

            var desc = producto.getDescription();
            detalle.setZone(desc.getZona());
            detalle.setSecction(desc.getSeccion());
            detalle.setRow(desc.getFila());
            detalle.setSeat(desc.getAsiento());

            var info = producto.getDetails();
            detalle.setNameClient(info.getName());
            detalle.setAddress(info.getSeasonPurchaseAddress());

            detalle.setVenta(venta);
            detalleVentaRepository.save(detalle);
        }
    }
    

    @Transactional
    public void cancelarVentaTotalPorOrderId(Long orderId) {
        Venta venta = ventaRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada para orderId: " + orderId));

        // Marcar venta cancelada
        venta.setCancel(true);
        ventaRepository.save(venta);

        // Marcar todos los detalles cancelados
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaOrderId(orderId);
        for (DetalleVenta d : detalles) {
            d.setCancel(true);
        }
        detalleVentaRepository.saveAll(detalles);
    }

    @Transactional
    public boolean cancelarItemPorOrderYAsiento(Long orderId, String secction, String row, String seat) {
        Venta venta = ventaRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada para orderId: " + orderId));

        DetalleVenta detalle = detalleVentaRepository
                .findFirstByVentaOrderIdAndSecctionAndRowAndSeat(orderId, secction, row, seat)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el asiento (" + secction + ", " + row + ", " + seat + ") en la orden: " + orderId));

        if (!detalle.isCancel()) {
            detalle.setCancel(true);
            detalleVentaRepository.save(detalle);
        }

        // Si TODOS los detalles ya están cancelados, marca la venta como cancelada
        boolean todosCancelados = detalleVentaRepository.findByVentaOrderId(orderId)
                .stream().allMatch(DetalleVenta::isCancel);

        if (todosCancelados && !venta.isCancel()) {
            venta.setCancel(true);
            ventaRepository.save(venta);
        }

        return true;
    }
}
