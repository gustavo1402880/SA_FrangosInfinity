package org.frangosInfinity.infrastructure.console.module.pedido;

import com.sun.mail.imap.SortTerm;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.PedidoController;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.SubPedidoController;
import org.frangosInfinity.infrastructure.util.Front;

public class CancelarPedido
{
    private static PedidoController controller;

    public static void cancelarPedido()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║            CANCELAR PEDIDO             ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println(" Qual o id do pedido que deseja cancelar:");

        Long id = (long) Front.lInteiro();

        controller.cancelarPedido(id);

    }
}
