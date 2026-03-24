package org.frangosInfinity.infrastructure.console.module.produto.controller;

import org.frangosInfinity.application.module.produto.request.CategoriaRequestDTO;
import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.core.service.module.produto.CardapioService;

import org.frangosInfinity.core.service.module.produto.Produtoservice;
import java.util.ArrayList;

public class CardapioController {

    private Produtoservice produtoservice;
    private CardapioService cardapioService;

    {
        try {
            produtoservice = new Produtoservice();
             cardapioService = new CardapioService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Produto> verCardapio(){

        return cardapioService.Cardapio();

    }

    public ArrayList<Categoria> verCategorias(){

        return cardapioService.listarCategorias();

    }

    public ArrayList<Produto> listarProdutos(){

        return cardapioService.listarProdutos();

    }

    public Produto buscarProduto(Long id){

        if (id >= 1) {
            return cardapioService.buscarProduto(id);
        }

        throw new RuntimeException("Erro - o id precisa ser positivo!");
    }

    public ArrayList<Produto> filtrarPorCategoria(Long id){

        if(id >= 1 ) {

            return cardapioService.filtrarPorCategoria(id);
        }

        throw new RuntimeException("Erro - os valores estão incorretos!");
    }

    public ArrayList<Produto> filtrarPorPreco(Double d){

        if(d >= 1) {

            return cardapioService.filtrarPorPreco(d);

        }

        throw new RuntimeException("Erro - O valor de preco precisa ser inteiro e positivo!");
    }



}
