package org.frangosInfinity.core.entity.module.pagamento;

import jakarta.persistence.*;
import org.frangosInfinity.core.enums.StatusPagamento;
import org.frangosInfinity.core.enums.TipoPagamento;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
public class Pagamento
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subpedido_id", nullable = false )
    private Long subPedidoId;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private Double valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao", nullable = false)
    private StatusPagamento statusPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento", nullable = false)
    private TipoPagamento tipoPagamento;

    @Column(name = "codigo_transacao", nullable = false)
    private String codigoTransacao;

    @OneToOne(mappedBy = "pagamento", cascade = CascadeType.ALL)
    private TransacaoPIX transacaoPIX;

    @OneToOne(mappedBy = "pagamento", cascade = CascadeType.ALL)
    private Comprovante comprovante;

    public Pagamento() {}

    public Pagamento(Long id_SubPedido, LocalDateTime dataHora, Double valor, StatusPagamento statusPagamento, TipoPagamento tipoPagamento, String codigoTransacao, Comprovante comprovante, TransacaoPIX transacaoPIX )
    {
        this.subPedidoId= id_SubPedido;
        this.dataHora = dataHora;
        this.valor = valor;
        this.statusPagamento = statusPagamento;
        this.tipoPagamento = tipoPagamento;
        this.codigoTransacao = codigoTransacao;
        this.comprovante = comprovante;
        this.transacaoPIX = transacaoPIX;
    }

    public Long getId_Pagamento()
    {
        return id;
    }

    public void setId_Pagamento(Long id)
    {
        this.id = id;
    }

    public Long getId_SubPedido()
    {
        return subPedidoId;
    }

    public void setId_SubPedido(Long id_SubPedido)
    {
        this.subPedidoId= id_SubPedido;
    }

    public LocalDateTime getDataHora()
    {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora)
    {
        this.dataHora = dataHora;
    }

    public Double getValor()
    {
        return valor;
    }

    public void setValor(Double valor)
    {
        this.valor = valor;
    }

    public StatusPagamento getStatusPagamento()
    {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento)
    {
        this.statusPagamento = statusPagamento;
    }

    public TipoPagamento getTipoPagamento()
    {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento)
    {
        this.tipoPagamento = tipoPagamento;
    }

    public String getCodigoTransacao()
    {
        return codigoTransacao;
    }

    public void setCodigoTransacao(String codigoTransacao)
    {
        this.codigoTransacao = codigoTransacao;
    }

    public TransacaoPIX getTransacaoPIX()
    {
        return transacaoPIX;
    }

    public void setTransacaoPIX(TransacaoPIX transacaoPIX)
    {
        this.transacaoPIX = transacaoPIX;
    }

    public Comprovante getComprovante()
    {
        return comprovante;
    }

    public void setComprovante(Comprovante comprovante)
    {
        this.comprovante = comprovante;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", subPedidoId=" + subPedidoId +
                ", valor=" + valor +
                ", status=" + statusPagamento +
                ", tipo=" + tipoPagamento +
                '}';
    }
}
