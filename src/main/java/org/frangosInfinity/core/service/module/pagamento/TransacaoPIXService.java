package org.frangosInfinity.core.service.module.pagamento;

import org.frangosInfinity.application.module.pagamento.response.PIXResponseDTO;
import org.frangosInfinity.core.entity.module.pagamento.Pagamento;
import org.frangosInfinity.core.entity.module.pagamento.TransacaoPIX;
import org.frangosInfinity.core.enums.TipoPagamento;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.PagamentoDAO;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.TransacaoDAO;
import org.frangosInfinity.infrastructure.util.GeradorQRCode;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TransacaoPIXService
{
    private final GeradorQRCode geradorQRCode;

    public TransacaoPIXService()
    {
        this.geradorQRCode = new GeradorQRCode();
    }

    private Boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    public PIXResponseDTO gerarPix(Long pagamentoId)
    {
        return gerarPix(pagamentoId, 600);
    }

    public PIXResponseDTO gerarPix(Long pagamentoId, Integer tempoExpiracaoSegundos)
    {
        Connection connection = null;
        try
        {
            if (!validarId(pagamentoId))
            {

            }

            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            PagamentoDAO pagamentoDAO = new PagamentoDAO(connection);
            TransacaoDAO transacaoDAO = new TransacaoDAO(connection);

            var pagamentoOpt = pagamentoDAO.buscarPorId(pagamentoId);
            if (pagamentoOpt.isEmpty())
            {

            }

            Pagamento pagamento = pagamentoOpt.get();

            if (pagamento.getTipoPagamento() != TipoPagamento.PIX)
            {

            }

            var pixExistente = transacaoDAO.buscarPorPagamentoId(pagamentoId);
            if (pixExistente.isPresent())
            {
                TransacaoPIX pix = pixExistente.get();
                if (!pix.idExpirado())
                {
                    PIXResponseDTO response = PIXResponseDTO.fromEntity(pix);
                    response.setMensagem("PIX já existente e válido");
                    return response;
                }
            }

            String codigoPix = gerarCodigoPix(pagamento.getValor());
            String qrCode = "QR_"+ UUID.randomUUID().toString().substring(0,8);

            TransacaoPIX pix = new TransacaoPIX(pagamentoId, qrCode, codigoPix);
            if (tempoExpiracaoSegundos != null && tempoExpiracaoSegundos > 0)
            {
                pix.setTempoExpiracaoSegundos(tempoExpiracaoSegundos);
            }

            TransacaoPIX pixSalvo = transacaoDAO.salvar(pix);

            connection.commit();

            PIXResponseDTO response = PIXResponseDTO.fromEntity(pixSalvo);
            response.setMensagem("PIX gerado com sucesso");
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
                    throw new RuntimeException("Erro no rollback: "+ex.getMessage());
                }
            }
            return criarRespostaErro("Erro ao gerar PIX:" + e.getMessage());
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
                    throw new RuntimeException("Erro ao fechar conexão: "+e.getMessage());
                }
            }
        }
    }

    public TransacaoPIX buscarPorId(Long id)
    {
        try (Connection connection = ConnectionFactory.getConnection())
        {
            if (!validarId(id))
            {
                return null;
            }

            TransacaoDAO pixDAO = new TransacaoDAO(connection);
            return pixDAO.buscarPorId(id).orElse(null);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public TransacaoPIX buscarPorPagamentoId(Long pagamentoId)
    {
        try (Connection connection = ConnectionFactory.getConnection())
        {
            if(!validarId(pagamentoId))
            {
                return null;
            }

            TransacaoDAO transacaoDAO = new TransacaoDAO(connection);

            return transacaoDAO.buscarPorPagamentoId(pagamentoId).orElse(null);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public List<TransacaoPIX> listarPorPagamentoId(Long pagamentoId)
    {
        try (Connection connection = ConnectionFactory.getConnection())
        {
            if(!validarId(pagamentoId))
            {
                return List.of();
            }

            TransacaoDAO transacaoDAO = new TransacaoDAO(connection);

            return transacaoDAO.listarPorPagamentoId(pagamentoId);
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public PIXResponseDTO renovarPix(Long pagamentoId)
    {
        Connection connection = null;
        try
        {
            if(!validarId(pagamentoId))
            {
                return criarRespostaErro("ID do pagamento inválido");
            }

            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            TransacaoDAO transacaoDAO = new TransacaoDAO(connection);

            var pixOpt = transacaoDAO.buscarPorPagamentoId(pagamentoId);
            if (pixOpt.isEmpty())
            {
                return criarRespostaErro("PIX não encontrado para este pagamento");
            }

            TransacaoPIX pix = pixOpt.get();
            pix.renovar();
            transacaoDAO.atualizar(pix);

            connection.commit();

            PIXResponseDTO response = PIXResponseDTO.fromEntity(pix);
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
                    return criarRespostaErro("Erro no rollback "+ ex.getMessage());
                }
            }
            return criarRespostaErro("Erro ao renovar Pix: "+e.getMessage());
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

    public Boolean verificarExpiracao(Long pagamentoId)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            if (!validarId(pagamentoId))
            {
                return true;
            }

            TransacaoDAO transacaoDAO = new TransacaoDAO(connection);

            var pixopt = transacaoDAO.buscarPorPagamentoId(pagamentoId);
            if (pixopt.isEmpty())
            {
                return true;
            }
            return pixopt.get().idExpirado();
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    private String gerarCodigoPix(Double valor)
    {
        String chave = UUID.randomUUID().toString().replace("-","").substring(0,20);

        return String.format("00020126580014br.gov.bcb.pix0136%s520400005303986540%.2f5802BR5903WEG6014JARAGUA DO SUL62290525%s6304",chave, valor, chave.substring(0,10));
    }

    private PIXResponseDTO criarRespostaErro(String mensagem)
    {
        PIXResponseDTO response = new PIXResponseDTO();
        response.setSucesso(false);
        response.setMensagem(mensagem);
        return response;
    }
}
