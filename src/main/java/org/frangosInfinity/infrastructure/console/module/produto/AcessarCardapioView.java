package org.frangosInfinity.infrastructure.console.module.produto;

import org.frangosInfinity.core.entity.module.produto.Cardapio;
import org.frangosInfinity.infrastructure.console.module.produto.controller.CardapioController;

public class AcessarCardapioView
{

    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║


    public static void Acessarcardapio()
    {

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                CARDAPIO                ║");
        System.out.println("╚════════════════════════════════════════╝");

        CardapioController cardapioController = new CardapioController();

        cardapioController.verCardapio();

    }
}
