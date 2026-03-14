package org.frangosInfinity.core.service.module.mesa;

import org.frangosInfinity.application.module.mesa.request.QRCodeRequestDTO;
import org.frangosInfinity.application.module.mesa.response.QRCodeResponseDTO;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.mesa.QRCode;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.mesa.MesaDAO;
import org.frangosInfinity.infrastructure.persistence.module.mesa.QRCodeDAO;
import org.frangosInfinity.infrastructure.util.Configuracao;
import org.frangosInfinity.infrastructure.util.Formatador;
import org.frangosInfinity.infrastructure.util.GeradorQRCode;
import org.frangosInfinity.infrastructure.util.Validator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class QRCodeService
{
    private final GeradorQRCode geradorQRCode;
    private final Validator validator;
    private final Configuracao configuracao;
    private final Formatador formatador;

    public QRCodeService()
    {
        this.geradorQRCode = new GeradorQRCode();
        this.validator = new Validator();
        this.configuracao = Configuracao.getInstance();
        this.formatador = new Formatador();
    }

    public QRCodeResponseDTO gerarQRCode(QRCodeRequestDTO request)
    {
        Connection connection = null;

        try
        {
            if(!validator.validarRequestQRCode(request))
            {
                return QRCodeResponseDTO.erro("Dados inválidos para geração do QR Code");
            }

            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            MesaDAO mesaDAO = new MesaDAO(connection);
            QRCodeDAO qrCodeDAO = new QRCodeDAO(connection);

            var mesaOpt = mesaDAO.buscarPorId(request.getIdMesa());
            if (mesaOpt.isEmpty())
            {
                return QRCodeResponseDTO.erro("Mesa não encontrada: "+ request.getIdMesa());
            }

            Mesa mesa = mesaOpt.get();

            if (!mesa.isAtiva())
            {
                return QRCodeResponseDTO.erro("Mesa " + mesa.getNumero() + " está desativada");
            }

            var qrAntigo = qrCodeDAO.buscarAtivoPorMesa(mesa.getId());
            qrAntigo.ifPresent(qr ->
            {
                try
                {
                    qr.setAtivo(false);
                }
                catch (Exception e)
                {
                    return ;
                }
            });

            QRCode qrCode = new QRCode();
            qrCode.setIdMesa(mesa.getId());

            int tempoExpiracao = configuracao.getIntProperty("qr.code.tempo.expiracao", 120);

            if (request.getTempoExpiracaoSgundos() != null && request.getTempoExpiracaoSgundos() > 0)
            {
                tempoExpiracao = request.getTempoExpiracaoSgundos();
            }

            qrCode.setDataExpiracao(
                    qrCode.getDataCriacao().plusSeconds(tempoExpiracao)
            );

            String baseUrl = configuracao.getProperty("app.baseUrl", "localhost:8080");
            String url = String.format("%s/auth/mesa/%d/%s", baseUrl, mesa.getId(), qrCode.getTokenSessao());
            qrCode.setUrlAutenticacao(url);

            String diretorio = configuracao.getProperty("qr.code.diretorio", "./qrcodes/");
            String nomeArquivo = String.format("mesa_%d_%s.png",
                    mesa.getNumero(),
                    qrCode.getCodigo()
            );

            String caminhoImagem = geradorQRCode.gerarQRCode(url, nomeArquivo);

            if (caminhoImagem == null)
            {
                return QRCodeResponseDTO.erro("Falha ao gerar arquivo de imagem");
            }

            qrCodeDAO.salvar(qrCode);

            connection.commit();

            QRCodeResponseDTO resposta = QRCodeResponseDTO.sucesso(qrCode, caminhoImagem);
            resposta.setMensagem("QR Code gerado com sucesso! Expira em " +
                    formatador.formatarTempoRestante(qrCode.getDataExpiracao()));

            return resposta;

        }
        catch (Exception e)
        {
            if (connection != null)
            {
                try
                {
                    connection.rollback();
                }
                catch (SQLException ex)
                {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao gerar QR Code: " + e.getMessage());
            return QRCodeResponseDTO.erro("Erro ao gerar QR Code: " + e.getMessage());
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
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    public boolean validarQRCode(Long idMesa, String tokenSessao)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            QRCodeDAO qrCodeDAO = new QRCodeDAO(conn);
            MesaDAO mesaDAO = new MesaDAO(conn);

            var qrCodeOpt = qrCodeDAO.buscarPorTokenSessao(tokenSessao);

            if (qrCodeOpt.isEmpty())
            {
                System.out.println("Token não encontrado: " + tokenSessao);
                return false;
            }

            QRCode qrCode = qrCodeOpt.get();

            if (qrCode.getIdMesa().equals(idMesa))
            {
                System.out.println("Token pertence a outra mesa (esperado: " + idMesa + ", encontrado: " + qrCode.getIdMesa() + ")");
                return false;
            }

            if (!qrCode.podeSerUtilizado())
            {
                if (qrCode.isUtilizado())
                {
                    System.out.println("QR Code já foi utilizado");
                }
                else if (qrCode.isExpirado())
                {
                    System.out.println("QR Code expirado em " + formatador.formatarDataHora(qrCode.getDataExpiracao()));
                }
                else
                {
                    System.out.println("QR Code inválido");
                }
                return false;
            }

            qrCodeDAO.marcarComoUtilizado(qrCode.getId());

            mesaDAO.atualizarDisponibilidade(idMesa, false);

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
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao validar QR Code: " + e.getMessage());
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
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    public QRCode buscarPorId(Long id) {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            QRCodeDAO qrCodeDAO = new QRCodeDAO(conn);
            return qrCodeDAO.buscarPorId(id).orElse(null);
        }
        catch (SQLException e)
        {
            System.err.println("Erro ao buscar QR Code " + id + ": " + e.getMessage());
            return null;
        }
    }

    public List<QRCode> listarAtivos()
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            QRCodeDAO qrCodeDAO = new QRCodeDAO(conn);
            return qrCodeDAO.listarAtivos();
        }
        catch (SQLException e)
        {
            System.err.println("Erro ao listar QR Codes ativos: " + e.getMessage());
            return List.of();
        }
    }

    public List<QRCode> listarPorMesa(Long idMesa)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            QRCodeDAO qrCodeDAO = new QRCodeDAO(conn);
            return qrCodeDAO.listarPorMesa(idMesa);
        }
        catch (SQLException e)
        {
            System.err.println("Erro ao listar QR Codes da mesa " + idMesa + ": " + e.getMessage());
            return List.of();
        }
    }

    public void limparExpirados() {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            QRCodeDAO qrCodeDAO = new QRCodeDAO(conn);

            qrCodeDAO.desativarExpirados();
        }
        catch (SQLException e)
        {
            System.err.println("Erro ao limpar QR Codes expirados: " + e.getMessage());
        }
    }

    public QRCode getQRCodeAtivoDaMesa(Long idMesa)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            QRCodeDAO qrCodeDAO = new QRCodeDAO(conn);
            return qrCodeDAO.buscarAtivoPorMesa(idMesa).orElse(null);
        }
        catch (SQLException e)
        {
            System.err.println("Erro ao buscar QR Code ativo da mesa " + idMesa + ": " + e.getMessage());
            return null;
        }
    }
}
