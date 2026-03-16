package org.frangosInfinity.application.module.pedido.request;

import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;

public class PedidoRequestDTO {

    private final Long id;
    private final String numeroPedido;
    private final java.sql.Date dataHora;
    private final StatusPedido status;
    private final Long mesa_id;
    private final Long atendente_id;
    private final String tipo;

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
