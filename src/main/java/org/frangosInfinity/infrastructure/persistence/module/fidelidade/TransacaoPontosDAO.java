package org.frangosInfinity.infrastructure.persistence.module.fidelidade;

import org.frangosInfinity.core.entity.module.fidelidade.TransacaoPontos;
import org.frangosInfinity.core.enums.TipoTransacaoPontos;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransacaoPontosDAO
{
    private Connection connection;

    public TransacaoPontosDAO(Connection connection)
    {
        this.connection = connection;
    }

    public boolean salvar(TransacaoPontos transacao)
    {
        String sql = "INSERT INTO transacao_pontos (pontos_fidelidade_id, data, tipo, quantidade) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setLong(1, transacao.getPontosFidelidadeId());
            stmt.setTimestamp(2, Timestamp.valueOf(transacao.getData()));
            stmt.setString(3, transacao.getTipoTransacaoPontos().name());
            stmt.setInt(4, transacao.getQuantidade());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                {
                    transacao.setId(rs.getLong(1));
                }
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao salvar transação: " + e.getMessage());
        }
        return false;
    }

    public TransacaoPontos buscarPorId(Long id)
    {
        String sql = "SELECT * FROM transacao_pontos WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return mapearTransacao(rs);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao buscar transação por ID: " + e.getMessage());
        }
        return null;
    }

    public List<TransacaoPontos> buscarPorPontosId(Long pontosFidelidadeId)
    {
        List<TransacaoPontos> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacao_pontos WHERE pontos_fidelidade_id = ? ORDER BY data DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, pontosFidelidadeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                lista.add(mapearTransacao(rs));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao buscar transações: " + e.getMessage());
        }
        return lista;
    }

    public List<TransacaoPontos> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim)
    {
        List<TransacaoPontos> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacao_pontos WHERE data BETWEEN ? AND ? ORDER BY data DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setTimestamp(1, Timestamp.valueOf(inicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fim));
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                lista.add(mapearTransacao(rs));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao buscar transações por período: " + e.getMessage());
        }
        return lista;
    }

    public List<TransacaoPontos> buscarPorTipo(TipoTransacaoPontos tipo)
    {
        List<TransacaoPontos> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacao_pontos WHERE tipo = ? ORDER BY data DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, tipo.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                lista.add(mapearTransacao(rs));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao buscar transações por tipo: " + e.getMessage());
        }
        return lista;
    }

    public List<TransacaoPontos> listarTodos()
    {
        List<TransacaoPontos> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacao_pontos ORDER BY data DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                lista.add(mapearTransacao(rs));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar transações: " + e.getMessage());
        }
        return lista;
    }

    public boolean deletar(Long id)
    {
        String sql = "DELETE FROM transacao_pontos WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao deletar transação: " + e.getMessage());
        }
        return false;
    }

    private TransacaoPontos mapearTransacao(ResultSet rs) throws SQLException
    {
        TipoTransacaoPontos tipo = TipoTransacaoPontos.valueOf(rs.getString("tipo"));

        TransacaoPontos transacao = new TransacaoPontos(
                rs.getLong("pontos_fidelidade_id"),
                tipo,
                rs.getInt("quantidade")
        );
        transacao.setId(rs.getLong("id"));
        transacao.setData(rs.getTimestamp("data").toLocalDateTime());

        return transacao;
    }
}
