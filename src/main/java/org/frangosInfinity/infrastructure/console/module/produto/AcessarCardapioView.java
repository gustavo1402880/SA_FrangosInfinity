package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Cardapio;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.console.module.produto.controller.CardapioController;

import java.util.List;

public class AcessarCardapioView
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║


    public static void Acessarcardapio()
    {

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                CARDAPIO                ║");
        System.out.println("╚════════════════════════════════════════╝");

        CardapioController cardapioController = new CardapioController();

        List<Produto> produtos = cardapioController.verCardapio();

        for(Produto p : produtos)
        {
            System.out.println("\n");
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║             ID:  %-23║%n"+ p.getId());
            System.out.println("║           Nome: %-23s║%n"+ p.getNome());
            System.out.println("║      Descrição: %-23s║%n"+ p.getDescricao());
            System.out.println("║          Preço: %-23s║%n"+ p.getPreco());
            System.out.println("║Disponibilidade: %-23s║%n"+ p.getDisponivel());
            System.out.println("╚════════════════════════════════════════╝");

        }

    }
}
