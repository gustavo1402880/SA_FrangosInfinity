package org.frangosInfinity.core.entity.module.produto;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nome;

    @Column(length = 200)
    private String descricao;

    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao;

    @Column(nullable = false)
    private Boolean ativa;

    @OneToMany(mappedBy = "categoria")
    private List<Produto> produtos;

    public Categoria() {}

    public Categoria(String nome, Integer ordemExibicao)
    {
        this.nome = nome;
        this.ordemExibicao = ordemExibicao;
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

    public List<Produto> getProdutos()
    {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos)
    {
        this.produtos = produtos;
    }
}
