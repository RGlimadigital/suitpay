package com.project.suitpay.controllers;

import com.project.suitpay.entities.produtos.ProdutoDTO;
import com.project.suitpay.entities.produtos.ProdutoModelAssembler;
import com.project.suitpay.entities.produtos.ProdutoRequest;
import com.project.suitpay.services.ProdutoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
@Validated
public class ProdutoController {

    private final ProdutoService service;
    private final ProdutoModelAssembler assembler;

    public ProdutoController(ProdutoService service, ProdutoModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EntityModel<ProdutoDTO>> novoProduto(@RequestBody @Valid ProdutoRequest form) {
        return ResponseEntity.ok(assembler.toModel(service.criarProduto(form)));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProdutoDTO>>> listarProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(required = false) String nomeCategoria,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            PagedResourcesAssembler<ProdutoDTO> pagedAssembler) {


        Page<ProdutoDTO> produtosPage = service.listarProdutos(nome, descricao, precoMin, precoMax, nomeCategoria, idCategoria, page, size, sort, direction);
        PagedModel<EntityModel<ProdutoDTO>> pagedModel = pagedAssembler.toModel(produtosPage, assembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProdutoDTO>> produtoPorId(@PathVariable("id") Long id) {
        ProdutoDTO produto = service.produtoPorId(id);
        return ResponseEntity.ok(assembler.toModel(produto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProdutoDTO>> atualizarProduto(@RequestBody @Valid ProdutoRequest form, @PathVariable("id") Long id) {
        ProdutoDTO produto = new ProdutoDTO(service.atualizaProduto(form, id));
        return ResponseEntity.ok(assembler.toModel(produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable("id") Long id) {
        service.deleteProduto(id);
        return ResponseEntity.noContent().build();
    }
}
