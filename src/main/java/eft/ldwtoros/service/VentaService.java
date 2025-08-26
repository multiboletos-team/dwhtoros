package eft.ldwtoros.service;

import eft.ldwtoros.dto.VentaRequestDTO;
import eft.ldwtoros.dto.CancelarParcialItem;
import eft.ldwtoros.dto.DetalleVentaDTO;
import eft.ldwtoros.dto.ProductoDTO;
import eft.ldwtoros.dto.VentaDTO;
import eft.ldwtoros.entity.DetalleVenta;
import eft.ldwtoros.entity.Venta;
import eft.ldwtoros.repository.DetalleVentaRepository;
import eft.ldwtoros.repository.VentaRepository;
import eft.ldwtoros.repository.spec.VentaSpecifications;
import jakarta.persistence.EntityManager;

import jakarta.persistence.PersistenceContext;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VentaService {
	
	@PersistenceContext
	private EntityManager em;


    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    public VentaService(VentaRepository ventaRepo, DetalleVentaRepository detalleRepo) {
        this.ventaRepository = ventaRepo;
        this.detalleVentaRepository = detalleRepo;
    }

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
    public boolean cancelarItemPorOrderYAsiento(Long orderId, String secction, String row, Long seat) {
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
    
    @Transactional
    public boolean revisarYMarcarVentaCancelada(Long orderId) {
        long vivos = detalleVentaRepository.countByVentaOrderIdAndCancelFalse(orderId);
        if (vivos == 0) {
            // no quedan detalles vivos → marca la venta como cancelada
            ventaRepository.marcarCanceladaPorOrderId(orderId);
            return true;
        }
        return false;
    }
    
    public List<VentaDTO> buscarVentas(Long orderId, LocalDate fechaDesde, LocalDate fechaHasta, Boolean cancel) {
        Specification<Venta> spec = Specification
                .where(VentaSpecifications.orderIdEq(orderId))
                .and(VentaSpecifications.fechaBetween(fechaDesde, fechaHasta)) // <-- optimizada
                // .and(VentaSpecifications.fechaBetweenDateCast(fechaDesde, fechaHasta)) // literal
                //.and(VentaSpecifications.fechaVentaEq(fechaVenta))
                .and(VentaSpecifications.cancelEq(cancel));

        return ventaRepository.findAll(spec,Sort.by(Direction.ASC, "id")).stream()
                .map(this::toVentaDTO)
                .toList();
    }

    public List<DetalleVentaDTO> listarDetallesPorOrderId(Long orderId) {
        return detalleVentaRepository.findByVentaOrderId(orderId).stream()
                .sorted(Comparator.comparing(DetalleVenta::getId))                 // ASC
                // .sorted(Comparator.comparing(DetalleVenta::getId).reversed())   // DESC
                .map(this::toDetalleDTO)
                .toList();
    }
    
    @Transactional
    public void actualizarTotalesVenta(Long orderId,
                                       BigDecimal totalExTax,
                                       BigDecimal totalIncTax,
                                       Integer itemsTotal) {
        Venta v = em.createQuery(
                "SELECT v FROM Venta v WHERE v.orderId = :orderId", Venta.class)
            .setParameter("orderId", orderId)
            .getSingleResult();

        v.setTotalExTax(totalExTax);
        v.setTotalIncTax(totalIncTax);
        if (itemsTotal != null) {
            v.setItemsTotal(itemsTotal);
        }
        em.merge(v);
    }

    /* (opcional) mantén el método anterior para compatibilidad */
    @Transactional
    public void actualizarTotalesVenta(Long orderId,
                                       BigDecimal totalExTax,
                                       BigDecimal totalIncTax) {
        actualizarTotalesVenta(orderId, totalExTax, totalIncTax, null);
    }
    
    @Transactional
    public CancelOutcome tryCancelarItem(Long orderId, String secction, String row, Long seat) {
        Optional<Venta> ventaOpt = ventaRepository.findByOrderId(orderId);
        if (ventaOpt.isEmpty()) {
            throw new IllegalArgumentException("Venta no encontrada para orderId: " + orderId);
        }

        var optDetalle = detalleVentaRepository
            .findFirstByVentaOrderIdAndSecctionAndRowAndSeat(orderId, secction, row, seat);

        if (optDetalle.isEmpty()) {
            return CancelOutcome.NOT_FOUND;
        }

        DetalleVenta d = optDetalle.get();
        if (d.isCancel()) {
            return CancelOutcome.ALREADY_CANCELLED;
        }

        d.setCancel(true);
        detalleVentaRepository.save(d);
        return CancelOutcome.CANCELLED;
    }

    /** Procesa un lote de asientos y devuelve mapa con resultados por item */
    @Deprecated
    @Transactional
    public Map<String, Object> cancelarParcialEnLoteOLD(Long orderId, List<eft.ldwtoros.dto.CancelarParcialItem> items) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> detalleResultados = new ArrayList<>();

        int cancelled = 0, already = 0, notFound = 0;

        for (var it : items) {
            Map<String, Object> r = new HashMap<>();
            r.put("secction", it.getSecction());
            r.put("row", it.getRow());
            r.put("seat", it.getSeat());

            try {
                CancelOutcome outcome = tryCancelarItem(orderId, it.getSecction(), it.getRow(), it.getSeat());
                r.put("status", outcome.name());
                switch (outcome) {
                    case CANCELLED -> cancelled++;
                    case ALREADY_CANCELLED -> already++;
                    case NOT_FOUND -> notFound++;
                }
            } catch (IllegalArgumentException ex) {
                // Si la venta no existe, abortamos todo el lote
                throw ex;
            } catch (Exception ex) {
                r.put("status", "ERROR");
                r.put("message", ex.getMessage());
            }

            detalleResultados.add(r);
        }

        boolean ventaCancelada = revisarYMarcarVentaCancelada(orderId);

        Map<String, Integer> totals = new HashMap<>();
        totals.put("requested", items.size());
        totals.put("cancelled", cancelled);
        totals.put("alreadyCancelled", already);
        totals.put("notFound", notFound);

        result.put("orderId", orderId);
        result.put("results", detalleResultados);
        result.put("totals", totals);
        result.put("ventaCancelada", ventaCancelada);
        return result;
    }
    
    @Transactional
    public Map<String, Object> cancelarParcialEnLote(Long orderId, List<CancelarParcialItem> items) {
        // 0) Validaciones básicas
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos un item para cancelar.");
        }

        // 1) Traemos TODO lo de la orden en 1 consulta
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaOrderId(orderId);
        if (detalles.isEmpty()) {
            throw new IllegalArgumentException("Venta no encontrada o sin detalles para orderId: " + orderId);
        }

        // 2) Indexamos por clave compuesta: "secction|row|seat"
        Map<String, DetalleVenta> index = new HashMap<>(detalles.size() * 2);
        for (DetalleVenta d : detalles) {
            String key = buildKey(d.getSecction(), d.getRow(), d.getSeat());
            index.put(key, d);
        }

        // 3) Procesamos solicitudes
        List<Map<String, Object>> detalleResultados = new ArrayList<>(items.size());
        List<Long> idsParaCancelar = new ArrayList<>();

        int cancelled = 0, already = 0, notFound = 0;

        for (var it : items) {
            String key = buildKey(it.getSecction(), it.getRow(), it.getSeat());
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("secction", it.getSecction());
            r.put("row", it.getRow());
            r.put("seat", it.getSeat());

            DetalleVenta d = index.get(key);
            if (d == null) {
                r.put("status", CancelOutcome.NOT_FOUND.name());
                notFound++;
            } else if (Boolean.TRUE.equals(d.isCancel())) {
                r.put("status", CancelOutcome.ALREADY_CANCELLED.name());
                already++;
            } else {
                // Marcaremos este ID en el UPDATE masivo
                idsParaCancelar.add(d.getId());
                r.put("status", CancelOutcome.CANCELLED.name());
                cancelled++;
            }

            detalleResultados.add(r);
        }

        // 4) Persistimos en un solo golpe
        if (!idsParaCancelar.isEmpty()) {
            detalleVentaRepository.bulkCancelByIds(idsParaCancelar);
            // Si no quieres bulk:
            // for (Long id : idsParaCancelar) index.get(id).setCancel(true);
            // detalleVentaRepository.saveAll(index.values()); // o sólo los modificados
        }

        // 5) Regla de “venta cancelada” (si aplica en tu dominio)
        boolean ventaCancelada = revisarYMarcarVentaCancelada(orderId);

        Map<String, Integer> totals = new LinkedHashMap<>();
        totals.put("requested", items.size());
        totals.put("cancelled", cancelled);
        totals.put("alreadyCancelled", already);
        totals.put("notFound", notFound);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("orderId", orderId);
        payload.put("totals", totals);
        payload.put("results", detalleResultados);
        payload.put("ventaCancelada", ventaCancelada);

        return payload;
    }

    /** Clave compuesta normalizada para mapas O(1) */
    private String buildKey(String secction, String row, Long seat) {
        // Normaliza espacios/caso si lo consideras necesario
        String s = secction == null ? "" : secction.trim();
        String r = row == null ? "" : row.trim();
        String t = seat == null ? "" : seat.toString();
        return s + "|" + r + "|" + t;
    }

    

    /* ---------- Mapping ---------- */

    private VentaDTO toVentaDTO(Venta v) {
        VentaDTO dto = new VentaDTO();
        dto.setId(v.getId());                               // Long
        dto.setOrderId(v.getOrderId());                     // Long
        dto.setEventId(v.getEventId());                     // int -> Integer
        dto.setItemsTotal(v.getItemsTotal());               // int -> Integer
        dto.setEventDescription(v.getEventDescription());
        dto.setSeriesGame(v.getSeriesGame());
        dto.setTotalExTax(v.getTotalExTax());               // BigDecimal
        dto.setTotalIncTax(v.getTotalIncTax());             // BigDecimal
        dto.setTotalDiscount(v.getTotalDiscount());         // BigDecimal
        dto.setPaymentMethod(v.getPaymentMethod());
        dto.setPaymentCompleted(v.isPaymentCompleted());    // boolean -> Boolean
        dto.setCurrencyCode(v.getCurrencyCode());
        dto.setOrderSource(v.getOrderSource());
        dto.setStoreCreditAmount(v.getStoreCreditAmount()); // BigDecimal
        dto.setFechaVenta(v.getFechaVenta());               // LocalDateTime
        dto.setEmailCliente(v.getEmailCliente());
        dto.setTelefonoCliente(v.getTelefonoCliente());
        dto.setCancel(v.isCancel());                        // boolean -> Boolean
        return dto;
    }

    private DetalleVentaDTO toDetalleDTO(DetalleVenta d) {
        DetalleVentaDTO dto = new DetalleVentaDTO();
        dto.setId(d.getId());
        dto.setVentaId(d.getVenta().getId());
        dto.setSku(d.getSku());
        dto.setZone(d.getZone());
        dto.setSecction(d.getSecction());
        dto.setRow(d.getRow());
        dto.setSeat(d.getSeat());
        dto.setDiscountAplied(d.getDiscountAplied());
        dto.setPriceExTax(d.getPriceExTax());
        dto.setPriceIncTax(d.getPriceIncTax());
        dto.setTypeProduct(d.getTypeProduct());
        dto.setCancel(d.isCancel());
        return dto;
    }
}
