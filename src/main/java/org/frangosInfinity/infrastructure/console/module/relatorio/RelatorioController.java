package org.frangosInfinity.infrastructure.console.module.relatorio;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.frangosInfinity.application.module.mesa.response.MesaResponseDTO;
import org.frangosInfinity.application.module.relatorio.request.RelatorioRequestDTO;
import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;
import org.frangosInfinity.core.service.module.relatorio.RelatorioVendasService;
import org.frangosInfinity.core.service.module.relatorio.RelatorioVendasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController
{
    @Autowired
    private RelatorioVendasService relatorioService;

    @PostMapping
    @Operation(summary = "Gerar novo relatório")
    public ResponseEntity<RelatorioResponseDTO> processarGerarRelatorio(@Valid @RequestBody RelatorioRequestDTO request)
    {
        RelatorioResponseDTO response = relatorioService.gerarRelatorio(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar relatório por ID")
    public ResponseEntity<RelatorioResponseDTO> processarBuscarPorId(@PathVariable Long id)
    {
        RelatorioResponseDTO response = relatorioService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ultimo")
    @Operation(summary = "Buscar úlitmo relatório")
    public ResponseEntity<RelatorioResponseDTO> processarBuscarUltimo()
    {
        RelatorioResponseDTO response = relatorioService.buscarUltimo();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os relatórios")
    public ResponseEntity<List<RelatorioResponseDTO>> processarListarTodos()
    {
        List<RelatorioResponseDTO> responses = relatorioService.listarTodos();

        return responses.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(responses);
    }

    @GetMapping("/data-geracao")
    @Operation(summary = "Listar relatórios por data de geração")
    public ResponseEntity<List<RelatorioResponseDTO>> processarBuscarPorDataGeracao(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataGeracao)
    {
        List<RelatorioResponseDTO> response = relatorioService.buscarPorDataGeracao(dataGeracao);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/pdf")
    @Operation(summary = "Exportar relatório para PDF")
    public ResponseEntity<byte[]> processarExportarPdfPorId(@PathVariable Long id)
    {
        byte[] pdf = relatorioService.gerarRelatorioPdfPorId(id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"relatorio_" + id + ".pdf\"")
                .body(pdf);
    }

    @GetMapping("/periodo/pdf")
    @Operation(summary = "Exportar relatórios do período para PDF")
    public ResponseEntity<byte[]> processarExportarPdfPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim)
    {
        byte[] pdf = relatorioService.gerarRelatorioPdfPorPeriodo(inicio, fim);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"relatorio_" + inicio.toLocalDate() + "_a_" + fim.toLocalDate() + ".pdf\"")
                .body(pdf);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir relatório")
    public ResponseEntity<Void> processarExcluirRelatorio(@PathVariable Long id)
    {
        relatorioService.excluirRelatorio(id);

        return ResponseEntity.noContent().build();
    }
}