package org.frangosInfinity.core.entity.module.pedido;

import jakarta.persistence.*;

@Entity
@Table(name = "item_pedido")
public class ItemPedido
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "sub_pedido_id", nullable = false)
    private SubPedido subPedido;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Column(nullable = false, length = 100)
    private String nomeProduto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 500)
    private String Observacao;

    @Column(name = "preco_unitario", nullable = false)
    private Double precoUnitario;

    @Column(name = "sub_total", nullable = false)
    private Double subTotal;

    @Column(name = "tempo_preparo_minutos", nullable = false)
    private Integer tempoPreparoEstimado;

    public ItemPedido() {}

    public ItemPedido(Long produtoId, String nome, Integer quantidade, Double precoUnitario, Integer tempoPreparoEstimado)
    {
        this.produtoId = produtoId;
        this.nomeProduto = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.tempoPreparoEstimado = tempoPreparoEstimado;
        this.subTotal = calcularSubTotal();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public SubPedido getSubPedido()
    {
        return subPedido;
    }

    public void setSubPedido(SubPedido subPedido)
    {
        this.subPedido = subPedido;
    }

    public Long getProdutoId()
    {
        return produtoId;
    }

    public void setProdutoId(Long produtoId)
    {
        this.produtoId = produtoId;
    }

    public String getNomeProduto()
    {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto)
    {
        this.nomeProduto = nomeProduto;
    }

    public Integer getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade)
    {
        this.quantidade = quantidade;
        this.subTotal = calcularSubTotal();
    }

    public String getObservacao()
    {
        return Observacao;
    }

    public void setObservacao(String observacao)
    {
        Observacao = observacao;
    }

    public Double getPrecoUnitario()
    {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario)
    {
        this.precoUnitario = precoUnitario;
    }

    public Double getSubTotal()
    {
        return subTotal;
    }

    public void setSubTotal(Double subTotal)
    {
        this.subTotal = subTotal;
    }

    public Integer getTempoPreparoEstimado()
    {
        return tempoPreparoEstimado;
    }

    public void setTempoPreparoEstimado(Integer tempoPreparoEstimado)
    {
        this.tempoPreparoEstimado = tempoPreparoEstimado;
    }

    public Double calcularSubTotal()
    {
        return quantidade * precoUnitario;
    }
}
