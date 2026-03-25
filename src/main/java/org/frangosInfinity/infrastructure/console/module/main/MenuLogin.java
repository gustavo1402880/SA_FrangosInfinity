package org.frangosInfinity.infrastructure.console.module.main;

import org.frangosInfinity.application.module.usuario.response.UsuarioResponseDTO;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.infrastructure.console.module.usuario.*;
import org.frangosInfinity.infrastructure.util.Front;

public class MenuLogin
{
    public static void login()
    {
        System.out.println(Front.AQUA_BLUE+"┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                         "+Front.ORANGE_DARK+"LOGIN"+Front.AQUA_BLUE+"                          ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
        System.out.print("┃ ➤ Digite seu EMAIL: ");
        String email = Front.lString();

        System.out.print("┃ ➤ Digite sua senha: ");
        String senhaLogin = Front.lString();

        Front.limpaTerminal();
        //Front.ProcessamentoDeDados.main();

        UsuarioResponseDTO response = Main.usuarioController.processarLogin(email, senhaLogin);
        if(response.getSucesso())
        {
            Front.mensagemSucesso(response.getMensagem());
            if (NivelAcesso.ADMINISTRADOR.equals(response.getNivelAcesso())) {
                MenuAdministrador.menuIniciar(response.getId(), response.getNivelAcesso());
                Front.limpaTerminal();
            }

            if(NivelAcesso.COZINHEIRO.equals(response.getNivelAcesso())) {
                MenuCozinheiro.menuInicial(response.getId(), response.getNivelAcesso());
                Front.limpaTerminal();
            }

            if(NivelAcesso.ATENDENTE.equals(response.getNivelAcesso())) {
                MenuAtendente.menuInicial(response.getId());
                Front.limpaTerminal();
            }

            if(NivelAcesso.CAIXA.equals(response.getNivelAcesso())) {
                MenuCaixa.menuInicial(response.getId(), response.getNivelAcesso());
                Front.limpaTerminal();
            }

            if(NivelAcesso.CLIENTE.equals(response.getNivelAcesso())) {
                MenuCliente.menuInicial(response.getId(), response.getNivelAcesso());
                Front.limpaTerminal();
            }
        } else {
            Front.mensagemErro(response.getMensagem());
        }
    }
}
