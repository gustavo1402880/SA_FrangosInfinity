package org.frangosInfinity.infrastructure.console.module.relatorio;

import org.frangosInfinity.application.module.relatorio.request.RelatorioRequestDTO;
import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;

import java.time.LocalDateTime;
import java.util.Scanner;

public class GerarRelatorioView
{
    private final RelatorioController controller = new RelatorioController();
    private final Scanner scanner = new Scanner(System.in);

    public void exibir() {
        try {
            System.out.println("=== GERAR RELATÓRIO ===");

            System.out.print("Digite a data inicial (yyyy-MM-ddTHH:mm): ");
            LocalDateTime inicio = LocalDateTime.parse(scanner.nextLine());

            System.out.print("Digite a data final (yyyy-MM-ddTHH:mm): ");
            LocalDateTime fim = LocalDateTime.parse(scanner.nextLine());

            RelatorioRequestDTO request = new RelatorioRequestDTO(inicio, fim);

            RelatorioResponseDTO response = controller.processarGerarRelatorio(request);

            System.out.println("Sucesso: " + response.getMensagem());

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
