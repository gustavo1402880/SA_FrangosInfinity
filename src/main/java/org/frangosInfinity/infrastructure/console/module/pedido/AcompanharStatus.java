package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.enums.StatusPedido;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.SubPedidoController;
import org.frangosInfinity.infrastructure.util.Front;

public class AcompanharStatus
{
    private static SubPedidoController controller;

    public static void acompanharstatus()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           ACOMPANHAR STATUS            ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println(" Digite o ID do pedido: ");

        Long id = (long) Front.lInteiro();

        controller.consultarStatus(id);
    }
}
