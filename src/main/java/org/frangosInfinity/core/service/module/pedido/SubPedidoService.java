package org.frangosInfinity.core.service.module.pedido;

import org.frangosInfinity.application.module.pedido.request.ItemPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.SubPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.PedidoResponseDTO;
import org.frangosInfinity.application.module.pedido.response.SubPedidoResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.core.enums.StatusPedido;
import org.frangosInfinity.infrastructure.persistence.module.pedido.PedidoRepository;
import org.frangosInfinity.infrastructure.persistence.module.pedido.SubPedidoRepository;
import org.frangosInfinity.infrastructure.persistence.module.produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubPedidoService
{
    @Autowired
    private SubPedidoRepository subPedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;


    public Boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    @Transactional
    @CacheEvict(value = "subPedidos")
    public SubPedidoResponseDTO criarSubPedido(SubPedidoRequestDTO request)
    {
        Pedido pedido = pedidoRepository.findById(request.getPedidoId()).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        SubPedido subPedido = new SubPedido(pedido, request.getClienteId());

        for (ItemPedidoRequestDTO itemRequest : request.getItens())
        {
            Produto produto = produtoRepository.findById(itemRequest.getProdutoId()).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            if (!produto.getDisponivel())
            {
                throw new BusinessException("Produto não está diponível");
            }

            if (produto.getEstoque() != null && !produto.getEstoque().temEstoque(itemRequest.getQuantidade()))
            {
                throw new BusinessException("Estoque insuficiente");
            }

            ItemPedido itemPedido = new ItemPedido(
                    produto.getId(),
                    produto.getNome(),
                    itemRequest.getQuantidade(),
                    produto.getPreco(),
                    produto.getTempoPreparoMinuto()
            );
            itemPedido.setObservacao(itemRequest.getObservacao());

            subPedido.adicionarItem(itemPedido);

            if (produto.getEstoque() != null)
            {
                produto.getEstoque().baixarEstoque(itemRequest.getQuantidade());
            }

            produtoRepository.save(produto);
        }

        SubPedido subPedidoSalvo = subPedidoRepository.save(subPedido);

        return SubPedidoResponseDTO.fromEntity(subPedidoSalvo);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pedidos", key = "#id")
    public SubPedidoResponseDTO buscarPorId(Long id)
    {
        if (!validarId(id))
        {
            throw new BusinessException("ID inválido");
        }

        SubPedido subPedido = subPedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SubPedido não encontrado"));

        return SubPedidoResponseDTO.fromEntity(subPedido);
    }

    @Transactional(readOnly = true)
    public List<SubPedidoResponseDTO> listarPorPedidoId(Long pedidoId)
    {
        if (!validarId(pedidoId))
        {
            throw new BusinessException("ID inválido");
        }

        List<SubPedido> subPedidos = subPedidoRepository.findByPedidoId(pedidoId);

        return subPedidos.stream()
                .map(SubPedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubPedidoResponseDTO atualizarStatus(Long id, StatusPedido statusPedido)
    {
        SubPedido subPedido = subPedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SubPedido não encontrado"));

        subPedido.setStatus(statusPedido);

        subPedidoRepository.save(subPedido);
        return SubPedidoResponseDTO.fromEntity(subPedido);
    }

    @Transactional(readOnly = true)
    public SubPedidoResponseDTO finalizarSubPedido(Long id)
    {
        return atualizarStatus(id, StatusPedido.PENDENTE);
    }

    @Transactional(readOnly = true)
    public SubPedidoResponseDTO confimarSubPedido(Long id)
    {
        return atualizarStatus(id, StatusPedido.CONFIMADO);
    }

    @Transactional(readOnly = true)
    public SubPedidoResponseDTO prepararSubPedido(Long id)
    {
        return atualizarStatus(id, StatusPedido.EM_PREPARO);
    }

    @Transactional(readOnly = true)
    public SubPedidoResponseDTO marcarProntoSubPedido(Long id)
    {
        return atualizarStatus(id, StatusPedido.PRONTO);
    }

    @Transactional(readOnly = true)
    public SubPedidoResponseDTO entregarSubPedido(Long id)
    {
        return atualizarStatus(id, StatusPedido.ENTREGUE);
    }

    @Transactional(readOnly = true)
    public SubPedidoResponseDTO cancelarSubPedido(Long id)
    {
        return atualizarStatus(id, StatusPedido.CANCELADO);
    }
}
