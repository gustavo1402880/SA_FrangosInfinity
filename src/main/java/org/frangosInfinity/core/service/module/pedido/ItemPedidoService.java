package org.frangosInfinity.core.service.module.pedido;

import org.frangosInfinity.core.entity.module.pedido.ItemPedido;

public class ItemPedidoService {

    public Double calcularSubtotal(ItemPedido itemPedido)throws Exception{

        if(itemPedido != null){

            return itemPedido.calcularSubTotal();

        }

        throw new Exception("Erro ao calcularSubtotal");
    }
}
