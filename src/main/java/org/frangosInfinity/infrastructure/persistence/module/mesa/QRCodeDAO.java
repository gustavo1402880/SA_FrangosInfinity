package org.frangosInfinity.infrastructure.persistence.module.mesa;

import org.frangosInfinity.core.entity.module.mesa.QRCode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QRCodeDAO
{
    private final Connection connection;

    public QRCodeDAO(Connection connection) {
        this.connection = connection;
    }

    public QRCode salvar(QRCode qrCode) throws SQLException
    {
        String sql = "INSERT INTO qr_code (codigo, url_autenticacao, data_criacao, " +
                "data_expiracao, ativo, utilizado, id_mesa, token_sessao) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, qrCode.getCodigo());
            stmt.setString(2, qrCode.getUrlAutenticacao());
            stmt.setTimestamp(3, Timestamp.valueOf(qrCode.getDataCriacao()));
            stmt.setTimestamp(4, Timestamp.valueOf(qrCode.getDataExpiracao()));
            stmt.setBoolean(5, qrCode.isAtivo());
            stmt.setBoolean(6, qrCode.isUtilizado());
            stmt.setLong(7, qrCode.getIdMesa());
            stmt.setString(8, qrCode.getTokenSessao());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();

            if (generatedKeys.next())
            {
                qrCode.setId(generatedKeys.getLong(1));
            }

            return qrCode;
        }
    }

    public Optional<QRCode> buscarPorId(Long id) throws SQLException
    {
        String sql = "SELECT * FROM qr_code WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of(mapResultSetToQRCode(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<QRCode> buscarPorTokenSessao(String token) throws SQLException
    {
        String sql = "SELECT * FROM qr_code WHERE token_sessao = ? AND ativo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, token);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of(mapResultSetToQRCode(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<QRCode> buscarAtivoPorMesa(Long idMesa) throws SQLException
    {
        String sql = "SELECT * FROM qr_code WHERE id_mesa = ? AND ativo = true AND utilizado = false " +
                "AND data_expiracao > CURRENT_TIMESTAMP ORDER BY data_criacao DESC LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, idMesa);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of(mapResultSetToQRCode(rs));
            }
        }
        return Optional.empty();
    }

    public List<QRCode> listarPorMesa(Long idMesa) throws SQLException
    {
        List<QRCode> qrCodes = new ArrayList<>();

        String sql = "SELECT * FROM qr_code WHERE id_mesa = ? ORDER BY data_criacao DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, idMesa);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                qrCodes.add(mapResultSetToQRCode(rs));
            }
        }
        return qrCodes;
    }

    public List<QRCode> listarAtivos() throws SQLException
    {
        List<QRCode> qrCodes = new ArrayList<>();

        String sql = "SELECT * FROM qr_code WHERE ativo = true AND utilizado = false " +
                "AND data_expiracao > CURRENT_TIMESTAMP";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {

            while (rs.next())
            {
                qrCodes.add(mapResultSetToQRCode(rs));
            }
        }
        return qrCodes;
    }

    public void marcarComoUtilizado(Long id) throws SQLException
    {
        String sql = "UPDATE qr_code SET utilizado = true, ativo = false WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);

            stmt.executeUpdate();
        }
    }

    public void desativarExpirados() throws SQLException
    {
        String sql = "UPDATE qr_code SET ativo = false WHERE data_expiracao <= CURRENT_TIMESTAMP";

        try (Statement stmt = connection.createStatement())
        {
            stmt.executeUpdate(sql);
        }
    }

    private QRCode mapResultSetToQRCode(ResultSet rs) throws SQLException
    {
        QRCode qrCode = new QRCode();
        qrCode.setId(rs.getLong("id"));
        qrCode.setCodigo(rs.getString("codigo"));
        qrCode.setUrlAutenticacao(rs.getString("url_autenticacao"));
        qrCode.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        qrCode.setDataExpiracao(rs.getTimestamp("data_expiracao").toLocalDateTime());
        qrCode.setAtivo(rs.getBoolean("ativo"));
        qrCode.setUtilizado(rs.getBoolean("utilizado"));
        qrCode.setIdMesa(rs.getLong("id_mesa"));
        qrCode.setTokenSessao(rs.getString("token_sessao"));

        return qrCode;
    }
}
