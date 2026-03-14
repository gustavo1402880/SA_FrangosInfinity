package org.frangosInfinity.core.entity.module.fidelidade;

public class RegrasFidelidade
{
    private Long id;
    private double pontosPorReal;
    private int diasExpiracao;
    private int pontosMinimosResgate;
    private double valorDescontoPorBloco;
    private int pontosPorBloco;
    private double valorMinimoProdutoDesconto;
    private boolean ativo;

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

    public double getPontosPorReal()
    {
        return pontosPorReal;
    }
    public void setPontosPorReal(double pontosPorReal)
    {
        this.pontosPorReal = pontosPorReal;
    }

    public int getDiasExpiracao()
    {
        return diasExpiracao;
    }
    public void setDiasExpiracao(int diasExpiracao)
    {
        this.diasExpiracao = diasExpiracao;
    }

    public int getPontosMinimosResgate()
    {
        return pontosMinimosResgate;
    }

    public void setPontosMinimosResgate(int pontosMinimosResgate)
    {
        this.pontosMinimosResgate = pontosMinimosResgate;
    }

    public double getValorDescontoPorBloco()
    {
        return valorDescontoPorBloco;
    }

    public void setValorDescontoPorBloco(double valorDescontoPorBloco)
    {
        this.valorDescontoPorBloco = valorDescontoPorBloco;
    }

    public int getPontosPorBloco()
    {
        return pontosPorBloco;
    }

    public void setPontosPorBloco(int pontosPorBloco)
    {
        this.pontosPorBloco = pontosPorBloco;
    }

    public double getValorMinimoProdutoDesconto()
    {
        return valorMinimoProdutoDesconto;
    }

    public void setValorMinimoProdutoDesconto(double valorMinimoProdutoDesconto)
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

    public double calcularDescontoPorPontos(int pontos)
    {
        if (pontos < pontosMinimosResgate)
        {
            return 0;
        }
        int blocos = pontos / pontosPorBloco;
        return blocos * valorDescontoPorBloco;
    }

    public boolean podeResgatar(int pontos, double valorProduto)
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
