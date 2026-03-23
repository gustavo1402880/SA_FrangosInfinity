package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.infrastructure.util.Front;

import java.sql.SQLOutput;
import java.util.Scanner;

public class PedidoView
{
    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║

    private String espaco = " ";
    private Scanner sc = new Scanner(System.in);
    public void pedidosViewCliente()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║             MENU DE PEDIDOS            ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║       1 - Vizualizar Cardapio          ║");
        System.out.println("║       2 - Fazer Pedido                 ║");
        System.out.println("║       3 - Confirmar Pedido             ║");
        System.out.println("║       4 - Acompanhar Status            ║");
        System.out.println("║       5 - Cancelar pedido              ║");
        System.out.println("║       6 - Vizualizar Historico         ║");
        System.out.println("║       7 - Solicitar Pagamento          ║");
        System.out.println("║       8 - ver Pontos                   ║");
        System.out.println("║       9 Sair                           ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║   Qual opção deseja %-21s║%n ", espaco);
        int opcao = Front.lInteiro();

        switch (opcao)
        {
            case 1 :
                VizualizarCardapio vizualizarCardapio = new VizualizarCardapio();

                vizualizarCardapio.VizualizarCardapioo();
                break;

            case 2 :
                FazerPedido fazerPedido = new FazerPedido();

                fazerPedido.fazerPedido();
                break;

            case 3 :
                ConfirmarPedido confirmarPedido = new ConfirmarPedido();

                confirmarPedido.confirmarPedido();

                break;

            case 4 :
                AcompanharStatus acompanharStatus = new AcompanharStatus();

                acompanharStatus.acompanharstatus();


                break;

            case 5 :
                CancelarPedido cancelarPedido = new CancelarPedido();

                cancelarPedido.cancelarPedido();
                break;

            case 6 :
                VizualizarHistorico vizualizarHistorico = new VizualizarHistorico();

                vizualizarHistorico.vizualizarHistorico();
                break;

            case 7 :
                SolicitarPagamento solicitarPagamento = new SolicitarPagamento();

                solicitarPagamento.solicitarPagamento();
                break;

            case 8 :
                VerPontos verPontos = new VerPontos();

                verPontos.verPontos();
                break;

            case 9 :
                System.out.println(" Obrigado pela Visita");
                break;

            default:
                System.out.println(" Opção errada, tente novamente");
        }




    }
}
