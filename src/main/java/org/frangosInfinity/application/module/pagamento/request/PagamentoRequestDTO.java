package org.frangosInfinity.application.module.pagamento.request;

import org.frangosInfinity.core.enums.TipoPagamento;

public class PagamentoRequestDTO
{
    private Long subPedidoId;
    private Double valor;
    private TipoPagamento tipo;

    public PagamentoRequestDTO() {}

    public PagamentoRequestDTO(Long subPedidoId, Double valor, TipoPagamento tipo)
    {
        this.subPedidoId = subPedidoId;
        this.valor = valor;
        this.tipo = tipo;
    }

    public Long getSubPedidoId() {
        return subPedidoId;
    }

    public void setSubPedidoId(Long subPedidoId) {
        this.subPedidoId = subPedidoId;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoPagamento tipo) {
        this.tipo = tipo;
    }

    public Boolean valido()
    {
        return subPedidoId != null && subPedidoId > 0
                && valor != null && valor > 0
                && tipo != null;
    }
}
