package org.frangosInfinity.infrastructure.persistence.module.notificacao;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.frangosInfinity.core.entity.module.notificacao.Notificacao;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;
import org.frangosInfinity.core.enums.TipoNotificacao;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.relatorio.RelatorioDAO;

import java.net.NoRouteToHostException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<Notificacao> buscarPorData(LocalDateTime data) throws SQLException
    {
        String sql = "SELECT * FROM notificacao WHERE data = ?";

        List<Notificacao> notificacoes = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setTimestamp(1, Timestamp.valueOf(data));

            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                notificacoes.add(mapearNotificacoes(rs));
            }
        }
        return notificacoes;
    }

    public List<Notificacao> buscarPorDestinatario(String destinatario) throws SQLException
    {
        String sql = "SELECT * FROM notificacao WHERE destinatario = ?";

        List<Notificacao> notificacaos = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, destinatario);

            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                notificacaos.add(mapearNotificacoes(rs));
            }
        }

        return notificacaos;
    }

    public List<Notificacao> buscarPorLidas(boolean lida) throws SQLException
    {
        String sql = "SELECT * FROM notificacao WHERE lida = ?";

        List<Notificacao> notificacaos = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setBoolean(1, lida);

            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                notificacaos.add(mapearNotificacoes(rs));
            }
        }

        return notificacaos;
    }

    public List<Notificacao> buscarPorTipo(TipoNotificacao tipoNotificacao) throws SQLException
    {
        String sql = "SELECT * FROM notificacao WHERE tipo_id = ?";

        List<Notificacao> notificacaos = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, tipoNotificacao.toString());

            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                notificacaos.add(mapearNotificacoes(rs));
            }
        }

        return notificacaos;
    }

    public Notificacao mapearNotificacoes(ResultSet rs) throws SQLException
    {
        Notificacao notificacao = new Notificacao();

        notificacao.setId(rs.getLong("id"));
        notificacao.setTipoNotificacao(decidirTipo(rs.getInt("tipo notificação")));
        notificacao.setMensagem(rs.getString("mensagem"));
        notificacao.setDataHora(rs.getTimestamp("data hora").toLocalDateTime());
        notificacao.setLida(rs.getBoolean("lida"));
        notificacao.setDestinatario(rs.getString("destinatario"));


        Long idNotificacao = rs.getLong("idNotificacao");

        if (!rs.wasNull())
        {
            notificacao.setId(idNotificacao);
        }

        return notificacao;
    }

    public TipoNotificacao decidirTipo(int id)
    {
        return TipoNotificacao.fromCodigo(id);
    }
}
