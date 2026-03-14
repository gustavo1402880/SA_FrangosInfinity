package org.frangosInfinity.infrastructure.persistence.module.pagamento;

import org.frangosInfinity.core.entity.module.pagamento.Comprovante;
import org.frangosInfinity.core.enums.TipoPagamento;

import javax.swing.text.html.Option;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComprovanteDAO
{
    private Connection connection;

    public ComprovanteDAO(Connection connection)
    {
        this.connection = connection;
    }

    public Comprovante salvar(Comprovante comprovante) throws SQLException
    {
        String sql = "INSERT INTO comprovante(pagamento_id, numero, data_hora, cnpj, valor_total, forma_pagamento_id, qr_code_pix) VALUES (?,?,?,?,?,?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setLong(1, comprovante.getId());
            stmt.setString(2, comprovante.getNumero());
            stmt.setTimestamp(3, Timestamp.valueOf(comprovante.getDataHora()));
            stmt.setString(4, comprovante.getCnpj());
            stmt.setInt(5, comprovante.getFormaPagamento().getCodigo());
            stmt.setString(6, comprovante.getQrCodeString());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if(generatedKeys.next())
            {
                comprovante.setId(generatedKeys.getLong(1));
            }

            return comprovante;
        }
    }

    public Optional<Comprovante> buscarPorId(Long id) throws SQLException
    {
        String sql = "SELECT * FROM comprovante WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return Optional.of(mapResultSetToComprovante(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<Comprovante> buscarProPagamentoId(Long pagamentoId) throws SQLException
    {
        String sql = "SELECT * FROM comprovante WHERE pagamento_id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, pagamentoId);

            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return Optional.of(mapResultSetToComprovante(rs));
            }
        }
        return Optional.empty();
    }

    public List<Comprovante> listarTodos() throws SQLException
    {
        List<Comprovante> comprovantes = new ArrayList<>();
        String sql = "SELECT * FROM comprovante";

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                comprovantes.add(mapResultSetToComprovante(rs));
            }
        }
        return comprovantes;
    }

    private Comprovante mapResultSetToComprovante(ResultSet rs) throws SQLException
    {
        Comprovante comprovante = new Comprovante();
        comprovante.setId(rs.getLong("id"));
        comprovante.setIdPagamento(rs.getLong("pagamento_id"));
        comprovante.setNumero(rs.getString("numero"));
        comprovante.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        comprovante.setCnpj(rs.getString("cnpj"));
        comprovante.setValorTotal(rs.getDouble("valor_total"));
        comprovante.setFormaPagamento(TipoPagamento.fromCodigo(rs.getInt("forma_pagamento_id")));
        comprovante.setQrCodeString(rs.getString("qr_code_pix"));
        return comprovante;
    }
}
