package org.frangosInfinity.core.service.module.pagamento;

import org.frangosInfinity.application.module.pagamento.request.PagamentoRequestDTO;
import org.frangosInfinity.application.module.pagamento.response.PagamentoResponseDTO;
import org.frangosInfinity.core.entity.module.pagamento.Comprovante;
import org.frangosInfinity.core.entity.module.pagamento.Pagamento;
import org.frangosInfinity.core.enums.StatusPagamento;
import org.frangosInfinity.core.enums.TipoPagamento;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.ComprovanteDAO;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.PagamentoDAO;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.TransacaoDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PagamentoService
{
    private final TransacaoPIXService pixService;

    public PagamentoService()
    {
        this.pixService = new TransacaoPIXService();
    }

    private Boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    private Boolean validarValor(Double valor)
    {
        return valor != null && valor > 0;
    }

    private Boolean validarTipoPagamento(TipoPagamento tipoPagamento)
    {
        return tipoPagamento != null;
    }

    public PagamentoResponseDTO processarPagamento(PagamentoRequestDTO request)
    {
        Connection connection = null;
        try
        {
            if (!validarId(request.getSubPedidoId()))
            {
                return criarRespostaErro("ID do subpedido inválido");
            }

            if (!validarValor(request.getValor()))
            {
                return criarRespostaErro("valor do pagamento inválido");
            }

            if (!validarTipoPagamento(request.getTipo()))
            {
                return criarRespostaErro("Tipo de pagamento inválido");
            }

            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            PagamentoDAO pagamentoDAO = new PagamentoDAO(connection);
            ComprovanteDAO comprovanteDAO = new ComprovanteDAO(connection);
            TransacaoDAO transacaoDAO = new TransacaoDAO(connection);

            var pagamentoExistente = pagamentoDAO.buscarPorSubPedidoId(request.getSubPedidoId());
            if (pagamentoExistente.isPresent())
            {
                return criarRespostaErro("Já existe um pagamento para este pedido");
            }

            Pagamento pagamento = new Pagamento();
            pagamento.setId_SubPedido(request.getSubPedidoId());
            pagamento.setValor(request.getValor());
            pagamento.setTipoPagamento(request.getTipo());
            pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
            pagamento.setCodigoTransacao(UUID.randomUUID().toString());

            Pagamento pagamentoSalvo = pagamentoDAO.salvar(pagamento);

            if (request.getTipo() == TipoPagamento.PIX)
            {
                pixService.gerarPix(pagamentoSalvo.getId_Pagamento());
            }

            Comprovante comprovante = gerarComprovante(pagamentoSalvo);
            comprovanteDAO.salvar(comprovante);

            connection.commit();

            return PagamentoResponseDTO.fromEntity(pagamentoSalvo);
        }
        catch (SQLException e)
        {
            if(connection != null)
            {
                try
                {
                    connection.rollback();
                }
                catch (SQLException ex)
                {
                    return criarRespostaErro("Erro no rollback: " +ex.getMessage());
                }
            }
            return criarRespostaErro("Erro ao processar pagamento: "+e.getMessage());
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
                    return criarRespostaErro("Erro ao fechar conexão: "+e.getMessage());
                }
            }
        }
    }

    public Pagamento buscarPorId(Long id)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            if(!validarId(id))
            {
                return null;
            }

            PagamentoDAO pagamentoDAO = new PagamentoDAO(connection);
            return pagamentoDAO.buscarPorId(id).orElse(null);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public Pagamento buscarPorsubPedidoId(Long id)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            if(!validarId(id))
            {
                return null;
            }

            PagamentoDAO pagamentoDAO = new PagamentoDAO(connection);
            return pagamentoDAO.buscarPorSubPedidoId(id).orElse(null);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public List<Pagamento> listarTodos()
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            PagamentoDAO pagamentoDAO = new PagamentoDAO(connection);
            return pagamentoDAO.listarTodos();
        }
        catch(SQLException e)
        {
            return List.of();
        }
    }

    public List<Pagamento> listarPendetes()
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            PagamentoDAO pagamentoDAO = new PagamentoDAO(connection);
            return pagamentoDAO.listarPorStatus(StatusPagamento.PENDENTE);
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public PagamentoResponseDTO confirmarPagamento(Long pagamentoId)
    {
        Connection connection = null;
        try {
            if (!validarId(pagamentoId)) {
                return criarRespostaErro("ID do pagamento inválido");
            }

            connection = ConnectionFactory.getConnection();
            PagamentoDAO pagamentoDAO = new PagamentoDAO(connection);

            var pagamentoOpt = pagamentoDAO.buscarPorId(pagamentoId);
            if (pagamentoOpt.isEmpty()) {
                return criarRespostaErro("Pagamento não encontrado");
            }

            Pagamento pagamento = pagamentoOpt.get();

            if (pagamento.getStatusPagamento() != StatusPagamento.PENDENTE) {
                return criarRespostaErro("Pagamento não encontrado");
            }

            pagamento.setStatusPagamento(StatusPagamento.CONFIRMADO);
            pagamentoDAO.atualizar(pagamento);

            return PagamentoResponseDTO.fromEntity(pagamento);
        }
        catch (SQLException e)
        {
            return criarRespostaErro("Erro ao confirmar pagamento: "+e.getMessage());
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
                    return criarRespostaErro("Erro ao fechar conexão: "+ e.getMessage());
                }
            }
        }
    }

    public PagamentoResponseDTO cancelarPagamento(Long pagamentoId)
    {
        Connection connection = null;
        try
        {
            if (!validarId(pagamentoId))
            {
                return criarRespostaErro("ID do pagamento inválido");
            }

            connection = ConnectionFactory.getConnection();
            PagamentoDAO pagamentoDAO = new PagamentoDAO(connection);

            var pagamentoOpt = pagamentoDAO.buscarPorId(pagamentoId);
            if (pagamentoOpt.isEmpty())
            {
                return criarRespostaErro("Pagamento não encontrado");
            }

            Pagamento pagamento = pagamentoOpt.get();

            if (pagamento.getStatusPagamento() != StatusPagamento.PENDENTE)
            {
                return criarRespostaErro("Apenas pagamentos pendentes podem ser cancelados");
            }

            pagamento.setStatusPagamento(StatusPagamento.CANCELADO);
            pagamentoDAO.atualizar(pagamento);

            return PagamentoResponseDTO.fromEntity(pagamento);
        }
        catch (SQLException e)
        {
            return criarRespostaErro("Erro ao calcular pagamento: "+e.getMessage());
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
                    return criarRespostaErro("Erro ao fechar conexão");
                }
            }
        }
    }

    public Comprovante getComprovante(Long pagamentoId)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            if(!validarId(pagamentoId))
            {
                return null;
            }

            ComprovanteDAO comprovanteDAO = new ComprovanteDAO(connection);
            return comprovanteDAO.buscarProPagamentoId(pagamentoId).orElse(null);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    private Comprovante gerarComprovante(Pagamento pagamento)
    {
        String numero = "COMP-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        String cnpj = "12.345.678/0001-90";

        Comprovante comprovante = new Comprovante();
        comprovante.setIdPagamento(pagamento.getId_Pagamento());
        comprovante.setNumero(numero);
        comprovante.setCnpj(cnpj);
        comprovante.setValorTotal(pagamento.getValor());
        comprovante.setFormaPagamento(pagamento.getTipoPagamento());
        comprovante.setDataHora(LocalDateTime.now());

        return comprovante;
    }

    private PagamentoResponseDTO criarRespostaErro(String mensagem)
    {
        PagamentoResponseDTO response = new PagamentoResponseDTO();
        response.setSucesso(false);
        response.setMensagem(mensagem);
        return response;
    }
}
