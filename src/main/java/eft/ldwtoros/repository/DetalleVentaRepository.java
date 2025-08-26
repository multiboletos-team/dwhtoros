
package eft.ldwtoros.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eft.ldwtoros.entity.DetalleVenta;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
	 // Trae TODOS los detalles de la orden en UN solo query
	 List<DetalleVenta> findByVentaOrderId(Long orderId);
	 
	// ¿Quedan asientos vivos?
	 long countByVentaOrderIdAndCancelFalse(Long orderId);

	 Optional<DetalleVenta> findFirstByVentaOrderIdAndSecctionAndRowAndSeat(
	            Long orderId, String secction, String row, Long seat
	        );
	 
	 // Opción: actualización masiva en un solo statement
	    @Modifying
	    @Query("UPDATE DetalleVenta d SET d.cancel = true WHERE d.id IN :ids AND d.cancel = false")
	    int bulkCancelByIds(@Param("ids") List<Long> ids);
}
