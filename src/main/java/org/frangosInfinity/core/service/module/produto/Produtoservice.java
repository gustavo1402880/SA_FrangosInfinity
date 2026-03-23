package org.frangosInfinity.core.service.module.produto;
import org.frangosInfinity.application.module.produto.request.ProdutoRequestDTO;
import org.frangosInfinity.application.module.produto.response.ProdutoRespondeDTO;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.produto.ProdutoDAO;
import java.sql.SQLException;
import java.util.ArrayList;

public class Produtoservice {

    private ProdutoDAO produtoDAO;

    {
        try {
            produtoDAO = new ProdutoDAO(ConnectionFactory.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<Produto> listarProdutos(){

        return produtoDAO.buscarProduto();

    }

    public ProdutoRespondeDTO criarProduto(ProdutoRequestDTO produtoRequestDTO){

        Produto produto = new Produto(produtoRequestDTO.getCodigo(),produtoRequestDTO.getNome(),produtoRequestDTO.getDescricao(),produtoRequestDTO.getPreco(),produtoRequestDTO.getCusto(), produtoRequestDTO.getTempoPreparoMinuto(),true, produtoRequestDTO.getImagemUrl(), produtoRequestDTO.getVendasUltimos30dias(),produtoRequestDTO.getPrecoPendenteAprovacao(),produtoRequestDTO.getCategoria());

        produtoDAO.adicionarProduto(produto);

        ProdutoRespondeDTO produtoRespondeDTO;

        return produtoRespondeDTO = new ProdutoRespondeDTO(produto.getId(),produtoRequestDTO.getCodigo(),produtoRequestDTO.getCodigo(),produtoRequestDTO.getNome(),produtoRequestDTO.getPreco(),produtoRequestDTO.getPreco(),produtoRequestDTO.getTempoPreparoMinuto(),true, produtoRequestDTO.getImagemUrl(),produtoRequestDTO.getVendasUltimos30dias(), produtoRequestDTO.getPrecoPendenteAprovacao(),produtoRequestDTO.getCategoria());
    }

    public void excluirProduto(ProdutoRequestDTO produtoRequestDTO){

        produtoDAO.deletarProduto(produtoRequestDTO.getId());

    }

    public Boolean verificarDisponibilidade(ProdutoRequestDTO produtoRequestDTO){

        Produto produto = produtoDAO.buscarPorId(produtoRequestDTO.getId());

        return produto.getDisponivel();

    }

    public void alterarPreco(ProdutoRequestDTO produtoRequestDTO){

        produtoDAO.alterarPreco(produtoRequestDTO.getId(),produtoRequestDTO.getPreco());

    }

}
