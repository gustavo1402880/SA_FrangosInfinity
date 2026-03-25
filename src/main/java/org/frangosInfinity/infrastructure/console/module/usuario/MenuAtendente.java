package org.frangosInfinity.infrastructure.console.module.usuario;

import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.infrastructure.console.module.pedido.RegistrarPedidoManual;

public class MenuAtendente
{
    public static void menuInicial(long idAtendente)
    {
        RegistrarPedidoManual.RegistrarPedido(idAtendente);
    }
}
