package org.frangosInfinity.infrastructure.persistence.module.produto;
import org.frangosInfinity.core.entity.module.produto.Cardapio;
import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.core.entity.module.produto.Estoque;
import org.frangosInfinity.core.entity.module.produto.Produto;
import java.sql.*;
import java.util.ArrayList;

public class ProdutoDAO {

    private Connection connection;

    public ProdutoDAO(Connection connection)
    {
        this.connection = connection;
    }
    public void adicionarProduto(Produto produto)
    {
        String querySQL = "insert into produto(codigo,nome,descricao,preco,custo,tempo_preparo_minutos,disponivel,imagem_url,vendas_ultimos30dias,preco_pendente_aprovacao,categoria_id) values(?,?,?,?,?,?,?,?,?,?,?);";

        try(PreparedStatement stmt = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS))
        {
            Categoria categoria = produto.getCategoria();

            stmt.setString(1 , produto.getCodigo());
            stmt.setString(2,produto.getNome());
            stmt.setString(3,produto.getDescricao());
            stmt.setDouble(4,produto.getCusto());
            stmt.setDouble(5,produto.getPreco());
            stmt.setInt(6,produto.getTempoPreparoMinuto());
            stmt.setBoolean(7,produto.getDisponivel());
            stmt.setString(8,produto.getImagemUrl());
            stmt.setInt(9,produto.getVendasUltimos300dias());
            stmt.setDouble(10,produto.getPrecoPendenteAprovacao());
            stmt.setLong(11,categoria.getId());

            int linhasAlteradas = stmt.executeUpdate();

            if(linhasAlteradas > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();

                produto.setId(rs.getLong("id"));

            }


        }catch (SQLException e)
        {
            System.out.println("Erro ao adicionar produto: " + e.getMessage());
        }
    }

    public ArrayList<Produto> buscarProduto(){

        ArrayList<Produto> produtos = new ArrayList<>();
        String querySQL = "select * from produto";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                produtos.add(mapearProduto(rs));
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar produto: " + e.getMessage());
        }

        return produtos;
    }

    public Produto buscarPorId(Long id){

        String querySQL = "select * from produto\n" +"where id = ?";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                return mapearProduto(rs);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar produto: " + e.getMessage());
        }

        return null;

    }

    public boolean deletarProduto(Long id){

        String querySQL = "delete from produto\n" +
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
            System.out.println("Erro ao excluir produto: " + e.getMessage());
        }
        return false;
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException
    {

        Produto produto = new Produto();
        CategoriaDAO categoriaDAO = new CategoriaDAO(connection);

        produto.setId(rs.getLong("id"));
        produto.setCodigo(rs.getString("codigo"));
        produto.setNome(rs.getString("nome"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setCusto(rs.getDouble("custo"));
        produto.setTempoPreparoMinuto(rs.getInt("tempo_preparo_minutos"));
        produto.setDisponivel(rs.getBoolean("disponivel"));
        produto.setImagemUrl(rs.getString("imagem_url"));
        produto.setVendasUltimos300dias(rs.getInt("vendas_ultimos30dias"));
        produto.setPrecoPendenteAprovacao(rs.getDouble("preco_pendente_aprovacao"));
        Categoria categoria = categoriaDAO.buscarPorId(rs.getLong("categoria_id"));
        produto.setCategoria(categoria);

        return produto;
    }

}
