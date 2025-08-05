
package eft.ldwtoros.controller;

import eft.ldwtoros.dto.ApiResponse;
import eft.ldwtoros.dto.VentaRequestDTO;
import eft.ldwtoros.entity.ErrorLog;
import eft.ldwtoros.entity.Venta;
import eft.ldwtoros.repository.ErrorLogRepository;
import eft.ldwtoros.repository.VentaRepository;
import eft.ldwtoros.service.VentaService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {
	
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ErrorLogRepository errorLogRepository;

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @PostMapping
    public Venta crearVenta(@RequestBody Venta venta) {
        return ventaRepository.save(venta);
    }

    @Deprecated
    @PutMapping("/{id}/cancelar")
    public Venta cancelarVentaOld(@PathVariable Long id) {
        Venta venta = ventaRepository.findById(id).orElseThrow();
        venta.setCancelada(true);
        return ventaRepository.save(venta);
    }
    
    @PutMapping("/cancelar/{orderId}")
    public ResponseEntity<Map<String, Object>> cancelarVenta(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Venta> optionalVenta = ventaRepository.findByOrderId(orderId);

            if (optionalVenta.isEmpty()) {
                response.put("codigo", 404);
                response.put("mensaje", "Venta no encontrada para el orderId: " + orderId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Venta venta = optionalVenta.get();
            venta.setCancelada(true);
            ventaRepository.save(venta);

            response.put("codigo", 200);
            response.put("mensaje", "Venta cancelada correctamente");
            response.put("orderId", orderId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("codigo", 500);
            response.put("mensaje", "Error interno al cancelar la venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
    public ResponseEntity<ApiResponse> procesarVentaV2(@RequestBody VentaRequestDTO dto, HttpServletRequest request) {
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
