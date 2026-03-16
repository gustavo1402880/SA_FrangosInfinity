package org.frangosInfinity.application.module.pedido.request;

import org.frangosInfinity.core.entity.module.pedido.ItemPedido;

public class ItemPedidoRequestDTO {

    private final Long id_ItemPedido;
    private final Long subPedidoID;
    private final Long produtoid;
    private final Integer quantidade;
    private final Double precoUnitario;
    private final String Observacao;
    private final Double subTotal;

    public ItemPedidoRequestDTO(Long id_ItemPedido, Long subPedidoID, Long produtoid, Integer quantidade, Double precoUnitario, String observacao, Double subTotal) {

        if(id_ItemPedido >= 1 && subPedidoID >= 1 && produtoid >= 1 && quantidade >= 1 && precoUnitario >= 1 && !observacao.isEmpty() && subTotal >= 1) {
            this.id_ItemPedido = id_ItemPedido;
            this.subPedidoID = subPedidoID;
            this.produtoid = produtoid;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
            this.Observacao = observacao;
            this.subTotal = subTotal;
        }

        throw new IllegalArgumentException("Erro - dados de Item pedido invalidos");

    }

    public Long getId_ItemPedido() {
        return id_ItemPedido;
    }

    public Long getSubPedidoID() {
        return subPedidoID;
    }

    public Long getProdutoid() {
        return produtoid;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public String getObservacao() {
        return Observacao;
    }

    public Double getSubTotal() {
        return subTotal;
    }
}
