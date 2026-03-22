package org.frangosInfinity.infrastructure.persistence.module.pagamento;

import org.frangosInfinity.core.entity.module.pagamento.TransacaoPIX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransacaoPIXRepository extends JpaRepository<TransacaoPIX, Long>
{
    Optional<TransacaoPIX> findByPagamentoId(Long pagamentoId);

    @Query("SELECT t FROM TransacaoPÌX t WHERE t.dataExpiracao < :now")
    List<TransacaoPIX> findExpirados(@Param("now")LocalDateTime now);

    @Query("SELECT t FROM TransacaoPIX t WHERE t.dataExpiracao > :now")
    List<TransacaoPIX> findValidos(@Param("now") LocalDateTime now);

    @Modifying
    @Query("DELETE FROM TransacaoPIX t WHERE t.dataExpiracao < :dataLimite")
    Integer deleteExpiradoAntigos(@Param("dataLimite") LocalDateTime dataLimite);

    Boolean existsByPagamentoId(Long pagamentoId);
}
