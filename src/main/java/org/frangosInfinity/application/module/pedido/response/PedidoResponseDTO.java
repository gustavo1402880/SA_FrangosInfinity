package org.frangosInfinity.application.module.pedido.response;

import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;

public class PedidoResponseDTO {

    private final Long id;
    private final String numeroPedido;
    private final java.sql.Date dataHora;
    private final StatusPedido status;
    private final Long mesa_id;
    private final Long atendente_id;
    private final String tipo;

    public PedidoResponseDTO(Long id, String numeroPedido, Date dataHora, StatusPedido status, Long mesa_id, Long atendente_id, String tipo) {
        this.id = id;
        this.numeroPedido = numeroPedido;
        this.dataHora = dataHora;
        this.status = status;
        this.mesa_id = mesa_id;
        this.atendente_id = atendente_id;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public Long getMesa_id() {
        return mesa_id;
    }

    public Long getAtendente_id() {
        return atendente_id;
    }

    public String getTipo() {
        return tipo;
    }
}
