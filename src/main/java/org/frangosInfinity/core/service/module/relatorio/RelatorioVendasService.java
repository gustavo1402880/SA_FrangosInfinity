package org.frangosInfinity.core.service.module.relatorio;

import org.frangosInfinity.application.module.relatorio.request.RelatorioRequestDTO;
import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.relatorio.RelatorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioVendasService
{
    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private PDFService pdfService;

    @Transactional
    @CacheEvict(value = "relatorios")
    public RelatorioResponseDTO gerarRelatorio(RelatorioRequestDTO request)
    {
        if (request.getPeriodoInicio().isAfter(request.getPeriodoFim()))
        {
            throw new BusinessException("Data de início e fim inválidas");
        }

        RelatorioVendas relatorio = new RelatorioVendas(
                request.getPeriodoInicio(),
                request.getPeriodoFim(),
                request.getTotalVendas(),
                request.getTotalPedidor(),
                request.getTicketMedio()
        );
        relatorio.calcularTicketMedio();

        RelatorioVendas relSalvo = relatorioRepository.save(relatorio);

        return RelatorioResponseDTO.fromEntity(relSalvo);
    }

    @Transactional
    public RelatorioResponseDTO gerarRelatorioAutomatico(LocalDateTime inicio, LocalDateTime fim)
    {
        RelatorioRequestDTO request = new RelatorioRequestDTO();
        request.setPeriodoInicio(inicio);
        request.setPeriodoFim(fim);
        request.setTotalVendas(0.0);
        request.setTotalPedidor(0);
        request.setTicketMedio(0.0);

        return gerarRelatorio(request);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "relatorios", key = "#id")
    public RelatorioResponseDTO buscarPorId(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new BusinessException("ID inválido");
        }

        RelatorioVendas relatorio = relatorioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Relatório não encontrdao"));

        return RelatorioResponseDTO.fromEntity(relatorio);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "relatorios")
    public List<RelatorioResponseDTO> listarTodos()
    {
        return relatorioRepository.findAll().stream()
                .map(RelatorioResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RelatorioResponseDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim)
    {
        if (inicio.isAfter(fim))
        {
            throw new BusinessException("Data de início e fim inválidas");
        }

        return relatorioRepository.findByPeriodo(inicio, fim).stream()
                .map(RelatorioResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RelatorioResponseDTO> buscarPorDataGeracao(LocalDateTime dataGeracao)
    {
        return relatorioRepository.findByDataGeracaoBetween(
        dataGeracao.withHour(0).withMinute(0).withSecond(0),
        dataGeracao.withHour(23).withMinute(59).withSecond(39)
        ).stream()
                .map(RelatorioResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RelatorioResponseDTO buscarUltimo()
    {
        RelatorioVendas relatorio = relatorioRepository.findFirstByOrderByDataGeracaoDesc().orElseThrow(() -> new ResourceNotFoundException("Relatório não encontrado"));

        return RelatorioResponseDTO.fromEntity(relatorio);
    }

    public byte[] gerarRelatorioPdfPorPeriodo(LocalDateTime inicio, LocalDateTime fim)
    {
        List<RelatorioVendas> relatorios = relatorioRepository.findByPeriodo(inicio, fim);

        if (relatorios.isEmpty())
        {
            throw new ResourceNotFoundException("Nenhum relatório encontrado para o período");
        }

        return pdfService.gerarPdf(relatorios);
    }

    public byte[] gerarRelatorioPdfPorId(Long id)
    {
        RelatorioVendas relatorio = relatorioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Relatório não encontrado"));

        return pdfService.gerarPdf(List.of(relatorio));
    }

    @Transactional
    @CacheEvict(value = "relatorios")
    public void excluirRelatorio(Long id)
    {
        if (!relatorioRepository.existsById(id))
        {
            throw new ResourceNotFoundException("Relatório não encontrado");
        }

        relatorioRepository.deleteById(id);
    }
}
