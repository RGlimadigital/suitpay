package com.project.suitpay.services;

import com.project.suitpay.entities.categorias.Categoria;
import com.project.suitpay.entities.categorias.CategoriaDTO;
import com.project.suitpay.entities.categorias.CategoriaRequest;
import com.project.suitpay.exceptions.CategoriaComProdutosException;
import com.project.suitpay.exceptions.CategoriaNaoEncontradaException;
import com.project.suitpay.repositories.CategoriaRepository;
import com.project.suitpay.repositories.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final ProdutoRepository produtoRepository;

    public CategoriaService(CategoriaRepository repository, ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }


    public CategoriaDTO criandoCategoria(CategoriaRequest cat) {
        Categoria categoria = repository.save(cat.toModel());
        return new CategoriaDTO(categoria);
    }

    public Page<CategoriaDTO> listandoCategorias(String nome, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Categoria> categorias = (nome == null || nome.isEmpty())
                ? repository.findAll(pageable)
                : repository.findByNomeContainingIgnoreCase(nome, pageable);

        return categorias.map(CategoriaDTO::new);
    }


    public CategoriaDTO categoriaEspecifica(Long id) {
        return new CategoriaDTO(categoriasPorId(id));
    }

    public Categoria categoriasPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(id));
    }

    public CategoriaDTO atualizarCategoria(CategoriaRequest form, Long id) {
        Categoria categoria = categoriasPorId(id);
        categoria.setNome(form.nome());
        return new CategoriaDTO(repository.save(categoria));
    }

    public void eliminaCategoria(Long id) {
        Categoria categoria = categoriasPorId(id);
        categoriaAssociadas(categoria);
        repository.delete(categoria);
    }

    private void categoriaAssociadas(Categoria categoria) {
        boolean possuiProdutos = produtoRepository.existsByCategoria(categoria);
        if (possuiProdutos) {
            throw new CategoriaComProdutosException();
        }
    }
}
