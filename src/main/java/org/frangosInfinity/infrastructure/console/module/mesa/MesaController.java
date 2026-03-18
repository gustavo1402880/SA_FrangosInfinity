package org.frangosInfinity.infrastructure.console.module.mesa;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.frangosInfinity.application.module.mesa.request.MesaRequestDTO;
import org.frangosInfinity.application.module.mesa.request.QRCodeRequestDTO;
import org.frangosInfinity.application.module.mesa.response.MesaResponseDTO;
import org.frangosInfinity.application.module.mesa.response.QRCodeResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.module.mesa.IotConfig;
import org.frangosInfinity.core.service.module.mesa.IoTConfigService;
import org.frangosInfinity.core.service.module.mesa.MesaService;
import org.frangosInfinity.core.service.module.mesa.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mesas")
@Tag(name = "Mesas", description = "Endpoints para gerenciamento de mesas")
public class MesaController
{
    @Autowired
    private MesaService mesaService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private IoTConfigService ioTConfigService;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova mesa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mesa cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<MesaResponseDTO> processarCadastroMesa(@Valid @RequestBody MesaRequestDTO request)
    {
        MesaResponseDTO response = mesaService.criarmesa(request);

        if(!response.getSucesso())
        {
            return  ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas as mesas")
    public ResponseEntity<List<MesaResponseDTO>> processarListagensTodasMesas()
    {
        List<MesaResponseDTO> mesas = mesaService.listarTodas();

        if(mesas.isEmpty())
        {
            return ResponseEntity.badRequest().body(mesas);
        }

        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar mesas disponíveis")
    public ResponseEntity<List<MesaResponseDTO>> processarListagemMesasDisponiveis()
    {
        List<MesaResponseDTO> mesas = mesaService.listarDisponiveis();

        if (mesas.isEmpty())
        {
            return ResponseEntity.badRequest().body(mesas);
        }

        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mesa por ID")
    public ResponseEntity<MesaResponseDTO> processarBuscaMesaPorId(@PathVariable Long id)
    {
        MesaResponseDTO mesa = mesaService.buscarPorId(id);

        if (!mesa.getSucesso())
        {
            ResponseEntity.badRequest().body(mesa);
        }

        return ResponseEntity.ok(mesa);
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Buscar mesa pelo número")
    public ResponseEntity<MesaResponseDTO> processarBuscaMesaPorNumero(@PathVariable Integer numero)
    {
        if (numero == null || numero <= 0)
        {
            throw new BusinessException("Número inválido");
        }

        MesaResponseDTO mesa = mesaService.buscarPorNumero(numero);

        if (!mesa.getSucesso())
        {
            return ResponseEntity.badRequest().body(mesa);
        }

        return ResponseEntity.ok(mesa);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da mesa")
    public ResponseEntity<MesaResponseDTO> processarAtualizarStatusMesa(@PathVariable Long idMesa, @RequestParam String acao)
    {
        MesaResponseDTO response = mesaService.atualizarStatus(idMesa, acao);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover mesa")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "204", description = "Mesa removida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não é possível remover mesa ocupada"),
            @ApiResponse(responseCode = "404", description = "Mesa não encontrada")
    })
    public ResponseEntity<Void> processarRemoverMesa(@PathVariable Long idMesa)
    {
       mesaService.deletarMesa(idMesa);

       return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/qrcode")
    @Operation(summary = "Gerar QR Code para uma mesa")
    public ResponseEntity<QRCodeResponseDTO> processarGerarQRCode(@PathVariable Long id, @Valid @RequestBody QRCodeRequestDTO request)
    {
        request.setIdMesa(id);

        QRCodeResponseDTO response = qrCodeService.gerarQRCode(request);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/qrcode/validar")
    @Operation(summary = "Validar QR Code de uma mesa")
    public ResponseEntity<Boolean> processarValidarQRCode(@PathVariable Long idMesa, @RequestParam String tokenSessao)
    {
        Boolean valido = qrCodeService.validarQRCode(idMesa, tokenSessao);

        return ResponseEntity.ok(valido);
    }

    @PostMapping("/qrcode/{id}")
    @Operation(summary = "Buscar QRCode por ID")
    public ResponseEntity<QRCodeResponseDTO> processarBuscarQRCodePorId(@PathVariable Long id)
    {
        QRCodeResponseDTO response = qrCodeService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/qrcodes/ativos")
    @Operation(summary = "Listar QR Codes por ID")
    public ResponseEntity<List<QRCodeResponseDTO>> processarListarQRCodesAtivos()
    {
        List<QRCodeResponseDTO> responses = qrCodeService.listarAtivos();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/qrcode")
    @Operation(summary = "Listar QR Code Ativos da Mesa")
    public ResponseEntity<List<QRCodeResponseDTO>> processarListarQRCodesPorMesa(@PathVariable Long idMesa)
    {
        List<QRCodeResponseDTO> qrCodes = qrCodeService.buscarAtivoPorMesa(idMesa);

        return ResponseEntity.ok(qrCodes);
    }

    @PostMapping("/qrcode/limpar-expirados")
    @Operation(summary = "Limpar QR Codes expirados")
    public ResponseEntity<Void> processarLimparQRCodesExpirados()
    {
        qrCodeService.limparExpirados();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/iot")
    @Operation(summary = "Buscar Configuração IoT pelo ID da mesa")
    public ResponseEntity<IotConfig> processarBuscarIoTConfigPorMesa(@PathVariable Long idMesa)
    {
        IotConfig iotConfig = mesaService.getConfiguracaoIoT(idMesa);

        return ResponseEntity.ok(iotConfig);
    }

    @PostMapping("/iot/{idConfig}/comunicar")
    @Operation(summary = "Comunicar com dispositivo IoT")
    public ResponseEntity<String> processarComunicarComIoT(@PathVariable Long idConfig, @RequestParam String comando)
    {
        String resposta = ioTConfigService.comunicarComIoT(idConfig, comando);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/iot/{idConfig}/status")
    @Operation(summary = "Listar todos os dispositivos IoT")
    public ResponseEntity<Boolean> processarVerificarStatusIoT(Long idConfig)
    {
        Boolean online = ioTConfigService.isOnline(idConfig);

        return ResponseEntity.ok(online);
    }

    @GetMapping("/iot")
    @Operation(summary = "Listar todos dispositivos IoT")
    public ResponseEntity<List<IotConfig>> processarListarTodosIoT()
    {
        List<IotConfig> dispositivos = ioTConfigService.listarTodos();

        return ResponseEntity.ok(dispositivos);
    }

    @GetMapping("/iot/online")
    @Operation(summary = "Listar dipositivos IoT online")
    public ResponseEntity<List<IotConfig>> processarListarIoTOnline()
    {
        List<IotConfig> dispositivos = ioTConfigService.listarOnline();

        return ResponseEntity.ok(dispositivos);
    }

    @PatchMapping("/iot/{idConfig}/firmware")
    public ResponseEntity<Boolean> processarAtualizarFirmware(@PathVariable Long idConfig, @RequestParam String versao)
    {
        Boolean atualizado = ioTConfigService.atualizarFirmware(idConfig, versao);

        return ResponseEntity.ok(atualizado);
    }
}