package org.frangosInfinity.infrastructure.console.module.mesa;

import org.frangosInfinity.application.module.mesa.request.MesaRequestDTO;

import java.util.Scanner;

public class MesaView {
    private final MesaController controller = new MesaController();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("\n=== MESAS ===");
            System.out.println("1 - Criar mesa");
            System.out.println("2 - Listar mesas");
            System.out.println("3 - Mesas disponíveis");
            System.out.println("4 - Buscar por ID");
            System.out.println("5 - Atualizar status");
            System.out.println("6 - Remover mesa");
            System.out.println("0 - Voltar");

            int op = scanner.nextInt();

            try {
                switch (op) {
                    case 1 -> criarMesa();
                    case 2 -> controller.processarListagensTodasMesas().forEach(System.out::println);
                    case 3 -> controller.processarListagemMesasDisponiveis().forEach(System.out::println);
                    case 4 -> buscarPorId();
                    case 5 -> atualizarStatus();
                    case 6 -> removerMesa();
                    case 0 -> { return; }
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private void criarMesa() {
        System.out.print("Número: ");
        int numero = scanner.nextInt();
        System.out.print("Capacidade: ");
        int capacidade = scanner.nextInt();

        MesaRequestDTO req = new MesaRequestDTO(numero, capacidade);
        System.out.println(controller.processarCadastroMesa(req));
    }

    private void buscarPorId() {
        System.out.print("ID: ");
        Long id = scanner.nextLong();
        System.out.println(controller.processarBuscaMesaPorId(id));
    }

    private void atualizarStatus() {
        System.out.print("ID mesa: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Ação (OCUPAR/LIBERAR): ");
        String acao = scanner.nextLine();

        System.out.println(controller.processarAtualizarStatusMesa(id, acao));
    }

    private void removerMesa() {
        System.out.print("ID mesa: ");
        Long id = scanner.nextLong();
        controller.processarRemoverMesa(id);
        System.out.println("Removida!");
    }
}
