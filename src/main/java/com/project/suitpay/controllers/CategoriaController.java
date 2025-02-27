package com.project.suitpay.controllers;

import com.project.suitpay.entities.categorias.CategoriaDTO;
import com.project.suitpay.entities.categorias.CategoriaModelAssembler;
import com.project.suitpay.entities.categorias.CategoriaRequest;
import com.project.suitpay.services.CategoriaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
@Validated
public class CategoriaController {

    private final CategoriaService service;
    private final CategoriaModelAssembler assembler;

    public CategoriaController(CategoriaService service, CategoriaModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EntityModel<CategoriaDTO>> criaCategoria(@RequestBody @Valid CategoriaRequest form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(service.criandoCategoria(form)));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CategoriaDTO>>> listarCategorias(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<CategoriaDTO> pagedAssembler
    ) {
        Page<CategoriaDTO> categoriasPage = service.listandoCategorias(nome, page, size);
        PagedModel<EntityModel<CategoriaDTO>> categoias = pagedAssembler.toModel(categoriasPage, assembler);
        return ResponseEntity.ok(categoias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CategoriaDTO>> categoriaPorId(@PathVariable("id") Long id) {
        CategoriaDTO categoria = service.categoriaEspecifica(id);
        return ResponseEntity.ok(assembler.toModel(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CategoriaDTO>> atualizarCategoria(@RequestBody @Valid CategoriaRequest form, @PathVariable("id") Long id) {
        CategoriaDTO categoria = service.atualizarCategoria(form, id);
        return ResponseEntity.ok(assembler.toModel(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaCategoria(@PathVariable("id") Long id) {
        service.eliminaCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
