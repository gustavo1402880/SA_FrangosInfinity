package org.frangosInfinity.core.service.module.pedido;

import org.frangosInfinity.application.module.pedido.request.PedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.SubPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.PedidoResponseDTO;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.enums.StatusPedido;
import org.frangosInfinity.infrastructure.persistence.module.pedido.PedidoRepository;
import org.frangosInfinity.infrastructure.persistence.module.pedido.SubPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService
{
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private SubPedidoRepository subPedidoRepository;

    @Autowired
    private CarrinhoService carrinhoService;

    private Boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    @Transactional
    @CacheEvict(value = "pedidos")
    public PedidoResponseDTO criarPedido(PedidoRequestDTO pedidoRequest)
    {
        if (pedidoRequest.getMesaId() != null)
        {
            List<Pedido> ativos = pedidoRepository.findAtivosPorMesa(pedidoRequest.getMesaId());
            if (!ativos.isEmpty())
            {
                return PedidoResponseDTO.erro("Mesa já possui um pedido ativo");
            }
        }

        Pedido pedido = new Pedido(
                pedidoRequest.getTipo(),
                pedidoRequest.getMesaId(),
                pedidoRequest.getAtendenteId()
        );

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return PedidoResponseDTO.fromEntity(pedidoSalvo);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pedidos", key = "#id")
    public PedidoResponseDTO buscarPorId(Long id)
    {
        if (!validarId(id))
        {
            return PedidoResponseDTO.erro("ID inválido");
        }

        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        return PedidoResponseDTO.fromEntity(pedido);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorNumero(String numeroPedido)
    {
        Pedido pedido = pedidoRepository.findByNumeroPedido(numeroPedido).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        return PedidoResponseDTO.fromEntity(pedido);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pedidos")
    public List<PedidoResponseDTO> listarTodos()
    {
        return pedidoRepository.findAll().stream()
                .map(PedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pedidos", key = "#status")
    public List<PedidoResponseDTO> listarPorStatus(StatusPedido status)
    {
        return pedidoRepository.findByStatus(status).stream()
                .map(PedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim)
    {
        return pedidoRepository.findByPeriodo(inicio, fim).stream()
                .map(PedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarAtivosPorMesa(Long mesaId)
    {
        return pedidoRepository.findAtivosPorMesa(mesaId).stream()
                .map(PedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @CacheEvict(value = "pedidos")
    public void deletarPedido(Long id)
    {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        pedidoRepository.delete(pedido);
    }
}
