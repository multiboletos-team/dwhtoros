
package eft.ldwtoros.controller;

import eft.ldwtoros.dto.ApiResponse;
import eft.ldwtoros.dto.VentaRequestDTO;
import eft.ldwtoros.entity.Venta;
import eft.ldwtoros.repository.VentaRepository;
import eft.ldwtoros.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {
	
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Autowired
    private VentaRepository ventaRepository;

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @PostMapping
    public Venta crearVenta(@RequestBody Venta venta) {
        return ventaRepository.save(venta);
    }

    @PutMapping("/{id}/cancelar")
    public Venta cancelarVenta(@PathVariable Long id) {
        Venta venta = ventaRepository.findById(id).orElseThrow();
        venta.setCancelada(true);
        return ventaRepository.save(venta);
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
    public ResponseEntity<ApiResponse> procesarVentaV2(@RequestBody VentaRequestDTO dto) {
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

        } catch (Exception e) {
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
