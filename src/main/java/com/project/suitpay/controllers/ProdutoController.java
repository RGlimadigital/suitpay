package com.project.suitpay.controllers;

import com.project.suitpay.entities.produtos.ProdutoDTO;
import com.project.suitpay.entities.produtos.ProdutoModelAssembler;
import com.project.suitpay.entities.produtos.ProdutoRequest;
import com.project.suitpay.entities.produtos.ProdutoResponse;
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

import java.util.List;

@RestController
@Validated
public class ProdutoController {

    private final ProdutoService service;
    private final ProdutoModelAssembler assembler;

    public ProdutoController(ProdutoService service, ProdutoModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping("/produtos")
    @Transactional
    public ResponseEntity<EntityModel<ProdutoDTO>> novoProduto(@RequestBody @Valid ProdutoRequest form) {
        return ResponseEntity.ok(assembler.toModel(service.criarProduto(form)));
    }

    @GetMapping("/listando-produtos")
    public ResponseEntity<CollectionModel<EntityModel<ProdutoDTO>>> listandoProdutos(@RequestParam(defaultValue = "0") int page,
                                                                                     @RequestParam(defaultValue = "10") int size,
                                                                                     PagedResourcesAssembler<ProdutoDTO> pagedAssembler) {
        Page<ProdutoDTO> produtosPage = service.listadoProdutos(page, size);
        PagedModel<EntityModel<ProdutoDTO>> pagedModel = pagedAssembler.toModel(produtosPage, assembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/produtos-ordenados")
    public ResponseEntity<CollectionModel<EntityModel<ProdutoDTO>>> listaOrdenadaPorQuantidade(
            @RequestParam(defaultValue = "asc") String ordem,
            @RequestParam(defaultValue = "quantidade") String campo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<ProdutoDTO> pagedAssembler
    ) {


        Page<ProdutoDTO> produtos = service.listaOrdenada(ordem, campo, page, size);
        PagedModel<EntityModel<ProdutoDTO>> pagedModel = pagedAssembler.toModel(produtos, assembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<EntityModel<ProdutoDTO>> produtoPorId(@PathVariable("id") Long id) {
        ProdutoDTO produto = service.produtoPorId(id);
        return ResponseEntity.ok(assembler.toModel(produto));
    }

    @GetMapping("/produtos-filtrados")
    public ResponseEntity<CollectionModel<EntityModel<ProdutoDTO>>> filtrandoProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(required = false) String nomeCategoria,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<ProdutoDTO> pagedAssembler) {

        Page<ProdutoDTO> produtos = service.filtrandoProdutos(nome, descricao, precoMin, precoMax, nomeCategoria, idCategoria, page, size);
        PagedModel<EntityModel<ProdutoDTO>> result = pagedAssembler.toModel(produtos, assembler);


        return ResponseEntity.ok(result);

    }

    @PutMapping("/atualiza-produto/{id}")
    public ResponseEntity<EntityModel<ProdutoDTO>> atualizarProduto(@RequestBody @Valid ProdutoRequest form, @PathVariable("id") Long id) {
        ProdutoDTO produto = new ProdutoDTO(service.atualizaProduto(form, id));
        return ResponseEntity.ok(assembler.toModel(produto));
    }

    @DeleteMapping("/deleta-produto/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable("id") Long id) {
        service.deleteProduto(id);
        return ResponseEntity.noContent().build();
    }
}
