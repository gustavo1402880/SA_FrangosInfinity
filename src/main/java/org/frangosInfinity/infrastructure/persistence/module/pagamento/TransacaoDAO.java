package org.frangosInfinity.infrastructure.persistence.module.pagamento;

import org.frangosInfinity.core.entity.module.pagamento.TransacaoPIX;

import javax.swing.text.html.Option;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransacaoDAO
{
    private Connection connection;

    public TransacaoDAO(Connection connection)
    {
        this.connection = connection;
    }

    public TransacaoPIX salvar(TransacaoPIX transacaoPIX) throws SQLException
    {
        String sql = "INSERT INTO transacao_pix(pagamento_id, qr_code, codigo_copia_cola, tempo_expiracao_segundos, data_expiracao) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setLong(1, transacaoPIX.getPagamentoId());
            stmt.setString(2, transacaoPIX.getQrCode());
            stmt.setString(3, transacaoPIX.getCodigoCopiaCola());
            stmt.setInt(4, transacaoPIX.getTempoExpiracaoSegundos());
            stmt.setTimestamp(5, Timestamp.valueOf(transacaoPIX.getDataExpiracao()));

            int linhasAfetadas = stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next())
            {
                transacaoPIX.setId(rs.getLong(1));
            }
            return transacaoPIX;
        }
    }

    public Optional<TransacaoPIX> buscarPorId(Long id) throws SQLException
    {
        String sql = "SELECT * FROM transacao_pix WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return Optional.of(mapearTransacao(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<TransacaoPIX> buscarPorPagamentoId(Long pagamentoId) throws SQLException
    {
        String sql = "SELECT * FROM transacao_pix WHERE pagamento_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, pagamentoId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return Optional.of(mapearTransacao(rs));
            }
        }
        return Optional.empty();
    }

    public List<TransacaoPIX> listarTodas() throws SQLException
    {
        List<TransacaoPIX> transacoes = new ArrayList<>();
        String sql = "SELECT * FROM transacao_pix";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                transacoes.add(mapearTransacao(rs));
            }
        }
        return transacoes;
    }

    public List<TransacaoPIX> listarPorPagamentoId(Long pagamentoId) throws SQLException
    {
        List<TransacaoPIX> transacoes = new ArrayList<>();
        String sql = "SELECT * FROM transacao_pix WHERE pagamento_pix = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, pagamentoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                transacoes.add(mapearTransacao(rs));
            }
        }
        return transacoes;
    }

    public Boolean atualizar(TransacaoPIX transacaoPIX) throws SQLException
    {
        String sql = "UPDATE transacao_pix SET qr_code = ?, codigo_copia_cola = ?, data_expiracao = ? WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, transacaoPIX.getQrCode());
            stmt.setString(2, transacaoPIX.getCodigoCopiaCola());
            stmt.setTimestamp(3, Timestamp.valueOf(transacaoPIX.getDataExpiracao()));
            stmt.setLong(4, transacaoPIX.getId());

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;
        }
    }

    public TransacaoPIX mapearTransacao(ResultSet rs) throws SQLException
    {
        TransacaoPIX transacaoPIX = new TransacaoPIX();
        transacaoPIX.setId(rs.getLong("id"));
        transacaoPIX.setPagamentoId(rs.getLong("pagamento_id"));
        transacaoPIX.setQrCode(rs.getString("qr_code"));
        transacaoPIX.setCodigoCopiaCola(rs.getString("codigo_copia_cola"));
        transacaoPIX.setTempoExpiracaoSegundos(rs.getInt("tempo_expiracao_segundos"));
        transacaoPIX.setDataExpiracao(rs.getTimestamp("data_expiracao").toLocalDateTime());
        return  transacaoPIX;
    }
}
