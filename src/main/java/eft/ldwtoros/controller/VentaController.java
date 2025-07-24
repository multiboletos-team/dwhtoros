
package eft.ldwtoros.controller;

import eft.ldwtoros.dto.VentaRequestDTO;
import eft.ldwtoros.entity.Venta;
import eft.ldwtoros.repository.VentaRepository;
import eft.ldwtoros.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
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
    
    @PostMapping("/boletomovil")
    public ResponseEntity<String> procesarVenta(@RequestBody VentaRequestDTO dto) {
        ventaService.insertarVenta(dto);
        return ResponseEntity.ok("Venta procesada correctamente");
    }
}
