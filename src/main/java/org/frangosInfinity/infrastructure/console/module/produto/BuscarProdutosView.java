package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Cardapio;
import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.console.module.produto.controller.CardapioController;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class BuscarProdutosView
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║

    private CardapioController controller;
    private Scanner sc = new Scanner(System.in);
    public void BuscarProduto()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║             BUSCAR PRODUTO             ║");
        System.out.println("╠════════════════════════════════════════╣");

        System.out.println("║  Digite o ID do Produto: ");

        Long id = (long) Front.lInteiro();
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         RESULTADOS ENCONTRADOS         ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Produto> produtos = Collections.singletonList(controller.buscarProduto(id));

        for(Produto p : produtos)
        {
            System.out.println("╔════════════════════════════════════════╗");
            if(p.getId().equals(id))
            {
                System.out.printf("║ NOME PRODUTOS: %-25s║%n", p.getNome());
            }
        }
        System.out.println("╚════════════════════════════════════════╝");
    }
}
