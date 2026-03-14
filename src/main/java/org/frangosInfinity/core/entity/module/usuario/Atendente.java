package org.frangosInfinity.core.entity.module.usuario;

import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;

public class Atendente extends Funcionario
{
    public Atendente(String nome, String email, String senha, TipoUsuario usuario, String matricula)
    {
        super(nome, email, senha, usuario, matricula, NivelAcesso.ATENDENTE);
    }

    public Atendente() {

    }

    public String getTipoUsuario()
    {
        return "ATENDENTE";
    }

    public void registrarPedidoManual()
    {
        System.out.println("Atendente " + this.getNome() + " registrando pedido manual...");
    }

    public void visualizarPedidosApp()
    {
        System.out.println("Visualizando pedidos do app...");
    }

    public void modificarPedido()
    {
        System.out.println("Modificando pedido...");
    }
}
