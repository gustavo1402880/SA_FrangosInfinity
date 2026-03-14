package org.frangosInfinity.infrastructure.persistence.module.pedido;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.Pedido;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ItemPedidoDAO
{
    private Connection connection;

    public ItemPedidoDAO(Connection connection)
    {
        this.connection = connection;
    }

    public void adicionarItemPedido(ItemPedido itemPedido){

        String querySQL = "insert into item_pedido (subpedido_id , produto_id , quantidade , preco_unitario , observacoes, subtotal ) values (?,?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setLong(1, itemPedido.getSubPedido() );
            stmt.setLong(2, itemPedido.getProduto() );
            stmt.setInt(3, itemPedido.getQuantidade());
            stmt.setDouble(4, itemPedido.getPrecoUnitario());
            stmt.setString(5, itemPedido.getObservacao());
            stmt.setDouble(6, itemPedido.getSubTotal());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                {
                    itemPedido.setId_ItemPedido(rs.getLong(1));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao salvar item pedido: " + e.getMessage());
        }

    }

    public ArrayList<ItemPedido> buscarItensPedido(){

        ArrayList<ItemPedido> itemPedidos = new ArrayList<>();
        String querySQL = "select * from item_pedido";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                itemPedidos.add(mapearItemPedido(rs));
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar itens: " + e.getMessage());
        }

        return itemPedidos;
    }

    public ItemPedido buscarPorId(Long id){

        ItemPedido itemPedidos = new ItemPedido();
        String querySQL = "select * from item_pedido\n" +"where id = ?";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                return mapearItemPedido(rs);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar item: " + e.getMessage());
        }

        return null;

    }

    public ItemPedido updateQuantidade(ItemPedido itemPedido, int quantidade){

        String querySQL = "update intem_pedido \n" +
                "set quantidade = ?\n" +
                "where id = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(querySQL))
        {
            stmt.setInt(1, quantidade );
            stmt.setLong(2, itemPedido.getId_ItemPedido());

            stmt.executeUpdate();

            itemPedido.setQuantidade(quantidade);

            return itemPedido;
        }
        catch (SQLException e)
        {
            System.out.println("Erro atualizar quantidade item pedido: " + e.getMessage());
        }

        return null;
    }

    public boolean deletarItemPedido(Long id){

        String querySQL = "delete from item_pedido\n" +
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
            System.out.println("Erro ao excluir item pedido: " + e.getMessage());
        }
        return false;
    }

    private ItemPedido mapearItemPedido(ResultSet rs) throws SQLException
    {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId_ItemPedido(rs.getLong("id"));
        itemPedido.setSubPedido(rs.getLong("subpedido_id"));
        itemPedido.setQuantidade(rs.getInt("quantidade"));
        itemPedido.setPrecoUnitario(rs.getDouble("preco_unitario"));
        itemPedido.setObservacao(rs.getString("observacoes"));
        itemPedido.setSubTotal(rs.getDouble("subtotal"));
        return itemPedido;
    }

}
