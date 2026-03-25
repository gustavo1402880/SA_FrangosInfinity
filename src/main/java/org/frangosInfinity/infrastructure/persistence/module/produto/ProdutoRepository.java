package org.frangosInfinity.infrastructure.persistence.module.produto;
import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>
{
    Optional<Produto> findByCodigo(String codigo);

    List<Produto> findByDisponivelTrue();

    List<Produto> findByCategoriaId(Long categoriaId);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT p FROM Produto p WHERE p.disponivel = true AND p.estoque.quantidadeAtual > 0")
    List<Produto> findDisponivelComEstoque();

    @Query("SELECT p FROM Produto p ORDER BY p.vendasUltimos30dias DESC")
    List<Produto> findMaisVendidos();

    @Query("SELECT p FROM Produto p WHERE p.precoPendenteAprovacao IS NOT NULL")
    List<Produto> findComPrecoPendente();

    Boolean existsByCodigo(String codigo);

    @Modifying
    @Query("UPDATE Produto p SET p.vendasUltimos30dias = 0")
    void resetarVendasMensais();
}
