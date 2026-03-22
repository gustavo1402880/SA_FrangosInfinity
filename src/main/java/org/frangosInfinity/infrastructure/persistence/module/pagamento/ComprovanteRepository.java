package org.frangosInfinity.infrastructure.persistence.module.pagamento;

import org.frangosInfinity.core.entity.module.pagamento.Comprovante;
import org.frangosInfinity.core.enums.TipoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComprovanteRepository extends JpaRepository<Comprovante, Long>
{
    Optional<Comprovante> findByPagamentoId(Long pagamentoId);

    Optional<Comprovante> findByNumero(String numero);

    List<Comprovante> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Comprovante> findByFormaPagamento(TipoPagamento formaPagamento);

    @Query("SELECT SUM(c.valorTotal) FROM Comprovante c WHERE c.dataHora BETWEEN :inicio AND :fim")
    Double sumvalorTotalNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    Boolean existsByNumero(String numero);
}
