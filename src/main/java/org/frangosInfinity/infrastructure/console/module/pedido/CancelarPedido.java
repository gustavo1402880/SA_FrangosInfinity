package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.infrastructure.util.Front;

import java.util.Scanner;

public class CancelarPedido
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║


    private PedidoController controller;
    private Scanner sc = new Scanner(System.in);

    public boolean CancelarPedido(Long pedidoID , String justificativa)
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║             CANCELAR PEDIDO            ║");
        System.out.println("╠════════════════════════════════════════╣");

        System.out.print("║ Digite o ID do pedido: ");

        Long pedidoId = (long) Front.lInteiro();

        try
        {
            controller.CancelarPedido();

            System.out.println("║ Pedido cancelado com sucesso!");
        }catch (Exception e)
        {
            System.out.println("║ Erro ao cancelar pedido: " + e.getMessage());
            return false;
        }


    }
}
