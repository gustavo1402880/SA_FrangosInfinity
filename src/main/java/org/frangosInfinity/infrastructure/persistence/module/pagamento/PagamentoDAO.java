package org.frangosInfinity.infrastructure.persistence.module.pagamento;

import org.frangosInfinity.core.entity.module.pagamento.Pagamento;
import org.frangosInfinity.core.enums.StatusPagamento;
import org.frangosInfinity.core.enums.TipoPagamento;

import javax.swing.text.html.Option;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PagamentoDAO
{
    private Connection connection;

    public PagamentoDAO(Connection connection)
    {
        this.connection = connection;
    }

    public Pagamento salvar(Pagamento pagamento) throws SQLException
    {
        String sql = "INSERT INTO pagamento (subpedido_id, data_hora, valor, status_id, tipo_id, codigo_transacao) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setLong(1, pagamento.getId_SubPedido());
            stmt.setTimestamp(2, Timestamp.valueOf(pagamento.getDataHora()));
            stmt.setDouble(3, pagamento.getValor());
            stmt.setInt(4, pagamento.getStatusPagamento().getCodigo());
            stmt.setInt(5, pagamento.getTipoPagamento().getCodigo());
            stmt.setString(6, pagamento.getCodigoTransacao());

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next())
            {
                pagamento.setId_Pagamento(rs.getLong(1));
            }
            return pagamento;
        }
    }

    public Optional<Pagamento> buscarPorId(Long id) throws SQLException
    {
        String sql = "SELECT * FROM pagamento WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return Optional.of(mapearPagamento(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<Pagamento> buscarPorSubPedidoId(Long id) throws SQLException
    {
        String sql = "SELECT * FROM pagamento WHERE subpedido_id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return Optional.of(mapearPagamento(rs));
            }
        }
        return Optional.empty();
    }

    public List<Pagamento> listarTodos() throws SQLException
    {
        List<Pagamento> pagamentos = new ArrayList<>();
        String sql = "SELECT * FROM pagamento ORDER BY data_hora DESC";

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                pagamentos.add(mapearPagamento(rs));
            }
        }
        return pagamentos;
    }

    public List<Pagamento> listarPorStatus(StatusPagamento status) throws SQLException
    {
        List<Pagamento> pagamentos = new ArrayList<>();
        String sql = "SELECT * FROM pagamento WHERE status_id = ? ORDER BY data_hora DESC";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, status.getCodigo());

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                pagamentos.add(mapearPagamento(rs));
            }
        }
        return pagamentos;
    }

    public Boolean atualizar(Pagamento pagamento) throws SQLException
    {
        String sql = "UPDATE pagamento SET status_id = ?, codigo_transacao = ? WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, pagamento.getStatusPagamento().getCodigo());
            stmt.setString(2, pagamento.getCodigoTransacao());
            stmt.setLong(3, pagamento.getId_Pagamento());

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;
        }
    }

    public Boolean deletar(Long id) throws SQLException
    {
        String sql = "DELETE FROM pagamento WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;
        }
    }

    private Pagamento mapearPagamento(ResultSet rs) throws SQLException
    {
        Pagamento pagamento = new Pagamento();
        pagamento.setId_Pagamento(rs.getLong("id"));
        pagamento.setId_SubPedido(rs.getLong("subpedido_id"));
        pagamento.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        pagamento.setValor(rs.getDouble("valor"));
        pagamento.setStatusPagamento(StatusPagamento.fromCodigo(rs.getInt("status_id")));
        pagamento.setTipoPagamento(TipoPagamento.fromCodigo(rs.getInt("tipo_id")));
        pagamento.setCodigoTransacao(rs.getString("codigo_transacao"));
        return pagamento;
    }
}
