package org.frangosInfinity.core.service.module.mesa;

import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.mesa.IotConfig;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.infrastructure.persistence.module.mesa.IoTConfigRepository;
import org.frangosInfinity.infrastructure.persistence.module.mesa.MesaRepository;
import org.frangosInfinity.infrastructure.util.Configuracao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IoTConfigService {
    @Autowired
    private IoTConfigRepository ioTConfigRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private Configuracao config;

    @Transactional
    public IotConfig criarConfiguracaoPadrao(Mesa mesa) {
        String ipBase = config.getProperty("iot.ip.base", "192.168.1");
        int portaBase = config.getIntProperty("iot.porta.base", 9000);

        int numeroMesa = (mesa.getNumero() % 100) + 10;
        String ip = ipBase + "." + numeroMesa;
        int porta = portaBase + (mesa.getNumero() % 100);

        IotConfig iotConfig = new IotConfig(mesa, ip, porta);

        IotConfig iotSalvo = ioTConfigRepository.save(iotConfig);

        return iotSalvo;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "iotConfigs", key = "#id")
    public IotConfig buscarPorId(Long id) {
        return ioTConfigRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Configuração IoT não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "iotConfigs", key = "#id")
    public IotConfig buscarPorMesa(Long idMesa) {
        return ioTConfigRepository.findByMesaId(idMesa).orElseThrow(() -> new ResourceNotFoundException("Configuração IoT não encontrada para a mesa: " + idMesa));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "iotConfigs")
    public List<IotConfig> listarTodos() {
        return ioTConfigRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "iotConfigs")
    public List<IotConfig> listarOnline() {
        return ioTConfigRepository.findByOnlineTrue();
    }

    @Transactional
    @Cacheable(value = "iotConfigs")
    public String comunicarComIoT(Long idConfig, String comando) {

        IotConfig iotConfig = ioTConfigRepository.findById(idConfig).orElseThrow(() -> new ResourceNotFoundException("Configuração IoT não encontrada: " + idConfig));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String resposta = iotConfig.enviarComando(comando);

        if (comando.equals("STATUS")) {
            boolean online = resposta.startsWith("ONLINE");
            iotConfig.setOnline(online);
            ioTConfigRepository.save(iotConfig);
        }

        return resposta;
    }

    @Transactional
    @Cacheable(value = "iotConfigs")
    public boolean atualizarFirmware(Long idConfig, String versao) {
        IotConfig iotConfig = ioTConfigRepository.findById(idConfig).orElseThrow(() -> new ResourceNotFoundException("Configuração IoT não encontrada: " + idConfig));

        iotConfig.setVersaoFirmware(versao);
        ioTConfigRepository.save(iotConfig);

        return true;
    }

    @Transactional(readOnly = true)
    public boolean isOnline(Long idConfig)
    {
        return ioTConfigRepository.findById(idConfig).map(IotConfig::isOnline).orElse(false);
    }

    @Transactional
    @CacheEvict(value = "iotConfigs")
    public void deletarCOnfiguracao(Long id)
    {
        if (!ioTConfigRepository.existsByMesaId(id))
        {
            throw new ResourceNotFoundException("Configuração IoT não encontrada");
        }

        ioTConfigRepository.deleteById(id);
    }
}
