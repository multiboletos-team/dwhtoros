
package eft.ldwtoros.controller;

import eft.ldwtoros.dto.ApiResponse;
import eft.ldwtoros.dto.CancelarItemRequest;
import eft.ldwtoros.dto.DetalleVentaDTO;
import eft.ldwtoros.dto.VentaDTO;
import eft.ldwtoros.dto.VentaRequestDTO;
import eft.ldwtoros.entity.DetalleVenta;
import eft.ldwtoros.entity.ErrorLog;
import eft.ldwtoros.entity.Venta;
import eft.ldwtoros.repository.DetalleVentaRepository;
import eft.ldwtoros.repository.ErrorLogRepository;
import eft.ldwtoros.repository.VentaRepository;
import eft.ldwtoros.service.VentaService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;
import java.time.LocalDate;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {
	
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }
    
    @Autowired
    private ObjectMapper objectMapper; // Spring la inyecta automáticamente
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    @Autowired
    private ErrorLogRepository errorLogRepository;


    @GetMapping("/consulta")
    public ResponseEntity<ApiResponse> listarVentas(
            @RequestParam(name = "order_id", required = false) Long orderIdSnake,
            @RequestParam(name = "orderId",  required = false) Long orderIdCamel,
            @RequestParam(name = "fechaDesde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(name = "fechaHasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(name = "fechaVenta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaVenta,
            @RequestParam(required = false) Boolean cancel
    ) {
        Long orderId = (orderIdSnake != null) ? orderIdSnake : orderIdCamel;

        // Compatibilidad: si pasaron fechaVenta, úsala como rango 1 día
        if (fechaDesde == null && fechaHasta == null && fechaVenta != null) {
            fechaDesde = fechaVenta;
            fechaHasta = fechaVenta;
        }
        // Si solo mandan una de las dos, completa con la otra para "un solo día"
        if (fechaDesde != null && fechaHasta == null) fechaHasta = fechaDesde;
        if (fechaHasta != null && fechaDesde == null) fechaDesde = fechaHasta;

        List<VentaDTO> data = ventaService.buscarVentas(orderId, fechaDesde, fechaHasta, cancel);
        return ResponseEntity.ok(new ApiResponse(200, "OK", data));
    }

    // GET /api/v1/ventas/{order_id}/detalles  (DetalleVenta por order_id)
    @GetMapping("/{order_id}/detalle")
    public ResponseEntity<ApiResponse> listarDetalles(@PathVariable("order_id") Long orderId) {
        List<DetalleVentaDTO> data = ventaService.listarDetallesPorOrderId(orderId);
        return ResponseEntity.ok(new ApiResponse(200, "OK", data));
    }

    @Deprecated
    @PutMapping("/{id}/cancelar")
    public Venta cancelarVentaOld(@PathVariable Long id) {
        Venta venta = ventaRepository.findById(id).orElseThrow();
        venta.setCancel(true);
        return ventaRepository.save(venta);
    }
    
    @PutMapping("/cancelar/{orderId}")
    public ResponseEntity<Map<String, Object>> cancelarVenta(@PathVariable Long orderId, HttpServletRequest httpReq) {
        Map<String, Object> response = new HashMap<>();
        try {
            // ahora usamos el servicio que también cancela los detalles
            ventaService.cancelarVentaTotalPorOrderId(orderId);

            response.put("codigo", 200);
            response.put("mensaje", "Venta cancelada correctamente");
            response.put("orderId", orderId);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("codigo", 404);
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            String rawBody = String.format(
                    "{\"orderId\":%s}",
                    orderId
                );
        	
        	// Captura traza completa para bitácora
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String fullStackTrace = sw.toString();

            // Guardar en bitácora
            errorLogRepository.save(new ErrorLog(
            	httpReq.getRequestURI(),
            	httpReq.getMethod(),
                rawBody,
                String.valueOf(orderId),           // ✅ Ahora puedes pasar el Long
                "Order ID duplicado",
                fullStackTrace.substring(0, 1995)
            ));
            response.put("codigo", 500);
            response.put("mensaje", "Error interno al cancelar la venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PutMapping("/cancelar/parcial")
    public ResponseEntity<ApiResponse> cancelarParcial(
            @RequestBody CancelarItemRequest req,
            HttpServletRequest httpReq) {

        try {
            // Validación: orderId, secction, row, seat
            if (req.getOrderId() == null ||
                req.getSecction() == null || req.getSecction().isBlank() ||
                req.getRow() == null || req.getRow().isBlank() ||
                req.getSeat() == null || req.getSeat() <= 0) {

                return ResponseEntity.badRequest()
                    .body(new ApiResponse(400,
                        "Parámetros incompletos: orderId, secction, row y seat son obligatorios",
                        null));
            }
            
            // Si envían uno de los montos, exige ambos
            if ( (req.getTotalExTax() != null || req.getTotalIncTax() != null)
                 && (req.getTotalExTax() == null || req.getTotalIncTax() == null) ) {
                return ResponseEntity.badRequest().body(
                    new ApiResponse(400,
                        "Si envías montos, totalExTax y totalIncTax son obligatorios ambos",
                        null)
                );
            }

            // 1) Cancelar asiento
            // Llama al servicio con tipos numéricos para secction y seat
            ventaService.cancelarItemPorOrderYAsiento(
                    req.getOrderId(),
                    req.getSecction(),
                    req.getRow().trim(),
                    req.getSeat());
            
            // 2) Actualizar montos de la venta (opcional)
            if (req.getTotalExTax() != null && req.getTotalIncTax() != null) {
                ventaService.actualizarTotalesVenta(
                        req.getOrderId(),
                        req.getTotalExTax(),
                        req.getTotalIncTax()
                );
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("orderId", req.getOrderId());
            payload.put("secction", req.getSecction());
            payload.put("row", req.getRow());
            payload.put("seat", req.getSeat());

            return ResponseEntity.ok(new ApiResponse(200, "Asiento cancelado correctamente", payload));

        } catch (IllegalArgumentException iae) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("orderId", req.getOrderId());
            payload.put("secction", req.getSecction());
            payload.put("row", req.getRow());
            payload.put("seat", req.getSeat());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, iae.getMessage(), payload));

        } catch (Exception e) {
            try {
                // Construye el request crudo con números sin comillas
                String rawBody = String.format(
                    "{\"orderId\":%s,\"secction\":%s,\"row\":\"%s\",\"seat\":%s}",
                    String.valueOf(req.getOrderId()),
                    String.valueOf(req.getSecction()),
                    req.getRow(),
                    String.valueOf(req.getSeat())
                );

                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String fullStackTrace = sw.toString();

                errorLogRepository.save(new ErrorLog(
                    httpReq.getRequestURI(),
                    httpReq.getMethod(),
                    rawBody,
                    String.valueOf(req.getOrderId()),
                    "Order ID duplicado",
                    fullStackTrace.substring(0, Math.min(fullStackTrace.length(), 1995))
                ));
            } catch (Exception ignored) {}

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Error al cancelar asiento: " + e.getMessage(), null));
        }
    }
    
    /**
     * @deprecated
     * @author mescalera
     * @param dto
     * @return String mensaje de respuesta
     */
    @PostMapping("/boletomovilV1")
    public ResponseEntity<String> procesarVenta(@RequestBody VentaRequestDTO dto) {
        ventaService.insertarVenta(dto);
        return ResponseEntity.ok("Venta procesada correctamente");
    }
    
    @PostMapping("/boletomovil")
    public ResponseEntity<ApiResponse> procesarVentaV2(@RequestBody VentaRequestDTO dto, HttpServletRequest request) throws JsonProcessingException {
        // Convertir DTO a String JSON
        String jsonRequest = objectMapper.writeValueAsString(dto);
        try {
            ventaService.insertarVenta(dto);

            ApiResponse response = new ApiResponse(
                200,
                "Venta procesada correctamente",
                dto // Opcional: puedes retornar el mismo DTO u otro objeto
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Manejo de errores de datos inválidos -> 400
            ApiResponse response = new ApiResponse(
                400,
                "Error en la solicitud: " + e.getMessage(),
                null
            );
            return ResponseEntity.badRequest().body(response);

        } catch (DataIntegrityViolationException e) {
        	// Captura traza completa para bitácora
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String fullStackTrace = sw.toString();

            // Guardar en bitácora
            errorLogRepository.save(new ErrorLog(
                request.getRequestURI(),
                request.getMethod(),
                jsonRequest.substring(0, Math.min(jsonRequest.length(), 2000)),
                String.valueOf(dto.getOrderId()),           // ✅ Ahora puedes pasar el Long
                "Order ID duplicado",
                fullStackTrace.substring(0, 1995)
            ));
        	
            // Violación de UNIQUE -> 400
            ApiResponse response = new ApiResponse(
                400,
                "El order_id " + dto.getOrderId() + " ya existe. No se puede duplicar.",
                null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }catch (Exception e) {
        	// Captura traza completa para bitácora
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String fullStackTrace = sw.toString();

            // Guardar en bitácora
            errorLogRepository.save(new ErrorLog(
                request.getRequestURI(),
                request.getMethod(),
                jsonRequest.substring(0, Math.min(jsonRequest.length(), 2000)),
                String.valueOf(dto.getOrderId()),           // ✅ Ahora puedes pasar el Long
                "Order ID duplicado",
                fullStackTrace.substring(0, 1995)
            ));
            
            // Manejo de errores inesperados -> 500
            ApiResponse response = new ApiResponse(
                500,
                "Algo falló al procesar la venta",
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
