package org.frangosInfinity.infrastructure.persistence.module.fidelidade;

import org.frangosInfinity.core.entity.module.fidelidade.TransacaoPontos;
import org.frangosInfinity.core.enums.TipoTransacaoPontos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacaoPontosRepository extends JpaRepository<TransacaoPontos, Long>
{
    @Query("SELECT t FROM TransacaoPontos t WHERE t.pontosFidelidade.id = :pontosFidelidadeID ORDER BY t.data DESC")
    List<TransacaoPontos> buscarPorPontosId(@Param("pontosFidelidadeID") Long pontosFidelidadeId);

    @Query("SELECT t FROM TransacaoPontos t WHERE t.data BETWEEN :inicio AND :fim ORDER BY t.data DESC")
    List<TransacaoPontos> buscarPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT t FROM TransacaoPontos t WHERE t.tipoTransacaoPontos = :tipo ORDER BY t.data DESC")
    List<TransacaoPontos> buscarPorTipo(@Param("tipo") TipoTransacaoPontos tipo);

    List<TransacaoPontos> findByPontosFidelidadeIdOrderByDataDesc(Long pontosFidelidadadeId);

    @Query("SELECT t FROM TransacaoPontos t WHERE t.pontosFidelidade.id = :pontosFidelidadeId AND t.tipoTransacaoPontos = :tipo ORDER BY t.data DESC")
    List<TransacaoPontos> buscarPorPontosETipo(@Param("pontosFidelidade") Long pontosFidelidadeID, @Param("tipo") TipoTransacaoPontos tipo);
}
