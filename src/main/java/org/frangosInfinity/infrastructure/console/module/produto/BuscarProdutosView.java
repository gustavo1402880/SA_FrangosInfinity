package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Cardapio;
import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.List;
import java.util.Scanner;

public class BuscarProdutosView
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║

    private ProdutoController controller;
    private Scanner sc = new Scanner(System.in);
    public void BuscarProduto()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║             BUSCAR PRODUTO             ║");
        System.out.println("╠════════════════════════════════════════╣");

        System.out.println("║  Digite o nome do Produto: ");

        String nomeProduto = Front.lString();
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         RESULTADOS ENCONTRADOS         ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Produto> produtos = controller.ListarProduto();

        for(Produto p : produtos)
        {
            System.out.println("╔════════════════════════════════════════╗");
            if(p.getNome().contains(nomeProduto))
            {
                System.out.printf("║ NOME PRODUTOS: %-25s║%n", p.getNome());
            }
        }
        System.out.println("╚════════════════════════════════════════╝");
    }
}
