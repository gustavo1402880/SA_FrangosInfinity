package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.infrastructure.console.module.main.EncerarSistema;
import org.frangosInfinity.infrastructure.util.Front;

import java.sql.SQLOutput;
import java.util.Scanner;

public class PedidoView
{
    //╚ ╝  ╣  ╠   ═ ╔  ╗  ║

    public static void pedidosViewCliente()
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
        System.out.println("║       8 - Ver Pontos                   ║");
        System.out.println("║       9 Sair                           ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.print("║   Qual opção deseja: ");
        int opcao = Front.lInteiro();

        switch (opcao)
        {
            case 1 :
                VizualizarCardapio.VizualizarCardapioo();
                break;
            case 2 :
                FazerPedido.fazerPedido();
                break;
            case 3 :
                ConfirmarPedido.confirmarPedido();

                break;
            case 4 :
                AcompanharStatus.acompanharstatus();

                break;
            case 5 :
                CancelarPedido.cancelarPedido();
                break;

            case 6 :
                VizualizarHistorico.vizualizarHistorico();
                break;
            case 7 :
                SolicitarPagamento.solicitarPagamento();
                break;
            case 8 :
                VerPontos.verPontos();
                break;
            case 9 :
                EncerarSistema.desligar();
                break;
            default:
                System.out.println(" Opção errada, tente novamente");
        }
    }
}
