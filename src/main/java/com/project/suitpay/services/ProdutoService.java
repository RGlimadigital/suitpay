package com.project.suitpay.services;

import com.project.suitpay.entities.categorias.Categoria;
import com.project.suitpay.entities.produtos.Produto;
import com.project.suitpay.entities.produtos.ProdutoDTO;
import com.project.suitpay.entities.produtos.ProdutoRequest;
import com.project.suitpay.repositories.CategoriaRepository;
import com.project.suitpay.repositories.ProdutoRepository;
import com.project.suitpay.specifications.ProdutoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public ProdutoDTO criarProduto(ProdutoRequest produtoRequest) {
        Categoria categoria = encontrarCategoria(produtoRequest.categoraiId());
        Produto produto = repository.save(produtoRequest.toModel(categoria));
        return new ProdutoDTO(produto);
    }

    public Page<ProdutoDTO> listadoProdutos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .map(ProdutoDTO::new);
    }

    public Page<ProdutoDTO> listaOrdenada(String ordem, String campo, int page, int size) {
        Sort.Direction direction = Sort.Direction.fromString(ordem);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, campo));
        return repository.findAll(pageable).map(ProdutoDTO::new);
    }

    public ProdutoDTO produtoPorId(Long id) {
        Produto produto = encontrandoProduto(id);
        return new ProdutoDTO(produto);
    }

    public Produto atualizaProduto(ProdutoRequest form, Long id) {
        Produto produto = encontrandoProduto(id);
        Categoria categoria = encontrarCategoria(form.categoraiId());
        atualizaParametros(produto, form, categoria);
        return repository.save(produto);
    }

    public Page<ProdutoDTO> filtrandoProdutos(String nome, String descricao,
                                              Double precoMin, Double precoMax, String nomeCategoria, Long idCategoria,
                                              int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Produto> spec = Specification.where(null);

        if (descricao != null && !descricao.isEmpty()) spec = spec.and(ProdutoSpecification.descricaoLike(descricao));
        if (nomeCategoria != null) spec = spec.and(ProdutoSpecification.categoriaNomeLike(nomeCategoria));
        if (nome != null && !nome.isEmpty()) spec = spec.and(ProdutoSpecification.nomeLike(nome));
        if (idCategoria != null) spec = spec.and(ProdutoSpecification.categoriaId(idCategoria));
        if (precoMin != null) spec = spec.and(ProdutoSpecification.precoMaiorQue(precoMin));
        if (precoMax != null) spec = spec.and(ProdutoSpecification.precoMenorQue(precoMax));

        return repository.findAll(spec, pageable).map(ProdutoDTO::new);

    }

    public void deleteProduto(Long id) {
        Produto produto = encontrandoProduto(id);
        repository.delete(produto);
    }

    private Categoria encontrarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    private void atualizaParametros(Produto produto, ProdutoRequest form, Categoria categoria) {
        produto.setNome(form.nome());
        produto.setPreco(form.preco());
        produto.setDescricao(form.descricao());
        produto.setQuantidade(form.quantidade());
        produto.setCategoria(categoria);
    }

    private Produto encontrandoProduto(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
}
