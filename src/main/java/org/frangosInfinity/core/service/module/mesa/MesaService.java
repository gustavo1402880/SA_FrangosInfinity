package org.frangosInfinity.core.service.module.mesa;

import org.frangosInfinity.application.module.mesa.request.MesaRequestDTO;
import org.frangosInfinity.application.module.mesa.response.MesaResponseDTO;
import org.frangosInfinity.core.entity.module.mesa.IotConfig;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.mesa.IoTConfigRepository;
import org.frangosInfinity.infrastructure.persistence.module.mesa.MesaRepository;
import org.frangosInfinity.infrastructure.util.Formatador;
import org.frangosInfinity.infrastructure.util.Validator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MesaService
{
    private final Validator validator;
    private final Formatador formatador;
    private final IoTConfigService ioTConfigService;

    public MesaService()
    {
        this.validator = new Validator();
        this.formatador = new Formatador();
        this.ioTConfigService = new IoTConfigService();
    }

    public MesaResponseDTO criarmesa(MesaRequestDTO request)
    {
        Connection conn = null;
        try
        {
            if (!validator.validarNumeroMesa(request.getNumero()))
            {
                return criarRespostaErro("Número da mesa deve ser positivo (1-999)");
            }

            if (!validator.validarCapacidade(request.getCapacidade()))
            {
                return criarRespostaErro("Capacidade deve ser entre 1 e 20 pessoas");
            }

            if (request.getLocalizacao() == null || request.getLocalizacao().trim().isEmpty())
            {
                return criarRespostaErro("Localização é obrigatória");
            }

            conn = ConnectionFactory.getConnection();

            conn.setAutoCommit(false);

            MesaRepository mesaRepository = new MesaRepository(conn);

            IoTConfigRepository iotConfigRepository = new IoTConfigRepository(conn);

            var mesaExistente = mesaRepository.buscarPorNumero(request.getNumero());

            if (mesaExistente.isPresent())
            {
                return criarRespostaErro("Já existe uma mesa com o número " + request.getNumero());
            }

            Mesa mesa = new Mesa(request.getNumero(), request.getCapacidade(), request.getLocalizacao());

            mesaRepository.salvar(mesa);

            IotConfig iotConfig = ioTConfigService.criarConfiguracaoPadrao(mesa.getId());
            iotConfigRepository.salvar(iotConfig);

            mesa.setIdIotConfig(iotConfig.getId());
            mesaRepository.atualizar(mesa);

            conn.commit();

            MesaResponseDTO resposta = MesaResponseDTO.fromEntity(mesa);
            resposta.setMensagem("Mesa criada com sucesso");
            return resposta;

        }
        catch (SQLException e)
        {
            if (conn != null)
            {
                try
                {
                    conn.rollback();
                }
                catch (SQLException ex)
                {
                    criarRespostaErro("Erro no rollback: " + ex.getMessage());
                }
            }
            return criarRespostaErro("Erro ao criar mesa: " + e.getMessage());
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    criarRespostaErro("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    public Mesa buscarPorId(Long id)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            MesaRepository mesaRepository = new MesaRepository(conn);

            return mesaRepository.buscarPorId(id).orElse(null);

        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public Mesa buscarPorNumero(int numero)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            MesaRepository mesaRepository = new MesaRepository(conn);

            return mesaRepository.buscarPorNumero(numero).orElse(null);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public List<Mesa> listarTodas()
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            MesaRepository mesaRepository = new MesaRepository(conn);

            return mesaRepository.listarTodos();
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public List<Mesa> listarDisponiveis()
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            MesaRepository mesaRepository = new MesaRepository(conn);

            return mesaRepository.listarDisponiveis();
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public MesaResponseDTO atualizarStatus(Long idMesa, String acao)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            MesaRepository mesaRepository = new MesaRepository(conn);

            var mesaOpt = mesaRepository.buscarPorId(idMesa);
            if (mesaOpt.isEmpty())
            {
                return criarRespostaErro("Mesa não encontrada");
            }

            Mesa mesa = mesaOpt.get();
            String acaoUpper = acao.toUpperCase();

            switch (acaoUpper)
            {
                case "OCUPAR":
                    if (!mesa.isDisponivel())
                    {
                        return criarRespostaErro("Mesa já está ocupada");
                    }
                    mesa.ocuparMesa();
                    System.out.println("Mesa " + mesa.getNumero() + " ocupada");
                    break;

                case "LIBERAR":
                    if (mesa.isDisponivel())
                    {
                        return criarRespostaErro("Mesa já está disponível");
                    }
                    mesa.liberarMesa();
                    System.out.println("Mesa " + mesa.getNumero() + " liberada");
                    break;

                case "ATIVAR":
                    mesa.setAtiva(true);
                    System.out.println("Mesa " + mesa.getNumero() + " ativada");
                    break;

                case "DESATIVAR":
                    mesa.setAtiva(false);
                    System.out.println("Mesa " + mesa.getNumero() + " desativada");
                    break;

                default:
                    return criarRespostaErro("Ação inválida: " + acao + ". Use: OCUPAR, LIBERAR, ATIVAR, DESATIVAR");
            }

            mesaRepository.atualizar(mesa);

            MesaResponseDTO resposta = MesaResponseDTO.fromEntity(mesa);
            resposta.setMensagem("Mesa " + acaoLower(acaoUpper) + " com sucesso");
            return resposta;

        }
        catch (SQLException e)
        {
            return criarRespostaErro("Erro ao atualizar mesa: " + e.getMessage());
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    return criarRespostaErro("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    public IotConfig getConfiguracaoIoT(Long idMesa)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            MesaRepository mesaRepository = new MesaRepository(conn);
            IoTConfigRepository iotConfigRepository = new IoTConfigRepository(conn);

            var mesaOpt = mesaRepository.buscarPorId(idMesa);
            if (mesaOpt.isPresent() && mesaOpt.get().getIdIotConfig() != null)
            {
                return iotConfigRepository.buscarPorId(mesaOpt.get().getIdIotConfig()).orElse(null);
            }
            return null;
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public boolean deletarMesa(Long idMesa)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            MesaRepository mesaRepository = new MesaRepository(conn);
            IoTConfigRepository iotConfigRepository = new IoTConfigRepository(conn);

            var mesaOpt = mesaRepository.buscarPorId(idMesa);
            if (mesaOpt.isEmpty())
            {
                return false;
            }

            Mesa mesa = mesaOpt.get();

            if (!mesa.isDisponivel())
            {
                return false;
            }

            if (mesa.getIdIotConfig() != null)
            {
                iotConfigRepository.deletar(mesa.getIdIotConfig());
            }

            mesaRepository.deletar(idMesa);

            conn.commit();
            return true;

        }
        catch (SQLException e)
        {
            if (conn != null)
            {
                try
                {
                    conn.rollback();
                }
                catch (SQLException ex)
                {
                    return false;
                }
            }
            return false;
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    return false;
                }
            }
        }
    }

    private String acaoLower(String acao)
    {
        switch (acao)
        {
            case "OCUPAR": return "ocupada";
            case "LIBERAR": return "liberada";
            case "ATIVAR": return "ativada";
            case "DESATIVAR": return "desativada";
            default: return acao.toLowerCase();
        }
    }

    private MesaResponseDTO criarRespostaErro(String mensagem)
    {
        MesaResponseDTO dto = new MesaResponseDTO();
        dto.setSucesso(false);
        dto.setMensagem(mensagem);
        return dto;
    }

    public void exibirMesasFormatadas()
    {
        List<Mesa> mesas = listarTodas();
        System.out.println(formatador.formatarListaMesas(mesas));
    }
}
