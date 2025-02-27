package com.project.suitpay.entities.produtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProdutoDTO  {
    @NotBlank
    private final Long id;
    private final String nome;
    private final Double preco;
    private final String descricao;
    private final Integer quantidade;
    @NotNull
    private final String categoriaId;

    public ProdutoDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.preco = produto.getPreco();
        this.descricao = produto.getDescricao();
        this.quantidade = produto.getQuantidade();
        this.categoriaId = produto.getCategoria().getId().toString();
    }


    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Double getPreco() {
        return preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getCategoriaId() {
        return categoriaId;
    }
}
