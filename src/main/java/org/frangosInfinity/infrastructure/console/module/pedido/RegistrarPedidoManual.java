package org.frangosInfinity.infrastructure.console.module.pedido;

import org.bouncycastle.asn1.cms.TimeStampedDataParser;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.entity.module.usuario.Cliente;
import org.frangosInfinity.core.enums.StatusPedido;
import org.frangosInfinity.infrastructure.console.module.pedido.controller.PedidoController;
import org.frangosInfinity.infrastructure.util.Front;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RegistrarPedidoManual
{
    static PedidoController controller;

    public static void RegistrarPedido(Long idAtendente)
    {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           REGISTRAR PEDIDO             ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("║ Qual o Número do pedido deseja escolher: ");
        String id = Front.lString();
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println(" Qual o Id do da mesa que você esta sentado: ");

        Long mesaID = (long) Front.lInteiro();

        Date dataHora = Date.valueOf(LocalDate.now());

        Pedido pedido = new Pedido(id, dataHora, StatusPedido.CONFIMADO, mesaID, idAtendente, "INDIVIDUAL");

        controller.registrarPedidoManual(pedido);


    }


}
