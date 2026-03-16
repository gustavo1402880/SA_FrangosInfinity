package org.frangosInfinity.application.module.pedido.request;

import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;

public class SubPedidoRequestDTO {

    private Long id;
    private Pedido pedidoHub;
    private String clienteID;
    private Date date;
    private StatusPedido status;
    private Double valorTotal;
    private int tempo_em_minutos;
    private String obsevacoes;

    public SubPedidoRequestDTO() {}

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

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedidoHub() {
        return pedidoHub;
    }

    public void setPedidoHub(Pedido pedidoHub) {
        this.pedidoHub = pedidoHub;
    }

    public String getClienteID() {
        return clienteID;
    }

    public void setClienteID(String clienteID) {
        this.clienteID = clienteID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getTempo_em_minutos() {
        return tempo_em_minutos;
    }

    public void setTempo_em_minutos(int tempo_em_minutos) {
        this.tempo_em_minutos = tempo_em_minutos;
    }

    public String getObsevacoes() {
        return obsevacoes;
    }

    public void setObsevacoes(String obsevacoes) {
        this.obsevacoes = obsevacoes;
    }
}
