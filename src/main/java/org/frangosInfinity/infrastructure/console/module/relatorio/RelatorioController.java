package org.frangosInfinity.infrastructure.console.module.relatorio;

import org.frangosInfinity.application.module.mesa.response.MesaResponseDTO;
import org.frangosInfinity.application.module.relatorio.request.RelatorioRequestDTO;
import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;
import org.frangosInfinity.core.service.module.relatorio.RelatorioVendasService;
import org.frangosInfinity.core.service.module.relatorio.RelatorioVendasService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioController {

    private final RelatorioVendasService relatorioService;

    public RelatorioController() {
        this.relatorioService = new RelatorioVendasService();
    }


    public RelatorioResponseDTO processarGerarRelatorio(RelatorioRequestDTO request) {
        if (request == null)
        {
            throw new IllegalArgumentException("Dados do relatório não podem ser nulos");
        }

        try
        {
            RelatorioResponseDTO responseDTO = relatorioService.gerarRelatorio(request);

            if (!responseDTO.isSucesso())
            {
                throw new RuntimeException(responseDTO.getMensagem());
            }

            return responseDTO;

        }
        catch (Exception e)
        {
            throw new RuntimeException("Erro ao processar geração de relatório: " + e.getMessage());
        }
    }

    public RelatorioResponseDTO processarBuscarPorId(Long id)
    {
        if(id == null)
        {
            throw new IllegalArgumentException("ID inválido");
        }

        RelatorioVendas relatorioVendas = relatorioService.buscarPorId(id);

        if (relatorioVendas == null) {
            throw new RuntimeException("Mesa com ID "+ id +" não encontrada");
        }

        return RelatorioResponseDTO.fromEntity(relatorioVendas);
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


    public List<RelatorioResponseDTO> processarListarPorPeriodo(LocalDateTime inicio, LocalDateTime fim)
    {
        if (inicio == null || fim == null)
        {
            throw new IllegalArgumentException("As datas de início e fim devem ser informadas");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após a final");
        }

        List<RelatorioVendas> relatorios = relatorioService.buscarPorPeriodo(inicio, fim);

        if (relatorios.isEmpty()) {
            throw new RuntimeException("Nenhum relatório encontrado para o período informado");
        }

        return relatorios.stream().map(RelatorioResponseDTO::fromEntity).toList();
    }

    public List<RelatorioResponseDTO> buscarPorDataGeracao(LocalDateTime dataGeracao) throws SQLException
    {
        if (dataGeracao.isBefore(LocalDateTime.now()))
        {
            throw new IllegalArgumentException("A data de geração não pode ser antes da data atual");
        }

        if(dataGeracao == null)
        {
            throw new IllegalArgumentException("A data de geração não pode ser nulo");
        }

        List<RelatorioVendas> relatorios = relatorioService.buscarPorDataGeracao(dataGeracao);

        if(relatorios.isEmpty())
        {
            throw new RuntimeException("Nenhum relatório encontrado para a data de geração informada");
        }

        return relatorios.stream().map(RelatorioResponseDTO::fromEntity).toList();
    }

    public RelatorioResponseDTO processarExcluirRelatorio(Long id)
    {
        if(id == null)
        {
            throw new IllegalArgumentException("O id do relatório não pode ser nula");
        }

        RelatorioResponseDTO response = relatorioService.excluirRelatorio(id);

        if (!response.isSucesso()) {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }
}