package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.Scanner;

public class FazerPedido
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║

    private Scanner sc = new Scanner(System.in);

    public static void fazerPedido()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║              FAZER PEDIDO              ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  Digite o ID do cliente: ");
        Long idCliente = (long) Front.lInteiro();
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  Digite o ID do Pedido:");
        Long idPedido = (long) Front.lInteiro();
        System.out.println("╚════════════════════════════════════════╝");
    }
}
