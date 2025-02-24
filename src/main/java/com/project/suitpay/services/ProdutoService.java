package com.project.suitpay.services;

import com.project.suitpay.entities.categorias.Categoria;
import com.project.suitpay.entities.produtos.Produto;
import com.project.suitpay.entities.produtos.ProdutoRequest;
import com.project.suitpay.entities.produtos.ProdutoResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    @PersistenceContext
   private EntityManager manager;

    public Produto criaProduto(ProdutoRequest produtoRequest) {
        Produto produto = produtoRequest.toModel(manager);
        manager.persist(produto);
        return produto;
    }
}
