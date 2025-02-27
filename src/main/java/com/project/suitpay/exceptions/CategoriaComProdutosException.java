package com.project.suitpay.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoriaComProdutosException extends RuntimeException{
    public CategoriaComProdutosException() {
        super("Não é possível excluir a categoria, pois existem produtos associados.");
    }
}
