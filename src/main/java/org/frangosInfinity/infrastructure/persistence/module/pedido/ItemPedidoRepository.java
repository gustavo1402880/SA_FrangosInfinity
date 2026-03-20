package org.frangosInfinity.infrastructure.persistence.module.pedido;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>
{
    List<ItemPedido> findBySubPedidoId(Long subPedidoId);

    @Query("SELECT i FROM ItemPedido i WHERE i.subPedido.pedido.id = :pedidoId")
    List<ItemPedido> findByPedidoId(@Param("pedidoId") Long pedidoId);

    @Query("SELECT i.produtoId, COUNT(i) as quantidade FROM ItemPedido i GROUP BY i.produtoId ORDER BY quantidade DESC")
    List<Object[]> findProdutosMaisVendidos();

    @Modifying
    @Query("UPDATE ItemPedido i SET i.observacao = :observacao WHERE i.id = :id")
    void atualizarObservacao(@Param("id") Long id, @Param("observacao") String observacao);
}
