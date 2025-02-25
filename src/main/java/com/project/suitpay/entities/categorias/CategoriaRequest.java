package com.project.suitpay.entities.categorias;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(
        @NotBlank
        String nome
) {

    public Categoria toModel() {
        return new Categoria(nome);
    }
}
