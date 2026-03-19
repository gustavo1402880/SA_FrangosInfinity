package org.frangosInfinity.core.entity.module.fidelidade;

import jakarta.persistence.*;

@Entity
@Table(name = "regras_fidelidade")
public class RegrasFidelidade
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pontos_por_real", nullable = false)
    private Double pontosPorReal;

    @Column(name = "dias_expiracao", nullable = false)
    private Integer diasExpiracao;

    @Column(name = "pontos_minimos_resgate", nullable = false)
    private Integer pontosMinimosResgate;

    @Column(name = "valor_desconto_por_bloco", nullable = false)
    private Double valorDescontoPorBloco;

    @Column(name = "pontos_por_bloco", nullable = false)
    private Integer pontosPorBloco;

    @Column(name = "valor_minimo_produto_desconto", nullable = false)
    private Double valorMinimoProdutoDesconto;

    @Column(nullable = false)
    private Boolean ativo;

    public RegrasFidelidade()
    {
        this.pontosPorReal = 0.25;
        this.diasExpiracao = 90;
        this.pontosMinimosResgate = 20;
        this.valorDescontoPorBloco = 5.0;
        this.pontosPorBloco = 20;
        this.valorMinimoProdutoDesconto = 5.0;
        this.ativo = true;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Double getPontosPorReal()
    {
        return pontosPorReal;
    }
    public void setPontosPorReal(Double pontosPorReal)
    {
        this.pontosPorReal = pontosPorReal;
    }

    public Integer getDiasExpiracao()
    {
        return diasExpiracao;
    }
    public void setDiasExpiracao(Integer diasExpiracao)
    {
        this.diasExpiracao = diasExpiracao;
    }

    public Integer getPontosMinimosResgate()
    {
        return pontosMinimosResgate;
    }

    public void setPontosMinimosResgate(Integer pontosMinimosResgate)
    {
        this.pontosMinimosResgate = pontosMinimosResgate;
    }

    public Double getValorDescontoPorBloco()
    {
        return valorDescontoPorBloco;
    }

    public void setValorDescontoPorBloco(Double valorDescontoPorBloco)
    {
        this.valorDescontoPorBloco = valorDescontoPorBloco;
    }

    public Integer getPontosPorBloco()
    {
        return pontosPorBloco;
    }

    public void setPontosPorBloco(Integer pontosPorBloco)
    {
        this.pontosPorBloco = pontosPorBloco;
    }

    public Double getValorMinimoProdutoDesconto()
    {
        return valorMinimoProdutoDesconto;
    }

    public void setValorMinimoProdutoDesconto(Double valorMinimoProdutoDesconto)
    {
        this.valorMinimoProdutoDesconto = valorMinimoProdutoDesconto;
    }

    public boolean isAtivo()
    {
        return ativo;
    }

    public void setAtivo(boolean ativo)
    {
        this.ativo = ativo;
    }

    public int calcularPontosPorValor(double valor)
    {
        return (int) (valor * this.pontosPorReal);
    }

    public double calcularDescontoPorPontos(Integer pontos)
    {
        if (pontos < pontosMinimosResgate)
        {
            return 0;
        }
        Integer blocos = pontos / pontosPorBloco;
        return blocos * valorDescontoPorBloco;
    }

    public boolean podeResgatar(Integer pontos, Double valorProduto)
    {
        return pontos >= pontosMinimosResgate && valorProduto >= valorMinimoProdutoDesconto;
    }

    public String toString()
    {
        return "RegrasFidelidade{" +
                "pontosPorR$=" + pontosPorReal +
                ", expiração=" + diasExpiracao + " dias" +
                ", mínimo=" + pontosMinimosResgate + " pontos" +
                ", desconto=R$" + valorDescontoPorBloco + "/" + pontosPorBloco + " pontos" +
                '}';
    }
}
