package org.frangosInfinity.infrastructure.console.module.usuario;

import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.infrastructure.util.Front;

public class MenuCliente
{
    public static void menuInicial(long idAdm, NivelAcesso nivelAcesso)
    {
        System.out.println(Front.AQUA_BLUE+"┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                        "+Front.ORANGE_DARK+"CLIENTE"+Front.AQUA_BLUE+"                         ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

    }
}
