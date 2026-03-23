package org.frangosInfinity.infrastructure.console.module.pagamento;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.frangosInfinity.application.module.pagamento.request.PIXRequestDTO;
import org.frangosInfinity.application.module.pagamento.request.PagamentoRequestDTO;
import org.frangosInfinity.application.module.pagamento.response.ComprovanteResponseDTO;
import org.frangosInfinity.application.module.pagamento.response.PIXResponseDTO;
import org.frangosInfinity.application.module.pagamento.response.PagamentoResponseDTO;
import org.frangosInfinity.core.enums.StatusPagamento;
import org.frangosInfinity.core.enums.TipoPagamento;
import org.frangosInfinity.core.service.module.pagamento.ComprovanteService;
import org.frangosInfinity.core.service.module.pagamento.PagamentoService;
import org.frangosInfinity.core.service.module.pagamento.TransacaoPIXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@Tag(name = "Pagamentos", description = "Gerenciamento de pagamentos e PIX")
public class PagamentoController
{
    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private TransacaoPIXService pixService;

    @Autowired
    private ComprovanteService comprovanteService;

    @PostMapping
    @Operation(summary = "Processar novo pagamento")
    public ResponseEntity<PagamentoResponseDTO> processarPagamento(@Valid @RequestBody PagamentoRequestDTO request)
    {
        PagamentoResponseDTO response = pagamentoService.processarPagamento(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Processar novo pagamento")
    public ResponseEntity<PagamentoResponseDTO> processarBuscarPagamentoPorId(@PathVariable Long id)
    {
        PagamentoResponseDTO response = pagamentoService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/subpedido/{subPedidoId}")
    @Operation(summary = "Buscar pagamento por ID do subpedido")
    public ResponseEntity<PagamentoResponseDTO> processarBuscarPagamentoPorSubPedido(@PathVariable Long subPedidoId)
    {
        PagamentoResponseDTO response = pagamentoService.buscarPorsubPedidoId(subPedidoId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Buscar pagamento por ID do subpedido")
    public ResponseEntity<List<PagamentoResponseDTO>> processarListarTodosPagamentos()
    {
        List<PagamentoResponseDTO> response = pagamentoService.listarTodos();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar pagamentos por status")
    public ResponseEntity<List<PagamentoResponseDTO>> processarListarPorStatus(@PathVariable StatusPagamento status)
    {
        List<PagamentoResponseDTO> response = pagamentoService.listarPorStatus(status);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar pagamentos por período")
    public ResponseEntity<List<PagamentoResponseDTO>> processarListarPorPeriodo(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime inicio, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime fim)
    {
        List<PagamentoResponseDTO> response = pagamentoService.listarPorPeriodo(inicio, fim);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar pagamento")
    public ResponseEntity<PagamentoResponseDTO> processarConfirmarPagamento(Long pagamentoId)
    {
        PagamentoResponseDTO response = pagamentoService.confirmarPagamento(pagamentoId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pagamento")
    public ResponseEntity<PagamentoResponseDTO> processarCancelarPagamento(Long pagamentoId)
    {
        PagamentoResponseDTO response = pagamentoService.cancelarPagamento(pagamentoId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reembolsar")
    @Operation(summary = "Reembolsar pagamento")
    public ResponseEntity<PagamentoResponseDTO> processarReembolsarPagamento(Long pagamentoId)
    {
        PagamentoResponseDTO response = pagamentoService.reembolsarPagamento(pagamentoId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pagamento")
    public ResponseEntity<Void> deletarPagamento(@PathVariable Long id)
    {
        pagamentoService.deletarPagamento(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pix")
    @Operation(summary = "Gerar PIX para pagamento")
    public ResponseEntity<PIXResponseDTO> processarGerarPix(@Valid @RequestBody PIXRequestDTO request)
    {
        PIXResponseDTO response = pixService.gerarPix(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pix/{id}")
    @Operation(summary = "Buscar PIX por ID")
    public ResponseEntity<PIXResponseDTO> processarBuscarPixPorId(@PathVariable Long id)
    {
        PIXResponseDTO response = pixService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pix/pagamento/{pagamentoId}")
    @Operation(summary = "Buscar PIX por ID do pagamento")
    public ResponseEntity<PIXResponseDTO> processarBuscarPixPorPagamento(@PathVariable Long pagamentoId)
    {
        PIXResponseDTO response = pixService.buscarPorPagamentoId(pagamentoId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/pix/{pagamentoId}/renovar")
    @Operation(summary = "Renovar PIX")
    public ResponseEntity<PIXResponseDTO> processarRenovarPix(@PathVariable Long pagamentoId, @RequestParam(required = false) Integer tempoSegundos)
    {
        PIXResponseDTO response = pixService.renovarPix(pagamentoId, tempoSegundos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pix/{pagamentoId}/expirado")
    @Operation(summary = "Verificar se PIX está expirado")
    public ResponseEntity<Boolean> processarVerifiacarExpiracaoPix(@PathVariable Long pagamentoId)
    {
        Boolean expirado = pixService.verificarExpiracao(pagamentoId);

        return ResponseEntity.ok(expirado);
    }

    @GetMapping("/comprovantes/{id}")
    @Operation(summary = "Buscar comprovante por ID")
    public ResponseEntity<ComprovanteResponseDTO> processarBuscarComprovantePorId(Long id)
    {
        ComprovanteResponseDTO response = comprovanteService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/comprovantes/pagamento/{pagamentoId}")
    @Operation(summary = "Buscar comprovante por ID do pagamento")
    public ResponseEntity<ComprovanteResponseDTO> processarBuscarComprovantePorPagamento(@PathVariable Long pagamentoId)
    {
        ComprovanteResponseDTO response = comprovanteService.buscarPorPagamentoId(pagamentoId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/comprovantes/numero/{numero}")
    @Operation(summary = "Buscar comprovante por número")
    public ResponseEntity<ComprovanteResponseDTO> processarBuscarComprovantePorNumero(@PathVariable String numero)
    {
        ComprovanteResponseDTO response = comprovanteService.buscarPorNumero(numero);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/comprovantes")
    @Operation(summary = "Listar todos os comprovantes")
    public ResponseEntity<List<ComprovanteResponseDTO>> processasrListarTodosComprovantes()
    {
        List<ComprovanteResponseDTO> response = comprovanteService.listarTodos();

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/comprovantes/forma/{formaPagamento}")
    @Operation(summary = "Listar comprovantes por forma de pagamento")
    public ResponseEntity<List<ComprovanteResponseDTO>> processarListarComprovantesPorFormaPagamento(@PathVariable TipoPagamento formaPagamento)
    {
        List<ComprovanteResponseDTO> response = comprovanteService.listarPorFormaPagamento(formaPagamento);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/comprovantes/{id}/texto")
    @Operation(summary = "Gerar texto do comprovante para impressão")
    public ResponseEntity<String> processarImprimirComprovante(@PathVariable Long id)
    {
        ComprovanteResponseDTO response = comprovanteService.buscarPorId(id);

        return ResponseEntity.ok(response.getTexto());
    }

    @GetMapping("/estatisticas/valor-periodo")
    @Operation(summary = "Somar valor total de comprovantes no período")
    public ResponseEntity<Double> somarValorNoPeriodo(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime inicio, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime fim )
    {
        Double total = comprovanteService.somarValorTotalNoPeriodo(inicio, fim);

        return ResponseEntity.ok(total);
    }
}
