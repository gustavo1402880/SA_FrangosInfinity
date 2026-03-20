package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Produto;

import java.util.List;

public class ListarProdutosView
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║
    private ProdutoController controller;

    public void ListarProdutos()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           LISTA DE PRODUTOS            ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Produto> produtos = controller.ListarProduto();

        System.out.println("╔════════════════════════════════════════╗");
        for(Produto p : produtos)
        {
            System.out.printf("║ PRODUTO: %-41s║%n", p.getNome());
        }
        System.out.println("╚════════════════════════════════════════╝");
    }
}
