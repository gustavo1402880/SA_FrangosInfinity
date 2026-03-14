package org.frangosInfinity.application.module.fidelidade.request;

public class ResgateRequestDTO
{
    private Long clienteId;
    private Integer pontos;
    private Double valorCompra;
    private Long pedidoId;
    private String descricao;

    private ResgateRequestDTO() {}

    public Long getClienteId()
    {
        return clienteId;
    }

    public void setClienteId(Long clienteId)
    {
        this.clienteId = clienteId;
    }

    public Integer getPontos()
    {
        return pontos;
    }

    public void setPontos(Integer pontos)
    {
        this.pontos = pontos;
    }

    public Double getValorCompra()
    {
        return valorCompra;
    }

    public void setValorCompra(Double valorCompra)
    {
        this.valorCompra = valorCompra;
    }

    public Long getPedidoId()
    {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId)
    {
        this.pedidoId = pedidoId;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public boolean isValid()
    {
        return clienteId != null && clienteId > 0 && pontos != null && pontos > 0;
    }
}
