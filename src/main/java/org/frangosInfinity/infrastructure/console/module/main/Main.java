package org.frangosInfinity.infrastructure.console.module.main;

import org.frangosInfinity.core.entity.module.usuario.Administrador;
import org.frangosInfinity.core.enums.TipoUsuario;
import org.frangosInfinity.core.service.module.fidelidade.PontosFidelidadeService;
import org.frangosInfinity.core.service.module.fidelidade.RegrasFidelidadeService;
import org.frangosInfinity.core.service.module.mesa.IoTConfigService;
import org.frangosInfinity.core.service.module.mesa.MesaService;
import org.frangosInfinity.core.service.module.mesa.QRCodeService;
import org.frangosInfinity.core.service.module.notificacao.EmailService;
import org.frangosInfinity.core.service.module.notificacao.NotificacaoService;
import org.frangosInfinity.core.service.module.pagamento.ComprovanteService;
import org.frangosInfinity.core.service.module.pagamento.PagamentoService;
import org.frangosInfinity.core.service.module.pagamento.TransacaoPIXService;
import org.frangosInfinity.core.service.module.pedido.CarrinhoService;
import org.frangosInfinity.core.service.module.pedido.ItemPedidoService;
import org.frangosInfinity.core.service.module.pedido.PedidoService;
import org.frangosInfinity.core.service.module.pedido.SubPedidoService;
import org.frangosInfinity.core.service.module.produto.CardapioService;
import org.frangosInfinity.core.service.module.produto.CategoriaService;
import org.frangosInfinity.core.service.module.produto.EstoqueService;
import org.frangosInfinity.core.service.module.produto.Produtoservice;
//import org.frangosInfinity.core.service.module.relatorio.RelatorioVendasService;
import org.frangosInfinity.core.service.module.usuario.UsuarioService;
import org.frangosInfinity.infrastructure.console.module.usuario.UsuarioController;
import org.frangosInfinity.infrastructure.util.Front;

public class Main
{

    public static UsuarioController usuarioController = new UsuarioController();


    public static void main(String[] args) {
        System.out.println(Front.OCEAN_BLUE+"          ▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉");
        System.out.println("          ▉▉▉  ");
        System.out.println("          ▉▉▉   ▉▉▉   ▉▉▉   ▉▉▉▉▉▉▉   ▉▉▉▉▉▉▉▉▉");
        System.out.println("          ▉▉▉   ▉▉▉   ▉▉▉   ▉▉▉       ▉▉▉   ▉▉▉");
        System.out.println("          ▉▉▉   ▉▉▉   ▉▉▉   ▉▉▉▉▉▉▉   ▉▉▉   ▉▉▉");
        System.out.println("          ▉▉▉   ▉▉▉   ▉▉▉   ▉▉▉       ▉▉▉   ▉▉▉");
        System.out.println("          ▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉   ▉▉▉▉▉▉▉   ▉▉▉▉▉▉▉▉▉");
        System.out.println("                                            ▉▉▉");
        System.out.println("          ▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉"+Front.AQUA_BLUE);

        MenuPrincipal.menuPrincipal();
    }
}