package org.frangosInfinity.infrastructure.console.module.usuario;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController
{
    @GetMapping("/")
    public String home()
    {
        return "home";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model)
    {
        if (error != null)
        {
            model.addAttribute("error", "Email ou senha inválidos");
        }

        if (logout != null)
        {
            model.addAttribute("message", "Logout realizado com sucesso");
        }

        if (expired != null)
        {
            model.addAttribute("message", "Sessão expirada. Faça login novamente");
        }

        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model)
    {
        model.addAttribute("message", "Bem vindo ao menu do usuário");
        return "dashboard";
    }
}
