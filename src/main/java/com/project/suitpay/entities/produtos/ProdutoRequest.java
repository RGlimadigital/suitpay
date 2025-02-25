package com.project.suitpay.entities.produtos;

import com.project.suitpay.entities.categorias.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;

public record ProdutoRequest(
        @NotBlank
        String nome,
        Double preco,
        String descricao,
        Integer quantidade,
        @NotNull
        Long categoraiId

) {

    public Produto toModel(Categoria categoria){

        return new Produto(nome, preco, descricao, quantidade, categoria);
    }
}
