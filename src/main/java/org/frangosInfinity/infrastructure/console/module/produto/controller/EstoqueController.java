package org.frangosInfinity.infrastructure.console.module.produto.controller;

import org.frangosInfinity.application.module.produto.request.EstoqueRequestDTO;
import org.frangosInfinity.application.module.produto.request.ProdutoRequestDTO;
import org.frangosInfinity.core.entity.module.produto.Estoque;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.core.service.module.produto.EstoqueService;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.produto.EstoqueDAO;

import java.sql.SQLException;

public class EstoqueController {

    private EstoqueService estoqueService;

    {
        try {
            estoqueService = new EstoqueService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verificarDisponibilidade(int quantidade, Produto produto){

        try {

            if (quantidade >= 1) {
                ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO(produto.getId(),produto.getCodigo(),
                        produto.getNome(),produto.getDescricao(),produto.getPreco(),produto.getCusto(),
                        produto.getTempoPreparoMinuto(),produto.getDisponivel(),produto.getImagemUrl(),
                        produto.getVendasUltimos300dias(),produto.getPrecoPendenteAprovacao(),produto.getCategoria());

                return estoqueService.verificarDisponibilidade(quantidade, produtoRequestDTO);
            }

            throw new RuntimeException("Erro - quantidade precisa ser positiva!");

        }catch (Exception e){

            throw new RuntimeException(e.getMessage());

        }
    }

    public void reduzirEstoque(int quantidade, Estoque estoque, Produto produto){

        try {

            if(quantidade >= 1 && estoque != null && produto != null){

                ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO(produto.getId(),produto.getCodigo(),
                        produto.getNome(),produto.getDescricao(),produto.getPreco(),produto.getCusto(),
                        produto.getTempoPreparoMinuto(),produto.getDisponivel(),produto.getImagemUrl(),
                        produto.getVendasUltimos300dias(),produto.getPrecoPendenteAprovacao(),produto.getCategoria());

                EstoqueRequestDTO estoqueRequestDTO = new EstoqueRequestDTO(estoque.getId(),
                        estoque.getProduto(),estoque.getQuantidadeAtual(),estoque.getQuantidadeMinima(),
                        estoque.getQuantidadeMaxima());

                estoqueService.reduzir(quantidade,produtoRequestDTO,estoqueRequestDTO);

            }

            throw new RuntimeException("Erro - valores invalidos");

        }catch (Exception e){

            throw new RuntimeException(e.getMessage());
        }
    }

    public void aumentarestoque(int quantidade, Produto produto, Estoque estoque){

        try {

            if(quantidade >= 1 && estoque != null && produto != null){

                ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO(produto.getId(),produto.getCodigo(),
                        produto.getNome(),produto.getDescricao(),produto.getPreco(),produto.getCusto(),
                        produto.getTempoPreparoMinuto(),produto.getDisponivel(),produto.getImagemUrl(),
                        produto.getVendasUltimos300dias(),produto.getPrecoPendenteAprovacao(),produto.getCategoria());

                EstoqueRequestDTO estoqueRequestDTO = new EstoqueRequestDTO(estoque.getId(),
                        estoque.getProduto(),estoque.getQuantidadeAtual(),estoque.getQuantidadeMinima(),
                        estoque.getQuantidadeMaxima());

                estoqueService.aumentar(quantidade,produtoRequestDTO,estoqueRequestDTO);

            }

            throw new RuntimeException("Erro - valores invalidos");

        }catch (Exception e){

            throw new RuntimeException(e.getMessage());
        }
    }



}
