package com.project.suitpay.specifications;

import com.project.suitpay.entities.produtos.Produto;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ProdutoSpecification {
    public static Specification<Produto> nomeLike(String nome) {
        return (root, query, cb) ->
                cb.like(root.get("nome"), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Produto> descricaoLike(String descricao) {
        return (root, query, cb) ->
                cb.like(root.get("descricao"), "%" + descricao.toLowerCase() + "%");
    }

    public static Specification<Produto> categoriaId(Long categoriaId) {
        return (root, query, cb) ->
                cb.equal(root.get("categoria").get("id"), categoriaId);
    }

    public static Specification<Produto> precoMaiorQue(Double preco) {
        return (root, query, cb) ->
                cb.greaterThan(root.get("preco"), preco);
    }

    public static Specification<Produto> precoMenorQue(Double preco) {
        return (root, query, cb) ->
                cb.lessThan(root.get("preco"), preco);
    }

    public static Specification<Produto> categoriaNomeLike(String nome) {
        return (root, query, cb) -> {
            Join<Object, Object> joinCategoria = root.join("categoria", JoinType.INNER);
            return cb.like(joinCategoria.get("nome"), "%" + nome.toLowerCase() + "%");
        };
    }
}
