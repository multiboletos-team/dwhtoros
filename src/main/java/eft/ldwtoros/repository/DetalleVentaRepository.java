
package eft.ldwtoros.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eft.ldwtoros.entity.DetalleVenta;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
	 List<DetalleVenta> findByVentaOrderId(Long orderId);
	 
	// ¿Quedan asientos vivos?
	 long countByVentaOrderIdAndCancelFalse(Long orderId);

	 Optional<DetalleVenta> findFirstByVentaOrderIdAndSecctionAndRowAndSeat(
	            Long orderId, String secction, String row, Long seat
	        );
}
