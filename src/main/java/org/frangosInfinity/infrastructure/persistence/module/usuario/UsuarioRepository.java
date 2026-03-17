package org.frangosInfinity.infrastructure.persistence.module.usuario;

import org.frangosInfinity.core.entity.module.usuario.*;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{
    Optional<Usuario> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<Usuario> findByAtivoTrue();

    List<Usuario> findByAtivoFalse();

    List<Usuario> findByTipo(TipoUsuario tipo);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT f FROM Funcionario f WHERE f.matricula = :matricula")
    Optional<Funcionario> buscarFuncionarioPorMatricula(@Param("matricula")String matricula);

    @Query("SELECT f FROM Funcionario f WHERE f.nivelAcesso = :nivelAcesso")
    List<Funcionario> buscarFuncionarioPorNivelAcesso(@Param("nivelAcesso")NivelAcesso nivelAcesso);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Funcionario f WHERE f.matricula = :matricula")
    Boolean existsFuncionarioByMatricula(@Param("matricula")String matricula);

    @Query("SELECT c FROM Cliente c WHERE c.idSessao = :idSessao")
    Optional<Cliente> buscarClientePorIdSessao(@Param("idSessao") String idSessao);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c WHERE c.idSessao = :idSessao")
    Boolean existsClienteByIdSessao(@Param("idSessao") String idSessao);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tipo = :tipo")
    Long contarPorTipo(@Param("tipo") TipoUsuario tipo);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ativo = true ")
    Long contarAtivos();

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ativo = false ")
    Long contarInativos();

    @Query("SELECT u FROM Usuario u WHERE u.dataCadastro BETWEEN :inicio AND :fim")
    List<Usuario> buscarPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim")LocalDateTime fim);
}
