package com.mobilishop.api.repository;

import com.mobilishop.api.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByProductId(Long productId);
}
