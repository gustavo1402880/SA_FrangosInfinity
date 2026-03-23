package org.frangosInfinity.core.service.module.produto;
import org.frangosInfinity.application.module.produto.request.EstoqueRequestDTO;
import org.frangosInfinity.application.module.produto.request.ProdutoRequestDTO;
import org.frangosInfinity.core.entity.module.produto.Estoque;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.produto.EstoqueDAO;
import org.frangosInfinity.infrastructure.persistence.module.produto.ProdutoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class EstoqueService {

    private EstoqueDAO estoqueDAO;

    {
        try {
            estoqueDAO= new EstoqueDAO(ConnectionFactory.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verificarDisponibilidade(int quantidade, ProdutoRequestDTO produtoRequestDTO) throws Exception {


        ArrayList<Estoque> estoques = estoqueDAO.buscarEstoque();
        Produto produto = new Produto(produtoRequestDTO.getCodigo(),produtoRequestDTO.getNome(),produtoRequestDTO.getDescricao(),
                produtoRequestDTO.getPreco(),produtoRequestDTO.getCusto(),produtoRequestDTO.getTempoPreparoMinuto(),
                produtoRequestDTO.isDisponivel(),produtoRequestDTO.getImagemUrl(),produtoRequestDTO.getVendasUltimos30dias(),
                produtoRequestDTO.getPrecoPendenteAprovacao(),produtoRequestDTO.getCategoria());

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

    public void reduzir(int quantidade, ProdutoRequestDTO produtoRequestDTO, EstoqueRequestDTO estoqueRequestDTO) throws Exception{

        if(quantidade < 0|| produtoRequestDTO == null || estoqueRequestDTO == null){

            throw new Exception("Erro ao diminuir o estoque");

        }

        Produto produto = new Produto(produtoRequestDTO.getCodigo(),produtoRequestDTO.getNome(),produtoRequestDTO.getDescricao(),
                produtoRequestDTO.getPreco(),produtoRequestDTO.getCusto(),produtoRequestDTO.getTempoPreparoMinuto(),
                produtoRequestDTO.isDisponivel(),produtoRequestDTO.getImagemUrl(),produtoRequestDTO.getVendasUltimos30dias(),
                produtoRequestDTO.getPrecoPendenteAprovacao(),produtoRequestDTO.getCategoria());

        Estoque estoque = new Estoque(estoqueRequestDTO.getId(),estoqueRequestDTO.getProduto(),
                estoqueRequestDTO.getQuantidadeAtual(),estoqueRequestDTO.getQuantidadeMinima(),
                estoqueRequestDTO.getQuantidadeMaxima());

        int quantidadefinal = estoqueRequestDTO.getQuantidadeAtual() - quantidade;

        if(quantidadefinal < estoqueRequestDTO.getQuantidadeMinima()){

            throw new Exception("Erro o estoque não pode ser reduzido para menos do estoque minimo");

        }else{

            estoqueDAO.atualizarQuantidade(quantidadefinal, produto, estoque);

        }

    }

    public void aumentar(int quantidade, ProdutoRequestDTO produtoRequestDTO, EstoqueRequestDTO estoqueRequestDTO)throws Exception{

        if(quantidade < 0|| produtoRequestDTO == null || estoqueRequestDTO == null){

            throw new Exception("Erro ao aumentar o estoque");

        }

        Produto produto = new Produto(produtoRequestDTO.getCodigo(),produtoRequestDTO.getNome(),produtoRequestDTO.getDescricao(),
                produtoRequestDTO.getPreco(),produtoRequestDTO.getCusto(),produtoRequestDTO.getTempoPreparoMinuto(),
                produtoRequestDTO.isDisponivel(),produtoRequestDTO.getImagemUrl(),produtoRequestDTO.getVendasUltimos30dias(),
                produtoRequestDTO.getPrecoPendenteAprovacao(),produtoRequestDTO.getCategoria());

        Estoque estoque = new Estoque(estoqueRequestDTO.getId(),estoqueRequestDTO.getProduto(),
                estoqueRequestDTO.getQuantidadeAtual(),estoqueRequestDTO.getQuantidadeMinima(),
                estoqueRequestDTO.getQuantidadeMaxima());

        int quantidadefinal = estoque.getQuantidadeAtual() + quantidade;

        if(quantidadefinal < estoque.getQuantidadeMinima()){

            throw new Exception("Erro o estoque não pode ser aumentado para mais do estoque maximo");

        }else{

            estoqueDAO.atualizarQuantidade(quantidadefinal, produto, estoque);

        }

    }


}
