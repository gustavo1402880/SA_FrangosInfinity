package org.frangosInfinity.infrastructure.persistence.module.mesa;

import org.frangosInfinity.core.entity.module.mesa.IotConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IoTConfigDAO
{
    private final Connection connection;

    public IoTConfigDAO(Connection connection)
    {
        this.connection = connection;
    }

    public IotConfig salvar(IotConfig config) throws SQLException
    {
        String sql = "INSERT INTO iot_config (id_mesa, ip_dispositivo, porta, modelo, online, versao_firmware) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setLong(1, config.getIdMesa());
            stmt.setString(2, config.getIpDispositivo());
            stmt.setInt(3, config.getPorta());
            stmt.setString(4, config.getModelo());
            stmt.setBoolean(5, config.isOnline());
            stmt.setString(6, config.getVersaoFirmware());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next())
            {
                config.setId(generatedKeys.getLong(1));
            }

            return config;
        }
    }

    public Optional<IotConfig> buscarPorId(Long id) throws SQLException
    {
        String sql = "SELECT * FROM iot_config WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of(mapResultSetToConfig(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<IotConfig> buscarPorMesa(Long idMesa) throws SQLException
    {
        String sql = "SELECT * FROM iot_config WHERE id_mesa = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, idMesa);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of(mapResultSetToConfig(rs));
            }
        }
        return Optional.empty();
    }

    public List<IotConfig> listarTodos() throws SQLException
    {
        List<IotConfig> configs = new ArrayList<>();

        String sql = "SELECT * FROM iot_config";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {

            while (rs.next())
            {
                configs.add(mapResultSetToConfig(rs));
            }
        }
        return configs;
    }

    public List<IotConfig> listarOnline() throws SQLException
    {
        List<IotConfig> configs = new ArrayList<>();

        String sql = "SELECT * FROM iot_config WHERE online = true";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {

            while (rs.next())
            {
                configs.add(mapResultSetToConfig(rs));
            }
        }
        return configs;
    }

    public void atualizarStatus(Long id, boolean online) throws SQLException {
        String sql = "UPDATE iot_config SET online = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setBoolean(1, online);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

    public void atualizarFirmware(Long id, String versao) throws SQLException
    {
        String sql = "UPDATE iot_config SET versao_firmware = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, versao);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException
    {
        String sql = "DELETE FROM iot_config WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private IotConfig mapResultSetToConfig(ResultSet rs) throws SQLException
    {
        IotConfig config = new IotConfig(
                rs.getLong("id_mesa"),
                rs.getString("ip_dispositivo"),
                rs.getInt("porta")
        );
        config.setId(rs.getLong("id"));
        config.setModelo(rs.getString("modelo"));
        config.setOnline(rs.getBoolean("online"));
        config.setVersaoFirmware(rs.getString("versao_firmware"));

        return config;
    }
}
