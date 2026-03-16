package org.frangosInfinity.application.module.pedido.request;

import org.frangosInfinity.core.entity.module.pedido.ItemPedido;

public class ItemPedidoRequestDTO {

    private Long id_ItemPedido;
    private Long subPedidoID;
    private Long produtoid;
    private Integer quantidade;
    private Double precoUnitario;
    private String Observacao;
    private Double subTotal;

    public ItemPedidoRequestDTO(){};

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

    public void setId_ItemPedido(Long id_ItemPedido) {
        this.id_ItemPedido = id_ItemPedido;
    }

    public Long getSubPedidoID() {
        return subPedidoID;
    }

    public void setSubPedidoID(Long subPedidoID) {
        this.subPedidoID = subPedidoID;
    }

    public Long getProdutoid() {
        return produtoid;
    }

    public void setProdutoid(Long produtoid) {
        this.produtoid = produtoid;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getObservacao() {
        return Observacao;
    }

    public void setObservacao(String observacao) {
        Observacao = observacao;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
