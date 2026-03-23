package org.frangosInfinity.infrastructure.persistence.module.relatorio;

import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;
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
public interface RelatorioRepository extends JpaRepository<RelatorioVendas, Long>
{
    List<RelatorioVendas> findByPeriodoInicioBetween(LocalDateTime inicio, LocalDateTime fim);

    List<RelatorioVendas> findByDataGeracaoBetween(LocalDateTime inicio, LocalDateTime fim);

    Optional<RelatorioVendas> findFirstByOrderByDataGeracaoDesc();

    @Query("SELECT r FROM RelatorioVendas r WHERE r.periodoInicio >= :inicio AND r.periodoFim <= :fim")
    List<RelatorioVendas> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT SUM(r.totalVendas) FROM RelatorioVendas r WHERE r.dataGeracao BETWEEN :inicio AND :fim")
    Double sumVendasPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
