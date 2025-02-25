package com.project.suitpay.repositories;

import com.project.suitpay.entities.categorias.Categoria;
import com.project.suitpay.entities.produtos.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByCategoria(Categoria categoria);
}
