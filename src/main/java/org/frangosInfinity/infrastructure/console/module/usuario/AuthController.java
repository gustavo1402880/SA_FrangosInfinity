package org.frangosInfinity.infrastructure.console.module.usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.frangosInfinity.application.module.usuario.response.UsuarioResponseDTO;
import org.frangosInfinity.config.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuário logado")
public class AuthController
{
    @GetMapping("/me")
    @Operation(summary = "Retorna dados do usuário logado")
    public ResponseEntity<UsuarioResponseDTO> getCurrentUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl))
        {
            return ResponseEntity.status(401).build();
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        UsuarioResponseDTO response = UsuarioResponseDTO.fromEntity(userDetails.getUsuario());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Realiza logout")
    public ResponseEntity<String> logout(HttpSession session)
    {
        session.invalidate();
        return ResponseEntity.ok("Logout realizado");
    }

    @GetMapping("/status")
    @Operation(summary = "Veririca status da autenticação")
    public ResponseEntity<Boolean> isAuthenticated()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String);

        return ResponseEntity.ok(isAuthenticated);
    }
}
