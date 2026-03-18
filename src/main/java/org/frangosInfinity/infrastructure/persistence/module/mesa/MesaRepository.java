package org.frangosInfinity.infrastructure.persistence.module.mesa;

import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long>
{
    @Query("SELECT m FROM Mesa m WHERE m.numero = :numero")
    Optional<Mesa> findByNumero(@Param("numero") Integer numero);

    List<Mesa> findByDisponivelTrueAndAtivaTrueOrderByNumero();

    Boolean existsByNumero(Integer numero);

    List<Mesa> findByAtivaTrue();

    List<Mesa> findByDisponivelFalse();

    @Query("SELECT m FROM Mesa m WHERE m.iotConfig.id IS NOT NULL")
    List<Mesa> findComIoT();

    @Query("SELECT m FROM Mesa m WHERE m.iotConfig.id IS NULL")
    List<Mesa> findSemIoT();

    @Modifying
    @Query("UPDATE Mesa m SET m.disponivel = :disponivel WHERE m.id = :id")
    void atualizarDisponibilidade(@Param("id") Long id,@Param("disponivel") Boolean disponivel);

    @Modifying
    @Query("UPDATE Mesa m SET m.ativa = :ativa WHERE m.id = :id")
    void atualizarAtiva(@Param("id")Long id, @Param("ativa") Boolean ativa);
}
