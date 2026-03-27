package org.frangosInfinity.infrastructure.console.module.usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.frangosInfinity.application.module.usuario.request.UsuarioRequestDTO;
import org.frangosInfinity.application.module.usuario.response.UsuarioResponseDTO;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.usuario.Usuario;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;
import org.frangosInfinity.core.service.module.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController
{
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/clientes")
    @Operation(summary = "Criar um novo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<UsuarioResponseDTO> processarAdicionarCliente(@Valid @RequestBody UsuarioRequestDTO request)
    {
        request.setTipoUsuario(TipoUsuario.CLIENTE);

        UsuarioResponseDTO response = usuarioService.adicionarUsuario(request);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/funcionarios")
    @Operation(summary = "Criar um novo funcionário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Funcionario criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<UsuarioResponseDTO> processarAdicionarFuncionario(@Valid @RequestBody UsuarioRequestDTO request)
    {
        request.setTipoUsuario(TipoUsuario.FUNCIONARIO);

        UsuarioResponseDTO response = usuarioService.adicionarUsuario(request);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioResponseDTO> processarBuscarPorId(@PathVariable Long id)
    {
        try
        {
            UsuarioResponseDTO response = usuarioService.buscarPorId(id);

            if (!response.getSucesso())
            {
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok(response);
        }
        catch (ResourceNotFoundException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuário por email")
    public ResponseEntity<UsuarioResponseDTO> processarBuscarPorEmail(@PathVariable String email)
    {
        try
        {
            UsuarioResponseDTO response = usuarioService.buscarPorEmail(email);

            if (!response.getSucesso())
            {
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok(response);
        }
        catch (ResourceNotFoundException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/sessao/{idSessao}")
    @Operation(summary = "Buscar cliente por ID de sessão")
    public ResponseEntity<UsuarioResponseDTO> processarBuscarPorIdSessao(@PathVariable String idSessao)
    {
        try
        {
            UsuarioResponseDTO response = usuarioService.buscarPorIdSessao(idSessao);

            if (!response.getSucesso())
            {
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok(response);
        }
        catch (ResourceNotFoundException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/matricula/{matricula}")
    @Operation(summary = "Buscar funcionário pela matrícula")
    public ResponseEntity<UsuarioResponseDTO> processarBuscarPorMatricula(@PathVariable String matricula)
    {
        try
        {
            UsuarioResponseDTO response = usuarioService.buscarPorMatricula(matricula);

            if (!response.getSucesso())
            {
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok(response);
        }
        catch (ResourceNotFoundException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<List<UsuarioResponseDTO>> processarListarTodos()
    {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();

        if (usuarios.isEmpty())
        {
            ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);

    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar uauários ativos")
    public ResponseEntity<List<UsuarioResponseDTO>> processarListarAtivos()
    {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodosAtivos();

        if (usuarios.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/inativos")
    @Operation(summary = "Listar usuários invativos")
    public ResponseEntity<List<UsuarioResponseDTO>> processarListarInativos()
    {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodosInativos();

        if (usuarios.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar usuários por tipo (CLIENTE/FUNCIONÁRIO)")
    public ResponseEntity<List<UsuarioResponseDTO>> processarListarPorTipo(@PathVariable TipoUsuario tipo)
    {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarPorTipoUsuario(tipo);

        if (usuarios.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/nivel-acesso/{nivel}")
    @Operation(summary = "Listar funcionários por nível de acesso")
    public ResponseEntity<List<UsuarioResponseDTO>> processarListarPorNivelAcesso(@PathVariable NivelAcesso nivelAcesso)
    {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarPorNivelAcesso(nivelAcesso);

        if (usuarios.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do usuário")
    public ResponseEntity<UsuarioResponseDTO> processarAtualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO request)
    {
        UsuarioResponseDTO response = usuarioService.atualizarUsuario(id, request);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/senha")
    @Operation(summary = "Atualizar senha do usuário")
    public ResponseEntity<UsuarioResponseDTO> processarAtualizarSenha(@PathVariable Long id, @RequestParam String senhaAntiga, @RequestParam String senhaNova)
    {
        UsuarioResponseDTO response = usuarioService.atualizarSenha(id, senhaAntiga, senhaNova);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/email")
    @Operation(summary = "Atualizar email do usuário")
    public ResponseEntity<UsuarioResponseDTO> processarAtualizarEmail(@RequestParam String emailAntigo, @RequestParam String emailNovo)
    {
        UsuarioResponseDTO response = usuarioService.atualizarEmail(emailAntigo, emailNovo);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar usuário")
    public ResponseEntity<UsuarioResponseDTO> processarDesativarUsuario(Long id)
    {
        UsuarioResponseDTO response = usuarioService.deletarUsuario(id);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar usuário")
    public ResponseEntity<UsuarioResponseDTO> processarAtivarUsuario(Long id)
    {
        UsuarioResponseDTO response = usuarioService.ativarUsuario(id);

        if (!response.getSucesso())
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
