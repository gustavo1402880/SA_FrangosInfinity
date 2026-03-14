package org.frangosInfinity.core.service.module.produto;
import org.frangosInfinity.core.entity.module.produto.Estoque;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.produto.EstoqueDAO;
import java.util.ArrayList;

public class EstoqueService {

    public boolean verifiucarDisponibilidade(int quantidade, Produto produto) throws Exception {

        EstoqueDAO estoqueDAO = new EstoqueDAO(ConnectionFactory.getConnection());
        ArrayList<Estoque> estoques = estoqueDAO.buscarEstoque();

        for(Estoque e : estoques){

            if(e.getProduto().equals(produto)){

                if(e.getQuantidadeAtual() > quantidade){

                    return true;

                }else {
                    return false;
                }

            }
        }

        throw new Exception("Erro ao verificar disponibilidade");
    }

    public void reduzir(int quantidade, Produto produto,Estoque estoque) throws Exception{

        if(quantidade < 0|| produto == null || estoque == null){

            throw new Exception("Erro ao diminuir o estoque");

        }
        EstoqueDAO estoqueDAO = new EstoqueDAO(ConnectionFactory.getConnection());

        int quantidadefinal = estoque.getQuantidadeAtual() - quantidade;

        if(quantidadefinal < estoque.getQuantidadeMinima()){

            throw new Exception("Erro o estoque não pode ser reduzido para menos do estoque minimo");

        }else{

            estoqueDAO.atualizarQuantidade(quantidadefinal, produto, estoque);

        }

    }

    public void aumentar(int quantidade, Produto produto, Estoque estoque)throws Exception{

        if(quantidade < 0|| produto == null || estoque == null){

            throw new Exception("Erro ao aumentar o estoque");

        }
        EstoqueDAO estoqueDAO = new EstoqueDAO(ConnectionFactory.getConnection());

        int quantidadefinal = estoque.getQuantidadeAtual() + quantidade;

        if(quantidadefinal < estoque.getQuantidadeMinima()){

            throw new Exception("Erro o estoque não pode ser aumentado para mais do estoque maximo");

        }else{

            estoqueDAO.atualizarQuantidade(quantidadefinal, produto, estoque);

        }

    }


}
