package org.frangosInfinity.infrastructure.console.module.produto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.frangosInfinity.application.module.produto.request.CategoriaRequestDTO;
import org.frangosInfinity.application.module.produto.request.ProdutoRequestDTO;
import org.frangosInfinity.application.module.produto.response.CategoriaResponseDTO;
import org.frangosInfinity.application.module.produto.response.ProdutoRespondeDTO;
import org.frangosInfinity.core.service.module.produto.CategoriaService;
import org.frangosInfinity.core.service.module.produto.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produto", description = "Gerenciamento de produtos, cardápio")
public class ProdutoController
{
    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/categorias")
    @Operation(summary = "Criar nova categoria")
    public ResponseEntity<CategoriaResponseDTO> processarCriarCategoria(@Valid @RequestBody CategoriaRequestDTO request)
    {
        CategoriaResponseDTO response = categoriaService.criarCategoria(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/categorias")
    @Operation(summary = "Listar todas as categorias")
    public ResponseEntity<List<CategoriaResponseDTO>> processarListarTodasCategorias()
    {
        List<CategoriaResponseDTO> response = categoriaService.listarTodas();

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/categorias/ativas")
    @Operation(summary = "Listar categorias ativas")
    public ResponseEntity<List<CategoriaResponseDTO>> processarListarCategoriasAtivas()
    {
        List<CategoriaResponseDTO> response = categoriaService.listarAtivas();

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/categorias/{id}")
    @Operation(summary = "Buscar cetegoria por ID")
    public ResponseEntity<CategoriaResponseDTO> processarBuscarCategoriaPorId(@PathVariable Long id)
    {
        CategoriaResponseDTO response = categoriaService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/categorias/{id}")
    @Operation(summary = "Atualizar categoria")
    public ResponseEntity<CategoriaResponseDTO> processarAtualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO request)
    {
        CategoriaResponseDTO response = categoriaService.atualizarCategoria(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/categorias/{id}/ativar")
    @Operation(summary = "ativar categoria")
    public ResponseEntity<CategoriaResponseDTO> processarAtivarCategoria(@PathVariable Long id)
    {
        CategoriaResponseDTO response = categoriaService.ativarCategoria(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/categorias/{id}/desativar")
    @Operation(summary = "Desativar categoria")
    public ResponseEntity<CategoriaResponseDTO> processarDesativarCategoria(@PathVariable Long id)
    {
        CategoriaResponseDTO response = categoriaService.desativarCategoria(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/categorias/{id}")
    @Operation(summary = "Deletar categoria")
    public ResponseEntity<Void> processarDeletarCategoria(@PathVariable Long id)
    {
        categoriaService.deletarCategoria(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Operation(summary = "Criar novo produto")
    public ResponseEntity<ProdutoRespondeDTO> processarCriarProduto(@Valid @RequestBody ProdutoRequestDTO request)
    {
        ProdutoRespondeDTO response = produtoService.criarProduto(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar produtos disponíveis")
    public ResponseEntity<List<ProdutoRespondeDTO>> processarListarProdutosDisponiveis()
    {
        List<ProdutoRespondeDTO> response = produtoService.listarDisponiveis();

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/categorias/{categoriaId}")
    @Operation(summary = "Listar produtos por categoria")
    public ResponseEntity<List<ProdutoRespondeDTO>> processarListarProdutosPorCategoria(@PathVariable Long categoriaId)
    {
        List<ProdutoRespondeDTO> response = produtoService.listarPorCategoria(categoriaId);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/mais-vendidos")
    @Operation(summary = "Listar produtos mais vendidos")
    public ResponseEntity<List<ProdutoRespondeDTO>> processarListarProdutosMaisVendidos()
    {
        List<ProdutoRespondeDTO> response = produtoService.listarMaisVendidos();

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ProdutoRespondeDTO> processarBuscarProdutoPorId(@PathVariable Long id)
    {
        ProdutoRespondeDTO response = produtoService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar produto por código")
    public ResponseEntity<ProdutoRespondeDTO> processarBuscarProdutoPorCodigo(@PathVariable String codigo)
    {
        ProdutoRespondeDTO response = produtoService.buscarPorCodigo(codigo);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ProdutoRespondeDTO> processarAtualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO request)
    {
        ProdutoRespondeDTO response = produtoService.atualizarProduto(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/disponibilidade")
    @Operation(summary = "Alterar disponibilidade do produto")
    public ResponseEntity<ProdutoRespondeDTO> processarAlterarDiponibilidade(@PathVariable Long id, @RequestParam Boolean disponivel)
    {
        ProdutoRespondeDTO response = produtoService.alterarDisponibilidade(id, disponivel);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/aprovar-preco")
    @Operation(summary = "Aprovar alteração de preço pendente")
    public ResponseEntity<ProdutoRespondeDTO> processarAprovarAlteracaoPreco(@PathVariable Long id)
    {
        ProdutoRespondeDTO response = produtoService.aprovarAlteracaoPreco(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto")
    public ResponseEntity<Void> processarDeletarProduto(@PathVariable Long id)
    {
        produtoService.deletarProduto(id);

        return ResponseEntity.noContent().build();
    }
}
