package org.frangosInfinity.infrastructure.console.module.pagamento;

import org.frangosInfinity.application.module.pagamento.request.PagamentoRequestDTO;

import java.util.Scanner;

public class PagamentoView {
    private final PagamentoController controller = new PagamentoController();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("\n=== PAGAMENTO ===");
            System.out.println("1 - Pagar");
            System.out.println("2 - Buscar pagamento");
            System.out.println("3 - Listar pagamentos");
            System.out.println("4 - Gerar PIX");
            System.out.println("5 - Confirmar pagamento");
            System.out.println("6 - Cancelar pagamento");
            System.out.println("0 - Voltar");

            int op = scanner.nextInt();

            try {
                switch (op) {
                    case 1 -> pagar();
                    case 2 -> buscar();
                    case 3 -> controller.processarListarTodosPagamentos().forEach(System.out::println);
                    case 4 -> gerarPix();
                    case 5 -> confirmar();
                    case 6 -> cancelar();
                    case 0 -> { return; }
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private void pagar() {
        System.out.print("ID subpedido: ");
        Long id = scanner.nextLong();
        System.out.print("Valor: ");
        Double valor = scanner.nextDouble();

        PagamentoRequestDTO req = new PagamentoRequestDTO(id, valor);
        System.out.println(controller.processarPagamento(req));
    }

    private void buscar() {
        System.out.print("ID pagamento: ");
        Long id = scanner.nextLong();
        System.out.println(controller.processarBuscarPagamentoPorId(id));
    }

    private void gerarPix() {
        System.out.print("ID pagamento: ");
        Long id = scanner.nextLong();
        System.out.println(controller.processarGerarPix(id, null));
    }

    private void confirmar() {
        System.out.print("ID pagamento: ");
        Long id = scanner.nextLong();
        System.out.println(controller.processarConfirmarPagamento(id));
    }

    private void cancelar() {
        System.out.print("ID pagamento: ");
        Long id = scanner.nextLong();
        System.out.println(controller.processarCancelarPagamento(id));
    }
}
