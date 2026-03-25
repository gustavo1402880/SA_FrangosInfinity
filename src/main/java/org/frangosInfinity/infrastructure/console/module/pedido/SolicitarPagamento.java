package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.infrastructure.console.module.pedido.controller.SubPedidoController;
import org.frangosInfinity.infrastructure.util.Front;

public class SolicitarPagamento
{
    private static SubPedidoController controller;

    public static void solicitarPagamento()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          SOLICITAR PAGAMENTO           ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println(" Digite o id do pedido que deseja pagar: ");

        Long id = (long) Front.lInteiro();

        controller.solicitarPagamento(id);

    }
}
