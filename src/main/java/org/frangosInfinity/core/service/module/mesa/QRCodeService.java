package org.frangosInfinity.core.service.module.mesa;

import org.frangosInfinity.application.module.mesa.request.QRCodeRequestDTO;
import org.frangosInfinity.application.module.mesa.response.QRCodeResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.mesa.QRCode;
import org.frangosInfinity.infrastructure.persistence.module.mesa.MesaRepository;
import org.frangosInfinity.infrastructure.persistence.module.mesa.QRCodeRepository;
import org.frangosInfinity.infrastructure.util.Configuracao;
import org.frangosInfinity.infrastructure.util.Formatador;
import org.frangosInfinity.infrastructure.util.GeradorQRCode;
import org.frangosInfinity.infrastructure.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QRCodeService {
    @Autowired
    private QRCodeRepository qrCodeRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private GeradorQRCode geradorQRCode;

    @Autowired
    private Validator validator;

    @Autowired
    private Configuracao configuracao;

    @Autowired
    private Formatador formatador;

    @Transactional
    @CacheEvict(value = "qrCodes")
    public QRCodeResponseDTO gerarQRCode(QRCodeRequestDTO request)
    {
        if (!validator.validarRequestQRCode(request))
        {
            return QRCodeResponseDTO.erro("Dados inválidos para geração do QR Code");
        }

        Mesa mesa = mesaRepository.findById(request.getIdMesa()).orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada: " + request.getIdMesa()));

        if (!mesa.isAtiva())
        {
            return QRCodeResponseDTO.erro("Mesa " + mesa.getNumero() + " está desativada");
        }

        qrCodeRepository.findByMesaIdAndAtivoTrue(mesa.getId())
                .forEach(qr -> {
                    qr.setAtivo(false);
                    qrCodeRepository.save(qr);
                });

        QRCode qrCode = new QRCode();
        qrCode.setIdMesa(mesa);

        int tempoExpiracao = configuracao.getIntProperty("qr.code.tempo.expiracao", 120);

        if (request.getTempoExpiracaoSgundos() != null && request.getTempoExpiracaoSgundos() > 0) {
            tempoExpiracao = request.getTempoExpiracaoSgundos();
        }

        qrCode.setDataExpiracao(qrCode.getDataCriacao().plusSeconds(tempoExpiracao));

        String baseUrl = configuracao.getProperty("app.baseUrl", "localhost:8080");
        String url = String.format("%s/auth/mesa/%d/%s", baseUrl, mesa.getId(), qrCode.getTokenSessao());
        qrCode.setUrlAutenticacao(url);

        String diretorio = configuracao.getProperty("qr.code.diretorio", "./qrcodes/");
        String nomeArquivo = String.format("mesa_%d_%s.png", mesa.getNumero(), qrCode.getCodigo());

        String caminhoImagem = geradorQRCode.gerarQRCode(url, nomeArquivo);

        if (caminhoImagem == null)
        {
            return QRCodeResponseDTO.erro("Falha ao gerar arquivo de imagem");
        }

        QRCode qrCodeSalvo = qrCodeRepository.save(qrCode);

        return QRCodeResponseDTO.fromEntity(qrCodeSalvo);
    }

    @Transactional
    @CacheEvict(value = "qrCodes")
    public boolean validarQRCode(Long idMesa, String tokenSessao)
    {
        QRCode qrCode = qrCodeRepository.findByTokenSessao(tokenSessao).orElseThrow(() -> new BusinessException("Token não encontrado"));

        if (qrCode.getIdMesa().getId().equals(idMesa))
        {
            return false;
        }

        if (!qrCode.podeSerUtilizado())
        {
            return false;
        }

        qrCodeRepository.marcarComoUtilizado(qrCode.getId());

        Mesa mesa = qrCode.getIdMesa();
        mesa.ocuparMesa();
        mesaRepository.atualizarDisponibilidade(idMesa, false);

        return true;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "qrCodes")
    public QRCodeResponseDTO buscarPorId(Long id)
    {
        QRCode qrCode = qrCodeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("QR Code não encontrado"));

        return QRCodeResponseDTO.fromEntity(qrCode);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "qrCodes", key = "#id")
    public QRCode buscarEntidadePorId(Long id)
    {
        return qrCodeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("QR Code não encontrado"));
    }

    @Transactional(readOnly = true)
    public QRCodeResponseDTO buscarPorToken(String tokenSessao)
    {
        QRCode qrCode = qrCodeRepository.findByTokenSessao(tokenSessao).orElseThrow(() -> new BusinessException("Token não encontrado"));

        return QRCodeResponseDTO.fromEntity(qrCode);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "qrCodes")
    public List<QRCodeResponseDTO> listarAtivos()
    {
        return qrCodeRepository.findAllAtivos().stream()
                .map(QRCodeResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "qrCodes")
    public List<QRCodeResponseDTO> buscarAtivoPorMesa(Long idMesa)
    {
        return qrCodeRepository.findByMesaIdAndAtivoTrue(idMesa).stream()
                .map(QRCodeResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Scheduled
    @Transactional
    @CacheEvict(value = "qrCodes")
    public void limparExpirados()
    {
        qrCodeRepository.desativarExpirados();
    }

}
