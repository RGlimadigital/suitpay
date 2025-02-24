package com.project.suitpay.entities.produtos;

import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;

public record ProdutoResponse(
        @NotBlank
        String nome,
        Double preco,
        String descricao,
        Integer quantidade,
        @NotNull
        String categoraiId
) {

    public ProdutoResponse(Produto produto) {
        this(
                produto.getNome(),
                produto.getPreco(),
                produto.getDescricao(),
                produto.getQuantidade(),
                produto.getCategoria().getId().toString()
        );
    }
}
