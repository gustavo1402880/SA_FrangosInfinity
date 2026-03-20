package org.frangosInfinity.application.module.produto.response;

import org.frangosInfinity.core.entity.module.produto.Categoria;

public class CategoriaResponseDTO
{
    private Long id;
    private String nome;
    private String descricao;
    private Integer ordemExibicao;
    private Boolean ativa;
    private Integer quantidadeProdutos;
    private Boolean sucesso;
    private String mensagem;

    public CategoriaResponseDTO() {}

    public static CategoriaResponseDTO fromEntity(Categoria categoria)
    {
        CategoriaResponseDTO response = new CategoriaResponseDTO();
        response.setId(categoria.getId());
        response.setNome(categoria.getNome());
        response.setDescricao(categoria.getDescricao());
        response.setOrdemExibicao(categoria.getOrdemExibicao());
        response.setAtiva(categoria.getAtiva());
        response.setQuantidadeProdutos(categoria.getProdutos() != null ? categoria.getProdutos().size() : 0);
        response.setSucesso(true);
        return response;
    }

    public static CategoriaResponseDTO erro(String mensagem)
    {
        CategoriaResponseDTO response = new CategoriaResponseDTO();
        response.setSucesso(false);
        response.setMensagem(mensagem);
        return response;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public Integer getOrdemExibicao()
    {
        return ordemExibicao;
    }

    public void setOrdemExibicao(Integer ordemExibicao)
    {
        this.ordemExibicao = ordemExibicao;
    }

    public Boolean getAtiva()
    {
        return ativa;
    }

    public void setAtiva(Boolean ativa)
    {
        this.ativa = ativa;
    }

    public Integer getQuantidadeProdutos()
    {
        return quantidadeProdutos;
    }

    public void setQuantidadeProdutos(Integer quantidadeProdutos)
    {
        this.quantidadeProdutos = quantidadeProdutos;
    }

    public Boolean getSucesso()
    {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso)
    {
        this.sucesso = sucesso;
    }

    public String getMensagem()
    {
        return mensagem;
    }

    public void setMensagem(String mensagem)
    {
        this.mensagem = mensagem;
    }
}
