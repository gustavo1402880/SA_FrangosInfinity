package org.frangosInfinity.application.module.produto.request;

public class ProdutoRequestDTO
{
    private String codigo;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer tempoPreparoMinuto;
    private String imagemUrl;
    private Long categoriaId;
    private Integer estoqueInicial;
    private Integer estoqueMinimo;
    private Integer estoqueMaximo;

    public ProdutoRequestDTO() {}

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public Double getPreco()
    {
        return preco;
    }

    public void setPreco(Double preco)
    {
        this.preco = preco;
    }

    public Integer getTempoPreparoMinuto()
    {
        return tempoPreparoMinuto;
    }

    public void setTempoPreparoMinuto(Integer tempoPreparoMinuto)
    {
        this.tempoPreparoMinuto = tempoPreparoMinuto;
    }

    public String getImagemUrl()
    {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl)
    {
        this.imagemUrl = imagemUrl;
    }

    public Long getCategoriaId()
    {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId)
    {
        this.categoriaId = categoriaId;
    }

    public Integer getEstoqueInicial()
    {
        return estoqueInicial;
    }

    public void setEstoqueInicial(Integer estoqueInicial)
    {
        this.estoqueInicial = estoqueInicial;
    }

    public Integer getEstoqueMinimo()
    {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo)
    {
        this.estoqueMinimo = estoqueMinimo;
    }

    public Integer getEstoqueMaximo()
    {
        return estoqueMaximo;
    }

    public void setEstoqueMaximo(Integer estoqueMaximo)
    {
        this.estoqueMaximo = estoqueMaximo;
    }
}
