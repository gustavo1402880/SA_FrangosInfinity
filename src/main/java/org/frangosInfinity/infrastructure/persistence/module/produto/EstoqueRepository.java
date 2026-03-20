package org.frangosInfinity.infrastructure.persistence.module.produto;
import org.frangosInfinity.core.entity.module.produto.Estoque;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long>
{
    @Query("SELECT e FROM Estoque e WHERE e.quantidadeAtual <= e.quantidadeMinima")
    List<Estoque> findEstoqueBaixo();

    @Query("SELECT e FROM Estoque e WHERE e.quantidadeAtual = 0")
    List<Estoque> findEstoqueZerado();

    @Modifying
    @Query("UPDATE Estoque e SET e.quantidadeAtual = e.quantidadeAtual - :quantidade WHERE e.produto.id = :produtoId AND e.quantidadeAtual >= :quantidade")
    Integer baixarestoque(@Param("produtoId") Long produtoId, @Param("quantidade") Integer quantidade);

    @Modifying
    @Query("UPDATE Estoque e SET e.quantidadeAtual = e.quantidadeAtual + :quantidade WHERE e.produto.id = :produtoId")
    Integer reporestoque(@Param("produtoId") Long produtoId, @Param("quantidade") Integer quantidade);

}
