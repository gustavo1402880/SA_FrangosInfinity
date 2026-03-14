package org.frangosInfinity.core.entity.module.pedido;

public class ItemPedido {

    // Atributos

    private Long id_ItemPedido;
    private Long subPedidoID;
    private Long produtoid;
    private Integer quantidade;
    private Double precoUnitario;
    private String Observacao;
    private Double subTotal;

    // Construtores


    public ItemPedido() {}

    public ItemPedido(Long id_ItemPedido,Long subPedido,Long produto, Integer quantidade, Double precoUnitario, String observacao, Double subTotal) {
        this.id_ItemPedido = id_ItemPedido;
        this.subPedidoID = subPedido;
        this.produtoid = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        Observacao = observacao;
        this.subTotal = subTotal;
    }

    // Getters & Setters


    public Long getId_ItemPedido() {
        return id_ItemPedido;
    }

    public void setId_ItemPedido(Long id_ItemPedido) {
        this.id_ItemPedido = id_ItemPedido;
    }

    public Long getProduto() {
        return produtoid;
    }

    public void setProduto(Long produto) {
        this.produtoid = produto;
    }

    public Long getSubPedido() {
        return subPedidoID;
    }

    public void setSubPedido(Long subPedido) {
        this.subPedidoID = subPedido;
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

    // Metodos

    public Double calcularSubTotal(){

        double Subtotal;

        subTotal = quantidade * precoUnitario;

        return subTotal;
    }
}
