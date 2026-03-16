package org.frangosInfinity.infrastructure.console.module.usuario;

import org.frangosInfinity.application.module.usuario.request.UsuarioRequestDTO;
import org.frangosInfinity.application.module.usuario.response.UsuarioResponseDTO;
import org.frangosInfinity.core.entity.module.usuario.Usuario;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;
import org.frangosInfinity.core.service.module.usuario.UsuarioService;

import java.util.List;

public class UsuarioController
{
    private final UsuarioService usuarioService;

    public UsuarioController()
    {
        this.usuarioService = new UsuarioService();
    }

    public UsuarioResponseDTO processarAdicionarUsuario(UsuarioRequestDTO request)
    {
        if (request == null)
        {
            throw new IllegalArgumentException("Request de usuário não pode ser nulo");
        }

        try
        {
            UsuarioResponseDTO responseDTO = usuarioService.adicionarUsuario(request);

            if (!responseDTO.getSucesso())
            {
                throw new RuntimeException(responseDTO.getMensagem());
            }

            return responseDTO;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Erro ao processar criação de usuário: "+ e.getMessage());
        }
    }

    public UsuarioResponseDTO processarBuscarPorId(Long id)
    {
        UsuarioResponseDTO response = usuarioService.buscarPorId(id);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public UsuarioResponseDTO processarBuscarPorEmail(String email)
    {
        UsuarioResponseDTO response = usuarioService.buscarPorEmail(email);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public UsuarioResponseDTO processarBuscarPorIdSessao(String idSessao)
    {
        UsuarioResponseDTO response = usuarioService.buscarPorIdSessao(idSessao);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public UsuarioResponseDTO processarBuscarPorMatricula(String matricula)
    {
        UsuarioResponseDTO response = usuarioService.buscarPorMatricula(matricula);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public List<UsuarioResponseDTO> processarListarTodos()
    {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();

        if (usuarios.isEmpty())
        {
            throw new RuntimeException("Nenhum usuário encontrado");
        }

        return usuarios;
    }

    public List<UsuarioResponseDTO> processarListarAtivos()
    {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodosAtivos();

        if (usuarios.isEmpty())
        {
            throw new RuntimeException("Nenhum usuário encontrado");
        }

        return usuarios;
    }

    public List<UsuarioResponseDTO> processarListarInativos()
    {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodosInativos();

        if (usuarios.isEmpty())
        {
            throw new RuntimeException("Nenhum usuário encontrado");
        }

        return usuarios;
    }

    public List<UsuarioResponseDTO> processarListarPorTipo(TipoUsuario tipo)
    {
        if (tipo == null)
        {
            throw new IllegalArgumentException("Tipo de usuário inválido");
        }

        List<UsuarioResponseDTO> usuarios = usuarioService.listarPorTipoUsuario(tipo);

        if (usuarios.isEmpty())
        {
            throw new RuntimeException("Nenhum usuário encontrado");
        }

        return usuarios;
    }

    public List<UsuarioResponseDTO> processarListarPorNivelAcesso(NivelAcesso nivelAcesso)
    {
        if (nivelAcesso == null)
        {
            throw new IllegalArgumentException("Nível de acesso inválido");
        }

        List<UsuarioResponseDTO> usuarios = usuarioService.listarPorNivelAcesso(nivelAcesso);

        if (usuarios.isEmpty())
        {
            throw new RuntimeException("Nenhum funcionário com nível "+nivelAcesso+" encontrado");
        }

        return usuarios;
    }

    public UsuarioResponseDTO processarAtualizarUsuario(Long id, UsuarioRequestDTO request)
    {
        if (!request.isValid())
        {
            throw new IllegalArgumentException("Dados de atualização não podem ser nulos");
        }

        UsuarioResponseDTO response = usuarioService.atualizarUsuario(id, request);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public UsuarioResponseDTO processarAtualizarSenha(Long id, String senhaAntiga, String senhaNova)
    {
        UsuarioResponseDTO response = usuarioService.atualizarSenha(id, senhaAntiga, senhaNova);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public UsuarioResponseDTO processarAtualizarEmail(String emailAntigo, String emailNovo)
    {
        UsuarioResponseDTO response = usuarioService.atualizarEmail(emailAntigo, emailNovo);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public UsuarioResponseDTO processarDesativarUsuario(Long id)
    {
        UsuarioResponseDTO response = usuarioService.deletarUsuario(id);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public UsuarioResponseDTO processarAtivarUsuario(Long id)
    {
        UsuarioResponseDTO response = usuarioService.ativarUsuario(id);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public UsuarioResponseDTO processarLogin(String email, String senha)
    {
        UsuarioResponseDTO response = usuarioService.login(email, senha);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }
}
