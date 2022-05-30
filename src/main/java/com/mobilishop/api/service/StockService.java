package com.mobilishop.api.service;

import com.mobilishop.api.model.Stock;

import java.util.List;

public interface StockService {
    Stock findByProductId(Long productId);

    Stock findByProductName(String productName);

    Stock createOne(Stock stock);

    Stock updateOne(Stock stock);

    void deleteOne(Long id);

    List<Stock> findAllStocks();

}
