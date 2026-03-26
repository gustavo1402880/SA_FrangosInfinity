package org.frangosInfinity.infrastructure.console.module.pedido;

import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.entity.module.produto.Cardapio;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.PedidoController;
import org.frangosInfinity.infrastructure.console.module.produto.controller.CardapioController;

import java.util.List;

public class VizualizarCardapio
{

    private static CardapioController controller;
    public static void Vizualizarsubpedido()
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                CARDAPIO                ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Produto> produtos = controller.verCardapio();

        for(Produto p : produtos)
        {
            System.out.println("\n");
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║            ID: %-23║%n"+ p.getId());
            System.out.println("║          Nome: %-23║%n"+ p.getNome());
            System.out.println("║        Codigo: %-23║%n"+ p.getCodigo());
            System.out.println("║     Descrição: %-23║%n"+p.getDescricao());
            System.out.println("║     categoria: %-23║%n"+ p.getCategoria());
            System.out.println("║         Custo: %-23║%n"+ p.getCusto());
            System.out.println("║      Disponivel:%-23║%n "+ p.getDisponivel());
            System.out.println("╚════════════════════════════════════════╝");

        }
    }
}
