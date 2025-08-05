
package eft.ldwtoros.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eft.ldwtoros.entity.DetalleVenta;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
	 List<DetalleVenta> findByVentaId(Long ventaId);
}
