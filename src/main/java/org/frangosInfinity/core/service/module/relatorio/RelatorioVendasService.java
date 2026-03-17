package org.frangosInfinity.core.service.module.relatorio;

import org.frangosInfinity.application.module.relatorio.request.RelatorioRequestDTO;
import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.relatorio.RelatorioDAO;

import java.sql.Connection;
import java.sql.SQLException;
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

            var relatorioExistente = relatorioDAO.buscarPorDataGeracao(request.getDataGeracao());

            //finalizar
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<RelatorioResponseDTO> processarListarTodos()
    {
        try(Connection conn = ConnectionFactory.getConnection())
        {
            RelatorioDAO relatorioDAO = new RelatorioDAO(conn);

            List<RelatorioVendas> lista = relatorioDAO.listarTodos();

            return lista.stream().map(RelatorioResponseDTO::fromEntity())
        }
        catch (SQLException e)
        {

            throw new RuntimeException(e);
        }

    }
}
