package org.frangosInfinity.infrastructure.persistence.module.pagamento;

import org.frangosInfinity.core.entity.module.pagamento.Pagamento;
import org.frangosInfinity.core.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>
{
    Optional<Pagamento> findBySubPedidoId(Long subPedidoId);

    List<Pagamento> findByStatusPagamento(StatusPagamento status);

    @Query("SELECT p FROM Pagamento p WHERE p.dataHora BETWEEN :inicio AND :fim ORDER BY p.dataHora DESC")
    List<Pagamento> findByPeriodo(@Param("inicio")LocalDateTime inicio, @Param("fim")LocalDateTime fim);

    @Query("SELECT COUNT(p) FROM Pagamento p WHERE p.statusPagamento = :status")
    Long countByStatus(@Param("status") StatusPagamento status);

    @Query("SELECT SUM(p.valor) FROM Pagamento p WHERE p.statusPagamento = :status")
    Double sumValorByStatus(@Param("status") StatusPagamento status);

    @Query("SELECT SUM(p.valor) FROM Pagamento p WHERE p.dataHora BETWEEN :inicio AND :fim AND p.statusPagamento = :status")
    Double sumValorByPetiodoAndStatus(@Param("inicio")LocalDateTime inicio, @Param("fim")LocalDateTime fim, @Param("status") StatusPagamento status);

    Boolean existsBySubPedidoId(Long subPedidoId);

}
