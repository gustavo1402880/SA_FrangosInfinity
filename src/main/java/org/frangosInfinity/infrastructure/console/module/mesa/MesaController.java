package org.frangosInfinity.infrastructure.console.module.mesa;

import org.frangosInfinity.application.module.mesa.request.MesaRequestDTO;
import org.frangosInfinity.application.module.mesa.request.QRCodeRequestDTO;
import org.frangosInfinity.application.module.mesa.response.MesaResponseDTO;
import org.frangosInfinity.application.module.mesa.response.QRCodeResponseDTO;
import org.frangosInfinity.core.entity.module.mesa.IotConfig;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.mesa.QRCode;
import org.frangosInfinity.core.service.module.mesa.IoTConfigService;
import org.frangosInfinity.core.service.module.mesa.MesaService;
import org.frangosInfinity.core.service.module.mesa.QRCodeService;

import java.util.ArrayList;
import java.util.List;

public class MesaController
{
    private final MesaService mesaService;
    private final QRCodeService qrCodeService;
    private final IoTConfigService ioTConfigService;

    public MesaController()
    {
        this.mesaService = new MesaService();
        this.qrCodeService = new QRCodeService();
        this.ioTConfigService = new IoTConfigService();
    }

    public MesaResponseDTO processarCadastroMesa(MesaRequestDTO request)
    {
        if(request.getNumero() <= 0)
        {
            throw new RuntimeException("Número da mesa deve ser positivo");
        }

        if(request.getCapacidade() <= 0 || request.getCapacidade() > 20)
        {
            throw new RuntimeException("Capacidade deve ser entre 1 e 20 pessoas");
        }

        MesaResponseDTO response = mesaService.criarmesa(request);

        if(!response.getSucesso())
        {
            throw new RuntimeException("Erro ao tentar criar mesa");
        }

        return response;
    }

    public List<MesaResponseDTO> processarListagensTodasMesas()
    {
        List<Mesa> mesas = mesaService.listarTodas();
        List<MesaResponseDTO> mesasRespose = new ArrayList<>();

        if(mesas.isEmpty())
        {
            throw new RuntimeException("Nenhuma mesa cadastrada");
        }

        for (Mesa mesa : mesas)
        {
            MesaResponseDTO response = MesaResponseDTO.fromEntity(mesa);
            mesasRespose.add(response);
        }

        return mesasRespose;
    }

    public List<MesaResponseDTO> processarListagemMesasDisponiveis() {
        List<Mesa> mesas = mesaService.listarDisponiveis();
        List<MesaResponseDTO> mesasResponse = new ArrayList<>();

        if (mesas.isEmpty()) {
            throw new RuntimeException("Nenhuma mesa disponível no momento");
        }

        for (Mesa mesa : mesas) {
            MesaResponseDTO response = MesaResponseDTO.fromEntity(mesa);
            mesasResponse.add(response);
        }

        return mesasResponse;
    }

    public MesaResponseDTO processarBuscaMesaPorId(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("ID inválido");
        }

        Mesa mesa = mesaService.buscarPorId(id);

        if (mesa == null)
        {
            throw new RuntimeException("Mesa com ID " + id + " não encontrada");
        }

