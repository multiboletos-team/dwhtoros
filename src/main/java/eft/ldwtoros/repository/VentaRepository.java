
package eft.ldwtoros.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eft.ldwtoros.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>, JpaSpecificationExecutor<Venta> {
	Optional<Venta> findByOrderId(Long orderId);
	
	@Modifying(flushAutomatically = true, clearAutomatically = false)
	@Query("UPDATE Venta v SET v.cancel = TRUE WHERE v.orderId = :orderId AND v.cancel = FALSE")
	int marcarCanceladaPorOrderId(@Param("orderId") Long orderId);
}

