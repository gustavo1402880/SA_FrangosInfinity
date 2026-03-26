package org.frangosInfinity.infrastructure.console.module.pedido;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.frangosInfinity.application.module.pedido.request.CarrinhoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.ItemPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.PedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.SubPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.CarrinhoResponseDTO;
import org.frangosInfinity.application.module.pedido.response.PedidoResponseDTO;
import org.frangosInfinity.application.module.pedido.response.SubPedidoResponseDTO;
import org.frangosInfinity.application.module.produto.response.CategoriaResponseDTO;
import org.frangosInfinity.application.module.produto.response.ProdutoRespondeDTO;
import org.frangosInfinity.core.enums.StatusPedido;
import org.frangosInfinity.core.service.module.pedido.CarrinhoService;
import org.frangosInfinity.core.service.module.pedido.PedidoService;
import org.frangosInfinity.core.service.module.pedido.SubPedidoService;
import org.frangosInfinity.core.service.module.produto.CategoriaService;
import org.frangosInfinity.core.service.module.produto.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos")
public class PedidoController
{
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private SubPedidoService subPedidoService;

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/carrinho")
    public ResponseEntity<CarrinhoResponseDTO> processarObterCarrinho(HttpSession session)
    {
        String sessaoId = session.getId();
        CarrinhoResponseDTO response = carrinhoService.obterCarrinho(sessaoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/carrinho/itens")
    @Operation(summary = "Adicionar item ao cerrinho")
    public ResponseEntity<CarrinhoResponseDTO> processarAdicionarItem(HttpSession session, @Valid @RequestBody CarrinhoRequestDTO request)
    {
        String sessaoId = session.getId();
        CarrinhoResponseDTO response = carrinhoService.adicionarItem(sessaoId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/carrinho/itens/{index}")
    @Operation(summary = "Remover item do carrinho")
    public ResponseEntity<CarrinhoResponseDTO> processarRemoverItem(HttpSession session, @PathVariable Integer index)
    {
        String sessaoId = session.getId();
        CarrinhoResponseDTO response = carrinhoService.removerItem(sessaoId, index);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/carrinho/itens/{index}")
    @Operation(summary = "Atualizar quantidade do item")
    public ResponseEntity<CarrinhoResponseDTO> processarAtualizarQuantidade(HttpSession session, @PathVariable Integer index, @RequestParam Integer quantidade)
    {
        String sessaoId = session.getId();

        CarrinhoResponseDTO response = carrinhoService.atualizarQuantidade(sessaoId, index, quantidade);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/carrinho")
    @Operation(summary = "Limpar carrinho")
    public ResponseEntity<CarrinhoResponseDTO> processarLimparCarrinho(HttpSession session)
    {
        String sessaoId = session.getId();

        CarrinhoResponseDTO response = carrinhoService.limparCarrinho(sessaoId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar novo pedido")
    public ResponseEntity<PedidoResponseDTO> procesarCriarPedido(@Valid @RequestBody PedidoRequestDTO request)
    {
        PedidoResponseDTO response = pedidoService.criarPedido(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{pedidoId}/subpedidos")
    @Operation(summary = "Criar subpedido a partir do carrinho")
    public ResponseEntity<SubPedidoResponseDTO> processarCriarSubPedido(@PathVariable Long pedidoId, HttpSession session, @Valid @RequestBody SubPedidoRequestDTO request)
    {
        String sessaoId = session.getId();
        String clienteId = sessaoId;

        CarrinhoResponseDTO carrinho = carrinhoService.obterCarrinho(sessaoId);

        if (carrinho.getItens() == null || carrinho.getItens().isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        request.setItens(carrinho.getItens().stream()
                .map(item -> {
                    ItemPedidoRequestDTO itemRequest = new ItemPedidoRequestDTO();
                    itemRequest.setProdutoId(item.getProdutoId());
                    itemRequest.setQuantidade(item.getQuantidade());
                    itemRequest.setObservacao(item.getObservacao());
                    return itemRequest;
                })
                .collect(Collectors.toList()));

        SubPedidoResponseDTO response = subPedidoService.criarSubPedido(request);

        carrinhoService.limparCarrinho(sessaoId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{pedidoId}/entrar")
    @Operation(summary = "Cliente entra em um pedido existente (após compartilhamento de link)")
    public ResponseEntity<SubPedidoResponseDTO> processarEntrarNoPedido(@PathVariable Long pedidoId, HttpSession session, @RequestParam Long clienteId)
    {
        String sessaoId = session.getId();

        PedidoResponseDTO pedidoResponse = pedidoService.buscarPorId(pedidoId);

        if (pedidoResponse == null || !pedidoResponse.getSucesso())
        {
            return ResponseEntity.badRequest().build();
        }

        List<SubPedidoResponseDTO> subPedidos = subPedidoService.listarPorPedidoId(pedidoId);

        Boolean existe = subPedidos.stream().anyMatch(sp -> sp.getClienteID().equals(clienteId));

        if (existe)
        {
            SubPedidoResponseDTO existente = subPedidos.stream()
                    .filter(sp -> sp.getClienteID().equals(clienteId))
                    .findFirst()
                    .orElse(null);
            return ResponseEntity.ok(existente);
        }

        SubPedidoRequestDTO request = new SubPedidoRequestDTO();
        request.setPedidoId(pedidoId);
        request.setClienteId(clienteId);
        request.setItens(new ArrayList<>());

        SubPedidoResponseDTO response = subPedidoService.criarSubPedido(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{pedidoId}")
    @Operation(summary = "Buscar pedido por ID")
    public ResponseEntity<PedidoResponseDTO> processarBuscarPorId(@PathVariable Long pedidoId)
    {
        PedidoResponseDTO response = pedidoService.buscarPorId(pedidoId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/numero/{numeroPedido}")
    @Operation(summary = "Listar todos os pedidos")
    public ResponseEntity<PedidoResponseDTO> processarBuscarPorNumero(@PathVariable String numeroPedido)
    {
        PedidoResponseDTO response = pedidoService.buscarPorNumero(numeroPedido);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> processarListarTodos()
    {
        List<PedidoResponseDTO> response = pedidoService.listarTodos();

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar pedidos por período")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime fim
    )
    {
        List<PedidoResponseDTO> response = pedidoService.listarPorPeriodo(inicio, fim);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/mesa/{mesaId}")
    @Operation(summary = "Listar pedidos ativos por mesa")
    public ResponseEntity<List<PedidoResponseDTO>> listarAtivosPorMesa(@PathVariable Long mesaId)
    {
        List<PedidoResponseDTO> response = pedidoService.listarAtivosPorMesa(mesaId);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/subpedidos/{id}")
    @Operation(summary = "Buscar subpedido por ID")
    public ResponseEntity<SubPedidoResponseDTO> processarBuscarSubPedidoPorId(@PathVariable Long id)
    {
        SubPedidoResponseDTO response = subPedidoService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{pedidoId}/subpedidos")
    @Operation(summary = "Listar todos os subpedidos de um pedido (todos os clientes da mesa)")
    public ResponseEntity<List<SubPedidoResponseDTO>> processarListarSubPedidosPorPedido(@PathVariable Long pedidoId)
    {
        List<SubPedidoResponseDTO> response = subPedidoService.listarPorPedidoId(pedidoId);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/subpedidos/cliente/{clienteId}")
    @Operation(summary = "Buscar subpedido por cliente ID")
    public ResponseEntity<SubPedidoResponseDTO> processarBuscarSubPedidoPorCliente(@PathVariable Long clienteId, @RequestParam Long pedidoId)
    {
        List<SubPedidoResponseDTO> subPedidos = subPedidoService.listarPorPedidoId(pedidoId);

        SubPedidoResponseDTO response = subPedidos.stream()
                .filter(sp -> sp.getClienteID().equals(clienteId))
                .findFirst()
                .orElse(null);

        if (response == null)
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/subpedidos/status/{status}")
    @Operation(summary = "Listar subpedidos por status")
    public ResponseEntity<List<SubPedidoResponseDTO>> processarListarSubPedidosPorStatus(@RequestParam StatusPedido status)
    {
        List<SubPedidoResponseDTO> response = subPedidoService.listarPorStatus(status);

        if (response == null)
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/subpedidos/{id}/confirmar")
    @Operation(summary = "Confirmar subpedido (atendente confirma o pedido do cliente)")
    public ResponseEntity<SubPedidoResponseDTO> processarConfirmarSubPedido(@PathVariable Long id)
    {
        SubPedidoResponseDTO response = subPedidoService.confimarSubPedido(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/subpedidos/{id}/preparar")
    @Operation(summary = "Iniciar preparo do subpedido (cozinha)")
    public ResponseEntity<SubPedidoResponseDTO> processarPrepararSubPedido(@PathVariable Long id)
    {
        SubPedidoResponseDTO response = subPedidoService.prepararSubPedido(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/subpedidos/{id}/pronto")
    @Operation(summary = "Marcar subpedido como pronto")
    public ResponseEntity<SubPedidoResponseDTO> processarMarcarProntoSubPedido(@PathVariable Long id)
    {
        SubPedidoResponseDTO response = subPedidoService.marcarProntoSubPedido(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/subpedidos/{id}/entregar")
    @Operation(summary = "Entregar subpedido")
    public ResponseEntity<SubPedidoResponseDTO> processarEntregarSubPedido(@PathVariable Long id)
    {
        SubPedidoResponseDTO response = subPedidoService.entregarSubPedido(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/subpedidos/{id}/cancelar")
    @Operation(summary = "Marcar subpedido como pronto")
    public ResponseEntity<SubPedidoResponseDTO> processarCancelarSubPedido(@PathVariable Long id)
    {
        SubPedidoResponseDTO response = subPedidoService.cancelarSubPedido(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pedido")
    public ResponseEntity<Void> processarDeletarPedido(@PathVariable Long id)
    {
        pedidoService.deletarPedido(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cardapio/categorias")
    @Operation(summary = "Listar categorias do cardápio")
    public ResponseEntity<List<CategoriaResponseDTO>> processarListarCategoriasCardapio()
    {
        List<CategoriaResponseDTO> response = categoriaService.listarAtivas();

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/cardapio/produtos")
    @Operation(summary = "Listar produtos do carápio")
    public ResponseEntity<List<ProdutoRespondeDTO>> processarListarProdutosCardapio()
    {
        List<ProdutoRespondeDTO> response = produtoService.listarDisponiveis();

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/cardapio/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoRespondeDTO>> processarListarProdutosCardapioPorCategoria(@PathVariable Long categoriaId)
    {
        List<ProdutoRespondeDTO> response = produtoService.listarPorCategoria(categoriaId);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }
}
