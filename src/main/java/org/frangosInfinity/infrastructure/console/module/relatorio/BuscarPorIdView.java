package org.frangosInfinity.infrastructure.console.module.relatorio;

import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;

import java.util.Scanner;

public class BuscarPorIdView
{
    private final RelatorioController controller = new RelatorioController();
    private final Scanner scanner = new Scanner(System.in);

    public void exibir() {
        try {
            System.out.println("=== BUSCAR RELATÓRIO POR ID ===");

            System.out.print("Digite o ID: ");
            Long id = scanner.nextLong();

            RelatorioResponseDTO response = controller.processarBuscarPorId(id);

            System.out.println("Relatório encontrado:");
            System.out.println(response);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
