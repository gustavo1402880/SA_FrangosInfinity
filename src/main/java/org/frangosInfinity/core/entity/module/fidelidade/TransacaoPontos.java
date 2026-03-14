package org.frangosInfinity.core.entity.module.fidelidade;

import org.frangosInfinity.core.enums.TipoTransacaoPontos;

import java.time.LocalDateTime;

public class TransacaoPontos
{
    private Long id;
    private Long pontosFidelidadeId;
    private LocalDateTime data;
    private TipoTransacaoPontos tipoTransacaoPontos;
    private Integer quantidade;

    public TransacaoPontos(Long pontosFidelidadeId, TipoTransacaoPontos tipoTransacaoPontos, Integer quantidade)
    {
        this.pontosFidelidadeId = pontosFidelidadeId;
        this.data = LocalDateTime.now();
        this.quantidade = quantidade;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getPontosFidelidadeId()
    {
        return pontosFidelidadeId;
    }

    public void setPontosFidelidadeId(Long pontosFidelidadeId)
    {
        this.pontosFidelidadeId = pontosFidelidadeId;
    }

    public LocalDateTime getData()
    {
        return data;
    }

    public void setData(LocalDateTime data)
    {
        this.data = data;
    }

    public TipoTransacaoPontos getTipoTransacaoPontos()
    {
        return tipoTransacaoPontos;
    }

    public void setTipoTransacaoPontos(TipoTransacaoPontos tipoTransacaoPontos)
    {
        this.tipoTransacaoPontos = tipoTransacaoPontos;
    }

    public Integer getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade)
    {
        this.quantidade = quantidade;
    }

    public boolean isAcumulo()
    {
        return this.tipoTransacaoPontos == TipoTransacaoPontos.ACUMULO;
    }

    public boolean isResgate()
    {
        return this.tipoTransacaoPontos == TipoTransacaoPontos.RESGATE;
    }

    public boolean isExpiracao()
    {
        return this.tipoTransacaoPontos == TipoTransacaoPontos.EXPIRACAO;
    }
}
