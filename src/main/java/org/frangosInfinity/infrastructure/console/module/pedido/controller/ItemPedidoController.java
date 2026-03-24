package org.frangosInfinity.infrastructure.console.module.pedido.controller;

import org.frangosInfinity.application.module.pedido.request.ItemPedidoRequestDTO;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.service.module.pedido.ItemPedidoService;

public class ItemPedidoController {

    public Double calcularSubTotal(ItemPedido itemPedido){

        if(itemPedido != null){

            ItemPedidoRequestDTO itemPedidoRequestDTO = new ItemPedidoRequestDTO(itemPedido.getId_ItemPedido(),
                    itemPedido.getSubPedido(),itemPedido.getProduto(),itemPedido.getQuantidade(),itemPedido.getPrecoUnitario(),
                    itemPedido.getObservacao(),itemPedido.calcularSubTotal());

            ItemPedidoService itemPedidoService = new ItemPedidoService();

            Double d;
            d = itemPedidoService.calcularSubTotal(itemPedidoRequestDTO);

            return d;
        }

        throw new RuntimeException("Erro - o item pedido não pode ser nulo");
    }
}
