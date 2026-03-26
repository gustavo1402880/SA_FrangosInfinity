package org.frangosInfinity.core.service.module.pagamento;

import org.frangosInfinity.application.module.pagamento.request.PagamentoRequestDTO;
import org.frangosInfinity.application.module.pagamento.response.PagamentoResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.pagamento.Comprovante;
import org.frangosInfinity.core.entity.module.pagamento.Pagamento;
import org.frangosInfinity.core.entity.module.pagamento.TransacaoPIX;
import org.frangosInfinity.core.enums.StatusPagamento;
import org.frangosInfinity.core.enums.TipoPagamento;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.ComprovanteRepository;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.PagamentoRepository;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.TransacaoPIXRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PagamentoService
{
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private TransacaoPIXRepository pixRepository;

    @Autowired
    private ComprovanteRepository comprovanteRepository;


    private Boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    private Boolean validarValor(Double valor)
    {
        return valor != null && valor > 0;
    }

    private Boolean validarTipoPagamento(TipoPagamento tipoPagamento)
    {
        return tipoPagamento != null;
    }

    @Transactional
    @CacheEvict(value = "pagamentos")
    public PagamentoResponseDTO processarPagamento(PagamentoRequestDTO request)
    {
        if (!validarId(request.getSubPedidoId()))
        {
            return criarRespostaErro("ID do subpedido inválido");
        }

        if (!validarValor(request.getValor()))
        {
            return criarRespostaErro("valor do pagamento inválido");
        }

        if (!validarTipoPagamento(request.getTipo()))
        {
            return criarRespostaErro("Tipo de pagamento inválido");
        }

        if (pagamentoRepository.existsBySubPedidoId(request.getSubPedidoId()))
        {
            throw new BusinessException("Já existe um pagamento para este subpedido");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setId_SubPedido(request.getSubPedidoId());
        pagamento.setValor(request.getValor());
        pagamento.setTipoPagamento(request.getTipo());
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        pagamento.setCodigoTransacao(UUID.randomUUID().toString());

        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        Comprovante comprovante = gerarComprovante(pagamentoSalvo);
        comprovanteRepository.save(comprovante);

        return PagamentoResponseDTO.fromEntity(pagamentoSalvo);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pagamentos", key = "#id")
    public PagamentoResponseDTO buscarPorId(Long id)
    {
        if (!validarId(id))
        {
            return PagamentoResponseDTO.erro("ID inválido");
        }

        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado"));

        return PagamentoResponseDTO.fromEntity(pagamento);
    }


    @Cacheable(value = "pagamentos", key = "#subPedidoId")
    public PagamentoResponseDTO buscarPorsubPedidoId(Long id) {

        if (!validarId(id)) {
            return null;
        }

        Pagamento pagamento = pagamentoRepository.findBySubPedidoId(id).orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado"));

        return PagamentoResponseDTO.fromEntity(pagamento);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pagamentos")
    public List<PagamentoResponseDTO> listarTodos()
    {
        return pagamentoRepository.findAll().stream()
                .map(PagamentoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pagamentos", key = "#status")
    public List<PagamentoResponseDTO> listarPorStatus(StatusPagamento status)
    {
        return pagamentoRepository.findByStatusPagamento(status).stream()
                .map(PagamentoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim)
    {
        return pagamentoRepository.findByPeriodo(inicio, fim).stream()
                .map(PagamentoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "pagamentos")
    public PagamentoResponseDTO confirmarPagamento(Long pagamentoId)
    {
        if (!validarId(pagamentoId))
        {
            return PagamentoResponseDTO.erro("ID do pagamento inválido");
        }

        Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado"));

        if (pagamento.getStatusPagamento() != StatusPagamento.PENDENTE)
        {
            throw new BusinessException("Apenas pagamentos pendentes podem ser confirmados");
        }

        pagamento.setStatusPagamento(StatusPagamento.CONFIRMADO);
        pagamentoRepository.save(pagamento);

        return PagamentoResponseDTO.fromEntity(pagamento);
    }

    @Transactional
    @CacheEvict(value = "pagamentos")
    public PagamentoResponseDTO cancelarPagamento(Long pagamentoId)
    {
        if (!validarId(pagamentoId))
        {
            return PagamentoResponseDTO.erro("ID do pagamento inválido");
        }

        Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado"));

        if (pagamento.getStatusPagamento() != StatusPagamento.PENDENTE)
        {
            throw new BusinessException("Apenas pagamentos pendentes podem ser cancelados");
        }

        pagamento.setStatusPagamento(StatusPagamento.CANCELADO);
        pagamentoRepository.save(pagamento);

        return PagamentoResponseDTO.fromEntity(pagamento);
    }

    @Transactional
    @CacheEvict(value = "pagamentos")
    public PagamentoResponseDTO reembolsarPagamento(Long pagamentoId)
    {
        if (!validarId(pagamentoId))
        {
            return PagamentoResponseDTO.erro("ID do pagamento inválido");
        }

        Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado"));

        if (pagamento.getStatusPagamento() != StatusPagamento.PENDENTE)
        {
            throw new BusinessException("Apenas pagamentos pendentes podem ser reembolsados");
        }

        pagamento.setStatusPagamento(StatusPagamento.REEMBOLSADO);
        pagamentoRepository.save(pagamento);

        return PagamentoResponseDTO.fromEntity(pagamento);
    }

    private Comprovante gerarComprovante(Pagamento pagamento)
    {
        String numero = "COMP-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        String cnpj = "12.345.678/0001-90";

        Comprovante comprovante = new Comprovante();
        comprovante.setPagamento(pagamento);
        comprovante.setNumero(numero);
        comprovante.setCnpj(cnpj);
        comprovante.setValorTotal(pagamento.getValor());
        comprovante.setFormaPagamento(pagamento.getTipoPagamento());
        comprovante.setDataHora(LocalDateTime.now());

        return comprovante;
    }

    private PagamentoResponseDTO criarRespostaErro(String mensagem)
    {
        PagamentoResponseDTO response = new PagamentoResponseDTO();
        response.setSucesso(false);
        response.setMensagem(mensagem);
        return response;
    }

    @Transactional
    @CacheEvict(value = "pagamentos")
    public void deletarPagamento(Long id)
    {
        if (!validarId(id))
        {
            throw new BusinessException("ID inválido");
        }

        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado"));

        if (pagamento.getStatusPagamento() != StatusPagamento.CANCELADO)
        {
            throw new BusinessException("Apenas pagamentos cancelados pode ser deletados");
        }

        pagamentoRepository.delete(pagamento);
    }
}
