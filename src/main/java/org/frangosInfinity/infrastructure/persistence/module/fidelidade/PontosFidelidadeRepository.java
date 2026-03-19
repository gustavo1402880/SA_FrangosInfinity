package org.frangosInfinity.infrastructure.persistence.module.fidelidade;

import org.frangosInfinity.core.entity.module.fidelidade.PontosFidelidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PontosFidelidadeRepository extends JpaRepository<PontosFidelidade, Long>
{
    Optional<PontosFidelidade> findByClienteId(Long clienteId);

    @Query("SELECT p FROM PontosFidelidade p WHERE p.saldo > 0")
    List<PontosFidelidade> listarComSaldoPositivo();

    @Query("SELECT p FROM PontosFidelidade p WHERE p.dataValidade < CURRENT_TIMESTAMP AND p.saldo > 0")
    List<PontosFidelidade> buscarExpirados();
}
