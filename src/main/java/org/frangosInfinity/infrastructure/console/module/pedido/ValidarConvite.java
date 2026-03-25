package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.infrastructure.console.module.pedido.controller.PedidoController;
import org.frangosInfinity.infrastructure.util.Front;

public class ValidarConvite
{
    private static PedidoController controller;

     public static void ValidarConvite(String token)
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║             VALIDAR PEDIDO             ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println(" Digite o ID que deseja validar: ");

        Long id = (long) Front.lInteiro();

        // nao sei oque esta dando de errado aqui, tenho que passar o pedido
        //controller.validarConvite(id);
    }
}
