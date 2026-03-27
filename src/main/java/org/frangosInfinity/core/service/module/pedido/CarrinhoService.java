package org.frangosInfinity.core.service.module.pedido;
import org.frangosInfinity.application.module.pedido.request.CarrinhoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.ItemPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.CarrinhoResponseDTO;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.pedido.Carrinho;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.persistence.module.produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CarrinhoService
{
    private final Map<String, Carrinho> carrinhos = new ConcurrentHashMap<>();

    @Autowired
    private ProdutoRepository produtoRepository;

    public CarrinhoResponseDTO obterCarrinho(String sessaoId)
    {
        Carrinho carrinho = carrinhos.get(sessaoId);

        if (carrinho == null)
        {
            carrinho = new Carrinho();
            carrinho.setSessaoId(sessaoId);
            carrinhos.put(sessaoId, carrinho);
        }

        return CarrinhoResponseDTO.fromEntity(carrinho);
    }

    public CarrinhoResponseDTO adicionarItem(String sessaoId, CarrinhoRequestDTO request)
    {
        Produto produto = produtoRepository.findById(request.getProdutoId()).orElseThrow(() -> new ResourceNotFoundException("Produto "+request.getProdutoId()+" não encontrado"));

        Carrinho carrinho = carrinhos.get(sessaoId);

        if (carrinho == null)
        {
            carrinho = new Carrinho();
            carrinho.setId(sessaoId);
        }

        Carrinho.ItemCarrinho itemCarrinho = new Carrinho.ItemCarrinho(
                produto.getId(),
                produto.getNome(),
                request.getQuantidade(),
                produto.getPreco(),
                produto.getTempoPreparoMinuto()
        );

        carrinho.adicionarItem(itemCarrinho);
        salvarCarrinho(sessaoId, carrinho);

        return CarrinhoResponseDTO.fromEntity(carrinho);
    }

    public CarrinhoResponseDTO removerItem(String sessaoId, Integer index)
    {
        Carrinho carrinho = carrinhos.get(sessaoId);
        if (carrinho == null)
        {
            carrinho = new Carrinho();
            carrinho.setSessaoId(sessaoId);
        }

        carrinho.removerItem(index);
        salvarCarrinho(sessaoId, carrinho);
        return CarrinhoResponseDTO.fromEntity(carrinho);
    }

    public CarrinhoResponseDTO atualizarQuantidade(String sessaoId, Integer index, Integer quantidade)
    {
        Carrinho carrinho = carrinhos.get(sessaoId);
        if (carrinho == null)
        {
            carrinho = new Carrinho();
            carrinho.setSessaoId(sessaoId);
        }

        carrinho.atualizarQuantidade(index, quantidade);
        salvarCarrinho(sessaoId, carrinho);
        return CarrinhoResponseDTO.fromEntity(carrinho);
    }

    public CarrinhoResponseDTO limparCarrinho(String sessaoId)
    {
        Carrinho carrinho = carrinhos.get(sessaoId);
        if (carrinho == null)
        {
            carrinho = new Carrinho();
            carrinho.setSessaoId(sessaoId);
        }

        carrinho.limpar();
        salvarCarrinho(sessaoId, carrinho);
        return CarrinhoResponseDTO.fromEntity(carrinho);
    }

    public void definirMesa(String sessaoId, Long mesaId)
    {
        Carrinho carrinho = carrinhos.get(sessaoId);
        if (carrinho == null)
        {
            carrinho = new Carrinho();
            carrinho.setSessaoId(sessaoId);
        }

        carrinho.setMesaId(mesaId);
        salvarCarrinho(sessaoId, carrinho);
    }

    public void salvarCarrinho(String sessaoId, Carrinho carrinho)
    {
        carrinhos.put(sessaoId,carrinho);
    }
}
