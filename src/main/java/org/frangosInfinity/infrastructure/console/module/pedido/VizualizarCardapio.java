package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.entity.module.produto.Cardapio;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.PedidoController;

import java.util.List;

public class VizualizarCardapio
{

    private static PedidoController controller;
    public static void Vizualizarsubpedido()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                CARDAPIO                ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<SubPedido> produtos = controller.listarSubPedidos();

        for(SubPedido p : produtos)
        {
            System.out.println("\n");
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║            ID: %-23║%n"+ p.getId());
            System.out.println("║ ID do Cliente: %-23║%n"+ p.getClienteID());
            System.out.println("║        Pedido: %-23║%n"+p.getPedidoHub());
            System.out.println("║   Observaçoes: %-23║%n"+ p.getObsevacoes());
            System.out.println("║        Status: %-23║%n"+ p.getStatus());
            System.out.println("║   Valor Total: %-23║%n"+ p.getValorTotal());
            System.out.println("╚════════════════════════════════════════╝");

        }
    }
}
