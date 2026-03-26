package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.PedidoController;
import org.frangosInfinity.infrastructure.util.Front;

import java.sql.Date;
import java.util.Scanner;

public class FazerPedido
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║

    private static PedidoController controller;

    private Scanner sc = new Scanner(System.in);

    public static void fazerPedido()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║              FAZER PEDIDO              ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  Digite o ID do Pedido:");
        Long idPedido = (long) Front.lInteiro();
        System.out.println("╚════════════════════════════════════════╝");

        Date dataHora = Date.valueOf()

        controller.criarPedidoHub()
    }
}
