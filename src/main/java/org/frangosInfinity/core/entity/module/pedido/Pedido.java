package org.frangosInfinity.core.entity.module.pedido;
import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;
import java.time.LocalDateTime;

public class Pedido {

    // Atributos

    private Long id;
    private String numeroPedido;
    private java.sql.Date dataHora;
    private StatusPedido status;
    private Long mesa_id;
    private Long atendente_id;
    private String tipo;

    // Construtores

    public Pedido(){}

    public Pedido(String numeroPedido, java.sql.Date dataHora, StatusPedido status, Long mesa, Long atendente, String tipo) {
        this.numeroPedido = numeroPedido;
        this.dataHora = dataHora;
        this.status = status;
        this.mesa_id = mesa;
        this.atendente_id = atendente;
        this.tipo = tipo;
    }

    // Getters & Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAtendente_id() {
        return atendente_id;
    }

    public void setAtendente_id(Long atendente_id) {
        this.atendente_id = atendente_id;
    }

    public Long getMesa_id() {
        return mesa_id;
    }

    public void setMesa_id(Long mesa_id) {
        this.mesa_id = mesa_id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public java.sql.Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(java.sql.Date dataHora) {
        this.dataHora = dataHora;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Long getMesa() {
        return mesa_id;
    }

    public void setMesa(Long mesa) {
        this.mesa_id = mesa;
    }

    public Long getAtendente() {
        return atendente_id;
    }

    public void setAtendente(Long atendente) {
        this.atendente_id = atendente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Métodos

}
