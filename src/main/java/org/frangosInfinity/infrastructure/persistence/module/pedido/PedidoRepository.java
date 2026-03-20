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

    List<Pedido> findByStatus(StatusPedido statusPedido);

    List<Pedido> findByMesaIdAndStatus(Long mesaId, StatusPedido statusPedido);

    @Query("SELECT p FROM Pedido p WHERE p.dataHora BETWEEN :inicio AND :fim ORDER BY p.dataHora DESC")
    List<Pedido> findByPeriodo(@Param("inicio")LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.status = :status")
    Long countByStatus(@Param("status") StatusPedido status);

    @Query("SELECT p FROM Pedido p WHERE p.mesaId = :mesaId AND p.statusId != 6 ORDER BY p.dataHora DESC")
    List<Pedido> findAtivosPorMesa(@Param("mesaId") Long mesaId);
}
