package org.frangosInfinity.core.service.module.pedido;

import org.frangosInfinity.application.module.pedido.request.ItemPedidoRequestDTO;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;

public class ItemPedidoService {

    public Double calcularSubTotal(ItemPedidoRequestDTO itemPedido){

        if(itemPedido != null){

            return itemPedido.getSubTotal();

        }

        throw new RuntimeException("Erro ao calcularSubtotal");
    }
}
