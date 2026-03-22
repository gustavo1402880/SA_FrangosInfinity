package org.frangosInfinity.core.service.module.pagamento;

import org.frangosInfinity.application.module.pagamento.response.ComprovanteResponseDTO;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.pagamento.Comprovante;
import org.frangosInfinity.core.enums.TipoPagamento;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.ComprovanteRepository;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComprovanteService
{
    @Autowired
    private ComprovanteRepository comprovanteRepository;

    private Boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "comprovantes", key = "#id")
    public ComprovanteResponseDTO buscarPorId(Long id)
    {
        if (!validarId(id))
        {
            return null;
        }

        Comprovante comprovante = comprovanteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comprovante não encontrado"));

        ComprovanteResponseDTO response = ComprovanteResponseDTO.fromEntity(comprovante);
        response.setTexto(gerarTextoComprovante(comprovante));

        return response;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "comprovantes", key = "#pagamentoId")
    public ComprovanteResponseDTO buscarPorPagamentoId(Long pagamentoId)
    {
        if (!validarId(pagamentoId))
        {
            return criarRespostaErro("Id do pagamento inválido");
        }

        Comprovante comprovante = comprovanteRepository.findByPagamentoId(pagamentoId).orElseThrow(() -> new ResourceNotFoundException("Comprovante não encontrado"));

        ComprovanteResponseDTO response = ComprovanteResponseDTO.fromEntity(comprovante);
        response.setTexto(gerarTextoComprovante(comprovante));

        return response;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "comprovantes")
    public List<ComprovanteResponseDTO> listarTodos()
    {
        return comprovanteRepository.findAll().stream()
                .map(comprovante -> {
                    ComprovanteResponseDTO response = ComprovanteResponseDTO.fromEntity(comprovante);
                    response.setTexto(gerarTextoComprovante(comprovante));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComprovanteResponseDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim)
    {
        return comprovanteRepository.findByDataHoraBetween(inicio, fim).stream()
                .map(ComprovanteResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComprovanteResponseDTO> listarPorFormaPagamento(TipoPagamento formaPagamento)
    {
        return comprovanteRepository.findByFormaPagamento(formaPagamento).stream()
                .map(ComprovanteResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ComprovanteResponseDTO buscarPorNumero(String numero)
    {
        Comprovante comprovante = comprovanteRepository.findByNumero(numero).orElseThrow(() -> new ResourceNotFoundException("Comprovante não encontrado"));

        ComprovanteResponseDTO response = ComprovanteResponseDTO.fromEntity(comprovante);
        response.setTexto(gerarTextoComprovante(comprovante));

        return response;
    }

    @Transactional(readOnly = true)
    public Double somarValorTotalNoPeriodo(LocalDateTime inicio, LocalDateTime fim)
    {
        Double soma = comprovanteRepository.sumvalorTotalNoPeriodo(inicio, fim);

        return soma != null ? soma : 0.0;
    }

    public String gerarTextoComprovante(Comprovante comprovante)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(50)).append("\n");
        sb.append("           COMPROVANTE DE PAGAMENTO\n");
        sb.append("=".repeat(50)).append("\n\n");
        sb.append(String.format("Número: %s\n", comprovante.getNumero()));
        sb.append(String.format("Data: %s\n", comprovante.getDataHora().format(formatter)));
        sb.append(String.format("CNPJ: %s\n", comprovante.getCnpj()));
        sb.append(String.format("Valor: R$ %.2f\n", comprovante.getValorTotal()));
        sb.append(String.format("Forma: %s\n", comprovante.getFormaPagamento()));

        if (comprovante.getQrCodeString() != null)
        {
            sb.append("\n").append("QR Code PIX:").append("\n");
            sb.append(comprovante.getQrCodeString()).append("\n");
        }

        sb.append("\n").append("=".repeat(50)).append("\n");

        return sb.toString();
    }

    private ComprovanteResponseDTO criarRespostaErro(String mensagem)
    {
        ComprovanteResponseDTO response = new ComprovanteResponseDTO();
        response.setSucesso(false);
        response.setMensagem(mensagem);
        return response;
    }
}
