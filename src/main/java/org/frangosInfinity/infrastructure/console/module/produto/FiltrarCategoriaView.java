package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Categoria;
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
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println(" Digite o id da categoria que deseja: ");

        Long opcao = (long) Front.lInteiro();

        List<Categoria> categorias = controller.ListarCategorias();

        System.out.println(" Produtos encotrados");
        for(Categoria c : categorias)
        {
            if(c.getId().equals(opcao))
            {
                System.out.println("Nome: "+c.getNome());
            }
        }


    }
}
