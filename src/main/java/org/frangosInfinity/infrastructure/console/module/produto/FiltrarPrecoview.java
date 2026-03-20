package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.List;

public class FiltrarPrecoview
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║

    private ProdutoController controller;
    public void Filtrarpreco()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        FILTRAR PREÇO DE PRODUTO        ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("║ %-34s║%n  Digite o preço minimo para os produtos: ");

        double minimo = Front.lDouble();
        System.out.println("╠════════════════════════════════════════╣ ");

        System.out.println("║ %-34s║%n  Digite o preço maximo para os produtos: ");

        double maximo = Front.lDouble();
        System.out.println("╚════════════════════════════════════════╝");

        List<Produto> produtos = controller.ListarProdutos();
        System.out.println("╔════════════════════════════════════════╗");
        for(Produto p : produtos)
        {
            if(p.getPreco() > minimo && p.getPreco() < maximo)
            {
                System.out.printf("║ Nome Produto: %-34s║", p.getNome() + "\nPreço: %-34s║ "+ p.getPreco());
                System.out.println("\n\n");
            }
        }
        System.out.println("╚════════════════════════════════════════╝");


    }
}
