package org.frangosInfinity.core.entity.module.pagamento;

import org.frangosInfinity.core.enums.StatusPagamento;
import org.frangosInfinity.core.enums.TipoPagamento;

import java.time.LocalDateTime;

public class Pagamento
{
    private Long id;
    private Long subPedidoId;
    private LocalDateTime dataHora;
    private Double valor;
    private StatusPagamento statusPagamento;
    private TipoPagamento tipoPagamento;
    private String codigoTransacao;

    public Pagamento() {}

    public Pagamento(Long id_SubPedido, LocalDateTime dataHora, Double valor, StatusPagamento statusPagamento, TipoPagamento tipoPagamento, String codigoTransacao)
    {
        this.subPedidoId= id_SubPedido;
        this.dataHora = dataHora;
        this.valor = valor;
        this.statusPagamento = statusPagamento;
        this.tipoPagamento = tipoPagamento;
        this.codigoTransacao = codigoTransacao;
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
