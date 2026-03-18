package org.frangosInfinity.infrastructure.persistence.module.produto;
import org.frangosInfinity.core.entity.module.produto.Estoque;
import org.frangosInfinity.core.entity.module.produto.Produto;
import java.sql.*;
import java.util.ArrayList;

public class EstoqueDAO {

    private Connection connection;

    public EstoqueDAO(Connection connection)
    {
        this.connection = connection;
    }
    public void adicionarEstoque(Estoque estoque)
    {
        String querySQL = "insert into estoque (produto_id , quantidade_atual , quantidade_minima , quantidade_maxima) values (?,?,?,?);";

        Produto produto = estoque.getProduto();

        try(PreparedStatement stmt = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS))
        {

            stmt.setLong(1 , produto.getId());
            stmt.setInt(2 , estoque.getQuantidadeAtual());
            stmt.setInt(3  , estoque.getQuantidadeMinima());
            stmt.setInt(4 , estoque.getQuantidadeMaxima());

            int linhasAlteradas = stmt.executeUpdate();

            if(linhasAlteradas > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();

                estoque.setId(rs.getLong("id"));

            }


        }catch (SQLException e)
        {
            System.out.println("Erro ao adicionar estoque: " + e.getMessage());
        }
    }

    public ArrayList<Estoque> buscarEstoque(){

        ArrayList<Estoque> estoques = new ArrayList<>();
        String querySQL = "select * from estoque";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                estoques.add(mapearEstoque(rs));
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar estoque: " + e.getMessage());
        }

        return estoques;
    }

    public Estoque buscarPorId(Long id){

        String querySQL = "select * from estoque\n" +"where id = ?";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                return mapearEstoque(rs);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar estoque: " + e.getMessage());
        }

        return null;

    }

    public Estoque atualizarQuantidade(int quantidade, Produto produto,Estoque estoque){

        String querySQL = "update estoque \n" +
                "set quantidade_atual = ?\n" +
                "where produto_id = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(querySQL))
        {
            stmt.setInt(1, quantidade );
            stmt.setLong(2, produto.getId());

            stmt.executeUpdate();

            estoque.setQuantidadeAtual(quantidade);

            return estoque;
        }
        catch (SQLException e)
        {
            System.out.println("Erro atualizar quantidade item pedido: " + e.getMessage());
        }

        return null;

    }

    public boolean deletarEstoque(Long id){

        String querySQL = "delete from estoque\n" +
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
            System.out.println("Erro ao excluir estoque: " + e.getMessage());
        }
        return false;
    }

    private Estoque mapearEstoque(ResultSet rs) throws SQLException
    {
        ProdutoDAO produtoDAO = new ProdutoDAO(connection);
        Produto produto = produtoDAO.buscarPorId(rs.getLong("produto_id"));
        Estoque estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setQuantidadeAtual(rs.getInt("quantidade_atual"));
        estoque.setQuantidadeMinima(rs.getInt("quantidade_minima"));
        estoque.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));

        return estoque;
    }

}
