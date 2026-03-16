package org.frangosInfinity.infrastructure.console.module.relatorio;

import org.frangosInfinity.application.module.relatorio.request.RelatorioRequestDTO;
import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;
import org.frangosInfinity.core.service.module.relatorio.RelatorioService;
import org.frangosInfinity.core.service.module.relatorio.RelatorioVendasService;

import java.time.LocalDateTime;
import java.util.List;

public class RelatorioController {

    private final RelatorioVendasService relatorioService;

    public RelatorioController() {
        this.relatorioService = new RelatorioService();
    }


    public RelatorioResponseDTO processarGerarRelatorio(RelatorioRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Dados do relatório não podem ser nulos");
        }

        try {
            RelatorioResponseDTO responseDTO = relatorioService.gerarRelatorio(request);

            if (!responseDTO.getSucesso()) {
                throw new RuntimeException(responseDTO.getMensagem());
            }

            return responseDTO;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar geração de relatório: " + e.getMessage());
        }
    }

    public RelatorioResponseDTO processarBuscarPorId(Long id) {
        RelatorioResponseDTO response = relatorioService.buscarPorId(id);

        if (!response.getSucesso()) {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public List<RelatorioResponseDTO> processarListarTodos() {
        List<RelatorioResponseDTO> relatorios = relatorioService.listarTodos();

        if (relatorios.isEmpty()) {
            throw new RuntimeException("Nenhum relatório encontrado");
        }

        return relatorios;
    }


    public List<RelatorioResponseDTO> processarListarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas de início e fim devem ser informadas");
        }

        List<RelatorioResponseDTO> relatorios = relatorioService.listarPorPeriodo(inicio, fim);

        if (relatorios.isEmpty()) {
            throw new RuntimeException("Nenhum relatório encontrado para o período informado");
        }

        return relatorios;
    }

    public RelatorioResponseDTO processarExcluirRelatorio(Long id) {
        RelatorioResponseDTO response = relatorioService.excluirRelatorio(id);

        if (!response.getSucesso()) {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }
}