package org.frangosInfinity.core.entity.module.produto;

import jakarta.persistence.*;
import org.frangosInfinity.core.entity.exception.BusinessException;

import java.time.LocalDateTime;

@Entity
@Table(name = "estoque")
public class Estoque
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "produto_id", unique = true, nullable = false)
    private Produto produto;

    @Column(name = "quantidade_atual", nullable = false)
    private Integer quantidadeAtual;

    @Column(name = "quantidade_minima")
    private Integer quantidadeMinima;

    @Column(name = "quantidade_maxima")
    private Integer quantidadeMaxima;

    @Column(name = "ultima_atualizacao")
    private LocalDateTime dataAtualizacao;

    public Estoque() {}

    public Estoque(Produto produto)
    {
        this.produto = produto;
        this.quantidadeAtual = 0;
        this.quantidadeMinima = 5;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Estoque(Produto produto, Integer quantidadeAtual, Integer quantidadeMinima, Integer quantidadeMaxima)
    {
        this.produto = produto;
        this.quantidadeAtual = quantidadeAtual;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Produto getProduto()
    {
        return produto;
    }

    public void setProduto(Produto produto)
    {
        this.produto = produto;
    }

    public Integer getQuantidadeAtual()
    {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Integer quantidadeAtual)
    {
        this.quantidadeAtual = quantidadeAtual;
    }

    public Integer getQuantidadeMinima()
    {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(Integer quantidadeMinima)
    {
        this.quantidadeMinima = quantidadeMinima;
    }

    public Integer getQuantidadeMaxima()
    {
        return quantidadeMaxima;
    }

    public void setQuantidadeMaxima(Integer quantidadeMaxima)
    {
        this.quantidadeMaxima = quantidadeMaxima;
    }

    public LocalDateTime getDataAtualizacao()
    {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao)
    {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Boolean temEstoque(Integer quantidadeRequisitada)
    {
        return quantidadeAtual >= quantidadeRequisitada;
    }

    public void baixarEstoque(Integer quantidade)
    {
        if (!temEstoque(quantidade))
        {
            throw new BusinessException("Estoque insuficiente!");
        }
        this.quantidadeAtual -= quantidade;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void reporEstoque(Integer quantidade)
    {
        this.quantidadeAtual += quantidade;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Boolean isEstoqueBaixo()
    {
        return quantidadeAtual <= quantidadeMinima;
    }

    public Boolean isEstoqueExcessivo()
    {
        return quantidadeAtual >= quantidadeMaxima;
    }
}
