package org.frangosInfinity.core.service.module.pedido;
import lombok.extern.slf4j.Slf4j;
import org.frangosInfinity.application.module.pedido.request.CarrinhoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.ItemPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.CarrinhoResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.pedido.Carrinho;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.persistence.module.produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
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

        if (request.getProdutoId() == null)
        {
            throw new BusinessException("Produto ID não pode ser nulo");
        }

        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto " + request.getProdutoId() + " não encontrado"));

        log.info("Produto encontrado: {} - ID: {}", produto.getNome(), produto.getId());

        Carrinho carrinho = carrinhos.get(sessaoId);

        if (carrinho == null)
        {
            carrinho = new Carrinho();
            carrinho.setSessaoId(sessaoId);
            carrinhos.put(sessaoId, carrinho);
        }

        if (carrinho.getItens() == null)
        {
            carrinho.setItens(new ArrayList<>());
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
