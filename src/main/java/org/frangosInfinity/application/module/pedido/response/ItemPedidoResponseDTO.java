package org.frangosInfinity.application.module.pedido.response;

import org.frangosInfinity.core.entity.module.pedido.ItemPedido;

public class ItemPedidoResponseDTO
{
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private Integer quantidade;
    private Double precoUnitario;
    private String observacao;
    private Double subTotal;
    private Integer tempoPreparoMinutos;

    public ItemPedidoResponseDTO() {}

    public static ItemPedidoResponseDTO fromEntity(ItemPedido itemPedido)
    {
        ItemPedidoResponseDTO response = new ItemPedidoResponseDTO();
        response.setId(itemPedido.getId());
        response.setProdutoId(itemPedido.getProdutoId());
        response.setNomeProduto(itemPedido.getNomeProduto());
        response.setQuantidade(itemPedido.getQuantidade());
        response.setPrecoUnitario(itemPedido.getPrecoUnitario());
        response.setSubTotal(itemPedido.getSubTotal());
        response.setTempoPreparoMinutos(itemPedido.getTempoPreparoEstimado());
        response.setObservacao(itemPedido.getObservacao());

        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
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
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getTempoPreparoMinutos() {
        return tempoPreparoMinutos;
    }

    public void setTempoPreparoMinutos(Integer tempoPreparoMinutos) {
        this.tempoPreparoMinutos = tempoPreparoMinutos;
    }
}
