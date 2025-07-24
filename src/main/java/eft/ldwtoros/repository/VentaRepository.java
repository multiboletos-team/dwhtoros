
package eft.ldwtoros.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eft.ldwtoros.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {}
