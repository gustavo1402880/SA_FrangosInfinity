package org.frangosInfinity.core.service.module.pagamento;

import org.frangosInfinity.application.module.pagamento.request.PIXRequestDTO;
import org.frangosInfinity.application.module.pagamento.response.PIXResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.pagamento.Pagamento;
import org.frangosInfinity.core.entity.module.pagamento.TransacaoPIX;
import org.frangosInfinity.core.enums.TipoPagamento;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.PagamentoRepository;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.TransacaoPIXRepository;
import org.frangosInfinity.infrastructure.util.GeradorQRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransacaoPIXService
{
    @Autowired
    private TransacaoPIXRepository pixRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private GeradorQRCode geradorQRCode;

    @Value("${app.baseUrl:http://localhost:8080")
    private String baseUrl;

    private Boolean validarId(Long id) {
        return id != null && id > 0;
    }

    @Transactional
    @CacheEvict(value = "pix")
    public PIXResponseDTO gerarPix(PIXRequestDTO request) {
        if (request.getPagamentoId() == null || request.getPagamentoId() <= 0) {
            throw new BusinessException("ID do pagamento inválido");
        }

        Pagamento pagamento = pagamentoRepository.findById(request.getPagamentoId()).orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado"));

        if (pagamento.getTipoPagamento() != TipoPagamento.PIX) {
            throw new BusinessException("Este pagamento não é do tipo PIX");
        }

        var pixEsistente = pixRepository.findByPagamentoId(request.getPagamentoId());

        if (pixEsistente.isPresent()) {
            TransacaoPIX pix = pixEsistente.get();
            if (!pix.idExpirado()) {
                PIXResponseDTO response = PIXResponseDTO.fromEntity(pix);

                response.setMensagem("PIX já existente e válido");

                String caminhoImagem = gerarQRCodeImagem(pix);
                response.setQrCode(caminhoImagem);

                return response;
            }
        }

        TransacaoPIX pix = new TransacaoPIX();
        pix.setPagamento(pagamento);

        String codigoPix = gerarCodigoPix(pagamento.getValor());
        pix.setCodigoCopiaCola(codigoPix);

        Integer tempoExpiracao = request.getTempoExpiracaoSegundos() != null ? request.getTempoExpiracaoSegundos() : 600;
        pix.setTempoExpiracaoSegundos(tempoExpiracao);
        pix.setDataExpiracao(LocalDateTime.now().plusSeconds(tempoExpiracao));

        String qrCodeUrl = baseUrl + "/pagamentos/pix/" + pagamento.getId_Pagamento() + "/confimar";

        String nomeArquivo = String.format("pix_pagamento_%d.png", pagamento.getId_Pagamento());

        String caminhoImagem = geradorQRCode.gerarQRCode(qrCodeUrl, nomeArquivo);

        if (caminhoImagem != null) {
            pix.setQrCode(caminhoImagem);
        } else {
            pix.setQrCode(null);
        }

        TransacaoPIX pixSalvo = pixRepository.save(pix);

        PIXResponseDTO response = PIXResponseDTO.fromEntity(pixSalvo);
        response.setQrCode(caminhoImagem);

        return response;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pix", key = "#id")
    public PIXResponseDTO buscarPorId(Long id) {
        if (!validarId(id)) {
            throw new BusinessException("ID inválido");
        }

        TransacaoPIX pix = pixRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PIX não encontrado"));

        return PIXResponseDTO.fromEntity(pix);
    }

    @Transactional
    @Cacheable(value = "pix", key = "#pagamentoId")
    public PIXResponseDTO buscarPorPagamentoId(Long pagamentoId) {
        if (!validarId(pagamentoId)) {
            throw new BusinessException("ID inválido");
        }

        TransacaoPIX pix = pixRepository.findByPagamentoId(pagamentoId).orElseThrow(() -> new ResourceNotFoundException("PIX não encontrado"));

        return PIXResponseDTO.fromEntity(pix);
    }

    @Transactional
    @CacheEvict(value = "pix")
    public PIXResponseDTO renovarPix(Long pagamentoId, Integer novoTempoSegundos) {
        if (!validarId(pagamentoId)) {
            return criarRespostaErro("ID do pagamento inválido");
        }

        TransacaoPIX pix = pixRepository.findByPagamentoId(pagamentoId).orElseThrow(() -> new ResourceNotFoundException("PIX não encontrado"));

        if (novoTempoSegundos != null && novoTempoSegundos > 0) {
            pix.setTempoExpiracaoSegundos(novoTempoSegundos);
        }
        pix.renovar();

        pixRepository.save(pix);

        return PIXResponseDTO.fromEntity(pix);
    }

    @Transactional(readOnly = true)
    public Boolean verificarExpiracao(Long pagamentoId)
    {
        return pixRepository.findByPagamentoId(pagamentoId)
                .map(TransacaoPIX::idExpirado)
                .orElse(true);
    }

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void limparPIXExpirados()
    {
        LocalDateTime dataLimite = LocalDateTime.now().minusHours(24);
        Integer deletados = pixRepository.deleteExpiradoAntigos(dataLimite);
    }

    private String gerarCodigoPix(Double valor)
    {
        String chave = UUID.randomUUID().toString().replace("-","").substring(0,20);

        return String.format("00020126580014br.gov.bcb.pix0136%s520400005303986540%.2f5802BR5903WEG6014JARAGUA DO SUL62290525%s6304",chave, valor, chave.substring(0,10));
    }

    private String gerarQRCodeImagem(TransacaoPIX pix)
    {
        String qrCodeUrl = baseUrl + "/pagamentos/pix/" + pix.getPagamento().getId_Pagamento() + "/confirmar";

        String nomeArquivo = String.format("pix_pagamento_%d.png",pix.getPagamento().getId_Pagamento());

        return geradorQRCode.gerarQRCode(qrCodeUrl, nomeArquivo);
    }

    private PIXResponseDTO criarRespostaErro(String mensagem)
    {
        PIXResponseDTO response = new PIXResponseDTO();
        response.setSucesso(false);
        response.setMensagem(mensagem);
        return response;
    }
}
