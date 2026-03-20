package org.frangosInfinity.infrastructure.persistence.module.produto;

import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>
{
    Optional<Categoria> findByNome(String nome);

    List<Categoria> findByAtivaTrueOrderByOrdemExibicao();

    @Query("SELECT c FROM Categoria c WHERE SIZE(c.produtos) > 0")
    List<Categoria> findCategoriaComProdutos();

    Boolean existsByNome(String nome);
}
