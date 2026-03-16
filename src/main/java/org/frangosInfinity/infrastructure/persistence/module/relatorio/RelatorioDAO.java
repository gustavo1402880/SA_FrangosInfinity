package org.frangosInfinity.infrastructure.persistence.module.relatorio;

import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;

import java.sql.*;
import java.util.Optional;

public class RelatorioDAO
{
    private final Connection connection;

    public RelatorioDAO(Connection connection){this.connection = connection;}

    public RelatorioVendas salvar(RelatorioVendas relatorio) throws SQLException
    {
        String sql = "INSERT INTO relatorio_vendas(id, periodoInicio, periodoFim, dataGeracao, totalVendas, totalPedidos, ticketMedio)" +
                "values (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setLong(1, relatorio.getId());
            stmt.setTimestamp(2, Timestamp.valueOf(relatorio.getPeriodoInicio()));
            stmt.setTimestamp(3, Timestamp.valueOf(relatorio.getPeriodoFim()));
            stmt.setTimestamp(4, Timestamp.valueOf(relatorio.getDataGeracao()));
            stmt.setDouble(5, relatorio.getTotalVendas());
            stmt.setInt(6, relatorio.getTotalPedidos());
            stmt.setDouble(7, relatorio.getTicketMedio());

            stmt.executeUpdate();

            ResultSet generateKeys = stmt.getGeneratedKeys();
            if(generateKeys.next())
            {
                relatorio.setId(generateKeys.getLong(1));
            }

            return relatorio;
        }
    }
    public Optional<RelatorioVendas> buscarPorId(Long id) throws SQLException
    {
        String sql = "SELECT * FROM relatorio_vendas WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return Optional.of(mapearRelatorioVendas(rs));
            }
        }

        return Optional.empty();
    }

    public RelatorioVendas mapearRelatorioVendas(ResultSet rs) throws SQLException
    {
        RelatorioVendas relatorioVendas = new RelatorioVendas();

        relatorioVendas.setId(rs.getLong("id"));
        relatorioVendas.setPeriodoInicio(rs.getTimestamp("periodo inicio").toLocalDateTime());
        relatorioVendas.setPeriodoFim(rs.getTimestamp("periodo fim").toLocalDateTime());
        relatorioVendas.setDataGeracao(rs.getTimestamp("data geração").toLocalDateTime());
        relatorioVendas.setTotalVendas(rs.getDouble("total vendas"));
        relatorioVendas.setTotalPedidos(rs.getInt("total pedidos"));
        relatorioVendas.setTicketMedio(rs.getDouble("ticket medio"));

        Long idRelatorio = rs.getLong("idRelatorio");

        if (!rs.wasNull())
        {
            relatorioVendas.setId(idRelatorio);
        }

        return relatorioVendas;
    }

}
