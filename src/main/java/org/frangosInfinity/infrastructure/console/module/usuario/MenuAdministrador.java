package org.frangosInfinity.infrastructure.console.module.usuario;


import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.infrastructure.console.module.main.EncerarSistema;
import org.frangosInfinity.infrastructure.util.Front;

public class MenuAdministrador
{
    static UsuarioController usuarioController = new UsuarioController();


    public static void menuIniciar(long idAdm, NivelAcesso nivelAcesso)
    {
        boolean continuar = true;
        while(continuar) {

            System.out.println(Front.AQUA_BLUE + "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("┃                        " + Front.ORANGE_DARK + "ADMINISTRADOR" + Front.AQUA_BLUE + "                         ┃");
            System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

            System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("┃ 1 - Cadastrar                                          ┃");
            System.out.println("┃ 0 - Sair                                               ┃");
            System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
            System.out.println("");
            int opcao = Front.lInteiro();

            switch(opcao)
            {
                case 1 -> CriarUsuario.cadastrar();
                case 0 -> {
                    continuar = false;
                    EncerarSistema.desligar();
                }
            }
        }
    }

}
