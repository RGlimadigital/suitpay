package com.project.suitpay.controllers;

import com.project.suitpay.entities.produtos.Produto;
import com.project.suitpay.entities.produtos.ProdutoRequest;
import com.project.suitpay.entities.produtos.ProdutoResponse;
import com.project.suitpay.services.ProdutoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<ProdutoResponse>> listandoProdutos() {
        return ResponseEntity.ok(service.listadoProdutos());
    }

    @GetMapping("/produtos-ordenados")
    public ResponseEntity<List<Produto>> listaOrdenadaPorQuantidade(
            @RequestParam(defaultValue = "asc") String ordem,
            @RequestParam(defaultValue = "quantidade") String campo) {
        return ResponseEntity.ok(service.listaOrdenada(ordem, campo));
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<ProdutoResponse> produtoPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.produtoPorId(id));
    }

    @GetMapping("/produtos-filtrados")
    public ResponseEntity<List<ProdutoResponse>> filtrandoProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(required = false) String nomeCategoria,
            @RequestParam(required = false) Long idCategoria) {
        return ResponseEntity.ok(service.filtrandoProdutos(nome, precoMin, precoMax, nomeCategoria, idCategoria));

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
