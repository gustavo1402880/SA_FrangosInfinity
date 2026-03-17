package org.frangosInfinity.infrastructure.console.module.relatorio;

import org.frangosInfinity.application.module.mesa.response.MesaResponseDTO;
import org.frangosInfinity.application.module.relatorio.request.RelatorioRequestDTO;
import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;
import org.frangosInfinity.core.service.module.relatorio.RelatorioVendasService;
import org.frangosInfinity.core.service.module.relatorio.RelatorioVendasService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioController {

    private final RelatorioVendasService relatorioService;

    public RelatorioController() {
        this.relatorioService = new RelatorioVendasService();
    }


    public RelatorioResponseDTO processarGerarRelatorio(RelatorioRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Dados do relatório não podem ser nulos");
        }

        try {
            RelatorioResponseDTO responseDTO = relatorioService.gerarRelatorio(request);

            if (!responseDTO.isSucesso()) {
                throw new RuntimeException(responseDTO.getMensagem());
            }

            return responseDTO;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar geração de relatório: " + e.getMessage());
        }
    }

    public RelatorioResponseDTO processarBuscarPorId(Long id) {
        RelatorioResponseDTO response = relatorioService.buscarPorId(id);

        if (!response.isSucesso()) {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public List<RelatorioResponseDTO> processarListarTodos()
    {
        List<RelatorioVendas> relatorios = relatorioService.listarTodos();
        List<RelatorioResponseDTO> relatoriosDTO = new ArrayList<>();

        if (relatoriosDTO.isEmpty()) {
            throw new RuntimeException("Nenhum relatório encontrado");
        }

        for (RelatorioVendas relatorioVendas : relatorios)
        {
            RelatorioResponseDTO response = RelatorioResponseDTO.fromEntity(relatorioVendas);
            relatoriosDTO.add(response);
        }

        return relatoriosDTO;
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

        if (!response.isSucesso()) {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }
}