package org.frangosInfinity.infrastructure.console.module.main;

import org.frangosInfinity.infrastructure.util.Front;

public class EncerarSistema
{
    public static void desligar()
    {
        System.out.println(Front.AQUA_BLUE+"┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                    "+Front.ORANGE_DARK+"DESLIGANDO . . ."+Front.AQUA_BLUE+"                    ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

        Front.Delay(1500);

        System.exit(0);
    }
}
