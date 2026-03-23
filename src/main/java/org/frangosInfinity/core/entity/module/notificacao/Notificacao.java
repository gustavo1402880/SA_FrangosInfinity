package org.frangosInfinity.core.entity.module.notificacao;

import jakarta.persistence.*;
import org.frangosInfinity.core.enums.TipoNotificacao;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
public class Notificacao
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacao", nullable = false)
    private TipoNotificacao tipoNotificacao;

    @Column(nullable = false, length = 500)
    private String mensagem;

    @CreatedDate
    @Column(name = "data_hora", nullable = false, updatable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private Boolean lida;

    @Column(length = 250)
    private String destinatario;

    @Column(name = "email_enviado")
    private Boolean emailEnviado;

    public Notificacao(){}

    public Notificacao(TipoNotificacao tipo, String mensagem, String destinatario)
    {
        this.tipoNotificacao = tipo;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
        this.lida = false;
        this.emailEnviado = false;
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

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public Boolean getEmailEnviado() {
        return emailEnviado;
    }

    public void setEmailEnviado(Boolean emailEnviado) {
        this.emailEnviado = emailEnviado;
    }

    public void marcarComoLida()
    {
        this.lida = true;
    }

    public void marcarEmailEnviado()
    {
        this.emailEnviado = true;
    }
}
