package eft.ldwtoros.repository.spec;


import org.springframework.data.jpa.domain.Specification;

import eft.ldwtoros.entity.Venta;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class VentaSpecifications {
	public static Specification<Venta> orderIdEq(Long orderId) {
        return (root, q, cb) -> orderId == null ? null : cb.equal(root.get("orderId"), orderId);
    }

	public static Specification<Venta> fechaBetween(LocalDate desde, LocalDate hasta) {
	    if (desde == null && hasta == null) return null;
	    if (desde == null) desde = hasta;
	    if (hasta == null) hasta = desde;
	    if (hasta.isBefore(desde)) { LocalDate t = desde; desde = hasta; hasta = t; }

	    LocalDateTime ini = desde.atStartOfDay();
	    LocalDateTime finExclusivo = hasta.plusDays(1).atStartOfDay();

	    return (root, q, cb) -> cb.and(
	        cb.greaterThanOrEqualTo(root.get("fechaVenta"), ini),
	        cb.lessThan(root.get("fechaVenta"), finExclusivo)
	    );
	}


    public static Specification<Venta> cancelEq(Boolean cancel) {
        return (root, q, cb) -> cancel == null ? null : cb.equal(root.get("cancel"), cancel);
    }
}
