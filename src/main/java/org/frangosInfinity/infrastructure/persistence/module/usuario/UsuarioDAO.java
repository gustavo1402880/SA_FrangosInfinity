package org.frangosInfinity.infrastructure.persistence.module.usuario;

import org.frangosInfinity.core.entity.module.mesa.IotConfig;
import org.frangosInfinity.core.entity.module.usuario.*;
import org.frangosInfinity.core.enums.TipoUsuario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO
{
    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }


    public Usuario salvar(Usuario usuario) throws SQLException
    {
        String querySQL = "insert into usuario (nome,email,senha,telefone,data_cadastro,ativo, tipo)" +
                " values (?,?,?,?,?,?,?)";

        try(PreparedStatement stmt = connection.prepareStatement(querySQL , Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setLong(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3 , usuario.getEmail());
            stmt.setString(4 , usuario.getSenha());
            stmt.setString(5 , usuario.getTelefone());
            stmt.setTimestamp(6, Timestamp.valueOf(usuario.getDataCadastro()));
            stmt.setBoolean( 7 , usuario.isAtivo());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();

            if(generatedKeys.next())
            {
                usuario.setId(generatedKeys.getLong(1));
            }

            return usuario;

        }
    }
        public Optional<Usuario> BuscarPorId(long id ) throws SQLException
        {
            String querySQL = "SELECT u.*, " +
                    "c.*, " +
                    "f.*, " +
                    "FROM usuario u " +
                    "LEFT JOIN cliente c ON u.id = c.id " +
                    "LEFT JOIN funcionario f ON u.id = f.id " +
                    "WHERE u.id = ?";

            try(PreparedStatement stmt = connection.prepareStatement(querySQL))
            {
                stmt.setLong(1,id);

                ResultSet rs = stmt.executeQuery();

                if(rs.next())
                {
                    if(rs.getInt("tipo_usuario") == TipoUsuario.FUNCIONARIO.getCodigo())
                    {
                        switch (rs.getInt("tipo_funcionario"))
                        {
                            case 1:
                                return Optional.of(mapearAtendente(rs));
                            case 2:
                                return Optional.of(mapearCaixa(rs));
                            case 3:
                                return Optional.of(mapearCozinheiro(rs));
                            case 4:
                                return Optional.of(mapearAdministrador(rs));
                        }
                        return Optional.of(mapearCliente(rs));
                    }

                }
                return Optional.empty();
            }

        }

    public Optional<Usuario> buscarPorUsuario(Long id) throws SQLException
        {
            String sql = "SELECT * FROM usuario WHERE id_mesa = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql))
            {
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();

                if (rs.next())
                {
                    return Optional.of(mapResultSetUsuario(rs));
                }
            }
            return Optional.empty();
        }

    public List<Usuario> ListarTodos() throws  SQLException
        {
            List<Usuario> usuarios = new ArrayList<>();

            String querySQL = "SELECT * FROM usuario";

            try(Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(querySQL) )
            {

                while (rs.next())
                {
                    usuarios.add(mapResultSetUsuario(rs));
                }
            }
            return usuarios;
        }

    public void deletar(long id) throws SQLException
        {
            String querySQL = "DELETE FROM usuario where id = ?";

            try(PreparedStatement stmt = connection.prepareStatement(querySQL))
            {
                stmt.setLong(1 ,  id);
                stmt.executeUpdate();
            }
        }

    private Cliente mapearCliente(ResultSet rs) throws SQLException
    {
        TipoUsuario tipoUsuario = TipoUsuario.fromCodigo(rs.getInt("tipo"));

        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setSenha(rs.getString(" senha"));
        cliente.setDataCadastro(rs.getTimestamp("dataCadastro").toLocalDateTime());
        cliente.setAtivo(rs.getBoolean("ativo"));
        cliente.setUsuario(tipoUsuario);
        cliente.setIdSessao(rs.getString("idSessao"));
        cliente.setDataNascimento(LocalDate.from(rs.getTimestamp("dataNascimento").toLocalDateTime()));
        cliente.setTotalGasto(rs.getDouble("totalGasto"));
        cliente.setPontosFidelidade(rs.getInt("pontosFidelidade"));

        return cliente;
    }

    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {

        TipoUsuario tipoUsuario = TipoUsuario.fromCodigo(rs.getInt("tipo"));

        Administrador administrador = new Administrador();
        administrador.setId(rs.getLong("id"));
        administrador.setNome(rs.getString("nome"));
        administrador.setEmail(rs.getString("email"));
        administrador.setTelefone(rs.getString("telefone"));
        administrador.setSenha(rs.getString(" senha"));
        administrador.setDataCadastro(rs.getTimestamp("dataCadastro").toLocalDateTime());
        administrador.setAtivo(rs.getBoolean("ativo"));
        administrador.setUsuario(tipoUsuario);

        return administrador;

    }
    private Cozinheiro mapearCozinheiro(ResultSet rs) throws  SQLException {

        TipoUsuario tipoUsuario = TipoUsuario.fromCodigo(rs.getInt("tipo"));

        Cozinheiro cozinheiro = new Cozinheiro();
        cozinheiro.setId(rs.getLong("id"));
        cozinheiro.setNome(rs.getString("nome"));
        cozinheiro.setEmail(rs.getString("email"));
        cozinheiro.setTelefone(rs.getString("telefone"));
        cozinheiro.setSenha(rs.getString(" senha"));
        cozinheiro.setDataCadastro(rs.getTimestamp("dataCadastro").toLocalDateTime());
        cozinheiro.setAtivo(rs.getBoolean("ativo"));
        cozinheiro.setUsuario(tipoUsuario);

        return cozinheiro;
    }

    private Caixa mapearCaixa(ResultSet rs) throws SQLException
    {
        TipoUsuario tipoUsuario = TipoUsuario.fromCodigo(rs.getInt("tipo"));

        Caixa caixa = new Caixa();
        caixa.setId(rs.getLong("id"));
        caixa.setNome(rs.getString("nome"));
        caixa.setEmail(rs.getString("email"));
        caixa.setTelefone(rs.getString("telefone"));
        caixa.setSenha(rs.getString(" senha"));
        caixa.setDataCadastro(rs.getTimestamp("dataCadastro").toLocalDateTime());
        caixa.setAtivo(rs.getBoolean("ativo"));
        caixa.setUsuario(tipoUsuario);

        return caixa;
    }

    private Atendente mapearAtendente(ResultSet rs) throws  SQLException {

        TipoUsuario tipoUsuario = TipoUsuario.fromCodigo(rs.getInt("tipo"));

        Atendente atendente = new Atendente();
        atendente.setId(rs.getLong("id"));
        atendente.setNome(rs.getString("nome"));
        atendente.setEmail(rs.getString("email"));
        atendente.setTelefone(rs.getString("telefone"));
        atendente.setSenha(rs.getString(" senha"));
        atendente.setDataCadastro(rs.getTimestamp("dataCadastro").toLocalDateTime());
        atendente.setAtivo(rs.getBoolean("ativo"));
        atendente.setUsuario(tipoUsuario);

        return atendente;

    }
}
