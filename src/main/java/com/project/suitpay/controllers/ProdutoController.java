package com.project.suitpay.controllers;

import com.project.suitpay.entities.produtos.ProdutoRequest;
import com.project.suitpay.entities.produtos.ProdutoResponse;
import com.project.suitpay.services.ProdutoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping("/produtos")
    @Transactional
    public ResponseEntity<ProdutoResponse> novoProduto(@RequestBody @Valid ProdutoRequest form) {
        return ResponseEntity.ok(service.criarProduto(form));
    }

    @GetMapping("/listando-produtos")
    public ResponseEntity<Page<ProdutoResponse>> listandoProdutos(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listadoProdutos(page, size));
    }

    @GetMapping("/produtos-ordenados")
    public ResponseEntity<Page<ProdutoResponse>> listaOrdenadaPorQuantidade(
            @RequestParam(defaultValue = "asc") String ordem,
            @RequestParam(defaultValue = "quantidade") String campo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {


        Page<ProdutoResponse> produtos = service.listaOrdenada(ordem, campo, page, size);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<ProdutoResponse> produtoPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.produtoPorId(id));
    }

    @GetMapping("/produtos-filtrados")
    public ResponseEntity<List<ProdutoResponse>> filtrandoProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(required = false) String nomeCategoria,
            @RequestParam(required = false) Long idCategoria) {
        return ResponseEntity.ok(service.filtrandoProdutos(nome,descricao, precoMin, precoMax, nomeCategoria, idCategoria));

    }

    @PutMapping("/atualiza-produto/{id}")
    public ResponseEntity<ProdutoResponse> atualizarProduto(@RequestBody @Valid ProdutoRequest form, @PathVariable("id") Long id) {
        ProdutoResponse response = new ProdutoResponse(service.atualizaProduto(form, id));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleta-produto/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable("id") Long id) {
        service.deleteProduto(id);
        return ResponseEntity.noContent().build();
    }
}
