package org.frangosInfinity.core.service.module.pedido;
import org.frangosInfinity.application.module.pedido.request.CarrinhoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.ItemPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.CarrinhoResponseDTO;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.pedido.Carrinho;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pedido.ItemPedidoRepository;
import org.frangosInfinity.infrastructure.persistence.module.produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class CarrinhoService
{
    private RedisTemplate<String, Carrinho> redisTemplate;

    @Autowired
    private ProdutoRepository produtoRepository;

    private static String CARRINHO_KEY = "carrinho:";
    private static long CARRINHO_TTL = 3600;

    public CarrinhoResponseDTO obterCarrinho(String sessaoId)
    {
        String key = CARRINHO_KEY + sessaoId;
        Carrinho carrinho = redisTemplate.opsForValue().get(key);

        if (carrinho == null)
        {
            carrinho = new Carrinho();
            redisTemplate.opsForValue().set(key, carrinho, CARRINHO_TTL, TimeUnit.SECONDS);
        }

        return CarrinhoResponseDTO.fromEntity(carrinho);
    }

    public CarrinhoResponseDTO adicionarItem(String sessaoId, CarrinhoRequestDTO request)
    {
        Produto produto = produtoRepository.findById(request.getProdutoId()).orElseThrow(() -> new ResourceNotFoundException("Produto "+request.getProdutoId()+" não encontrado"));

        String key = CARRINHO_KEY + sessaoId;
        Carrinho carrinho = redisTemplate.opsForValue().get(key);

        if (carrinho == null)
        {
            carrinho = new Carrinho();
            redisTemplate.opsForValue().set(key, carrinho, CARRINHO_TTL, TimeUnit.SECONDS);
        }

        Carrinho.ItemCarrinho itemCarrinho = new Carrinho.ItemCarrinho(
                produto.getId(),
                produto.getNome(),
                produto.getEstoque().getQuantidadeAtual(),
                produto.getPreco(),
                produto.getTempoPreparoMinuto()
        );

        carrinho.adicionarItem(itemCarrinho);

        salvarCarrinho(sessaoId, carrinho);

        return CarrinhoResponseDTO.fromEntity(carrinho);
    }

    public CarrinhoResponseDTO removerItem(String sessaoId, Integer index)
    {
        String key = CARRINHO_KEY + sessaoId;
        Carrinho carrinho = redisTemplate.opsForValue().get(key);

        if (carrinho == null)
        {
            carrinho = new Carrinho();
            redisTemplate.opsForValue().set(key, carrinho, CARRINHO_TTL, TimeUnit.SECONDS);
        }

        carrinho.removerItem(index);
        salvarCarrinho(sessaoId, carrinho);
        return CarrinhoResponseDTO.fromEntity(carrinho);
    }

    public CarrinhoResponseDTO atualizarQuantidade(String sessaoId, Integer index, Integer quantidade)
    {
        String key = CARRINHO_KEY + sessaoId;
        Carrinho carrinho = redisTemplate.opsForValue().get(key);

        if (carrinho == null)
        {
            carrinho = new Carrinho();
        }

        carrinho.atualizarQuantidade(index, quantidade);
        salvarCarrinho(sessaoId, carrinho);
        return CarrinhoResponseDTO.fromEntity(carrinho);
    }

    public CarrinhoResponseDTO limparCarrinho(String sessaoId)
    {
        String key = CARRINHO_KEY + sessaoId;
        Carrinho carrinho = redisTemplate.opsForValue().get(key);

        if (carrinho == null)
        {
            carrinho = new Carrinho();
        }

        carrinho.limpar();
        salvarCarrinho(sessaoId, carrinho);
        return CarrinhoResponseDTO.fromEntity(carrinho);
    }

    public void definirMesa(String sessaoId, Long mesaId)
    {
        String key = CARRINHO_KEY + sessaoId;
        Carrinho carrinho = redisTemplate.opsForValue().get(key);

        if (carrinho == null)
        {
            carrinho = new Carrinho();
        }

        carrinho.setMesaId(mesaId);
        salvarCarrinho(sessaoId, carrinho);
    }

    public void salvarCarrinho(String sessaoId, Carrinho carrinho)
    {
        String key = CARRINHO_KEY + sessaoId;
        redisTemplate.opsForValue().set(key, carrinho);
    }
}
