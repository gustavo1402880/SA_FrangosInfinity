package org.frangosInfinity.infrastructure.persistence.module.pedido;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.enums.StatusPedido;
import java.sql.*;
import java.util.ArrayList;

public class PedidoDAO {

    private Connection connection;

    public PedidoDAO(Connection connection)
    {
        this.connection = connection;
    }

    public void adicionarPedido(Pedido pedido){

        String querySQL = "insert into pedido (numero_pedido,data_hora,status_id,mesa_id,atendente_id,tipo ) values (?,?,?,?,?,?)";

        StatusPedido stats = pedido.getStatus();

        try (PreparedStatement stmt = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, pedido.getNumeroPedido());
            stmt.setDate(2, pedido.getDataHora() );
            stmt.setInt(3, stats.getCodigo() );
            stmt.setLong(4, pedido.getMesa_id());
            stmt.setLong(5, pedido.getAtendente_id());
            stmt.setString(6, pedido.getTipo());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                {
                    pedido.setId(rs.getLong(1));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao salvar pedido: " + e.getMessage());
        }

    }

    public ArrayList<Pedido> buscarPedido(){

        ArrayList<Pedido> pedidos = new ArrayList<>();
        String querySQL = "select * from pedido";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                pedidos.add(mapearPedido(rs));
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar pedido: " + e.getMessage());
        }

        return pedidos;
    }

    public Pedido buscarPorId(Long id){

        String querySQL = "select * from pedido\n" +"where id = ?";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                return mapearPedido(rs);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar pedido: " + e.getMessage());
        }

        return null;

    }

    public ArrayList<Pedido> listarPedidosPendentes(){

        ArrayList<Pedido> pedidos = new ArrayList<>();

        String querySQL = "select * from pedido\n" +
                "        where status_id = 2";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                pedidos.add(mapearPedido(rs));
            }

            return pedidos;
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar pedido pendentes: " + e.getMessage());
        }

        return null;

    }

    public boolean deletarPedido(Long id){

        String querySQL = "delete from pedido\n" +
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
            System.out.println("Erro ao excluir pedido: " + e.getMessage());
        }
        return false;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException
    {
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(rs.getString("numero_pedido"));
        pedido.setAtendente(rs.getLong("atendente-id"));
        pedido.setMesa(rs.getLong("mesa_id"));
        pedido.setDataHora(rs.getDate("data_hora"));
        pedido.setTipo(rs.getString("tipo"));
        pedido.setStatus(DecidirStatus(rs.getInt("status_id")));

        return pedido;
    }

    public StatusPedido DecidirStatus(int id){

        return StatusPedido.fromCodigo(id);

    }

}
