package org.frangosInfinity.infrastructure.persistence.module.pedido;

import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.*;
import java.util.ArrayList;

public class SubPedidoDAO {

    private Connection connection;

    public SubPedidoDAO(Connection connection)
    {
        this.connection = connection;
    }

    public void adicionarSubPedido(SubPedido subPedido){

        String querySQL = "insert into subpedido (pedido_hub_id,cliente_id,data_hora,status_id,valor_total,tempo_estimado_minutos,observacoes) values (?,?,?,?,?,?,?)";

        StatusPedido stats = subPedido.getStatus();

        try (PreparedStatement stmt = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS))
        {

            stmt.setLong(1,subPedido.getPedidoHub().getId());
            stmt.setString(2,subPedido.getClienteID());
            stmt.setDate(3,subPedido.getDate());
            stmt.setInt(4, stats.getCodigo());
            stmt.setDouble(5,subPedido.getValorTotal());
            stmt.setInt(6,subPedido.getTempo_em_minutos());
            stmt.setString(7,subPedido.getObsevacoes());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                {
                    subPedido.setId(rs.getLong(1));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao salvar pedido: " + e.getMessage());
        }

    }

    public ArrayList<SubPedido> buscarSubPedido(){

        ArrayList<SubPedido> subPedidos = new ArrayList<>();
        String querySQL = "select * from subpedido";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                subPedidos.add(mapearSubPedidoPedido(rs));
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar pedido: " + e.getMessage());
        }

        return subPedidos;
    }

    public SubPedido buscarPorId(Long id){

        String querySQL = "select * from subpedido\n" +"where id = ?";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                return mapearSubPedidoPedido(rs);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar subpedido: " + e.getMessage());
        }

        return null;

    }

    public boolean deletarSubPedido(Long id){

        String querySQL = "delete from subpedido\n" +
                "        where id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(querySQL))
        {
            stmt.setLong(1,id);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0)
            {
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao excluir subpedido: " + e.getMessage());
        }
        return false;
    }

    private SubPedido mapearSubPedidoPedido(ResultSet rs) throws SQLException
    {
        PedidoDAO pedidoDAO = new PedidoDAO(connection);
        SubPedido subPedido = new SubPedido();
        Pedido pedido = pedidoDAO.buscarPorId(rs.getLong("numero_pedido"));
        subPedido.setPedidoHub(pedido);
        subPedido.setClienteID(rs.getString("cliente_id"));
        subPedido.setDate(rs.getDate("data_hora"));
        subPedido.setStatus(DecidirStatus(rs.getInt("status_id")));
        subPedido.setValorTotal(rs.getDouble("valor_total"));

        return subPedido;
    }

    public StatusPedido DecidirStatus(int id){

        return StatusPedido.fromCodigo(id);

    }
}
