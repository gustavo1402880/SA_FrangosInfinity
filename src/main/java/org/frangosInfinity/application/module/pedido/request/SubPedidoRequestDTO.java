package org.frangosInfinity.application.module.pedido.request;

import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;

public class SubPedidoRequestDTO {

    private final Long id;
    private final Pedido pedidoHub;
    private final String clienteID;
    private final Date date;
    private final StatusPedido status;
    private final Double valorTotal;
    private final int tempo_em_minutos;
    private final String obsevacoes;

    public SubPedidoRequestDTO(Long id, Pedido pedidoHub, String clienteID, Date date, StatusPedido status, Double valorTotal, int tempo_em_minutos, String obsevacoes) {

        if(id >= 1 && pedidoHub != null && clienteID.isEmpty() && date.after(Date.valueOf("1900-1-1")) && status != null && valorTotal >= 1 && tempo_em_minutos >= 1 && obsevacoes.isEmpty()) {
            this.id = id;
            this.pedidoHub = pedidoHub;
            this.clienteID = clienteID;
            this.date = date;
            this.status = status;
            this.valorTotal = valorTotal;
            this.tempo_em_minutos = tempo_em_minutos;
            this.obsevacoes = obsevacoes;
        }

        throw new IllegalArgumentException("Erro - os dados do SubPedido estão invalidos");
    }

    public Long getId() {
        return id;
    }

    public Pedido getPedidoHub() {
        return pedidoHub;
    }

    public String getClienteID() {
        return clienteID;
    }

    public Date getDate() {
        return date;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public int getTempo_em_minutos() {
        return tempo_em_minutos;
    }

    public String getObsevacoes() {
        return obsevacoes;
    }
}
