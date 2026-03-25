package org.frangosInfinity.core.entity.module.pedido;

import org.frangosInfinity.application.module.pedido.response.SubPedidoResponseDTO;
import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;
import java.time.LocalDateTime;

public class SubPedido {

    // Atributos

    private Long id;
    private Pedido pedidoHub;
    private Long clienteID;
    private Date date;
    private StatusPedido status;
    private Double valorTotal;
    private int tempo_em_minutos;
    private String obsevacoes;

    // Construtores

    public SubPedido(){}

    public SubPedido(Pedido pedidoHub, Long clienteID, Date date, StatusPedido status, Double valorTotal, int tempo_em_minutos, String obsevacoes) {
        this.pedidoHub = pedidoHub;
        this.clienteID = clienteID;
        this.date = date;
        this.status = status;
        this.valorTotal = valorTotal;
        this.tempo_em_minutos = tempo_em_minutos;
        this.obsevacoes = obsevacoes;
    }

    // Getters & Setters


    public void setObsevacoes(String obsevacoes) {
        this.obsevacoes = obsevacoes;
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

    public Long getClienteID() {
        return clienteID;
    }

    public void setClienteID(Long clienteID) {
        this.clienteID = clienteID;
    }

    public java.sql.Date getDate() {
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

    // Metodos
}
