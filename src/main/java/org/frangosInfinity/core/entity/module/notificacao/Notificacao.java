package org.frangosInfinity.core.entity.module.notificacao;

import org.frangosInfinity.core.enums.TipoNotificacao;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Notificacao
{
    private Long id;
    private TipoNotificacao tipoNotificacao;
    private String mensagem;
    private LocalDateTime dataHora;
    private boolean lida;
    private String destinatario;

    public Notificacao(){}

    public Notificacao(Long id, TipoNotificacao tipoNotificacao, String mensagem, LocalDateTime dataHora, boolean lida, String destinatario)
    {
        id = this.id;
        tipoNotificacao = this.tipoNotificacao;
        mensagem = this.mensagem;
        dataHora = this.dataHora;
        lida = this.lida;
        destinatario = this.destinatario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoNotificacao getTipoNotificacao() {
        return tipoNotificacao;
    }

    public void setTipoNotificacao(TipoNotificacao tipoNotificacao) {
        this.tipoNotificacao = tipoNotificacao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
}
