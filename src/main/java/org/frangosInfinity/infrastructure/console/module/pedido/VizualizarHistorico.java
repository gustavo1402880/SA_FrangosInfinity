package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.SubPedidoController;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.List;

public class VizualizarHistorico
{

    private static SubPedidoController controller;

    public static void  vizualizarHistorico()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         HISTORICO DE CLIENTES          ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println(" Digite o ID do cliente que seja ver o histórico: ");

        Long id = (long) Front.lInteiro();

        controller.listarHistorioCliente(id);

    }
}
