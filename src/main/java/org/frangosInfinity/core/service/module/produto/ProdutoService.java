package org.frangosInfinity.core.service.module.produto;

import org.frangosInfinity.application.module.produto.request.ProdutoRequestDTO;
import org.frangosInfinity.application.module.produto.response.ProdutoRespondeDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.core.entity.module.produto.Estoque;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.persistence.module.produto.CategoriaRepository;
import org.frangosInfinity.infrastructure.persistence.module.produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService
{
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    @Transactional
    public ProdutoRespondeDTO criarProduto(ProdutoRequestDTO request)
    {
        if (produtoRepository.existsByCodigo(request.getCodigo()))
        {
            throw new BusinessException("Código já existente");
        }

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Produto produto = new Produto(
                request.getCodigo(),
                request.getNome(),
                request.getPreco(),
                categoria
        );
        produto.setDescricao(request.getDescricao());
        produto.setPreco(request.getPreco());
        produto.setTempoPreparoMinuto(request.getTempoPreparoMinuto());
        produto.setImagemUrl(request.getImagemUrl());

        Produto produtoSalvo = produtoRepository.save(produto);

        Estoque estoque = new Estoque(
                produtoSalvo,
                request.getEstoqueInicial() != null ? request.getEstoqueInicial() : 0,
                request.getEstoqueInicial() != null ? request.getEstoqueMinimo() : 0,
                request.getEstoqueMaximo() != null ? request.getEstoqueMaximo() : 0
        );

        produtoSalvo.setEstoque(estoque);

        produtoRepository.save(produtoSalvo);

        return ProdutoRespondeDTO.fromEntity(produtoSalvo);
    }

    @Transactional
    @Cacheable(value = "pedidos", key = "#id")
    public ProdutoRespondeDTO buscarPorId(Long id)
    {
        if (!validarId(id))
        {
            throw new BusinessException("ID inválido");
        }

        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return ProdutoRespondeDTO.fromEntity(produto);
    }

    @Transactional(readOnly = true)
    public ProdutoRespondeDTO buscarPorCodigo(String codigo)
    {
        Produto produto = produtoRepository.findByCodigo(codigo).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return ProdutoRespondeDTO.fromEntity(produto);
    }

    @Transactional
    @Cacheable(value = "produtos")
    public List<ProdutoRespondeDTO> listarTodos()
    {
        return produtoRepository.findAll().stream()
                .map(ProdutoRespondeDTO::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional
    @Cacheable(value = "produtos")
    public List<ProdutoRespondeDTO> listarDisponiveis()
    {
        return produtoRepository.findByDisponivelTrue().stream()
                .map(ProdutoRespondeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Cacheable(value = "produtos")
    public List<ProdutoRespondeDTO> listarPorCategoria(Long categoriaId)
    {
        return produtoRepository.findByCategoriaId(categoriaId).stream()
                .map(ProdutoRespondeDTO::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional
    @Cacheable(value = "produtos")
    public List<ProdutoRespondeDTO> listarMaisVendidos()
    {
        return produtoRepository.findMaisVendidos().stream()
                .map(ProdutoRespondeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "produtos")
    public ProdutoRespondeDTO atualizarProduto(Long id, ProdutoRequestDTO request)
    {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (request.getNome() != null) produto.setNome(request.getNome());

        if (request.getDescricao() != null) produto.setDescricao(request.getDescricao());

        if (request.getTempoPreparoMinuto() != null) produto.setTempoPreparoMinuto(request.getTempoPreparoMinuto());

        if (request.getImagemUrl() != null) produto.setImagemUrl(request.getImagemUrl());

        if (request.getPreco() != null && !request.getPreco().equals(produto.getPreco()))
        {
            produto.solicitarAlteracaoPreco(request.getPreco());
        }

        if (request.getCategoriaId() != null && !request.getCategoriaId().equals(produto.getCategoria().getId()))
        {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

            produto.setCategoria(categoria);
        }

        produtoRepository.save(produto);

        return ProdutoRespondeDTO.fromEntity(produto);
    }

    @Transactional
    @CacheEvict(value = "produtos")
    public ProdutoRespondeDTO alterarDisponibilidade(Long id, Boolean disponivel)
    {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        produto.setDisponivel(disponivel);

        produtoRepository.save(produto);

        return ProdutoRespondeDTO.fromEntity(produto);
    }

    @Transactional
    @CacheEvict(value = "produtos")
    public ProdutoRespondeDTO aprovarAlteracaoPreco(Long id)
    {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        produto.aprovarAlteracaoPreco();

        produtoRepository.save(produto);

        return ProdutoRespondeDTO.fromEntity(produto);
    }

    @Transactional
    @CacheEvict(value = "produto")
    public void deletarProduto(Long id)
    {
        if (!validarId(id))
        {
            throw new BusinessException("ID inválido");
        }

        if (!produtoRepository.existsById(id))
        {
            throw new BusinessException("Produto não encontrado");
        }

        produtoRepository.deleteById(id);
    }
}
