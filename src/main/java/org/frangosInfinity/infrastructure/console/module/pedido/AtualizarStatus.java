package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.enums.StatusPedido;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.List;

public class AtualizarStatus
{
    private PedidoController controller;
    public void AtualizarPedido(Long pedidoID , StatusPedido novoStatus)
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║            ATUALIZAR PEDIDO            ║");
        System.out.println("╠════════════════════════════════════════╣");

        System.out.println(" Digite o id do pedido:");

        Long id = (long) Front.lInteiro();

        List<Pedido> pedidos = controller.ListarPedidos();

        String tipo ;
        for(Pedido p : pedidos)
        {
            if(p.getId().equals(id))
            {
                tipo = p.getTipo();
            }
        }
        // a terminar
    }
}
