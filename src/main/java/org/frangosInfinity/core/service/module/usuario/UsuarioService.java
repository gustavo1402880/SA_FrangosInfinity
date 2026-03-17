package org.frangosInfinity.core.service.module.usuario;

import org.frangosInfinity.application.module.usuario.request.UsuarioRequestDTO;
import org.frangosInfinity.application.module.usuario.response.UsuarioResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.usuario.*;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;
import org.frangosInfinity.infrastructure.persistence.module.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService
{
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Boolean validarId(Long id) {
        return id != null && id > 0;
    }

    private Boolean validarEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private Boolean validarTelefone(String telefone) {
        return telefone != null && telefone.matches("^[1-9]{2}9\\d{8}$");
    }

    private Boolean validarSenha(String senha) {
        return senha != null && senha.length() >= 6;
    }

    private Boolean validarIdSessao(String idSessao) {
        return idSessao != null && !idSessao.trim().isEmpty();
    }

    private Boolean validarMatricula(String matricula) {
        return matricula != null && !matricula.trim().isEmpty();
    }

    private Boolean validarSalario(Double salario) {
        return salario != null && salario >= 2500.00;
    }

    private Boolean validarNome(String nome) {
        return nome != null && nome.length() > 4;
    }

    @Transactional
    public UsuarioResponseDTO adicionarUsuario(UsuarioRequestDTO request)
    {
        if (!request.isValid())
        {
            return UsuarioResponseDTO.erro("Dados inválidos para criação de usuário");
        }

        if (!validarNome(request.getNome()))
        {
            return UsuarioResponseDTO.erro("Nome inválido ou menor que 4 caracteres");
        }

        if (!validarEmail(request.getEmail()))
        {
            return UsuarioResponseDTO.erro("Email inválido");
        }

        if (!validarTelefone(request.getTelefone()))
        {
            return UsuarioResponseDTO.erro("Telefone inválido");
        }

        if (!validarSenha(request.getSenha()))
        {
            return UsuarioResponseDTO.erro("Senha inválida ou muito fraca");
        }

        if (usuarioRepository.existsByEmail(request.getEmail()))
        {
            return UsuarioResponseDTO.erro("Email já existe");
        }

        Usuario usuario;

        if (request.getTipoUsuario() == TipoUsuario.CLIENTE)
        {
            if (!validarIdSessao(request.getIdSessao()) || usuarioRepository.existsClienteByIdSessao(request.getIdSessao()))
            {
                return UsuarioResponseDTO.erro("ID de sessão inválido ou já existente");
            }


            usuario = new Cliente(
                    request.getNome(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getSenha()),
                    TipoUsuario.CLIENTE,
                    request.getIdSessao()
            );
            usuario.setTelefone(request.getTelefone());
        }
        else if (request.getTipoUsuario() == TipoUsuario.FUNCIONARIO)
        {
            if (!validarMatricula(request.getMatricula()) || usuarioRepository.existsFuncionarioByMatricula(request.getMatricula()))
            {
                return UsuarioResponseDTO.erro("Matrícula inválida ou já existente");
            }

            if (!validarSalario(request.getSalario()))
            {
                return UsuarioResponseDTO.erro("Salário inválido ou abaixo de R$2500,00");
            }

            if (request.getNivelAcesso() == null)
            {
                return UsuarioResponseDTO.erro("Nível de acesso obrigatório para funcionário");
            }


            switch (request.getNivelAcesso())
            {
                case ATENDENTE:
                    usuario = new Atendente(
                            request.getNome(),
                            request.getEmail(),
                            passwordEncoder.encode(request.getSenha()),
                            TipoUsuario.FUNCIONARIO,
                            request.getMatricula()
                    );
                    break;
                case CAIXA:
                    usuario = new Caixa(
                            request.getNome(),
                            request.getEmail(),
                            passwordEncoder.encode(request.getSenha()),
                            TipoUsuario.FUNCIONARIO,
                            request.getMatricula()
                    );
                    break;
                case COZINHEIRO:
                    usuario = new Cozinheiro(
                            request.getNome(),
                            request.getEmail(),
                            passwordEncoder.encode(request.getSenha()),
                            TipoUsuario.FUNCIONARIO,
                            request.getMatricula()
                    );
                    break;
                case ADMINISTRADOR:
                    usuario = new Administrador(
                            request.getNome(),
                            request.getEmail(),
                            passwordEncoder.encode(request.getSenha()),
                            TipoUsuario.FUNCIONARIO,
                            request.getMatricula()
                    );
                    break;
                default:
                    return UsuarioResponseDTO.erro("Nível de acesso inválido");
            }
            usuario.setTelefone(request.getTelefone());
            ((Funcionario) usuario).setTurno(request.getTurno());
            ((Funcionario) usuario).setSalario(request.getSalario());
        }
        else
        {
            return UsuarioResponseDTO.erro("Tipo de usuário inválido");
        }

        Usuario usuarioSalvar = usuarioRepository.save(usuario);

        return UsuarioResponseDTO.fromEntity(usuarioSalvar);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id)
    {

        if (!validarId(id))
        {
            return UsuarioResponseDTO.erro("ID inválido");
        }

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário com ID: " + id + " não encontrado"));

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorEmail(String email)
    {

        if (!validarEmail(email)) {
            return UsuarioResponseDTO.erro("Email inválido");
        }

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuário com email " + email + " não encontrado"));

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos()
    {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodosAtivos()
    {
        return usuarioRepository.findByAtivoTrue().stream()
                .map(UsuarioResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodosInativos()
    {
        return usuarioRepository.findByAtivoFalse().stream()
                .map(UsuarioResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarPorTipoUsuario(TipoUsuario tipoUsuario)
    {
        return usuarioRepository.findByTipo(tipoUsuario).stream()
                .map(UsuarioResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public  List<UsuarioResponseDTO> listarPorNivelAcesso(NivelAcesso nivelAcesso)
    {
        return usuarioRepository.buscarFuncionarioPorNivelAcesso(nivelAcesso).stream()
                .map(UsuarioResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorIdSessao(String idSessao)
    {
        if (!validarIdSessao(idSessao))
        {
            return UsuarioResponseDTO.erro("ID de sessão inválido");
        }

        Cliente cliente = usuarioRepository.buscarClientePorIdSessao(idSessao).orElseThrow(() -> new ResourceNotFoundException("Cliente com id de sessão: " + idSessao + " não encontrado"));

        return UsuarioResponseDTO.fromEntity(cliente);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorMatricula(String matricula)
    {
        if (!validarMatricula(matricula))
        {
            return UsuarioResponseDTO.erro("Matrícula inválida");
        }

        Funcionario funcionario = usuarioRepository.buscarFuncionarioPorMatricula(matricula).orElseThrow(() ->  new ResourceNotFoundException("Funcionário com matrícula: " + matricula + " não encontrado"));

        return UsuarioResponseDTO.fromEntity(funcionario);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO request)
    {
        if (!validarId(id))
        {
            return UsuarioResponseDTO.erro("ID inválido");
        }

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (validarNome(request.getNome()) && !request.getNome().equals(usuario.getNome()))
        {
            usuario.setNome(request.getNome());
        }

        if (validarTelefone(request.getTelefone()) && !request.getTelefone().equals(usuario.getTelefone()))
        {
            usuario.setTelefone(request.getTelefone());
        }

        if (usuario instanceof Funcionario && request.getTurno() != null)
        {
            ((Funcionario) usuario).setTurno(request.getTurno());
        }

        usuarioRepository.save(usuario);

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizarSenha(Long id, String senhaAntiga, String senhaNova)
    {
        if (!validarId(id))
        {
            return UsuarioResponseDTO.erro("ID inválido");
        }

        if (!validarSenha(senhaNova) || senhaAntiga.equals(senhaNova))
        {
            return UsuarioResponseDTO.erro("Senha inválida ou muito fraca");
        }

        Usuario usuario = usuarioRepository.findById(id) .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senhaAntiga, usuario.getSenha()))
        {
            return UsuarioResponseDTO.erro("Senha antiga incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(senhaNova));
        usuarioRepository.save(usuario);

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizarEmail(String emailAntigo, String emailNovo)
    {
        if (!validarEmail(emailNovo) || emailAntigo.equals(emailNovo))
        {
            return UsuarioResponseDTO.erro("Email inválido ou iguais");
        }

        if (usuarioRepository.existsByEmail(emailNovo))
        {
            return UsuarioResponseDTO.erro("Email já cadastrado");
        }

        Usuario usuario = usuarioRepository.findByEmail(emailAntigo).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setEmail(emailNovo);
        usuarioRepository.save(usuario);

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    @Transactional
    public UsuarioResponseDTO login(String email, String senha)
    {
        if (!validarEmail(email))
        {
            return UsuarioResponseDTO.erro("Email inválido");
        }

        if (!validarSenha(senha))
        {
            return UsuarioResponseDTO.erro("Senha inválida");
        }

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new BusinessException("Email ou senha inválido"));

        if (!passwordEncoder.matches(senha, usuario.getSenha()))
        {
            return UsuarioResponseDTO.erro("Email ou senha inválidos");
        }

        if (!usuario.isAtivo())
        {
            return UsuarioResponseDTO.erro("Usuário invativo");
        }

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    @Transactional
    public UsuarioResponseDTO deletarUsuario(Long id)
    {

        if (!validarId(id))
        {
            return UsuarioResponseDTO.erro("ID inválido");
        }

        if (!usuarioRepository.existsById(id))
        {
            return UsuarioResponseDTO.erro("Usuário não encontrado");
        }

        usuarioRepository.deleteById(id);

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setSucesso(true);
        response.setMensagem("Usuário desativado com sucesso");
        return response;
    }

    @Transactional
    public UsuarioResponseDTO ativarUsuario(Long id)
    {
        if (!validarId(id))
        {
            return UsuarioResponseDTO.erro("ID inválido");
        }

        Usuario usuario = usuarioRepository.findById(id) .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.ativar();
        usuarioRepository.save(usuario);

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setMensagem("Usuário ativado com sucesso");
        response.setSucesso(true);
        return response;
    }
}
