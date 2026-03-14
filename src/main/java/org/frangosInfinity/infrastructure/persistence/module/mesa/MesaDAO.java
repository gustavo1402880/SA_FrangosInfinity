package org.frangosInfinity.infrastructure.persistence.module.mesa;

import org.frangosInfinity.core.entity.module.mesa.Mesa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MesaDAO
{
    private final Connection connection;

    public MesaDAO(Connection connection)
    {
        this.connection = connection;
    }

    public Mesa salvar(Mesa mesa) throws SQLException
    {
        String sql = "INSERT INTO mesa (numero, capacidade, localizacao, disponivel, ativa, id_iot_config) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, mesa.getNumero());
            stmt.setInt(2, mesa.getCapacidade());
            stmt.setString(3, mesa.getLocalizacao());
            stmt.setBoolean(4, mesa.isDisponivel());
            stmt.setBoolean(5, mesa.isAtiva());

            if (mesa.getIdIotConfig() != null)
            {
                stmt.setLong(6, mesa.getIdIotConfig());
            }
            else
            {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next())
            {
                mesa.setId(generatedKeys.getLong(1));
            }

            return mesa;
        }
    }

    public Optional<Mesa> buscarPorId(Long id) throws SQLException
    {
        String sql = "SELECT * FROM mesa WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of(mapResultSetToMesa(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<Mesa> buscarPorNumero(int numero) throws SQLException
    {
        String sql = "SELECT * FROM mesa WHERE numero = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, numero);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of(mapResultSetToMesa(rs));
            }
        }
        return Optional.empty();
    }

    public List<Mesa> listarTodos() throws SQLException
    {
        List<Mesa> mesas = new ArrayList<>();

        String sql = "SELECT * FROM mesa ORDER BY numero";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next())
            {
                mesas.add(mapResultSetToMesa(rs));
            }
        }
        return mesas;
    }

    public List<Mesa> listarDisponiveis() throws SQLException
    {
        List<Mesa> mesas = new ArrayList<>();

        String sql = "SELECT * FROM mesa WHERE disponivel = true AND ativa = true ORDER BY numero";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {

            while (rs.next())
            {
                mesas.add(mapResultSetToMesa(rs));
            }
        }
        return mesas;
    }

    public void atualizar(Mesa mesa) throws SQLException
    {
        String sql = "UPDATE mesa SET numero = ?, capacidade = ?, localizacao = ?, " +
                "disponivel = ?, ativa = ?, id_iot_config = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, mesa.getNumero());
            stmt.setInt(2, mesa.getCapacidade());
            stmt.setString(3, mesa.getLocalizacao());
            stmt.setBoolean(4, mesa.isDisponivel());
            stmt.setBoolean(5, mesa.isAtiva());

            if (mesa.getIdIotConfig() != null)
            {
                stmt.setLong(6, mesa.getIdIotConfig());
            }
            else
            {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setLong(7, mesa.getId());

            stmt.executeUpdate();
        }
    }

    public void atualizarDisponibilidade(Long id, boolean disponivel) throws SQLException
    {
        String sql = "UPDATE mesa SET disponivel = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setBoolean(1, disponivel);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException
    {
        String sql = "DELETE FROM mesa WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private Mesa mapResultSetToMesa(ResultSet rs) throws SQLException
    {
        Mesa mesa = new Mesa(
                rs.getInt("numero"),
                rs.getInt("capacidade"),
                rs.getString("localizacao")
        );
        mesa.setId(rs.getLong("id"));

        mesa.setDisponivel(rs.getBoolean("disponivel"));

        mesa.setAtiva(rs.getBoolean("ativa"));

        Long idIotConfig = rs.getLong("id_iot_config");

        if (!rs.wasNull())
        {
            mesa.setIdIotConfig(idIotConfig);
        }

        return mesa;
    }
}
