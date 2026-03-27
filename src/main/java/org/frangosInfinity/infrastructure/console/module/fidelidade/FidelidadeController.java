package org.frangosInfinity.infrastructure.console.module.fidelidade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.frangosInfinity.application.module.fidelidade.request.ResgateRequestDTO;
import org.frangosInfinity.application.module.fidelidade.response.PontosResponseDTO;
import org.frangosInfinity.application.module.fidelidade.response.RegrasResponseDTO;
import org.frangosInfinity.core.entity.module.fidelidade.RegrasFidelidade;
import org.frangosInfinity.core.service.module.fidelidade.PontosFidelidadeService;
import org.frangosInfinity.core.service.module.fidelidade.RegrasFidelidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "Fidelidade", description = "Endpoints para gerenciamento do programa de fidelidades")
public class FidelidadeController
{
    @Autowired
    private PontosFidelidadeService pontosFidelidadeService;

    @Autowired
    private RegrasFidelidadeService regrasFidelidadeService;

    @PostMapping("/contas/cliente/{clienteId}")
    @Operation(summary = "Criar conta de fidelidade para um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Cliente já possui conta")
    })
    public ResponseEntity<PontosResponseDTO> processarCriarConta(@PathVariable Long clienteId)
    {
        PontosResponseDTO response = pontosFidelidadeService.criarConta(clienteId);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/contas/cliente/{clienteId}")
    @Operation(summary = "Buscar pontos por ID do cliente")
    public ResponseEntity<PontosResponseDTO> processarBuscarPontosPorCliente(@PathVariable Long clienteId)
    {
        PontosResponseDTO response = pontosFidelidadeService.buscarPorCliente(clienteId);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/pontos/acumular")
    @Operation(summary = "Acumular pontos baseado no valor gasto")
    public ResponseEntity<PontosResponseDTO> processarAcumularPontos(@RequestParam Long clienteId, @RequestParam Double valorGasto)
    {
        PontosResponseDTO response = pontosFidelidadeService.acumularPontos(clienteId, valorGasto);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/pontos/resgatar")
    @Operation(summary = "Resgatar pontos para obter desconto")
    public ResponseEntity<PontosResponseDTO> processarResgatarPontos(@Valid @RequestBody ResgateRequestDTO request)
    {
        PontosResponseDTO response = pontosFidelidadeService.resgatarPontos(
                request.getClienteId(),
                request.getPontos(),
                request.getValorCompra()
        );

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/regras/ativas")
    @Operation(summary = "Buscar regras ativas do programa")
    public ResponseEntity<RegrasResponseDTO> processarVerRegras()
    {
        RegrasFidelidade regras = regrasFidelidadeService.buscarRegrasAtivas();

        return ResponseEntity.ok(RegrasResponseDTO.fromEntity(regras));
    }

    @GetMapping("/contas")
    @Operation(summary = "Listar todas as contas de fidelidade")
    public ResponseEntity<List<PontosResponseDTO>> processarListarTodosClientes()
    {
        List<PontosResponseDTO> lista = pontosFidelidadeService.listarTodos();

        if (lista.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/contas/{id}")
    @Operation(summary = "Remover conta de fidelidade")
    public ResponseEntity<Void> processarRemoverConta(@PathVariable Long id)
    {
        Boolean removido = pontosFidelidadeService.deletarConta(id);

        if (removido)
        {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/admin/regras")
    @Operation(summary = "Criar novas regras de fidelidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Regras criadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<RegrasResponseDTO> processarCriarRegras(@Valid @RequestBody RegrasFidelidade regras)
    {
        RegrasFidelidade criadas = regrasFidelidadeService.criarRegras(regras);

        if (criadas == null)
        {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(RegrasResponseDTO.fromEntity(regras));
    }

    @PutMapping("/admin/regras/{id}")
    @Operation(summary = "Atualizar regras existentes")
    public ResponseEntity<RegrasResponseDTO> processarAtualizarRegras(@PathVariable Long id, @Valid @RequestBody RegrasFidelidade regrasAtualizadas)
    {
        RegrasFidelidade regras = regrasFidelidadeService.buscarPorId(id);

        return ResponseEntity.ok(RegrasResponseDTO.fromEntity(regras));
    }

    @PatchMapping("/admin/regras/{id}/ativar")
    @Operation(summary = "Ativar regras")
    public ResponseEntity<RegrasResponseDTO> processarAtivarRegras(@PathVariable Long id)
    {
        RegrasFidelidade ativadas = regrasFidelidadeService.setAtivo(id, true);

        return ResponseEntity.ok(RegrasResponseDTO.fromEntity(ativadas));
    }

    @PatchMapping("/admin/regras/{id}/desativar")
    @Operation(summary = "Desativar regras")
    public ResponseEntity<RegrasResponseDTO> processarDesativarRegras(Long id)
    {
        RegrasFidelidade ativadas = regrasFidelidadeService.setAtivo(id, false);

        return ResponseEntity.ok(RegrasResponseDTO.fromEntity(ativadas));
    }

    @DeleteMapping("/admin/regras/{id}")
    @Operation(summary = "Deletar regras")
    public ResponseEntity<Void> processarDeletarRegras(@PathVariable Long id)
    {
        regrasFidelidadeService.deletar(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/regras")
    @Operation(summary = "Listar todas as regras")
    public ResponseEntity<List<RegrasResponseDTO>> processarListarTodasRegras()
    {
       List<RegrasFidelidade> regras = regrasFidelidadeService.listarTodas();

        if (regras.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }

        List<RegrasResponseDTO> response = regras.stream()
                .map(RegrasResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/processar-expiracao")
    @Operation(summary = "Processar expiração de pontos")
    public ResponseEntity<Integer> processarExecutarExpiracaoPontos()
    {
        int totalExpirados = pontosFidelidadeService.processarExpiracaoPontos();
        return ResponseEntity.ok(totalExpirados);
    }
}
