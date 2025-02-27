package com.project.suitpay.entities.produtos;

import com.project.suitpay.controllers.ProdutoController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ProdutoModelAssembler implements RepresentationModelAssembler<ProdutoDTO, EntityModel<ProdutoDTO>>{
    @Override
    public EntityModel<ProdutoDTO> toModel(ProdutoDTO produto) {
        return EntityModel.of(produto,
                linkTo(methodOn(ProdutoController.class).produtoPorId(produto.getId())).withSelfRel(),
                linkTo(methodOn(ProdutoController.class).atualizarProduto(null, produto.getId())).withRel("update"),
                linkTo(methodOn(ProdutoController.class).deletarProduto(produto.getId())).withRel("delete")
        );
    }
}
