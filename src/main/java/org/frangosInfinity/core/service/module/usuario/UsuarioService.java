package org.frangosInfinity.core.service.module.usuario;

import org.frangosInfinity.application.module.usuario.request.UsuarioRequestDTO;
import org.frangosInfinity.application.module.usuario.response.UsuarioResponseDTO;
import org.frangosInfinity.core.entity.module.usuario.*;
import org.frangosInfinity.core.enums.TipoUsuario;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.usuario.UsuarioDAO;

import java.sql.Connection;
import java.sql.SQLException;

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

    public UsuarioResponseDTO adicionarUsuario(UsuarioRequestDTO request) throws Exception
    {
        Connection connection = null;
        try
        {
            if (!request.isValid())
            {
                return UsuarioResponseDTO.erro("Dados inválidos para criação de usuário");
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
}
