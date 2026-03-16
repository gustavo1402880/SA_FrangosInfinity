package org.frangosInfinity.core.service.module.usuario;

import org.frangosInfinity.application.module.usuario.request.UsuarioRequestDTO;
import org.frangosInfinity.application.module.usuario.response.UsuarioResponseDTO;
import org.frangosInfinity.core.entity.module.usuario.*;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.usuario.UsuarioDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService
{
    private Boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    private Boolean validarEmail(String email)
    {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private Boolean validarTelefone(String telefone)
    {
        return telefone != null && telefone.matches("^[1-9]{2}9\\d{8}$");
    }

    private Boolean validarSenha(String senha)
    {
        return senha != null && senha.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])");
    }

    private Boolean validarIdSessao(String idSessao)
    {
        return idSessao != null && !idSessao.trim().isEmpty();
    }

    private Boolean validarMatricula(String matricula)
    {
        return matricula != null && !matricula.trim().isEmpty();
    }

    private Boolean validarSalario(Double salario)
    {
        return salario != null && salario >= 2500.00;
    }

    private Boolean validarNome(String nome){return nome != null && nome.length() > 4;}

    public UsuarioResponseDTO adicionarUsuario(UsuarioRequestDTO request) throws Exception
    {
        Connection connection = null;
        try
        {
            if (!request.isValid())
            {
                return UsuarioResponseDTO.erro("Dados inválidos para criação de usuário");
            }

            if (!validarNome(request.getNome()))
            {
                return UsuarioResponseDTO.erro("Nome inválido ou menor que 4 caracteres");
            }

            if (!validarEmail(request.getEmail()))
            {
                return UsuarioResponseDTO.erro("Email inválido");
            }

            if (!validarTelefone(request.getTelefone()))
            {
                return UsuarioResponseDTO.erro("Telefone inválido");
            }

            if (!validarSenha(request.getSenha()))
            {
                return UsuarioResponseDTO.erro("Senha inválida ou muito fraca");
            }


            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

            if (usuarioDAO.existeEmail(request.getEmail()))
            {
                return UsuarioResponseDTO.erro("Email já existe");
            }

            Usuario usuario;

            if (request.getTipoUsuario() == TipoUsuario.CLIENTE)
            {
                if (!validarIdSessao(request.getIdSessao()) || usuarioDAO.existeIdSessao(request.getIdSessao()))
                {
                    return UsuarioResponseDTO.erro("ID de sessão inválido ou já existente");
                }


                usuario = new Cliente(
                        request.getNome(),
                        request.getEmail(),
                        request.getSenha(),
                        TipoUsuario.CLIENTE,
                        request.getIdSessao()
                );
                usuario.setTelefone(request.getTelefone());
            }
            else if (request.getTipoUsuario() == TipoUsuario.FUNCIONARIO)
            {
                if (!validarMatricula(request.getMatricula()) || usuarioDAO.existeMatricula(request.getMatricula()))
                {
                    return UsuarioResponseDTO.erro("Matrícula inválida ou já existente");
                }

                if (!validarSalario(request.getSalario()))
                {
                    return UsuarioResponseDTO.erro("Salário inválido ou abaixo de R$2500,00");
                }

                if (request.getNivelAcesso() == null)
                {
                    return UsuarioResponseDTO.erro("Nível de acesso obrigatório para funcionário");
                }


                switch (request.getNivelAcesso())
                {
                    case ATENDENTE:
                        usuario = new Atendente(
                                request.getNome(),
                                request.getEmail(),
                                request.getSenha(),
                                TipoUsuario.FUNCIONARIO,
                                request.getMatricula()
                        );
                        break;
                    case CAIXA:
                        usuario = new Caixa(
                                request.getNome(),
                                request.getEmail(),
                                request.getSenha(),
                                TipoUsuario.FUNCIONARIO,
                                request.getMatricula()
                        );
                        break;
                    case COZINHEIRO:
                        usuario = new Cozinheiro(
                                request.getNome(),
                                request.getEmail(),
                                request.getSenha(),
                                TipoUsuario.FUNCIONARIO,
                                request.getMatricula()
                        );
                        break;
                    case ADMINISTRADOR:
                        usuario = new Administrador(
                                request.getNome(),
                                request.getEmail(),
                                request.getSenha(),
                                TipoUsuario.FUNCIONARIO,
                                request.getMatricula()
                        );
                        break;
                    default:
                        return UsuarioResponseDTO.erro("Nível de acesso inválido");
                }
                usuario.setTelefone(request.getTelefone());
                ((Funcionario) usuario).setTurno(request.getTurno());
                ((Funcionario) usuario).setSalario(request.getSalario());
            }
            else
            {
                return UsuarioResponseDTO.erro("Tipo de usuário inválido");
            }

            Usuario usuarioSalvar = usuarioDAO.salvar(usuario);

            connection.commit();

            return UsuarioResponseDTO.fromEntity(usuarioSalvar);
        }
        catch (SQLException e)
        {
            if (connection != null)
            {
                try
                {
                    connection.rollback();
                }
                catch (SQLException ex)
                {
                    return UsuarioResponseDTO.erro("Erro ao fechar conexção: "+ex.getMessage());
                }
            }
            return UsuarioResponseDTO.erro("Erro ao criar usuário: "+e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException ex)
                {
                    return UsuarioResponseDTO.erro("Erro ao fechar conexão: "+ ex.getMessage());
                }
            }
        }
    }

    public UsuarioResponseDTO buscarPorId(Long id)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            if (!validarId(id))
            {
                return UsuarioResponseDTO.erro("ID inválido");
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

            var usuarioOpt = usuarioDAO.BuscarPorId(id);
            if (usuarioOpt.isEmpty())
            {
                return UsuarioResponseDTO.erro("Usuário com ID: "+id+" não encontrado");
            }

            return UsuarioResponseDTO.fromEntity(usuarioOpt.get());
        }
        catch (SQLException e)
        {
            return UsuarioResponseDTO.erro("Erro ao buscar usuário: "+ e.getMessage());
        }
    }

    public UsuarioResponseDTO buscarPorEmail(String email)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            if (!validarEmail(email))
            {
                return UsuarioResponseDTO.erro("Email inválido");
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            var usuarioOpt = usuarioDAO.buscarPorEmail(email);

            if (usuarioOpt.isEmpty())
            {
                return UsuarioResponseDTO.erro("Usuário com email "+ email + " não encontrado");
            }

            return UsuarioResponseDTO.fromEntity(usuarioOpt.get());
        }
        catch (SQLException e)
        {
            return UsuarioResponseDTO.erro("Erro ao buscar usuário: "+e.getMessage());
        }
    }

    public List<UsuarioResponseDTO> listarTodos()
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            List<Usuario> usuarios = usuarioDAO.listarTodos();

            return usuarios.stream()
                    .map(UsuarioResponseDTO::fromEntity)
                    .collect(Collectors.toList());
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public List<UsuarioResponseDTO> listarTodosAtivos()
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            List<Usuario> usuarios = usuarioDAO.buscarAtivos();

            return usuarios.stream()
                    .map(UsuarioResponseDTO::fromEntity)
                    .collect(Collectors.toList());
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public List<UsuarioResponseDTO> listarTodosInativos()
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            List<Usuario> usuarios = usuarioDAO.buscarInvativos();

            return usuarios.stream()
                    .map(UsuarioResponseDTO::fromEntity)
                    .collect(Collectors.toList());
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public List<UsuarioResponseDTO> listarPorTipoUsuario(TipoUsuario tipoUsuario)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            List<Usuario> usuarios = usuarioDAO.buscarPorTipo(tipoUsuario);

            return usuarios.stream()
                    .map(UsuarioResponseDTO::fromEntity)
                    .collect(Collectors.toList());
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public  List<UsuarioResponseDTO> listarPorNivelAcesso(NivelAcesso nivelAcesso)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            List<Funcionario> usuarios = usuarioDAO.buscarPorNivelAcesso(nivelAcesso);

            return usuarios.stream()
                    .map(UsuarioResponseDTO::fromEntity)
                    .collect(Collectors.toList());

        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public UsuarioResponseDTO buscarPorIdSessao(String idSessao)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

            var usuarioOpt = usuarioDAO.buscarPorIdSessao(idSessao);
            if (usuarioOpt.isEmpty())
            {
                return UsuarioResponseDTO.erro("Cliente com id de sessão: "+idSessao+" não encontrado");
            }

            return UsuarioResponseDTO.fromEntity(usuarioOpt.get());
        }
        catch (SQLException e)
        {
            return UsuarioResponseDTO.erro("Erro ao buscar usuário: "+e.getMessage());
        }
    }

    public UsuarioResponseDTO buscarPorMatricula(String matricula)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

            var usuarioOpt = usuarioDAO.buscarPorMatricula(matricula);
            if (usuarioOpt.isEmpty())
            {
                return UsuarioResponseDTO.erro("Funcionário com matrícula: "+matricula+" não encontrado");
            }

            return UsuarioResponseDTO.fromEntity(usuarioOpt.get());
        }
        catch (SQLException e)
        {
            return UsuarioResponseDTO.erro("Erro ao buscar usuário: "+e.getMessage());
        }
    }

    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO request)
    {
        Connection connection = null;
        try
        {
            if (!validarId(id))
            {
                return UsuarioResponseDTO.erro("ID inválido");
            }

            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            var usuarioOpt = usuarioDAO.BuscarPorId(id);

            if (usuarioOpt.isEmpty())
            {
                return UsuarioResponseDTO.erro("Usuário não encontrado");
            }

            Usuario usuario = usuarioOpt.get();

            if (validarNome(request.getNome()) && !request.getNome().equals(usuario.getNome()))
            {
                usuario.setNome(request.getNome());
            }

            if (validarTelefone(request.getTelefone()) && !request.getTelefone().equals(usuario.getTelefone()))
            {
                usuario.setTelefone(request.getTelefone());
            }

            if (usuario instanceof Funcionario && request.getTurno() != null)
            {
                ((Funcionario) usuario).setTurno(request.getTurno());
            }

            usuarioDAO.atualizar(usuario);

            connection.commit();

            return UsuarioResponseDTO.fromEntity(usuario);
        }
        catch (SQLException e)
        {
            if (connection != null)
            {
                try
                {
                    connection.rollback();
                }
                catch (SQLException ex)
                {
                    return UsuarioResponseDTO.erro("Erro no rollback: "+ex.getMessage());
                }
            }
            return UsuarioResponseDTO.erro("Erro ao atualizar usuário: "+e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    return UsuarioResponseDTO.erro("Erro ao fechar conexão: "+e.getMessage());
                }
            }
        }
    }

    public UsuarioResponseDTO atualizarSenha(Long id, String senhaAntiga, String senhaNova)
    {
        Connection connection = null;
        try
        {
            if (!validarId(id))
            {
                return UsuarioResponseDTO.erro("ID inválido");
            }

            if (!validarSenha(senhaNova) || senhaAntiga.equals(senhaNova))
            {
                return UsuarioResponseDTO.erro("Senha inválida ou muito fraca");
            }

            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            var usuarioOpt = usuarioDAO.BuscarPorId(id);

            if (usuarioOpt.isEmpty())
            {
                return UsuarioResponseDTO.erro("Usuário não encontrado");
            }

            Usuario usuario = usuarioOpt.get();
            usuario.setSenha(senhaNova);

            usuarioDAO.atualizarSenha(id, senhaNova);

            return UsuarioResponseDTO.fromEntity(usuario);
        }
        catch (SQLException e)
        {
            if (connection != null)
            {
                try
                {
                    connection.rollback();
                }
                catch (SQLException ex)
                {
                    return UsuarioResponseDTO.erro("Erro no rollback: "+ex.getMessage());
                }
            }
            return UsuarioResponseDTO.erro("Erro ao atualizar usuário: "+e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    return UsuarioResponseDTO.erro("Erro ao fechar conexão");
                }
            }
        }
    }

    public UsuarioResponseDTO atualizarEmail(String emailAntigo, String emailNovo)
    {
        Connection connection = null;
        try
        {
            if (!validarEmail(emailNovo) || emailAntigo.equals(emailNovo))
            {
                return UsuarioResponseDTO.erro("Email inválido ou iguais");
            }

            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

            if (usuarioDAO.existeEmail(emailNovo))
            {
                return UsuarioResponseDTO.erro("Email já cadastrado");
            }

            var usuarioOpt = usuarioDAO.buscarPorEmail(emailAntigo);

            if (usuarioOpt.isEmpty())
            {
                return UsuarioResponseDTO.erro("Usuário não encontrado");
            }

            Usuario usuario = usuarioOpt.get();
            usuario.setEmail(emailNovo);

            usuarioDAO.atualizarEmail(usuario.getId(), emailNovo);

            return UsuarioResponseDTO.fromEntity(usuario);
        }
        catch (SQLException e)
        {
            if (connection != null)
            {
                try
                {
                    connection.rollback();
                }
                catch (SQLException ex)
                {
                    return UsuarioResponseDTO.erro("Erro no rollback: "+ex.getMessage());
                }
            }
            return UsuarioResponseDTO.erro("Erro ao atualizar usuário: "+e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    return UsuarioResponseDTO.erro("Erro ao fechar conexão");
                }
            }
        }
    }

    public UsuarioResponseDTO login(String email, String senha)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            if (!validarEmail(email))
            {
                return UsuarioResponseDTO.erro("Email inválido");
            }

            if (!validarSenha(senha))
            {
                return UsuarioResponseDTO.erro("Senha inválida");
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            var usuarioOpt = usuarioDAO.buscarPorEmail(email);

            if (usuarioOpt.isEmpty())
            {
                return UsuarioResponseDTO.erro("Não encontrado usuário com email: "+email);
            }

            Usuario usuario = usuarioOpt.get();

            if (!usuario.getSenha().equals(senha))
            {
                return UsuarioResponseDTO.erro("Email ou senha inválidos");
            }

            if (!usuario.isAtivo())
            {
                return UsuarioResponseDTO.erro("Usuário invativo");
            }

            return UsuarioResponseDTO.fromEntity(usuario);
        }
        catch (SQLException e)
        {
            return UsuarioResponseDTO.erro("Erro ao tentar fazer login: "+e.getMessage());
        }
    }

    public UsuarioResponseDTO deletarUsuario(Long id)
    {
        Connection connection = null;
        try
        {
            if (!validarId(id))
            {
                return UsuarioResponseDTO.erro("ID inválido");
            }

            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            var usuarioOpt = usuarioDAO.BuscarPorId(id);

            if (usuarioOpt.isEmpty())
            {
                return UsuarioResponseDTO.erro("Usuário não encontrado");
            }

            usuarioDAO.deletar(id);

            connection.commit();

            UsuarioResponseDTO response = new UsuarioResponseDTO();
            response.setSucesso(true);
            response.setMensagem("Usuário desativado com sucesso");
            return response;
        }
        catch (SQLException e)
        {
            if (connection != null)
            {
                try
                {
                    connection.rollback();
                }
                catch (SQLException ex)
                {
                    return UsuarioResponseDTO.erro("Erro no rollback: "+ex.getMessage());
                }
            }
            return UsuarioResponseDTO.erro("Erro ao desativar usuário: "+e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    return UsuarioResponseDTO.erro("Erro ao fechar conexão");
                }
            }
        }
    }

    public UsuarioResponseDTO ativarUsuario(Long id)
    {
        Connection connection = null;
        try
        {
            if (!validarId(id))
            {
                return UsuarioResponseDTO.erro("ID inválido");
            }

            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            var usuarioOpt = usuarioDAO.BuscarPorId(id);

            if (usuarioOpt.isEmpty())
            {
                return UsuarioResponseDTO.erro("Usuário não encontrado");
            }

            Usuario usuario = usuarioOpt.get();
            usuario.ativar();

            connection.commit();

            UsuarioResponseDTO response = new UsuarioResponseDTO();
            response.setMensagem("Usuário ativado com sucesso");
            response.setSucesso(true);
            return response;
        }
        catch (SQLException e)
        {
            if (connection != null)
            {
                try
                {
                    connection.rollback();
                }
                catch (SQLException ex)
                {
                    return UsuarioResponseDTO.erro("Erro no rollback: "+ex.getMessage());
                }
            }
            return UsuarioResponseDTO.erro("Erro ao ativar usuário: "+e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    return UsuarioResponseDTO.erro("Erro ao fechar conexão");
                }
            }
        }
    }
}
