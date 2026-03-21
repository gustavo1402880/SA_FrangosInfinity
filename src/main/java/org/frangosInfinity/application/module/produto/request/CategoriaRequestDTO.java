package org.frangosInfinity.application.module.produto.request;

public class CategoriaRequestDTO
{
    private String nome;
    private String descricao;
    private Integer ordemExibicao;

    public CategoriaRequestDTO() {}

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

    public Integer getOrdemExibicao()
    {
        return ordemExibicao;
    }

    public void setOrdemExibicao(Integer ordemExibicao)
    {
        this.ordemExibicao = ordemExibicao;
    }
}
