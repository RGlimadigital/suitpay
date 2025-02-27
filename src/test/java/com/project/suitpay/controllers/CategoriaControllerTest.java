package com.project.suitpay.controllers;

import com.project.suitpay.entities.categorias.CategoriaDTO;
import com.project.suitpay.entities.categorias.CategoriaModelAssembler;
import com.project.suitpay.entities.categorias.CategoriaRequest;
import com.project.suitpay.services.CategoriaService;
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
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private CategoriaModelAssembler assembler;

    @Mock
    private PagedResourcesAssembler<CategoriaDTO> pagedResourcesAssembler;

    @InjectMocks
    private CategoriaController categoriaController;

    private CategoriaDTO categoriaDTO;
    private CategoriaRequest categoriaRequest;
    private EntityModel<CategoriaDTO> categoriaModel;
    private PagedModel<EntityModel<CategoriaDTO>> pagedModel;

    @BeforeEach
    void setUp() {

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setNome("Categoria Teste");

        categoriaRequest = new CategoriaRequest("Categoria Teste");

        categoriaModel = EntityModel.of(categoriaDTO);

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(10, 0, 1);
        pagedModel = PagedModel.of(Arrays.asList(categoriaModel), metadata);
    }

    @Test
    void testCriaCategoria() throws Exception {
        when(categoriaService.criandoCategoria(any(CategoriaRequest.class))).thenReturn(categoriaDTO);
        when(assembler.toModel(any(CategoriaDTO.class))).thenReturn(categoriaModel);

        ResponseEntity<EntityModel<CategoriaDTO>> responseEntity = categoriaController.criaCategoria(categoriaRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(categoriaModel, responseEntity.getBody());

        verify(categoriaService, times(1)).criandoCategoria(any(CategoriaRequest.class));
        verify(assembler, times(1)).toModel(any(CategoriaDTO.class));
    }


    @Test
    void listarCategorias() {
        Page<CategoriaDTO> categoriasPage = new PageImpl<>(List.of(categoriaDTO));
        when(categoriaService.listandoCategorias(anyString(), anyInt(), anyInt())).thenReturn(categoriasPage);
        when(pagedResourcesAssembler.toModel(any(Page.class),
                any(CategoriaModelAssembler.class))).thenReturn(pagedModel);
        ResponseEntity<PagedModel<EntityModel<CategoriaDTO>>> responseEntity = categoriaController
                .listarCategorias("Categoria Teste", 0, 10, pagedResourcesAssembler);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(pagedModel, responseEntity.getBody());
        verify(categoriaService, times(1)).listandoCategorias(anyString(), anyInt(), anyInt());
        verify(pagedResourcesAssembler, times(1)).toModel(any(Page.class),
                any(CategoriaModelAssembler.class));
    }

    @Test
    void categoriaPorId() {
        when(categoriaService.categoriaEspecifica(anyLong())).thenReturn(categoriaDTO);
        when(assembler.toModel(any(CategoriaDTO.class))).thenReturn(categoriaModel);

        ResponseEntity<EntityModel<CategoriaDTO>> responseEntity = categoriaController.categoriaPorId(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(categoriaModel, responseEntity.getBody());
        verify(categoriaService, times(1)).categoriaEspecifica(anyLong());
        verify(assembler, times(1)).toModel(any(CategoriaDTO.class));
    }

    @Test
    void atualizarCategoria() {
        when(categoriaService.atualizarCategoria(any(CategoriaRequest.class), anyLong())).thenReturn(categoriaDTO);
        when(assembler.toModel(any(CategoriaDTO.class))).thenReturn(categoriaModel);

        ResponseEntity<EntityModel<CategoriaDTO>> responseEntity = categoriaController.atualizarCategoria(categoriaRequest, 1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(categoriaModel, responseEntity.getBody());
        verify(categoriaService, times(1)).atualizarCategoria(any(CategoriaRequest.class), eq(1L));
        verify(assembler, times(1)).toModel(any(CategoriaDTO.class));
    }

    @Test
    void deletaCategoria() {
        doNothing().when(categoriaService).eliminaCategoria(anyLong());
        ResponseEntity<Void> responseEntity = categoriaController.deletaCategoria(1L);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        verify(categoriaService, times(1)).eliminaCategoria(anyLong());
    }
}