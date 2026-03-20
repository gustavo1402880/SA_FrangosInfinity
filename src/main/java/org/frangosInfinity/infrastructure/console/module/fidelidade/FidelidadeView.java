package org.frangosInfinity.infrastructure.console.module.fidelidade;

import org.frangosInfinity.application.module.fidelidade.request.ResgateRequestDTO;

import java.util.Scanner;

public class FidelidadeView
{
    private final FidelidadeController controller = new FidelidadeController();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("\n=== FIDELIDADE ===");
            System.out.println("1 - Criar conta");
            System.out.println("2 - Ver pontos");
            System.out.println("3 - Acumular pontos");
            System.out.println("4 - Resgatar pontos");
            System.out.println("5 - Ver regras");
            System.out.println("6 - Listar clientes");
            System.out.println("7 - Remover conta");
            System.out.println("0 - Voltar");

            int op = scanner.nextInt();

            try {
                switch (op) {
                    case 1 -> criarConta();
                    case 2 -> verPontos();
                    case 3 -> acumular();
                    case 4 -> resgatar();
                    case 5 -> System.out.println(controller.processarVerRegras());
                    case 6 -> controller.processarListarTodosClientes().forEach(System.out::println);
                    case 7 -> removerConta();
                    case 0 -> { return; }
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private void criarConta() {
        System.out.print("ID cliente: ");
        Long id = scanner.nextLong();
        System.out.println(controller.processarCriarConta(id));
    }

    private void verPontos() {
        System.out.print("ID cliente: ");
        Long id = scanner.nextLong();
        System.out.println(controller.processarBuscarPontosPorCliente(id));
    }

    private void acumular() {
        System.out.print("ID cliente: ");
        Long id = scanner.nextLong();
        System.out.print("Valor gasto: ");
        Double valor = scanner.nextDouble();
        System.out.println(controller.processarAcumularPontos(id, valor));
    }

    private void resgatar() {
        System.out.print("ID cliente: ");
        Long id = scanner.nextLong();
        System.out.print("Pontos: ");
        Integer pontos = scanner.nextInt();
        System.out.print("Valor compra: ");
        Double valor = scanner.nextDouble();

        ResgateRequestDTO req = new ResgateRequestDTO(id, pontos, valor);
        System.out.println(controller.processarResgatarPontos(req));
    }

    private void removerConta() {
        System.out.print("ID cliente: ");
        Long id = scanner.nextLong();
        controller.processarRemoverConta(id);
        System.out.println("Removido!");
    }
}
