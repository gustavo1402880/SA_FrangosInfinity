package org.frangosInfinity.application.module.pedido.response;

public class ItemPedidoResponseDTO {

    private final Long id_ItemPedido;
    private final Long subPedidoID;
    private final Long produtoid;
    private final Integer quantidade;
    private final Double precoUnitario;
    private final String Observacao;
    private final Double subTotal;

    public ItemPedidoResponseDTO(Long id_ItemPedido, Long subPedidoID, Long produtoid, Integer quantidade, Double precoUnitario, String observacao, Double subTotal) {
        this.id_ItemPedido = id_ItemPedido;
        this.subPedidoID = subPedidoID;
        this.produtoid = produtoid;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        Observacao = observacao;
        this.subTotal = subTotal;
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
