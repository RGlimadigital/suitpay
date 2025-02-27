package com.project.suitpay.services;

import com.project.suitpay.entities.categorias.Categoria;
import com.project.suitpay.entities.produtos.Produto;
import com.project.suitpay.entities.produtos.ProdutoDTO;
import com.project.suitpay.entities.produtos.ProdutoRequest;
import com.project.suitpay.exceptions.CategoriaNaoEncontradaException;
import com.project.suitpay.exceptions.ProdutoNaoEncontradoException;
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

import java.util.List;

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


    public Page<ProdutoDTO> listarProdutos(String nome, String descricao, Double precoMin,
                                           Double precoMax, String nomeCategoria, Long idCategoria,
                                           int page, int size, String sort, String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        final Specification<Produto> spec = controleFiltros(nome, descricao, precoMin, precoMax, nomeCategoria, idCategoria);
        return repository.findAll(spec, pageable).map(ProdutoDTO::new);
    }


    public void deleteProduto(Long id) {
        Produto produto = encontrandoProduto(id);
        repository.delete(produto);
    }

    public List<ProdutoDTO> obterProdutosPorCategoria(Long categoriaId){
        Categoria categoria = encontrarCategoria(categoriaId);
        return repository.findByCategoria(categoria).stream().map(ProdutoDTO::new).toList();
    }

    private Categoria encontrarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    private void atualizaParametros(Produto produto, ProdutoRequest form, Categoria categoria) {
        produto.setNome(form.nome());
        produto.setPreco(form.preco());
        produto.setDescricao(form.descricao());
        produto.setQuantidade(form.quantidade());
        produto.setCategoria(categoria);
    }

    private static Specification<Produto> controleFiltros(String nome, String descricao,
                                                          Double precoMin, Double precoMax,
                                                          String nomeCategoria, Long idCategoria) {

        return Specification
                .where(isNotEmpty(nome) ? ProdutoSpecification.nomeLike(nome) : null)
                .and(isNotEmpty(nomeCategoria) ? ProdutoSpecification.categoriaNomeLike(nomeCategoria) : null)
                .and(isNotEmpty(descricao) ? ProdutoSpecification.descricaoLike(descricao) : null)
                .and(idCategoria != null ? ProdutoSpecification.categoriaId(idCategoria) : null)
                .and(precoMax != null ? ProdutoSpecification.precoMenorQue(precoMax) : null)
                .and(precoMin != null ? ProdutoSpecification.precoMaiorQue(precoMin): null);

    }

    private static boolean isNotEmpty(String str){
        return str !=null && !str.isEmpty();
    }

    private Produto encontrandoProduto(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(id));
    }
}
