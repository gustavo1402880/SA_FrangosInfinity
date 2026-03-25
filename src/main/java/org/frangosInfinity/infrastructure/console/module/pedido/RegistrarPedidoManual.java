package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.entity.module.usuario.Cliente;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.PedidoController;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.List;

public class RegistrarPedidoManual
{
<<<<<<< Updated upstream
    public static void RegistrarPedidoManual(Cliente cliente , Mesa mesa , List<ItemPedido> itens)
=======
    private static PedidoController controller;

    public static Pedido RegistrarPedidoManual(Cliente cliente , Mesa mesa , List<ItemPedido> itens)
>>>>>>> Stashed changes
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           REGISTRAR PEDIDO             ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("║ Qual o Id do pedido deseja escolher: ");
        Long id = (long) Front.lInteiro();
        System.out.println("╚════════════════════════════════════════╝");

         // nao sei oque esta dando de errado aqui, tenho que passar o pedido
        controller.registrarPedidoManual(id);
    }


}
