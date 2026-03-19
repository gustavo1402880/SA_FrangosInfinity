package org.frangosInfinity.infrastructure.persistence.module.fidelidade;

import org.frangosInfinity.core.entity.module.fidelidade.RegrasFidelidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegrasFidelidadeRepository extends JpaRepository<RegrasFidelidade, Long>
{
    @Query("SELECT r FROM RegrasFidelidade r WHERE r.ativo = true ORDER BY r.id DESC LIMIT 1")
    Optional<RegrasFidelidade> buscarAtiva();

    @Query("SELECT r FROM RegrasFidelidade r ORDER BY r.id DESC")
    List<RegrasFidelidade> listarTodasOrdenadas();

    List<RegrasFidelidade> findByAtivoTrue();

    @Query("UPDATE RegrasFidelidade r SET r.ativo = false WHERE r.ativo = true")
    void desativarRegrasAtivas();
}
