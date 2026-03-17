package org.frangosInfinity.core.service.module.relatorio;

import org.frangosInfinity.application.module.mesa.response.MesaResponseDTO;
import org.frangosInfinity.application.module.relatorio.request.RelatorioRequestDTO;
import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.relatorio.RelatorioDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
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

            RelatorioDAO relatorioDAO = new RelatorioDAO(conn);

            RelatorioVendas relatorioVendas = new RelatorioVendas(null, request.getPeriodoFim(), request.getDataGeracao(), request.getPeriodoFim(),
                    request.getTotalPedidor(), request.getTotalVendas(), request.getTicketMedio());

            relatorioDAO.salvar(relatorioVendas);

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

    public List<RelatorioVendas> listarTodos()
    {
        try(Connection conn = ConnectionFactory.getConnection())
        {
            RelatorioDAO relatorioDAO = new RelatorioDAO(conn);

            return relatorioDAO.listarTodos();
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
            RelatorioDAO relatorioDAO = new RelatorioDAO(conn);

            return relatorioDAO.buscarPorId(id).orElse(null);
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
            RelatorioDAO relatorioDAO = new RelatorioDAO(conn);

            return relatorioDAO.buscarPorPeriodo(inicio, fim);
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
            RelatorioDAO relatorioDAO = new RelatorioDAO(conn);

            return relatorioDAO.buscarPorDataGeracao(dataGeracao);
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

            RelatorioDAO relatorioDAO = new RelatorioDAO(conn);

            var relatorioVendas = relatorioDAO.buscarPorId(id);

            if(relatorioVendas.isEmpty())
            {
                return false;
            }

            relatorioDAO.deletar(id);

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
