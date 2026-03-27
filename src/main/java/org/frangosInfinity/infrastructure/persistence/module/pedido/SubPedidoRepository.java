package org.frangosInfinity.infrastructure.persistence.module.pedido;

import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubPedidoRepository extends JpaRepository<SubPedido, Long>
{
    List<SubPedido> findByPedidoId(Long pedidoId);

    List<SubPedido> findByClienteID(String clienteID);

    List<SubPedido> findByStatus(StatusPedido status);

    @Query("SELECT sp FROM SubPedido sp WHERE sp.pedido.id = :pedidoId ORDER BY sp.dataHora")
    List<SubPedido> findByPedidoIdOrdered(@Param("pedidoId") Long pedidoId);

    @Query("SELECT SUM(sp.valorTotal) FROM SubPedido sp WHERE sp.pedido.id = :pedidoId")
    Double sumValorTotalByPedidoId(@Param("pedidoId") Long pedidoId);

}
