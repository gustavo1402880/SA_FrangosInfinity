package org.frangosInfinity.infrastructure.persistence.module.fidelidade;

import org.frangosInfinity.core.entity.module.fidelidade.PontosFidelidade;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PontosFidelidadeDAO
{
    private Connection connection;

    public PontosFidelidadeDAO(Connection connection)
    {
        this.connection = connection;
    }

    public boolean salvar(PontosFidelidade pontos)
    {
        String sql = "INSERT INTO pontos_fidelidade (cliente_id, saldo, data_validade) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setLong(1, pontos.getClienteId());
            stmt.setInt(2, pontos.getSaldo());
            stmt.setTimestamp(3, Timestamp.valueOf(pontos.getDataValidade()));

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                {
                    pontos.setId(rs.getLong(1));
                }
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao salvar pontos: " + e.getMessage());
        }
        return false;
    }

    public PontosFidelidade buscarPorId(Long id)
    {
        String sql = "SELECT * FROM pontos_fidelidade WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return mapearPontos(rs);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao buscar pontos por ID: " + e.getMessage());
        }
        return null;
    }

    public PontosFidelidade buscarPorClienteId(Long clienteId)
    {
        String sql = "SELECT * FROM pontos_fidelidade WHERE cliente_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return mapearPontos(rs);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao buscar pontos por cliente: " + e.getMessage());
        }
        return null;
    }

    public List<PontosFidelidade> listarTodos()
    {
        List<PontosFidelidade> lista = new ArrayList<>();
        String sql = "SELECT * FROM pontos_fidelidade ORDER BY cliente_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                lista.add(mapearPontos(rs));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar pontos: " + e.getMessage());
        }
        return lista;
    }

    public boolean atualizar(PontosFidelidade pontos)
    {
        String sql = "UPDATE pontos_fidelidade SET saldo = ?, data_validade = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, pontos.getSaldo());
            stmt.setTimestamp(2, Timestamp.valueOf(pontos.getDataValidade()));
            stmt.setLong(3, pontos.getId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao atualizar pontos: " + e.getMessage());
        }
        return false;
    }

    public boolean deletar(Long id)
    {
        String sql = "DELETE FROM pontos_fidelidade WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao deletar pontos: " + e.getMessage());
        }
        return false;
    }

    private PontosFidelidade mapearPontos(ResultSet rs) throws SQLException
    {
        PontosFidelidade pontos = new PontosFidelidade(rs.getLong("cliente_id"));
        pontos.setId(rs.getLong("id"));
        pontos.setSaldo(rs.getInt("saldo"));
        pontos.setDataValidade(rs.getTimestamp("data_validade").toLocalDateTime());
        return pontos;
    }
}
