package org.frangosInfinity.infrastructure.console.module.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController
{
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            @RequestParam(value = "redirect", required = false) String redirect,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Email ou senha inválidos");
        }
        if (logout != null) {
            model.addAttribute("message", "Logout realizado com sucesso");
        }
        if (expired != null) {
            model.addAttribute("message", "Sessão expirada. Faça login novamente");
        }
        if (redirect != null) {
            model.addAttribute("redirect", redirect);
        }

        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    // ==================== PÁGINAS DO CLIENTE ====================

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("message", "Bem vindo ao menu do usuário");
        return "dashboard";
    }

    @GetMapping("/mesas")
    public String mesas() {
        return "mesas";
    }

    @GetMapping("/validar-qrcode")
    public String validarQRCode(
            @RequestParam(name = "mesa", required = true) Long mesa,
            @RequestParam(name = "token", required = true) String token,
            Model model) {
        model.addAttribute("mesaId", mesa);
        model.addAttribute("token", token);
        return "validar-qrcode";
    }

    @GetMapping("/cardapio")
    public String cardapio(
            @RequestParam(name = "mesaId", required = true) Long mesaId,
            @RequestParam(name = "pedidoId", required = true) Long pedidoId,
            Model model) {
        model.addAttribute("mesaId", mesaId);
        model.addAttribute("pedidoId", pedidoId);
        return "cardapio";
    }

    @GetMapping("/acompanhar-pedido")
    public String acompanharPedido(
            @RequestParam(name = "id", required = true) Long subPedidoId,
            Model model) {
        model.addAttribute("subPedidoId", subPedidoId);
        return "acompanhar-pedido";
    }

    // ==================== PÁGINAS DOS FUNCIONÁRIOS ====================

    @GetMapping("/cozinha")
    public String cozinha() {
        return "cozinha";
    }

    @GetMapping("/pedidos")
    public String pedidos() {
        return "pedidos";
    }

    @GetMapping("/pagamentos")
    public String pagamentos() {
        return "pagamentos";
    }

    // ==================== PÁGINAS DO ADMINISTRADOR ====================

    @GetMapping("/admin/produtos")
    public String adminProdutos() {
        return "admin/produtos";
    }

    @GetMapping("/admin/categorias")
    public String adminCategorias() {
        return "admin/categorias";
    }

    @GetMapping("/admin/usuarios")
    public String adminUsuarios() {
        return "admin/usuarios";
    }

    @GetMapping("/admin/relatorios")
    public String adminRelatorios() {
        return "admin/relatorios";
    }

    @GetMapping("/admin/fidelidade")
    public String adminFidelidade() {
        return "admin/fidelidade";
    }
}
