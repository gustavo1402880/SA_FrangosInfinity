package org.frangosInfinity.infrastructure.persistence.module.mesa;

import org.frangosInfinity.core.entity.module.mesa.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, Long>
{
    Optional<QRCode> findByTokenSessao(String token);

    Optional<QRCode> buscarAtivoPorMesa(Long idMesa);

    @Modifying
    @Query("UPDATE QRCode q SET q.utilizado = true, q.ativo = false WHERE q.id = :id ")
    void marcarComoUtilizado(@Param("id") Long id);

    @Modifying
    @Query("UPDATE QRCode q SET q.ativo = false WHERE q.data_expiracao <= CURRENT_TIMESTAMP")
    void desativarExpirados();

    List<QRCode> findByMesaIdOrderByDatCriacaoDesc(Long idMesa);

    @Query("SELECT q FROM QRCode q WHERE q.ativo = true AND q.utilizado = false AND q.dataExpiracao > CURRENT_TIMESTAMP")
    List<QRCode> findAllAtivos();

    List<QRCode> findByMesaIdAndAtivoTrue(Long idMesa);

    List<QRCode> findByMesaIdAndUtilizadoFalse();

    @Query("SELECT COUNT(q) FROM QRCode q WHERE q.mesa.id = :idMesa AND q.ativo = true")
    Long countAtivosPorMesa(@Param("idMesa") Long idMesa);
}
