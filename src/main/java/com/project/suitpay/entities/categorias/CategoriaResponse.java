package com.project.suitpay.entities.categorias;

public record CategoriaResponse(
        String nome
) {
    public CategoriaResponse(Categoria cat){
        this(
               cat.getNome()
        );
    }
}
