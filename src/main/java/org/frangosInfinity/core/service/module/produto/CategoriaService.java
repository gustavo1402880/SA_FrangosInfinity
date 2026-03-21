package org.frangosInfinity.core.service.module.produto;

import org.frangosInfinity.application.module.produto.request.CategoriaRequestDTO;
import org.frangosInfinity.application.module.produto.response.CategoriaResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.infrastructure.persistence.module.produto.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService
{
    @Autowired
    private CategoriaRepository categoriaRepository;

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
}
