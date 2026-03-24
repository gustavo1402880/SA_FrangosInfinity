package org.frangosInfinity.infrastructure.console.module.produto.controller;

import org.frangosInfinity.application.module.produto.request.ProdutoRequestDTO;
import org.frangosInfinity.application.module.produto.response.ProdutoRespondeDTO;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.core.service.module.produto.Produtoservice;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.produto.ProdutoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutoController {

    private Produtoservice produtoservice;

    {
        try {
            produtoservice = new Produtoservice();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Produto> listarProduto(){

        try {

            return produtoservice.listarProdutos();

        }catch (Exception e){

            throw new RuntimeException(e);
        }
    }

    public Produto criarProduto(Produto produto){

        if(produto == null){
            throw new RuntimeException("Erro - produto não pode ser vazio");
        }
        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO(produto.getId(),produto.getCodigo(),
                produto.getNome(),produto.getDescricao(),produto.getPreco(),produto.getCusto(),
                produto.getTempoPreparoMinuto(),produto.getDisponivel(),produto.getImagemUrl(),
                produto.getVendasUltimos300dias(),produto.getPrecoPendenteAprovacao(),produto.getCategoria());

        ProdutoRespondeDTO produtoRespondeDTO = produtoservice.criarProduto(produtoRequestDTO);

        Produto produto1 = new Produto(produtoRespondeDTO.getCodigo(),produto.getNome(),
                produto.getDescricao(),produto.getPreco(),produto.getCusto(),produto.getTempoPreparoMinuto(), produto.getDisponivel(),
                produto.getImagemUrl(),produto.getVendasUltimos300dias(),produto.getPrecoPendenteAprovacao(),produto.getCategoria());

        return produto1;

    }

    public void excluirproduto(Produto produto){

        if(produto == null){
            throw new RuntimeException("Erro - produto não pode ser vazio");
        }

        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO(produto.getId(),produto.getCodigo(),
                produto.getNome(),produto.getDescricao(),produto.getPreco(),produto.getCusto(),
                produto.getTempoPreparoMinuto(),produto.getDisponivel(),produto.getImagemUrl(),
                produto.getVendasUltimos300dias(),produto.getPrecoPendenteAprovacao(),produto.getCategoria());

        produtoservice.excluirProduto(produtoRequestDTO);

    }

    public Boolean verificarDisponibilidade(Produto produto){

        if(produto == null){
            throw new RuntimeException("Erro - produto não pode ser vazio");
        }

        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO(produto.getId(),produto.getCodigo(),
                produto.getNome(),produto.getDescricao(),produto.getPreco(),produto.getCusto(),
                produto.getTempoPreparoMinuto(),produto.getDisponivel(),produto.getImagemUrl(),
                produto.getVendasUltimos300dias(),produto.getPrecoPendenteAprovacao(),produto.getCategoria());

        Boolean b = produtoservice.verificarDisponibilidade(produtoRequestDTO);

        return b;
    }

    public void alterarProduto(Produto produto, double preco){

        if(produto == null){
            throw new RuntimeException("Erro - produto não pode ser vazio");
        }

        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO(produto.getId(),produto.getCodigo(),
                produto.getNome(),produto.getDescricao(),preco,produto.getCusto(),
                produto.getTempoPreparoMinuto(),produto.getDisponivel(),produto.getImagemUrl(),
                produto.getVendasUltimos300dias(),produto.getPrecoPendenteAprovacao(),produto.getCategoria());

        produtoservice.alterarPreco(produtoRequestDTO);
    }


}
