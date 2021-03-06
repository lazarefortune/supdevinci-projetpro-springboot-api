package com.mobilishop.api.repository;

import com.mobilishop.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    @Query("select p from Product p " +
            "where p.name like %?1% " +
            "or p.description like %?1%")
    List<Product> searchProducts(String query);
}
