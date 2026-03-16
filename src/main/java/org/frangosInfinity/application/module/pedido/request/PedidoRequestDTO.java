package org.frangosInfinity.application.module.pedido.request;

import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;

public class PedidoRequestDTO {

    private Long id;
    private String numeroPedido;
    private java.sql.Date dataHora;
    private StatusPedido status;
    private Long mesa_id;
    private Long atendente_id;
    private String tipo;

    public PedidoRequestDTO() {}

    public PedidoRequestDTO(Long id, String numeroPedido, Date dataHora, StatusPedido status, Long mesa_id, Long atendente_id, String tipo) {
        if(id >= 1 && !numeroPedido.isEmpty() && dataHora.after(Date.valueOf("1900-1-1")) && status != null && mesa_id >= 1 && atendente_id >= 1 && tipo.isEmpty()) {
            this.id = id;
            this.numeroPedido = numeroPedido;
            this.dataHora = dataHora;
            this.status = status;
            this.mesa_id = mesa_id;
            this.atendente_id = atendente_id;
            this.tipo = tipo;
        }

        throw new IllegalArgumentException("Erro - os dados de pedido estão invalidos");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Long getMesa_id() {
        return mesa_id;
    }

    public void setMesa_id(Long mesa_id) {
        this.mesa_id = mesa_id;
    }

    public Long getAtendente_id() {
        return atendente_id;
    }

    public void setAtendente_id(Long atendente_id) {
        this.atendente_id = atendente_id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
