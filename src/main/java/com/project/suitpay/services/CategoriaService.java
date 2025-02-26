package com.project.suitpay.services;

import com.project.suitpay.entities.categorias.Categoria;
import com.project.suitpay.entities.categorias.CategoriaRequest;
import com.project.suitpay.entities.categorias.CategoriaResponse;
import com.project.suitpay.repositories.CategoriaRepository;
import com.project.suitpay.repositories.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final ProdutoRepository produtoRepository;

    public CategoriaService(CategoriaRepository repository, ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }


    public CategoriaResponse criandoCategoria(CategoriaRequest cat) {
        Categoria categoria = repository.save(cat.toModel());
        return new CategoriaResponse(categoria);
    }

    public List<CategoriaResponse> listandoCategorias() {
        return repository.findAll().stream().map(CategoriaResponse::new).toList();
    }

    public Categoria categoriasPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    public CategoriaResponse atualizarCategoria(CategoriaRequest form, Long id) {
        Categoria categoria = categoriasPorId(id);
        categoria.setNome(form.nome());
        repository.save(categoria);
        return new CategoriaResponse(categoria);
    }

    public void eliminaCategoria(Long id) {
        Categoria categoria = categoriasPorId(id);
        categoriaAssociadas(categoria);
        repository.delete(categoria);
    }

    private void categoriaAssociadas(Categoria categoria) {
        boolean possuiProdutos = produtoRepository.existsByCategoria(categoria);
        if (possuiProdutos) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir a categoria, pois existem produtos associados");
        }
    }
}
