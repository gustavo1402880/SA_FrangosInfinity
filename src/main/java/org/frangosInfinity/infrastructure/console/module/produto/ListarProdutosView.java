package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.core.service.module.produto.CardapioService;
import org.frangosInfinity.core.service.module.produto.Produtoservice;
import org.frangosInfinity.infrastructure.console.module.produto.controller.ProdutoController;

import java.util.List;

public class ListarProdutosView
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║
    private org.frangosInfinity.infrastructure.console.module.produto.controller.ProdutoController produtoController;

    {
        try {

            produtoController = new ProdutoController();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void ListarProdutos()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           LISTA DE PRODUTOS            ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Produto> produtos = produtoController.listarProduto();

        System.out.println("╔════════════════════════════════════════╗");
        for(Produto p : produtos)
        {
            System.out.printf("║ PRODUTO: %-41s║%n", p.getNome());
        }
        System.out.println("╚════════════════════════════════════════╝");
    }
}
