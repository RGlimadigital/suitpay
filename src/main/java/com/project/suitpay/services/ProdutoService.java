package com.project.suitpay.services;

import com.project.suitpay.entities.categorias.Categoria;
import com.project.suitpay.entities.produtos.Produto;
import com.project.suitpay.entities.produtos.ProdutoRequest;
import com.project.suitpay.entities.produtos.ProdutoResponse;
import com.project.suitpay.repositories.CategoriaRepository;
import com.project.suitpay.repositories.ProdutoRepository;
import com.project.suitpay.specifications.ProdutoSpecification;
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

    public ProdutoResponse criarProduto(ProdutoRequest produtoRequest) {
        Categoria categoria = encontrarCategoria(produtoRequest.categoraiId());
        Produto produto = repository.save(produtoRequest.toModel(categoria));
        return new ProdutoResponse(produto);
    }

    public List<ProdutoResponse> listadoProdutos() {
        return repository.findAll().stream()
                .map(ProdutoResponse::new)
                .toList();
    }

    public List<Produto> listaOrdenada(String ordem, String campo) {
        return repository.findAll(Sort.by(Sort.Direction.fromString(ordem), campo));
    }

    public ProdutoResponse produtoPorId(Long id) {
        Produto produto = encontrandoProduto(id);
        return new ProdutoResponse(produto);
    }

    public Produto atualizaProduto(ProdutoRequest form, Long id) {
        Produto produto = encontrandoProduto(id);
        Categoria categoria = encontrarCategoria(form.categoraiId());
        atualizaParametros(produto, form, categoria);
        return repository.save(produto);
    }

    public List<ProdutoResponse> filtrandoProdutos(String nome, Double precoMin , Double precoMax, String nomeCategoria, Long idCategoria ){
        Specification<Produto> spec = Specification.where(null);
        if (nome!= null && !nome.isEmpty()) spec = spec.and(ProdutoSpecification.nomeLike(nome));
        if(precoMin!= null) spec = spec.and(ProdutoSpecification.precoMaiorQue(precoMin));
        if(precoMax!= null) spec = spec.and(ProdutoSpecification.precoMenorQue(precoMax));
        if(nomeCategoria != null) spec =  spec.and(ProdutoSpecification.categoriaNomeLike(nomeCategoria));
        if(idCategoria != null) spec = spec.and(ProdutoSpecification.categoriaId(idCategoria));
        return repository.findAll(spec).stream().map(ProdutoResponse::new).toList();

    }

    public void deleteProduto(Long id) {
        Produto produto = encontrandoProduto(id);
        repository.deleteById(id);
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
