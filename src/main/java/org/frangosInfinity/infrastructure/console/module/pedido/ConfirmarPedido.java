package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.infrastructure.util.Front;

import java.sql.SQLOutput;
import java.util.Scanner;

public class ConfirmarPedido
{

    private Scanner sc = new Scanner(System.in);


    public static void confirmarPedido()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║            CONFIRMAR PEDIDO            ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println(" Digite o id do Pedido: ");

        Long id = (long) Front.lInteiro();


    }
}
