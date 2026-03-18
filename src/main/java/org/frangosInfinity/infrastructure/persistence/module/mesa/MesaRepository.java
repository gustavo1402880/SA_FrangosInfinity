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
    Optional<Mesa> buscarPorNumero(Integer numero);

    List<Mesa> findByDisponivelTrueAndAtivaTrueOrderByNUmero();

    Boolean existsByNumero(Integer numero);

    List<Mesa> findByAtivaTrue();

    List<Mesa> findByDisponivelFalse();

    @Query("SELECT m FROM Mesa m WHERE m.id_iot_config IS NOT NULL")
    List<Mesa> findComIoT();

    @Query("SELECT m FROM Mesa m WHERE m.id_iot_config IS NULL")
    List<Mesa> findSemIoT();

    @Modifying
    @Query("UPDATE Mesa m SET m.disponivel = :disponivel WHERE m.id = :id")
    void atualizarDisponibilidade(@Param("id") Long id,@Param("disponivel") Boolean disponivel);

    @Modifying
    @Query("UPDATE Mesa m SET m.ativa = :ativa WHERE m.id = :id")
    void atualizarAtiva(@Param("id")Long id, @Param("ativa") Boolean ativa);
}
