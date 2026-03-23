package org.frangosInfinity.core.service.module.relatorio;

import org.frangosInfinity.application.module.relatorio.request.RelatorioRequestDTO;
import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.relatorio.RelatorioRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class RelatorioVendasService
{
    public RelatorioResponseDTO gerarRelatorio(RelatorioRequestDTO request)
    {
        Connection conn = null;

        try
        {
            conn = ConnectionFactory.getConnection();

            conn.setAutoCommit(false);

            RelatorioRepository relatorioRepository = new RelatorioRepository(conn);

            RelatorioVendas relatorioVendas = new RelatorioVendas(null, request.getPeriodoInicio(), request.getDataGeracao(), request.getPeriodoFim(),
                    request.getTotalPedidor(), request.getTotalVendas(), request.getTicketMedio());

            relatorioRepository.salvar(relatorioVendas);

            conn.commit();

            RelatorioResponseDTO response = RelatorioResponseDTO.fromEntity(relatorioVendas);
            response.setMensagem("Relatorio gerado com sucesso!");
            return response;
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
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        }
    }

    public byte[] gerarRelatorioPdfPorPeriodo(LocalDateTime inicio, LocalDateTime fim)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            RelatorioRepository relatorioRepository = new RelatorioRepository(conn);

            List<RelatorioVendas> relatorios = relatorioRepository.buscarPorPeriodo(inicio, fim);

            if (relatorios.isEmpty())
            {
                throw new RuntimeException("Nenhum relatório encontrado para o período");
            }

            // chama o PDF service
            PDFService pdfService = new PDFService();
            return pdfService.gerarPdf(relatorios);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    public List<RelatorioVendas> listarTodos()
    {
        try(Connection conn = ConnectionFactory.getConnection())
        {
            RelatorioRepository relatorioRepository = new RelatorioRepository(conn);

            return relatorioRepository.listarTodos();
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public RelatorioVendas buscarPorId(Long id)
    {
        try(Connection conn = ConnectionFactory.getConnection())
        {
            RelatorioRepository relatorioRepository = new RelatorioRepository(conn);

            return relatorioRepository.buscarPorId(id).orElse(null);
        }
        catch(SQLException e)
        {
            return null;
        }
    }

    public List<RelatorioVendas> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim)
    {
        try(Connection conn = ConnectionFactory.getConnection())
        {
            RelatorioRepository relatorioRepository = new RelatorioRepository(conn);

            return relatorioRepository.buscarPorPeriodo(inicio, fim);
        }
        catch(SQLException e)
        {
            throw new RuntimeException("Erro ao buscar relatórios" + e);
        }
    }

    public List<RelatorioVendas> buscarPorDataGeracao(LocalDateTime dataGeracao)
    {
        try(Connection conn = ConnectionFactory.getConnection())
        {
            RelatorioRepository relatorioRepository = new RelatorioRepository(conn);

            return relatorioRepository.buscarPorDataGeracao(dataGeracao);
        }
        catch(SQLException e)
        {
            throw new RuntimeException("Erro ao buscar relatórios "+ e);
        }
    }

    public boolean excluirRelatorio(Long id)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            RelatorioRepository relatorioRepository = new RelatorioRepository(conn);

            var relatorioVendas = relatorioRepository.buscarPorId(id);

            if(relatorioVendas.isEmpty())
            {
                return false;
            }

            relatorioRepository.deletar(id);

            conn.commit();
            return true;
        }
        catch(SQLException e)
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
    }
}
