package org.frangosInfinity.core.service.module.produto;
import org.frangosInfinity.application.module.produto.request.ProdutoRequestDTO;
import org.frangosInfinity.application.module.produto.response.ProdutoRespondeDTO;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.produto.ProdutoDAO;
import java.sql.SQLException;

public class Produtoservice {

    //  + edi tar(dados: Produto): void
    //  + exclui r(id: int ): void
    //  + l i star(): Li st<Produto>
    //  + veri f i carDi sponibi l idade(): Boolean
    //  + Al teracaoPreco(novoPreco: double): Boolean

    private ProdutoDAO produtoDAO;

    {
        try {
            produtoDAO = new ProdutoDAO(ConnectionFactory.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ProdutoRespondeDTO criarProduto(ProdutoRequestDTO produtoRequestDTO){

        Produto produto = new Produto(produtoRequestDTO.getCodigo(),produtoRequestDTO.getNome(),produtoRequestDTO.getDescricao(),produtoRequestDTO.getPreco(),produtoRequestDTO.getCusto(), produtoRequestDTO.getTempoPreparoMinuto(),true, produtoRequestDTO.getImagemUrl(), produtoRequestDTO.getVendasUltimos30dias(),produtoRequestDTO.getPrecoPendenteAprovacao(),produtoRequestDTO.getCategoria());

        produtoDAO.adicionarProduto(produto);

        ProdutoRespondeDTO produtoRespondeDTO;

        return produtoRespondeDTO = new ProdutoRespondeDTO(produto.getId(),produtoRequestDTO.getCodigo(),produtoRequestDTO.getCodigo(),produtoRequestDTO.getNome(),produtoRequestDTO.getPreco(),produtoRequestDTO.getPreco(),produtoRequestDTO.getTempoPreparoMinuto(),true, produtoRequestDTO.getImagemUrl(),produtoRequestDTO.getVendasUltimos30dias(), produtoRequestDTO.getPrecoPendenteAprovacao(),produtoRequestDTO.getCategoria());
    }



}
