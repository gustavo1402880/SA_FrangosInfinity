package org.frangosInfinity.application.module.pedido.request;

import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;
import java.util.List;

public class SubPedidoRequestDTO
{
    private Long pedidoId;
    private Long clienteId;
    private List<ItemPedidoRequestDTO> itens;

    public SubPedidoRequestDTO() {}

    public Long getPedidoId()
    {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId)
    {
        this.pedidoId = pedidoId;
    }

    public Long getClienteId()
    {
        return clienteId;
    }

    public void setClienteId(Long clienteId)
    {
        this.clienteId = clienteId;
    }

    public List<ItemPedidoRequestDTO> getItens()
    {
        return itens;
    }

    public void setItens(List<ItemPedidoRequestDTO> itens)
    {
        this.itens = itens;
    }
}
