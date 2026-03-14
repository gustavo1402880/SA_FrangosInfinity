package org.frangosInfinity.infrastructure.persistence.module.produto;

import org.frangosInfinity.core.entity.module.produto.Cardapio;
import org.frangosInfinity.core.entity.module.produto.Categoria;
import java.sql.*;
import java.util.ArrayList;

public class CategoriaDAO {

    private Connection connection;

    public CategoriaDAO(Connection connection)
    {
        this.connection = connection;
    }
    public void adicionarCategoria(Categoria categoria){

        String querySQL = "insert into categoria( nome, descricao, ordem_exibicao) values(?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1,categoria.getNome());
            stmt.setString(2,categoria.getDescricao());
            stmt.setInt(3,categoria.getOrdemExibicao());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                {
                    categoria.setId(rs.getLong(1));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao salvar categoria: " + e.getMessage());
        }

    }

    public ArrayList<Categoria> buscarCategoria(){

        ArrayList<Categoria> categorias = new ArrayList<>();
        String querySQL = "select * from categoria";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                categorias.add(mapearCategoria(rs));
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar categoria: " + e.getMessage());
        }

        return categorias;
    }

    public Categoria buscarPorId(Long id){

        Cardapio cardapio = new Cardapio();
        String querySQL = "select * from categoria\n" +"where id = ?";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                return mapearCategoria(rs);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar categoria: " + e.getMessage());
        }

        return null;

    }

    public boolean deletarCategoria(Long id){

        String querySQL = "delete from categoria\n" +
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
            System.out.println("Erro ao excluir categoria: " + e.getMessage());
        }
        return false;
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException
    {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("id"));
        categoria.setNome(rs.getString("nome"));
        categoria.setDescricao(rs.getString("descricao"));
        categoria.setOrdemExibicao(rs.getInt("ordem_exibicao"));

        return categoria;
    }
}
