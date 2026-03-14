package org.frangosInfinity.infrastructure.persistence.module.produto;
import org.frangosInfinity.core.entity.module.produto.Cardapio;
import java.sql.*;
import java.util.ArrayList;

public class CardapioDAO {

    private Connection connection;

    public CardapioDAO(Connection connection)
    {
        this.connection = connection;
    }

    public void adicionarCardapio(Cardapio cardapio){

        String querySQL = "insert into cardapio (versao,data_atualizacao) values (?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1,cardapio.getVersao());
            stmt.setDate(2,cardapio.getDataAtualizacao());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                {
                    cardapio.setId(rs.getLong(1));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao salvar cardapio: " + e.getMessage());
        }

    }

    public ArrayList<Cardapio> buscarCardapio(){

        ArrayList<Cardapio> cardapios = new ArrayList<>();
        String querySQL = "select * from cardapio";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                cardapios.add(mapearCardapio(rs));
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar cardapio: " + e.getMessage());
        }

        return cardapios;
    }

    public Cardapio buscarPorId(Long id){

        Cardapio cardapio = new Cardapio();
        String querySQL = "select * from pedido\n" +"where id = ?";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                return mapearCardapio(rs);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Erro ao listar cardapio: " + e.getMessage());
        }

        return null;

    }

    public boolean deletarCardapio(Long id){

        String querySQL = "delete from cardapio\n" +
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
            System.out.println("Erro ao excluir cardapio: " + e.getMessage());
        }
        return false;
    }

    private Cardapio mapearCardapio(ResultSet rs) throws SQLException
    {
        Cardapio cardapio = new Cardapio();
        cardapio.setId(rs.getLong("id"));
        cardapio.setVersao(rs.getInt("versao"));
        cardapio.setDataAtualizacao(rs.getDate("data_atualizacao"));

        return cardapio;
    }

}
