package org.frangosInfinity.infrastructure.persistence.module.usuario;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import org.frangosInfinity.core.entity.module.mesa.IotConfig;
import org.frangosInfinity.core.entity.module.usuario.*;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;

import javax.swing.text.html.Option;
import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.LongFunction;

public class UsuarioDAO {
    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }


    public Usuario salvar(Usuario usuario) throws SQLException {
        String querySQL = "insert into usuario (nome,email,senha,telefone,data_cadastro,ativo, tipo)" +
                " values (?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getTelefone());
            stmt.setTimestamp(6, Timestamp.valueOf(usuario.getDataCadastro()));
            stmt.setBoolean(7, usuario.isAtivo());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();

            if (generatedKeys.next()) {
                usuario.setId(generatedKeys.getLong(1));
            }

            if (usuario instanceof Funcionario) {
                salvarFuncioario((Funcionario) usuario);
            } else if (usuario instanceof Cliente) {
                salvarCliente((Cliente) usuario);
            }

            return usuario;
        }
    }

    private void salvarFuncioario(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionario (id, matricula, data_contratacao, turno, nivel_acesso, salario) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, funcionario.getId());
            stmt.setString(2, funcionario.getMatricula());
            stmt.setTimestamp(3, Timestamp.valueOf(funcionario.getDataContratacao()));
            stmt.setString(4, funcionario.getTurno());
            stmt.setInt(5, funcionario.getNivelAcesso().getCodigo());
            stmt.setDouble(6, funcionario.getSalario());

            stmt.executeUpdate();
        }
    }

    private void salvarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (id, id_sessao, data_nascimento, total_gasto, pontos_fidelidade) VALUES (?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cliente.getId());
            stmt.setString(2, cliente.getIdSessao());
            stmt.setDate(3, cliente.getDataNascimento() != null ? Date.valueOf(cliente.getDataNascimento()) : null);
            stmt.setDouble(4, cliente.getTotalGasto());
            stmt.setInt(5, cliente.getPontosFidelidade());

            stmt.executeUpdate();
        }
    }

    public Optional<Usuario> BuscarPorId(long id) throws SQLException {
        String querySQL = "SELECT u.*, " +
                "c.*, " +
                "f.* " +
                "FROM usuario u " +
                "LEFT JOIN cliente c ON u.id = c.id " +
                "LEFT JOIN funcionario f ON u.id = f.id " +
                "WHERE u.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(querySQL)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of(mapearUsuario(rs));
            }
            return Optional.empty();
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) throws SQLException
    {
        String sql = "SELECT u.*, " +
                "c.*," +
                "f.*" +
                "FROM usuario u " +
                "LEFT JOIN cliente c ON u.id = c.id " +
                "LEFT JOIN funcionario f ON u.id = f.id " +
                "WHERE u.email = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of(mapearUsuario(rs));
            }
            return Optional.empty();
        }
    }

    public List<Usuario> ListarTodos() throws SQLException
    {
        List<Usuario> usuarios = new ArrayList<>();

        String querySQL = "SELECT u.*, "+
                "c.*, " +
                "f.* " +
                "FROM usuario u " +
                "LEFT JOIN cliente c ON u.id = c.id " +
                "LEFT JOIN funcionario f ON u.id = f.id " +
                "ORDER BY u.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL))
        {
            while (rs.next())
            {
                usuarios.add(mapearUsuario(rs));
            }
        }
        return usuarios;
    }

    public void atualizar(Usuario usuario) throws SQLException
    {
        String sql = "UPDATE usuario SET nome = ?, email = ?, telefone = ?, ativo ? WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setBoolean(4, usuario.isAtivo());
            stmt.setLong(5, usuario.getId());

            stmt.executeUpdate();

            if (usuario instanceof Funcionario)
            {
                atualizarFuncionario((Funcionario) usuario);
            }
            else if (usuario instanceof Cliente)
            {
                atualizarCliente((Cliente) usuario);
            }
        }
    }

    private void atualizarFuncionario(Funcionario funcionario) throws SQLException
    {
        String sql = "UPDATE funconario SET turno = ?, salario = ?, WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, funcionario.getTurno());
            stmt.setDouble(2, funcionario.getSalario());
            stmt.setLong(3, funcionario.getId());
            stmt.executeUpdate();
        }
    }

    private void atualizarCliente(Cliente cliente) throws SQLException
    {
        String sql = "UPDATE cliente SET total_gasto = ?, pontos_fidelidade = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setDouble(1, cliente.getTotalGasto());
            stmt.setInt(2, cliente.getPontosFidelidade());
            stmt.setLong(3, cliente.getId());
            stmt.executeUpdate();
        }
    }

    public void  atualizarSenha(Long id, String novaSenha) throws SQLException
    {
        String sql = "UPDATE usuario SET senha = ? WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, novaSenha);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException
    {
        String sql = "UPDATE ativo = false FROM cliente WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Usuario> buscarPorTipo(TipoUsuario tipo) throws SQLException
    {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, " +
                "c.*, " +
                "f.* " +
                "FROM usuario u " +
                "LEFT JOIN cliente C ON u.id = c.id " +
                "LEFT JOIN funcionario f ON u.id = f.id " +
                "WHERE u.tipo = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, tipo.getCodigo());
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                usuarios.add(mapearUsuario(rs));
            }
        }
        return usuarios;
    }

    public List<Funcionario> buscarPorNivelAcesso(NivelAcesso acesso) throws SQLException
    {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT u.*, " +
                "f.* " +
                "INNER JOIN funcionario f ON u.id = f.id " +
                "WHERE f.nivel_acesso = ? ";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, acesso.getCodigo());
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                funcionarios.add((Funcionario) mapearUsuario(rs));
            }
        }
        return funcionarios;
    }

    public List<Usuario> buscarAtivos() throws SQLException
    {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, " +
                "c.*, " +
                "f.* " +
                "FROM usuario u " +
                "LEFT JOIN cliente ON u.id = c.id " +
                "LEFT JOIN funcionario ON u.id = f.id " +
                "WHERE u.ativo = true";

        try(Statement stmt = connection.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                usuarios.add(mapearUsuario(rs));
            }
        }
        return usuarios;
    }

    public List<Usuario> buscarInvativos() throws SQLException
    {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, " +
                "c.*, " +
                "f.* " +
                "FROM usuario u " +
                "LEFT JOIN cliente ON u.id = c.id " +
                "LEFT JOIN funcionario ON u.id = f.id " +
                "WHERE u.ativo = true";

        try(Statement stmt = connection.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                usuarios.add(mapearUsuario(rs));
            }
        }
        return usuarios;
    }

    public Optional<Cliente> buscarPorIdSessao(String idSessao) throws SQLException
    {
        String sql = "SELECT u.*, c.* FROM usuario u " +
                "INNER JOIN cliente c ON u.id = c.id " +
                "WHERE c.id_sessao = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, idSessao);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of((Cliente) mapearUsuario(rs));
            }
            return Optional.empty();
        }
    }

    public Optional<Funcionario> buscarPorMatricula(String matricula) throws SQLException
    {
        String sql = "SELECT u.*, f.* FROM usuario u " +
                "INNER JOIN funcionario f ON u.id = f.id " +
                "WHERE f.matricula = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, matricula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return Optional.of((Funcionario) mapearUsuario(rs));
            }
            return Optional.empty();
        }
    }

    public Boolean existeEmail(String email) throws SQLException
    {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public Boolean existeMatricula(String matricula) throws SQLException
    {
        String sql = "SELECT COUNT(*) FROM funcionario WHERE matricula = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1,matricula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public Boolean existeIdSessao(String idSessao) throws SQLException
    {
        String sql = "SELECT COUNT(*) FROM cliente WHERE id_sessao = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, idSessao);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException
    {
        TipoUsuario tipoUsuario = TipoUsuario.fromCodigo(rs.getInt("tipo"));

        if (tipoUsuario == TipoUsuario.CLIENTE)
        {
            return mapearCliente(rs);
        }
        else
        {
            NivelAcesso nivelAcesso = NivelAcesso.fromCodigo(rs.getInt("nivel_acesso"));

            switch (nivelAcesso)
            {
                case ATENDENTE:
                    return mapearAtendente(rs);
                case CAIXA:
                    return mapearCaixa(rs);
                case COZINHEIRO:
                    return mapearCozinheiro(rs);
                case ADMINISTRADOR:
                    return mapearAdministrador(rs);
                default:
                    throw new SQLException("Nível de acesso inválido: "+nivelAcesso);
            }
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
        cliente.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        cliente.setAtivo(rs.getBoolean("ativo"));
        cliente.setUsuario(TipoUsuario.CLIENTE);
        cliente.setIdSessao(rs.getString("id_sessao"));
        cliente.setDataNascimento(LocalDate.from(rs.getTimestamp("data_nascimento").toLocalDateTime()));
        cliente.setTotalGasto(rs.getDouble("total_gasto"));
        cliente.setPontosFidelidade(rs.getInt("pontos_fidelidade"));

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
        administrador.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        administrador.setAtivo(rs.getBoolean("ativo"));
        administrador.setUsuario(TipoUsuario.FUNCIONARIO);
        administrador.setMatricula(rs.getString("matricula"));
        administrador.setDataContratacao(rs.getTimestamp("data_contratacao") != null ? rs.getTimestamp("data_contratacao").toLocalDateTime() : LocalDateTime.now());
        administrador.setTurno(rs.getString("turno"));
        administrador.setNivelAcesso(NivelAcesso.ADMINISTRADOR);
        administrador.setSalario(rs.getDouble("salario"));

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
        cozinheiro.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        cozinheiro.setAtivo(rs.getBoolean("ativo"));
        cozinheiro.setUsuario(TipoUsuario.FUNCIONARIO);
        cozinheiro.setMatricula(rs.getString("matricula"));
        cozinheiro.setDataContratacao(rs.getTimestamp("data_contratacao") != null ? rs.getTimestamp("data_contratacao").toLocalDateTime() : LocalDateTime.now());
        cozinheiro.setTurno(rs.getString("turno"));
        cozinheiro.setNivelAcesso(NivelAcesso.COZINHEIRO);
        cozinheiro.setSalario(rs.getDouble("salario"));

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
        caixa.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        caixa.setAtivo(rs.getBoolean("ativo"));
        caixa.setUsuario(TipoUsuario.FUNCIONARIO);
        caixa.setMatricula(rs.getString("matricula"));
        caixa.setDataContratacao(rs.getTimestamp("data_contratacao") != null ? rs.getTimestamp("data_contratacao").toLocalDateTime() : LocalDateTime.now());
        caixa.setTurno(rs.getString("turno"));
        caixa.setNivelAcesso(NivelAcesso.CAIXA);
        caixa.setSalario(rs.getDouble("salario"));

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
        atendente.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        atendente.setAtivo(rs.getBoolean("ativo"));
        atendente.setUsuario(TipoUsuario.FUNCIONARIO);
        atendente.setMatricula(rs.getString("matricula"));
        atendente.setDataContratacao(rs.getTimestamp("data_contratacao") != null ? rs.getTimestamp("data_contratacao").toLocalDateTime() : LocalDateTime.now());
        atendente.setTurno(rs.getString("turno"));
        atendente.setNivelAcesso(NivelAcesso.ATENDENTE);
        atendente.setSalario(rs.getDouble("salario"));

        return atendente;
    }
}
