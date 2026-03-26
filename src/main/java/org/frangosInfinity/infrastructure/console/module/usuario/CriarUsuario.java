package org.frangosInfinity.infrastructure.console.module.usuario;

import org.frangosInfinity.application.module.usuario.request.UsuarioRequestDTO;
import org.frangosInfinity.application.module.usuario.response.UsuarioResponseDTO;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;
import org.frangosInfinity.infrastructure.console.module.main.Main;
import org.frangosInfinity.infrastructure.util.Front;

public class CriarUsuario
{
    static void cadastrar()
    {
        System.out.println(Front.AQUA_BLUE+"┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                "+Front.ORANGE_DARK+"CADASTRAR USUÁRIO"+Front.AQUA_BLUE+"                ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

        System.out.print("┃ ➤ Nome: ");
        String nome = Front.lString();

        System.out.print("┃ ➤ Email: ");
        String email = Front.lString();

        System.out.print("┃ ➤ Senha: ");
        String senha = Front.lString();

        System.out.print("┃ ➤ Telefone: ");
        String telefone = Front.lString();

        System.out.println("\n┃ ➤ Tipo de Usuário:");
        System.out.println("┃ 1 - CLIENTE");
        System.out.println("┃ 2 - FUNCIONARIO");
        int tipoOpcao = Front.lInteiro();

        TipoUsuario tipoUsuario = (tipoOpcao == 1)
                ? TipoUsuario.CLIENTE
                : TipoUsuario.FUNCIONARIO;

        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNome(nome);
        request.setEmail(email);
        request.setSenha(senha);
        request.setTelefone(telefone);
        request.setTipoUsuario(tipoUsuario);

        // 🔽 CAMPOS ESPECÍFICOS
        if (tipoUsuario == TipoUsuario.CLIENTE)
        {
            System.out.print("┃ ➤ ID Sessão: ");
            request.setIdSessao(Front.lString());

            // matrícula só se você realmente usa pra cliente
            System.out.print("┃ ➤ Matrícula: ");
            request.setMatricula(Front.lString());
        }
        else
        {
            System.out.println("\n┃ ➤ Nível de Acesso:");
            System.out.println("┃ 1 - ADMINISTRADOR");
            System.out.println("┃ 2 - COZINHEIRO");
            System.out.println("┃ 3 - ATENDENTE");
            System.out.println("┃ 4 - CAIXA");

            int nivel = Front.lInteiro();

            NivelAcesso nivelAcesso = switch (nivel)
            {
                case 1 -> NivelAcesso.ADMINISTRADOR;
                case 2 -> NivelAcesso.COZINHEIRO;
                case 3 -> NivelAcesso.ATENDENTE;
                case 4 -> NivelAcesso.CAIXA;
                default -> null;
            };

            request.setNivelAcesso(nivelAcesso);

            System.out.print("┃ ➤ Turno: ");
            request.setTurno(Front.lString());
            System.out.print("┃ ➤ Matrícula: ");
            request.setMatricula(Front.lString());
        }

        Front.limpaTerminal();
        Front.ProcessamentoDeDados.main();

        try
        {
            UsuarioResponseDTO response =
                    Main.usuarioController.processarAdicionarUsuario(request);

            if (response.getSucesso())
            {
                Front.mensagemSucesso(response.getMensagem());
            }
            else
            {
                Front.mensagemErro(response.getMensagem());
            }
        }
        catch (Exception e)
        {
            Front.mensagemErro("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }
        }

