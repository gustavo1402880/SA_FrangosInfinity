package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.entity.module.pedido.Carrinho;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.PedidoController;
import org.frangosInfinity.infrastructure.console.module.produto.AcessarCardapioView;
import org.frangosInfinity.infrastructure.console.module.produto.controller.CardapioController;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.ArrayList;
import java.util.List;

import static java.lang.ScopedValue.where;

public class CriarCarrinho
{

    static CardapioController controller;
    public static List<ItemPedido> CarrinhoPedidos()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          CARRINHO DE COMPRAS           ║");
        System.out.println("╚════════════════════════════════════════╝");

        AcessarCardapioView.Acessarcardapio();

        System.out.println("\n\n");

        System.out.println(" Digite o ID dos pedido que voce deseja pedir:");

        Long id = (long) Front.lInteiro();

        List<Produto> Subpedidos = controller.verCardapio();

        for(Produto p : Subpedidos)
        {
            if(p.getId().equals(id))
            {

            }
        }
        return ;
    }



}
