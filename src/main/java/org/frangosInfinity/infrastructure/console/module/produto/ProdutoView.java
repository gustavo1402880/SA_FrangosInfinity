package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.infrastructure.util.Front;

import java.util.Scanner;

public class ProdutoView
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║

    private ProdutoController produtoController;
    private Scanner sc = new Scanner(System.in);

   public void ExibirMenu()
   {

       System.out.println("\n╔════════════════════════════════════════╗ ");
       System.out.println("║              MENU CARDÁPIO              ║  ");
       System.out.println("╠═════════════════════════════════════════╣  ");
       System.out.println("║       1 - Acessar Cardápio              ║  ");
       System.out.println("║       2 - Listar Categorias             ║  ");
       System.out.println("║       3 - Listar Produtos               ║  ");
       System.out.println("║       4 - Buscar Produtos               ║  ");
       System.out.println("║       5 - Filtrar por Categoria         ║  ");
       System.out.println("║       6 - Filtrar por Preço             ║  ");
       System.out.println("║       0 - Sair                          ║  ");
       System.out.println("╠═════════════════════════════════════════╣  ");

       System.out.println("║ %-34s║%n Escolha a opção que deseja: ");
       int opcao = Front.lInteiro();
       System.out.println("╚════════════════════════════════════════╝");

       switch (opcao) {
           case 1:
               AcessarCardapioView acessarCardapioView = new AcessarCardapioView();

               acessarCardapioView.Acessarcardapio();

               break;

           case 2:
               ListarCategoriaView listarCategoriaView = new ListarCategoriaView();

               listarCategoriaView.ListaCategoria();
               break;

           case 3:
              ListarProdutosView listarProdutosView = new ListarProdutosView();

              listarProdutosView.ListarProdutos();
               break;

           case 4:
               BuscarProdutosView buscarProdutosView = new BuscarProdutosView();

               buscarProdutosView.BuscarProduto();
               break;

           case 5:
               FiltrarCategoriaView filtrarCategoriaView = new FiltrarCategoriaView();

               filtrarCategoriaView.FiltrarCategoria();
               break;

           case 6:
               FiltrarPrecoview filtrarPrecoview = new FiltrarPrecoview();

               filtrarPrecoview.Filtrarpreco();
               break;

           case 0:
               System.out.println("Encerrando sistema...");
               break;

           default:
               System.out.println("Opção inválida! Tente novamente.");
       }

   }

}
