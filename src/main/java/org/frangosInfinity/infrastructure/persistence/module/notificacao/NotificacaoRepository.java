package org.frangosInfinity.infrastructure.persistence.module.notificacao;

import org.frangosInfinity.core.entity.module.notificacao.Notificacao;
import org.frangosInfinity.core.enums.TipoNotificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long>
{
    List<Notificacao> findByDestinatarioorderByDataHoraDesc(String destinatario);

    List<Notificacao> findByLidaFalseAndDestinatarioOrderByDataHoraDesc(String destinatario);

    List<Notificacao> findByTipoNotificacaoOrderByDataHoraDesc(TipoNotificacao tipo);

    List<Notificacao> findByDestinatario(String destinatario);

    @Modifying
    @Query("UPDATE Notificacao n SET n.lida = true WHERE n.id = :id")
    Integer marcarComoLida(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Notificacao n SET n.lida = true WHERE n.destinatario = :destinatario")
    Integer marcarTodasComoLidas(@Param("destinatario") String destinatario);

    @Modifying
    @Query("UPDATE Notificacao n SET n.emailEnviado = true WHERE n.id = :id")
    Integer marcarEmailEnviado(@Param("id") Long id);

    @Query("SELECT COUNT(n) FROM Notificacao n WHERE n.lida = false AND n.destinatario = :destinatario")
    Long countNaoLidasPorDestinatario(@Param("destinatario") String destinatario);

    @Query("SELECT n FROM Notificacao n WHERE n.dataHora < :dataLimite AND n.lida = true")
    List<Notificacao> findAntigasLidas(@Param("dataLimite") LocalDateTime dataLimite);
}
