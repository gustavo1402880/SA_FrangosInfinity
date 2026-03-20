package org.frangosInfinity.core.entity.module.produto;

import jakarta.persistence.*;

@Entity
@Table(name = "produto")
public class Produto
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Double preco;

    @Column(name = "tempo_preparo_minutos")
    private Integer tempoPreparoMinuto;

    @Column(nullable = false)
    private Boolean disponivel;

    @Column(name = "imagem_url")
    private String imagemUrl;

    @Column(name = "vendas_ultimos_30_dias")
    private Integer vendasUltimos30dias;

    @Column(name = "preco_pendente_aprovacao")
    private Double precoPendenteAprovacao;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL)
    private Estoque estoque;

    public Produto() {};

    public Produto(String codigo, String nome, Double preco,Categoria categoria)
    {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.disponivel = true;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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

    public Boolean getDisponivel()
    {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel)
    {
        this.disponivel = disponivel;
    }

    public String getImagemUrl()
    {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl)
    {
        this.imagemUrl = imagemUrl;
    }

    public Integer getVendasUltimos30dias()
    {
        return vendasUltimos30dias;
    }

    public void setVendasUltimos30dias(Integer vendasUltimos30dias)
    {
        this.vendasUltimos30dias = vendasUltimos30dias;
    }

    public Double getPrecoPendenteAprovacao()
    {
        return precoPendenteAprovacao;
    }

    public void setPrecoPendenteAprovacao(Double precoPendenteAprovacao)
    {
        this.precoPendenteAprovacao = precoPendenteAprovacao;
    }

    public Categoria getCategoria()
    {
        return categoria;
    }

    public void setCategoria(Categoria categoria)
    {
        this.categoria = categoria;
    }

    public Estoque getEstoque()
    {
        return estoque;
    }

    public void setEstoque(Estoque estoque)
    {
        this.estoque = estoque;
    }

    public void solicitarAlteracaoPreco(Double novoPreco)
    {
        if (novoPreco == null || novoPreco <= 0)
        {
            throw new IllegalArgumentException("Preço inválido");
        }

        Double variacao = Math.abs((novoPreco - preco) / preco * 100);

        if (variacao > 20)
        {
            this.precoPendenteAprovacao = novoPreco;
            throw new IllegalArgumentException("Alteraçãoi acima de 20% requer aprovação. Slicitação registrada");
        }
        else
        {
            this.preco = novoPreco;
        }
    }

    public void aprovarAlteracaoPreco()
    {
        if (precoPendenteAprovacao != null)
        {
            this.preco = precoPendenteAprovacao;
            this.precoPendenteAprovacao = null;
        }
    }

    public void rejeitarAlteracaoPreco()
    {
        this.precoPendenteAprovacao = null;
    }

    public void incrementarVendas(Integer quantidade)
    {
        this.vendasUltimos30dias += quantidade;
    }
}
