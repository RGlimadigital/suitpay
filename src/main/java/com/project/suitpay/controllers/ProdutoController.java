package com.project.suitpay.controllers;

import com.project.suitpay.entities.produtos.ProdutoRequest;
import com.project.suitpay.entities.produtos.ProdutoResponse;
import com.project.suitpay.services.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdutoController {


    private ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping("/produtos")
    public ResponseEntity<ProdutoResponse> novoProduto(@RequestBody ProdutoRequest form){
        ProdutoResponse response = new ProdutoResponse(service.criaProduto(form));
        return ResponseEntity.ok(response);
    }

}
