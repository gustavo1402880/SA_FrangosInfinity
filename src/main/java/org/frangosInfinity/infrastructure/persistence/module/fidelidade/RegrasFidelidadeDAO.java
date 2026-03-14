package org.frangosInfinity.infrastructure.persistence.module.fidelidade;

import org.frangosInfinity.core.entity.module.fidelidade.RegrasFidelidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegrasFidelidadeDAO
{
    private Connection connection;

    public RegrasFidelidadeDAO(Connection connection)
    {
        this.connection = connection;
    }

    public boolean salvar(RegrasFidelidade regras)
    {
        String sql = "INSERT INTO regras_fidelidade (pontos_por_real, dias_expiracao, pontos_minimos_resgate, " +
                "valor_desconto_por_bloco, pontos_por_bloco, valor_minimo_produto_desconto, ativo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setDouble(1, regras.getPontosPorReal());
            stmt.setInt(2, regras.getDiasExpiracao());
            stmt.setInt(3, regras.getPontosMinimosResgate());
            stmt.setDouble(4, regras.getValorDescontoPorBloco());
            stmt.setInt(5, regras.getPontosPorBloco());
            stmt.setDouble(6, regras.getValorMinimoProdutoDesconto());
            stmt.setBoolean(7, regras.isAtivo());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                {
                    regras.setId(rs.getLong(1));
                }
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao salvar regras: " + e.getMessage());
        }
        return false;
    }

    public RegrasFidelidade buscarPorId(Long id)
    {
        String sql = "SELECT * FROM regras_fidelidade WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return mapearRegras(rs);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao buscar regras por ID: " + e.getMessage());
        }
        return null;
    }

    public RegrasFidelidade buscarAtiva()
    {
        String sql = "SELECT * FROM regras_fidelidade WHERE ativo = true ORDER BY id DESC LIMIT 1";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            if (rs.next())
            {
                return mapearRegras(rs);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao buscar regras ativas: " + e.getMessage());
        }
        return null;
    }

    public List<RegrasFidelidade> listarTodas()
    {
        List<RegrasFidelidade> lista = new ArrayList<>();
        String sql = "SELECT * FROM regras_fidelidade ORDER BY id DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                lista.add(mapearRegras(rs));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar regras: " + e.getMessage());
        }
        return lista;
    }

    public boolean atualizar(RegrasFidelidade regras)
    {
        String sql = "UPDATE regras_fidelidade SET pontos_por_real = ?, dias_expiracao = ?, " +
                "pontos_minimos_resgate = ?, valor_desconto_por_bloco = ?, pontos_por_bloco = ?, " +
                "valor_minimo_produto_desconto = ?, ativo = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setDouble(1, regras.getPontosPorReal());
            stmt.setInt(2, regras.getDiasExpiracao());
            stmt.setInt(3, regras.getPontosMinimosResgate());
            stmt.setDouble(4, regras.getValorDescontoPorBloco());
            stmt.setInt(5, regras.getPontosPorBloco());
            stmt.setDouble(6, regras.getValorMinimoProdutoDesconto());
            stmt.setBoolean(7, regras.isAtivo());
            stmt.setLong(8, regras.getId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao atualizar regras: " + e.getMessage());
        }
        return false;
    }

    public boolean deletar(Long id)
    {
        String sql = "DELETE FROM regras_fidelidade WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao deletar regras: " + e.getMessage());
        }
        return false;
    }

    private RegrasFidelidade mapearRegras(ResultSet rs) throws SQLException
    {
        RegrasFidelidade regras = new RegrasFidelidade();
        regras.setId(rs.getLong("id"));
        regras.setPontosPorReal(rs.getDouble("pontos_por_real"));
        regras.setDiasExpiracao(rs.getInt("dias_expiracao"));
        regras.setPontosMinimosResgate(rs.getInt("pontos_minimos_resgate"));
        regras.setValorDescontoPorBloco(rs.getDouble("valor_desconto_por_bloco"));
        regras.setPontosPorBloco(rs.getInt("pontos_por_bloco"));
        regras.setValorMinimoProdutoDesconto(rs.getDouble("valor_minimo_produto_desconto"));
        regras.setAtivo(rs.getBoolean("ativo"));
        return regras;
    }
}
