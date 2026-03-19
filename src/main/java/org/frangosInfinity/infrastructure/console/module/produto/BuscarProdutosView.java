package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Cardapio;

import java.util.Scanner;

public class BuscarProdutosView
{

    private Scanner sc = new Scanner(System.in);
    public void BuscarProduto()
    {
        System.out.println("\n=== BUSCAR PRODUTOS ===");

        System.out.print("Digite o nome ou termo de busca: ");

        String termo = sc.nextLine();

        System.out.println("\nBuscando produtos por: " + termo);



        System.out.println("Resultados encontrados:");

        for(int i = 0 ; i < Cardapio.size())// isso e so o rascunho
        {
            System.out.println(i);
        }
    }
}
