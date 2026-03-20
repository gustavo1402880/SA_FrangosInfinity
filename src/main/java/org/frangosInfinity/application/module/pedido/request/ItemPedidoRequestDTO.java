package org.frangosInfinity.application.module.pedido.request;

public class ItemPedidoRequestDTO
{
    private Long produtoId;

    private Integer quantidade;

    private String observacao;

    public ItemPedidoRequestDTO() {}

    public Long getProdutoId()
    {
        return produtoId;
    }

    public void setProdutoId(Long produtoId)
    {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade)
    {
        this.quantidade = quantidade;
    }

    public String getObservacao()
    {
        return observacao;
    }

    public void setObservacao(String observacao)
    {
        this.observacao = observacao;
    }
}
