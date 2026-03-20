package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.List;

public class FiltrarCategoriaView
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║

    private ProdutoController controller;

    public void FiltrarCategoria()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         FILTRAR POR CATEGORIA          ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Digite o id da categoria que deseja: ");

        Long opcao = (long) Front.lInteiro();
        System.out.println("╚════════════════════════════════════════╝");

        List<Produto> produtos = controller.ListarCategorias();

        System.out.println(" Produtos encotrados");

        System.out.println("╔════════════════════════════════════════╗");
        for(Produto p : produtos)
        {
            if(p.getId().equals(opcao))
            {
                System.out.printf("║ Nome: %-34s║%n", p.getNome());
            }
        }
        System.out.println("╚════════════════════════════════════════╝");


    }
}
