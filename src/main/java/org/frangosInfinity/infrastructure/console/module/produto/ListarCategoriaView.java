package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Categoria;

import java.util.List;

public class ListarCategoriaView
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║


    private ProdutoController controller;
    public void ListaCategoria()
    {

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           LISTA DE CATEGORIAS          ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Categoria> categorias = controller.ListarCategorias();

        System.out.println("╔════════════════════════════════════════╗");
        for(Categoria c : categorias)
        {

            System.out.println("║ %-41s║%n"+c.getId()+ " - "+ c.getNome()+" ");
        }
        System.out.println("╚════════════════════════════════════════╝");
    }
}
