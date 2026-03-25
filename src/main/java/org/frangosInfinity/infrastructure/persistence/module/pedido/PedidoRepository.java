package org.frangosInfinity.infrastructure.persistence.module.pedido;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>
{
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    @Query("SELECT DISTINCT p FROM Pedido p JOIN p.subPedidos s WHERE s.status = :status")
    List<Pedido> findByStatus(@Param("status") StatusPedido statusPedido);

    @Query("SELECT p FROM Pedido p WHERE p.dataHora BETWEEN :inicio AND :fim ORDER BY p.dataHora DESC")
    List<Pedido> findByPeriodo(@Param("inicio")LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT p FROM Pedido p WHERE p.mesaId = :mesaId AND EXISTS (SELECT s FROM SubPedido s WHERE s.pedido = p AND  s.status NOT IN ('ENTREGUE', 'CANCELADO') ORDER BY p.dataHora DESC)")
    List<Pedido> findAtivosPorMesa(@Param("mesaId") Long mesaId);
}
