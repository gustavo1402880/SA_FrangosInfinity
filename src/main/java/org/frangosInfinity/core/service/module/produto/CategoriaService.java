package org.frangosInfinity.core.service.module.produto;

import org.frangosInfinity.application.module.produto.request.CategoriaRequestDTO;
import org.frangosInfinity.application.module.produto.response.CategoriaResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.infrastructure.persistence.module.produto.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService
{
    @Autowired
    private CategoriaRepository categoriaRepository;

    public Boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    @Transactional
    @CacheEvict(value = "categorias")
    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO request) {
        if (categoriaRepository.existsByNome(request.getNome())) {
            throw new BusinessException("Categoria já existente");
        }

        Categoria categoria = new Categoria(
                request.getNome(),
                request.getOrdemExibicao()
        );
        categoria.setDescricao(request.getDescricao());

        Categoria categoriaSalva = categoriaRepository.save(categoria);

        return CategoriaResponseDTO.fromEntity(categoriaSalva);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "categorias", key = "#id")
    public CategoriaResponseDTO buscarPorId(Long id)
    {
        if (!validarId(id))
        {
            throw new BusinessException("ID inválido");
        }

        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        return CategoriaResponseDTO.fromEntity(categoria);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "categorias")
    public List<CategoriaResponseDTO> listarTodas()
    {
        return categoriaRepository.findAll().stream()
                .map(CategoriaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "categorias")
    public List<CategoriaResponseDTO> listarAtivas()
    {
        return categoriaRepository.findByAtivaTrueOrderByOrdemExibicao().stream()
                .map(CategoriaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "categorias")
    public CategoriaResponseDTO atualizarCategoria(Long id, CategoriaRequestDTO request)
    {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (request.getNome() != null && !request.getNome().equals(categoria.getNome()))
        {
            if (categoriaRepository.existsByNome(request.getNome()))
            {
                throw new BusinessException("Nome já cadastrado: "+request.getNome());
            }
        }

        if (request.getDescricao() != null) categoria.setDescricao(request.getDescricao());
        if (request.getOrdemExibicao() != null) categoria.setOrdemExibicao(request.getOrdemExibicao());

        categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(categoria);
    }

    @Transactional
    @CacheEvict(value = "categorias")
    public CategoriaResponseDTO ativarCategoria(Long id)
    {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        categoria.setAtiva(true);
        categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(categoria);
    }

    @Transactional
    @CacheEvict(value = "categorias")
    public CategoriaResponseDTO desativarCategoria(Long id)
    {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        categoria.setAtiva(false);
        categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(categoria);
    }

    @Transactional
    @CacheEvict(value = "categorias")
    public void deletarCategoria(Long id)
    {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (!categoria.getProdutos().isEmpty())
        {
            throw new BusinessException("Não é possível deletar categoria com produtos associados");
        }

        categoriaRepository.delete(categoria);
    }
}
