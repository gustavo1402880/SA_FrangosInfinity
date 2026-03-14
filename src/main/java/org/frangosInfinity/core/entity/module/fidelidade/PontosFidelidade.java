package org.frangosInfinity.core.entity.module.fidelidade;

import org.frangosInfinity.core.enums.TipoTransacaoPontos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PontosFidelidade
{
    private Long id;
    private Long clienteId;
    private Integer saldo;
    private List<TransacaoPontos> historico;
    private LocalDateTime dataValidade;

    public PontosFidelidade(Long clienteId)
    {
        this.clienteId = clienteId;
        this.saldo = 0;
        this.historico = new ArrayList<>();
        this.dataValidade = LocalDateTime.now().plusDays(90);
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getClienteId()
    {
        return clienteId;
    }

    public void setClienteId(Long clienteId)
    {
        this.clienteId = clienteId;
    }

    public Integer getSaldo()
    {
        return saldo;
    }

    public void setSaldo(Integer saldo)
    {
        this.saldo = saldo;
    }

    public List<TransacaoPontos> getHistorico()
    {
        return historico;
    }

    public void setHistorico(List<TransacaoPontos> historico)
    {
        this.historico = historico;
    }

    public LocalDateTime getDataValidade()
    {
        return dataValidade;
    }

    public void setDataValidade(LocalDateTime dataValidade)
    {
        this.dataValidade = dataValidade;
    }

    public void adicionarPontos(Integer quantidade)
    {
        this.saldo += quantidade;

        TransacaoPontos transacaoPontos = new TransacaoPontos(this.id, TipoTransacaoPontos.ACUMULO, quantidade);

        this.historico.add(transacaoPontos);

        if (this.dataValidade == null || this.dataValidade.isBefore(LocalDateTime.now()))
        {
            this.dataValidade = LocalDateTime.now().plusDays(90);
        }
    }

    public Boolean resgatarPontos(Integer quantidade)
    {
        if(this.saldo >= quantidade)
        {
            saldo -= quantidade;

            TransacaoPontos transacaoPontos = new TransacaoPontos(this.id, TipoTransacaoPontos.RESGATE, quantidade);

            this.historico.add(transacaoPontos);

            return true;
        }

        return false;
    }

    public void expirarPontos(Integer quantidade)
    {
        if(this.saldo.equals(quantidade))
        {
            this.saldo -= quantidade;

            TransacaoPontos transacaoPontos = new TransacaoPontos(this.id, TipoTransacaoPontos.EXPIRACAO, quantidade);

            this.historico.add(transacaoPontos);
        }
        else
        {
            this.saldo = 0;

            TransacaoPontos transacaoPontos = new TransacaoPontos(this.id, TipoTransacaoPontos.EXPIRACAO, this.saldo);

            this.historico.add(transacaoPontos);
        }
    }

    public Boolean verificarPontosSuficiente(Integer quantidade)
    {
        return this.saldo >= quantidade;
    }

    public Double calcularDesconto(Integer pontos, Double valorCompra)
    {
        int blocos = pontos / 20;
        double desconto = blocos * 5;

        return Math.min(desconto, valorCompra);
    }

    public String toString()
    {
            return "PontosFidelidade{"+
                    "clienteId="+clienteId + "\'"+
                    ", saldo="+saldo+
                    ", dataValidade="+dataValidade+
                    "}";
    }
}
