package org.frangosInfinity.infrastructure.persistence.module.notificacao;

import org.frangosInfinity.core.entity.module.notificacao.Notificacao;
import org.frangosInfinity.core.enums.TipoNotificacao;

import java.sql.*;

public class NotificacaoDAO
{
    private final Connection connection;

    public NotificacaoDAO(Connection connection){this.connection = connection;}

    public Notificacao salvar(Notificacao notificacao) throws SQLException
    {
        TipoNotificacao tntf = notificacao.getTipoNotificacao();

        String sql = "INSERT INTO notificacao(id, tipoNotificacao, mensagem, dataHora, lida, destinatario)" +
                "values(?, ?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setLong(1, notificacao.getId());
            stmt.setInt(2, tntf.getCodigo());
            stmt.setString(3, notificacao.getMensagem());
            stmt.setTimestamp(4, Timestamp.valueOf(notificacao.getDataHora()));
            stmt.setBoolean(5, notificacao.isLida());
            stmt.setString(6, notificacao.getDestinatario());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if(generatedKeys.next())
            {
                notificacao.setId(generatedKeys.getLong(1));
            }

            return notificacao;
        }
    }
}
