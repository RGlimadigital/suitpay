package com.project.suitpay.controllers;

import com.project.suitpay.entities.categorias.Categoria;
import com.project.suitpay.entities.produtos.Produto;
import com.project.suitpay.entities.produtos.ProdutoDTO;
import com.project.suitpay.entities.produtos.ProdutoModelAssembler;
import com.project.suitpay.entities.produtos.ProdutoRequest;
import com.project.suitpay.services.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @Mock
    private ProdutoModelAssembler assembler;

    @Mock
    private PagedResourcesAssembler<ProdutoDTO> pagedResourcesAssembler;

    @InjectMocks
    private ProdutoController produtoController;

    private ProdutoRequest produtoRequest;
    private ProdutoDTO produtoDTO;
    private Categoria categoria;
    private EntityModel<ProdutoDTO> produtoDTOModel;
    private List<ProdutoDTO> produtoDTOList;
    private Page<ProdutoDTO> produtoDTOPage;
    private PagedModel<EntityModel<ProdutoDTO>> pagedModel;

    @BeforeEach
    void setUp() {
        produtoRequest = new ProdutoRequest("Produto Teste", 99.90,
                "Descrição do produto teste", 10, 2L);


        produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        produtoDTO.setNome("Produto Teste");
        produtoDTO.setDescricao("Descrição do produto teste");
        produtoDTO.setQuantidade(10);
        produtoDTO.setPreco(99.90);

        produtoDTOModel = EntityModel.of(produtoDTO);
        produtoDTOModel.add(Link.of("/produtos/1").withSelfRel());

        produtoDTOList = Arrays.asList(produtoDTO);
        produtoDTOPage = new PageImpl<>(produtoDTOList);

        categoria = new Categoria();

        categoria.setId(2L);
        categoria.setNome("Categoria Teste");

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(10, 0, 1);
        pagedModel = PagedModel.of(Arrays.asList(produtoDTOModel), metadata);
    }

    @Test
    void testNovoProduto() {
        when(produtoService.criarProduto(any(ProdutoRequest.class))).thenReturn(produtoDTO);
        when(assembler.toModel(any(ProdutoDTO.class))).thenReturn(produtoDTOModel);

        ResponseEntity<EntityModel<ProdutoDTO>> responseEntity = produtoController.novoProduto(produtoRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(produtoDTOModel, responseEntity.getBody());
        verify(produtoService, times(1)).criarProduto(any(ProdutoRequest.class));
        verify(assembler, times(1)).toModel(any(ProdutoDTO.class));
    }

    @Test
    void testListarProdutos() {
        when(produtoService.listarProdutos(
                anyString(), anyString(), anyDouble(), anyDouble(), anyString(), anyLong(),
                anyInt(), anyInt(), anyString(), anyString()
        )).thenReturn(produtoDTOPage);

        when(pagedResourcesAssembler.toModel(any(Page.class), any(ProdutoModelAssembler.class)))
                .thenReturn(pagedModel);

        ResponseEntity<PagedModel<EntityModel<ProdutoDTO>>> responseEntity = produtoController.listarProdutos(
                "nome", "descricao", 10.0, 100.0,
                "categoria", 1L, 0, 10, "id", "asc", pagedResourcesAssembler
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(pagedModel, responseEntity.getBody());
        verify(produtoService, times(1)).listarProdutos(
                eq("nome"), eq("descricao"), eq(10.0), eq(100.0),
                eq("categoria"), eq(1L), eq(0), eq(10), eq("id"), eq("asc")
        );
        verify(pagedResourcesAssembler, times(1)).toModel(any(Page.class), any(ProdutoModelAssembler.class));
    }


    @Test
    void testProdutoPorId() {
        when(produtoService.produtoPorId(anyLong())).thenReturn(produtoDTO);
        when(assembler.toModel(any(ProdutoDTO.class))).thenReturn(produtoDTOModel);

        ResponseEntity<EntityModel<ProdutoDTO>> responseEntity = produtoController.produtoPorId(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(produtoDTOModel, responseEntity.getBody());
        verify(produtoService, times(1)).produtoPorId(1L);
        verify(assembler, times(1)).toModel(any(ProdutoDTO.class));
    }

    @Test
    void testAtualizarProduto() {
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setCategoria(categoria);

        when(produtoService.atualizaProduto(any(ProdutoRequest.class), anyLong())).thenReturn(produtoAtualizado);
        when(assembler.toModel(any(ProdutoDTO.class))).thenReturn(produtoDTOModel);

        ResponseEntity<EntityModel<ProdutoDTO>> responseEntity = produtoController.atualizarProduto(produtoRequest, 1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(produtoDTOModel, responseEntity.getBody());
        verify(produtoService, times(1)).atualizaProduto(any(ProdutoRequest.class), eq(1L));
        verify(assembler, times(1)).toModel(any(ProdutoDTO.class));
    }

    @Test
    void testDeletarProduto() {
        doNothing().when(produtoService).deleteProduto(anyLong());

        ResponseEntity<Void> responseEntity = produtoController.deletarProduto(1L);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        verify(produtoService, times(1)).deleteProduto(1L);
    }
}