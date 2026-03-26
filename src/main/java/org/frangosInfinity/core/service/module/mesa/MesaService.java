package org.frangosInfinity.core.service.module.mesa;

import lombok.extern.slf4j.Slf4j;
import org.frangosInfinity.application.module.mesa.request.MesaRequestDTO;
import org.frangosInfinity.application.module.mesa.response.MesaResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.mesa.IotConfig;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.infrastructure.persistence.module.mesa.MesaRepository;
import org.frangosInfinity.infrastructure.util.Formatador;
import org.frangosInfinity.infrastructure.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MesaService {
    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private Formatador formatador;

    @Autowired
    private IoTConfigService ioTConfigService;

    @Transactional
    @CacheEvict(value = "mesas")
    public MesaResponseDTO criarmesa(MesaRequestDTO request) {
        if (!validator.validarNumeroMesa(request.getNumero())) {
            return criarRespostaErro("Número da mesa deve ser positivo (1-999)");
        }

        if (!validator.validarCapacidade(request.getCapacidade())) {
            return criarRespostaErro("Capacidade deve ser entre 1 e 20 pessoas");
        }

        if (request.getLocalizacao() == null || request.getLocalizacao().trim().isEmpty()) {
            return criarRespostaErro("Localização é obrigatória");
        }

        Mesa mesa = new Mesa(request.getNumero(), request.getCapacidade(), request.getLocalizacao());

        Mesa mesaSalva = mesaRepository.save(mesa);

        IotConfig iotConfig = ioTConfigService.criarConfiguracaoPadrao(mesaSalva);
        mesaSalva.setIotConfig(iotConfig);
        Mesa mesaComIot = mesaRepository.save(mesaSalva);

        return MesaResponseDTO.fromEntity(mesaComIot);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "mesas", key = "#id")
    public MesaResponseDTO buscarPorId(Long id) {
        Mesa mesa = mesaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com ID: " + id));

        return MesaResponseDTO.fromEntity(mesa);
    }

    @Transactional(readOnly = true)
    public Mesa buscarEntityPorId(Long id) {
        return mesaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public MesaResponseDTO buscarPorNumero(int numero)
    {
        Mesa mesa = mesaRepository.findByNumero(numero).orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com número: " + numero));

        return MesaResponseDTO.fromEntity(mesa);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "mesas")
    public List<MesaResponseDTO> listarTodas()
    {
        return mesaRepository.findAll().stream()
                .map(MesaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "mesas")
    public List<MesaResponseDTO> listarDisponiveis()
    {
        return mesaRepository.findByDisponivelTrueAndAtivaTrueOrderByNumero().stream()
                .map(MesaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "mesas")
    public MesaResponseDTO atualizarStatus(Long idMesa, String acao)
    {
        Mesa mesa = mesaRepository.findById(idMesa).orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com ID: " + idMesa));

        String acaoUpper = acao.toUpperCase();

        switch (acaoUpper)
        {
            case "OCUPAR":
                if (!mesa.isDisponivel())
                {
                    return criarRespostaErro("Mesa já está ocupada");
                }
                mesa.ocuparMesa();
                System.out.println("Mesa " + mesa.getNumero() + " ocupada");
                break;

            case "LIBERAR":
                if (mesa.isDisponivel())
                {
                    return criarRespostaErro("Mesa já está disponível");
                }
                mesa.liberarMesa();
                System.out.println("Mesa " + mesa.getNumero() + " liberada");
                break;

            case "ATIVAR":
                mesa.setAtiva(true);
                System.out.println("Mesa " + mesa.getNumero() + " ativada");
                break;

            case "DESATIVAR":
                mesa.setAtiva(false);
                System.out.println("Mesa " + mesa.getNumero() + " desativada");
                break;

            default:
                return criarRespostaErro("Ação inválida: " + acao + ". Use: OCUPAR, LIBERAR, ATIVAR, DESATIVAR");
        }

        mesaRepository.save(mesa);

       return MesaResponseDTO.fromEntity(mesa);
    }

    @Transactional(readOnly = true)
    public IotConfig getConfiguracaoIoT(Long idMesa)
    {
        Mesa mesa = mesaRepository.findById(idMesa).orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada"));

        if (mesa.getIotConfig() == null)
        {
            throw new BusinessException("Mesa não possui configuração IoT");
        }

        return mesa.getIotConfig();
    }

    @Transactional
    @CacheEvict(value = "mesas")
    public void deletarMesa(Long idMesa)
    {
        Mesa mesa = mesaRepository.findById(idMesa).orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada"));

        if (!mesa.isDisponivel())
        {
            throw new BusinessException("Não foi possível deletar uma mesa ocupada");
        }

        mesaRepository.delete(mesa);
    }

    private String acaoLower(String acao)
    {
        switch (acao)
        {
            case "OCUPAR": return "ocupada";
            case "LIBERAR": return "liberada";
            case "ATIVAR": return "ativada";
            case "DESATIVAR": return "desativada";
            default: return acao.toLowerCase();
        }
    }

    private MesaResponseDTO criarRespostaErro(String mensagem)
    {
        MesaResponseDTO dto = new MesaResponseDTO();
        dto.setSucesso(false);
        dto.setMensagem(mensagem);
        return dto;
    }
}
