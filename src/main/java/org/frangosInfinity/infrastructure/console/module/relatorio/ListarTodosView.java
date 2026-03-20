package org.frangosInfinity.infrastructure.console.module.relatorio;

import org.frangosInfinity.application.module.relatorio.response.RelatorioResponseDTO;

import java.util.List;

public class ListarTodosView
{
    private final RelatorioController controller = new RelatorioController();

    public void exibir() {
        try {
            System.out.println("=== LISTAR RELATÓRIOS ===");

            List<RelatorioResponseDTO> lista = controller.processarListarTodos();

            lista.forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
