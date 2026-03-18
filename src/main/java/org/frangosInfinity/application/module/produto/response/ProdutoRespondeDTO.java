package org.frangosInfinity.application.module.produto.response;

import org.frangosInfinity.core.entity.module.produto.Categoria;

public class ProdutoRespondeDTO {

    private final Long id;
    private final String codigo;
    private final String nome;
    private final String descricao;
    private final Double preco;
    private final Double custo;
    private final Integer tempoPreparoMinuto;
    private final boolean disponivel;
    private final String imagemUrl;
    private final Integer vendasUltimos30dias;
    private final Double precoPendenteAprovacao;
    private final Categoria categoria;

    public ProdutoRespondeDTO(Long id, String codigo, String nome, String descricao, Double preco, Double custo, Integer tempoPreparoMinuto, boolean disponivel, String imagemUrl, Integer vendasUltimos30dias, Double precoPendenteAprovacao, Categoria categoria) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.custo = custo;
        this.tempoPreparoMinuto = tempoPreparoMinuto;
        this.disponivel = disponivel;
        this.imagemUrl = imagemUrl;
        this.vendasUltimos30dias = vendasUltimos30dias;
        this.precoPendenteAprovacao = precoPendenteAprovacao;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public Double getCusto() {
        return custo;
    }

    public Integer getTempoPreparoMinuto() {
        return tempoPreparoMinuto;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public Integer getVendasUltimos30dias() {
        return vendasUltimos30dias;
    }

    public Double getPrecoPendenteAprovacao() {
        return precoPendenteAprovacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}
