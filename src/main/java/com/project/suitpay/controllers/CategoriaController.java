package com.project.suitpay.controllers;

import com.project.suitpay.entities.categorias.Categoria;
import com.project.suitpay.entities.categorias.CategoriaRequest;
import com.project.suitpay.entities.categorias.CategoriaResponse;
import com.project.suitpay.services.CategoriaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping("/categorias")
    @Transactional
    public ResponseEntity<CategoriaResponse> criaCategoria(@RequestBody @Valid CategoriaRequest form) {
        return ResponseEntity.ok(service.criandoCategoria(form));
    }

    @GetMapping("/listando-categorias")
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        return ResponseEntity.ok(service.listandoCategorias());
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<CategoriaResponse> categoriaPorId(@PathVariable("id") Long id) {
        CategoriaResponse categoriaResponse = new CategoriaResponse(service.categoriasPorId(id));
        return ResponseEntity.ok(categoriaResponse);
    }

    @PutMapping("/atualiza-categoria/{id}")
    public ResponseEntity<CategoriaResponse> atualizarCategoria(@RequestBody @Valid CategoriaRequest form, @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.atualizarCategoria(form, id));
    }

    @DeleteMapping("/elimina-categoria/{id}")
    public ResponseEntity<Void> deletaCategoria(@PathVariable("id") Long id) {
        service.eliminaCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