        return MesaResponseDTO.fromEntity(mesa);
    }

    public MesaResponseDTO processarBuscaMesaPorNumero(Integer numero)
    {
        if (numero == null || numero <= 0)
        {
            throw new IllegalArgumentException("Número inválido");
        }

        Mesa mesa = mesaService.buscarPorNumero(numero);

        if (mesa == null)
        {
            throw new RuntimeException("Mesa número " + numero + " não encontrada");
        }

        return MesaResponseDTO.fromEntity(mesa);
    }

    public MesaResponseDTO processarAtualizarStatusMesa(Long idMesa, String acao)
    {
        if (idMesa == null || idMesa <= 0)
        {
            throw new IllegalArgumentException("ID da mesa inválido");
        }

        if (acao == null || acao.trim().isEmpty())
        {
            throw new IllegalArgumentException("Ação não pode ser vazia");
        }

        String acaoUpper = acao.toUpperCase();
        if (!acaoUpper.equals("OCUPAR") && !acaoUpper.equals("LIBERAR") && !acaoUpper.equals("ATIVAR") && !acaoUpper.equals("DESATIVAR"))
        {
            throw new IllegalArgumentException("Ação inválida. Use: OCUPAR, LIBERAR, ATIVAR ou DESATIVAR");
        }

        MesaResponseDTO response = mesaService.atualizarStatus(idMesa, acaoUpper);

        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao atualizar status: " + response.getMensagem());
        }

        return response;
    }

    public boolean processarRemoverMesa(Long idMesa)
    {
        if (idMesa == null || idMesa <= 0)
        {
            throw new IllegalArgumentException("ID da mesa inválido");
        }

        Mesa mesa = mesaService.buscarPorId(idMesa);
        if (mesa == null)
        {
            throw new RuntimeException("Mesa não encontrada");
        }

        if (!mesa.isDisponivel())
        {
            throw new RuntimeException("Não é possível remover uma mesa ocupada");
        }

        boolean removido = mesaService.deletarMesa(idMesa);

        if (!removido)
        {
            throw new RuntimeException("Erro ao remover mesa");
        }

        return true;
    }

    public QRCodeResponseDTO processarGerarQRCode(QRCodeRequestDTO request)
    {
        if (request == null)
        {
            throw new IllegalArgumentException("Request não pode ser nulo");
        }

        if (request.getIdMesa() <= 0)
        {
            throw new IllegalArgumentException("ID da mesa inválido");
        }

        Mesa mesa = mesaService.buscarPorId(request.getIdMesa());
        if (mesa == null)
        {
            throw new RuntimeException("Mesa não encontrada");
        }

        if (!mesa.isAtiva())
        {
            throw new RuntimeException("Mesa está desativada");
        }

        QRCodeResponseDTO response = qrCodeService.gerarQRCode(request);

        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao gerar QR Code: " + response.getMensagem());
        }

        return response;
    }

    public boolean processarValidarQRCode(Long idMesa, String tokenSessao)
    {
        if (idMesa == null || idMesa <= 0)
        {
            throw new IllegalArgumentException("ID da mesa inválido");
        }

        if (tokenSessao == null || tokenSessao.trim().isEmpty())
        {
            throw new IllegalArgumentException("Token de sessão não pode ser vazio");
        }

        Mesa mesa = mesaService.buscarPorId(idMesa);
        if (mesa == null)
        {
            throw new RuntimeException("Mesa não encontrada");
        }

        boolean valido = qrCodeService.validarQRCode(idMesa, tokenSessao);

        return valido;
    }

    public QRCodeResponseDTO processarBuscarQRCodePorId(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("ID inválido");
        }

        QRCode qrCode = qrCodeService.buscarPorId(id);

        if (qrCode == null)
        {
            throw new RuntimeException("QR Code não encontrado");
        }

        QRCodeResponseDTO response = new QRCodeResponseDTO();
        response.setId(qrCode.getId());
        response.setCodigo(qrCode.getCodigo());
        response.setUrlAutenticacao(qrCode.getUrlAutenticacao());
        response.setDataCriacao(qrCode.getDataCriacao());
        response.setDataExpiracao(qrCode.getDataExpiracao());
        response.setIdMesa(qrCode.getIdMesa());
        response.setSucesso(true);

        return response;
    }

    public List<QRCodeResponseDTO> processarListarQRCodesAtivos()
    {
        List<QRCode> qrCodes = qrCodeService.listarAtivos();
        List<QRCodeResponseDTO> responseList = new ArrayList<>();

        if (qrCodes.isEmpty())
        {
            throw new RuntimeException("Nenhum QR Code ativo encontrado");
        }

        for (QRCode qrCode : qrCodes)
        {
            QRCodeResponseDTO response = new QRCodeResponseDTO();
            response.setId(qrCode.getId());
            response.setCodigo(qrCode.getCodigo());
            response.setUrlAutenticacao(qrCode.getUrlAutenticacao());
            response.setDataCriacao(qrCode.getDataCriacao());
            response.setDataExpiracao(qrCode.getDataExpiracao());
            response.setIdMesa(qrCode.getIdMesa());
            response.setSucesso(true);
            responseList.add(response);
        }

        return responseList;
    }

    public List<QRCodeResponseDTO> processarListarQRCodesPorMesa(Long idMesa)
    {
        if (idMesa == null || idMesa <= 0)
        {
            throw new IllegalArgumentException("ID da mesa inválido");
        }

        Mesa mesa = mesaService.buscarPorId(idMesa);
        if (mesa == null)
        {
            throw new RuntimeException("Mesa não encontrada");
        }

        List<QRCode> qrCodes = qrCodeService.listarPorMesa(idMesa);
        List<QRCodeResponseDTO> responseList = new ArrayList<>();

        if (qrCodes.isEmpty())
        {
            throw new RuntimeException("Nenhum QR Code encontrado para esta mesa");
        }

        for (QRCode qrCode : qrCodes)
        {
            QRCodeResponseDTO response = new QRCodeResponseDTO();
            response.setId(qrCode.getId());
            response.setCodigo(qrCode.getCodigo());
            response.setUrlAutenticacao(qrCode.getUrlAutenticacao());
            response.setDataCriacao(qrCode.getDataCriacao());
            response.setDataExpiracao(qrCode.getDataExpiracao());
            response.setIdMesa(qrCode.getIdMesa());
            response.setSucesso(true);
            responseList.add(response);
        }

        return responseList;
    }

    public void processarLimparQRCodesExpirados()
    {
        qrCodeService.limparExpirados();
    }

    public IotConfig processarBuscarIoTConfigPorMesa(Long idMesa)
    {
        if (idMesa == null || idMesa <= 0)
        {
            throw new IllegalArgumentException("ID da mesa inválido");
        }

        Mesa mesa = mesaService.buscarPorId(idMesa);
        if (mesa == null)
        {
            throw new RuntimeException("Mesa não encontrada");
        }

        IotConfig iotConfig = mesaService.getConfiguracaoIoT(idMesa);

        if (iotConfig == null)
        {
            throw new RuntimeException("Mesa não possui configuração IoT");
        }

        return iotConfig;
    }

    public String processarComunicarComIoT(Long idConfig, String comando)
    {
        if (idConfig == null || idConfig <= 0)
        {
            throw new IllegalArgumentException("ID da configuração IoT inválido");
        }

        if (comando == null || comando.trim().isEmpty())
        {
            throw new IllegalArgumentException("Comando não pode ser vazio");
        }

        String resposta = ioTConfigService.comunicarComIoT(idConfig, comando);

        if (resposta.startsWith("ERRO"))
        {
            throw new RuntimeException(resposta);
        }

        return resposta;
    }

    public boolean processarVerificarStatusIoT(Long idConfig)
    {
        if (idConfig == null || idConfig <= 0)
        {
            throw new IllegalArgumentException("ID da configuração IoT inválido");
        }

        return ioTConfigService.isOnline(idConfig);
    }

    public List<IotConfig> processarListarTodosIoT()
    {
        List<IotConfig> dispositivos = ioTConfigService.listarTodos();

        if (dispositivos.isEmpty())
        {
            throw new RuntimeException("Nenhum dispositivo IoT configurado");
        }

        return dispositivos;
    }

    public List<IotConfig> processarListarIoTOnline()
    {
        List<IotConfig> dispositivos = ioTConfigService.listarOnline();

        if (dispositivos.isEmpty())
        {
            throw new RuntimeException("Nenhum dispositivo IoT online");
        }

        return dispositivos;
    }

    public boolean processarAtualizarFirmware(Long idConfig, String versao)
    {
        if (idConfig == null || idConfig <= 0)
        {
            throw new IllegalArgumentException("ID da configuração IoT inválido");
        }

        if (versao == null || versao.trim().isEmpty())
        {
            throw new IllegalArgumentException("Versão do firmware não pode ser vazia");
        }

        return ioTConfigService.atualizarFirmware(idConfig, versao);
    }
}
