package org.frangosInfinity.application.module.pedido.request;

public class PedidoRequestDTO
{
    private Long mesaId;
    private Long atendenteId;
    private String obervacao;

    public PedidoRequestDTO() {}

    public Long getMesaId()
    {
        return mesaId;
    }

    public void setMesaId(Long mesaId)
    {
        this.mesaId = mesaId;
    }

    public Long getAtendenteId()
    {
        return atendenteId;
    }

    public void setAtendenteId(Long atendenteId)
    {
        this.atendenteId = atendenteId;
    }

    public String getObervacao()
    {
        return obervacao;
    }

    public void setObervacao(String obervacao)
    {
        this.obervacao = obervacao;
    }
}
