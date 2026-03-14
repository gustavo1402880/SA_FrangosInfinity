package org.frangosInfinity.application.module.fidelidade.response;

import org.frangosInfinity.core.enums.TipoTransacaoPontos;

public class TransacaoResponseDTO
{
    private Long id;
    private String data;
    private TipoTransacaoPontos tipo;
    private Integer quantidade;

    public TransacaoResponseDTO() {}

    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }

    public String getData()
    {
        return data;
    }
    public void setData(String data)
    {
        this.data = data;
    }

    public TipoTransacaoPontos getTipo()
    {
        return tipo;
    }
    public void setTipo(TipoTransacaoPontos tipo)
    {
        this.tipo = tipo;
    }

    public Integer getQuantidade()
    {
        return quantidade;
    }
    public void setQuantidade(Integer quantidade)
    {
        this.quantidade = quantidade;
    }

    public String getSinal()
    {
        return tipo == TipoTransacaoPontos.ACUMULO ? "+" : "-";
    }

    public String getDescricaoFormatada()
    {
        return String.format("%s %d pontos em %s", getSinal(), quantidade, data);
    }
}
