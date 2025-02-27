package com.project.suitpay.entities.categorias;

import com.project.suitpay.controllers.CategoriaController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaDTO, EntityModel<CategoriaDTO>> {
    @Override
    public EntityModel<CategoriaDTO> toModel(CategoriaDTO categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaController.class).categoriaPorId(categoria.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).atualizarCategoria(null, categoria.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).deletaCategoria(categoria.getId())).withSelfRel()
                );
    }
}
