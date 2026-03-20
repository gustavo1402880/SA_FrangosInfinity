package org.frangosInfinity.core.service.module.produto;
import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.produto.CategoriaDAO;
import org.frangosInfinity.infrastructure.persistence.module.produto.ProdutoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class CardapioService {

    private ProdutoDAO produtoDAO;
    private CategoriaDAO categoriaDAO;

    {
        try {
            produtoDAO = new ProdutoDAO(ConnectionFactory.getConnection());
            categoriaDAO = new CategoriaDAO(ConnectionFactory.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Produto> Cardapio(){

        ArrayList<Produto> produtos;

        return produtos = produtoDAO.buscarProduto();

    }

    public ArrayList<Categoria> listarCategorias(){

        ArrayList<Categoria> categorias;

        return categorias = categoriaDAO.buscarCategoria();

    }

    public ArrayList<Produto> listarProdutos(){

        ArrayList<Produto> produtos;

        return produtos = produtoDAO.buscarProduto();

    }

    public Produto buscarProduto(Long id){

        Produto produto = produtoDAO.buscarPorId(id);

        return produto;
    }

    public ArrayList<Produto> filtrarPorCategoria(int categoria){

        ArrayList<Produto> produtos = new ArrayList<>();
        ArrayList<Produto>  produtos1 = new ArrayList<>();

        produtos = produtoDAO.buscarProduto();

        for(Produto produto : produtos){
            if(produto.getCategoria().getId() == categoria ){
                produtos1.add(produto);
            }
        }

        return produtos1;

    }

    public ArrayList<Produto> filtrarPorPreco(double d ){

        ArrayList<Produto> produtos = new ArrayList<>();
        ArrayList<Produto>  produtos1 = new ArrayList<>();

        produtos = produtoDAO.buscarProduto();

        for(Produto produto : produtos){
            if(produto.getPreco() == d){
                produtos1.add(produto);
            }
        }

        return produtos1;

    }

}
