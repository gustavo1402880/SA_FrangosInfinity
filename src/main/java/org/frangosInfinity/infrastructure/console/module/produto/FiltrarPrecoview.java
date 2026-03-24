package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.CarrinhoController;
import org.frangosInfinity.infrastructure.console.module.produto.controller.CardapioController;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.List;

public class FiltrarPrecoview
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║

    private CardapioController controller;
    public void Filtrarpreco()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        FILTRAR PREÇO DE PRODUTO        ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("║  Digite o preço para os produtos: ");

        Double preco = Front.lDouble();
        System.out.println("╚════════════════════════════════════════╝");

        List<Produto> produtos = controller.filtrarPorPreco(preco);
        System.out.println("╔════════════════════════════════════════╗");
        for(Produto p : produtos)
        {
            if(p.getPreco().equals(preco))
            {
                System.out.printf("║ Nome Produto: %-34s║", p.getNome() + "\nPreço: %-34s║ "+ p.getPreco());
                System.out.println("\n\n");
            }

        }
        System.out.println("╚════════════════════════════════════════╝");
    }
}
