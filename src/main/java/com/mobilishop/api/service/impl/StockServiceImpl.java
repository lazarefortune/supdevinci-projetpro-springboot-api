package com.mobilishop.api.service.impl;

import com.mobilishop.api.exception.ApiRequestException;
import com.mobilishop.api.model.Product;
import com.mobilishop.api.model.Stock;
import com.mobilishop.api.repository.ProductRepository;
import com.mobilishop.api.repository.StockRepository;
import com.mobilishop.api.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Stock findByProductId(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ApiRequestException("Product not found", HttpStatus.NOT_FOUND);
        }
        Stock stock = stockRepository.findByProductId(product.getId());
        if (stock == null) {
            throw new ApiRequestException("Stock not found", HttpStatus.NOT_FOUND);
        }
        return stock;
    }

    @Override
    public Stock findByProductName(String productName) {
        Product product = productRepository.findByName(productName);
        if (product == null) {
            throw new ApiRequestException("Product with name : " + product.getName() + " does not exist", HttpStatus.NOT_FOUND);
        }
        Stock stock = stockRepository.findByProductId(product.getId());
        if (stock == null) {
            throw new ApiRequestException("Stock not found for product : " + product.getName(), HttpStatus.NOT_FOUND);
        }
        return stock;
    }

    @Override
    public Stock createOne(Stock stock) {
        Product product = productRepository.findByName(stock.getProduct().getName());
        if (product == null) {
            throw new ApiRequestException("Product with name " + stock.getProduct().getName() + " does not exist", HttpStatus.NOT_FOUND);
        }
        Stock existingStock = stockRepository.findByProductId(product.getId());
        if (existingStock != null) {
            throw new ApiRequestException("Stock already exists for product with name : " + product.getName());
        }
        stock.setProduct(product);

        return stockRepository.save(stock);
    }

    @Override
    public Stock updateOne(Stock stock) {
        // Check if productId exists
        Product product = productRepository.findByName(stock.getProduct().getName());
        if (product == null) {
            throw new ApiRequestException("Product with name " + stock.getProduct().getName() + " does not exist", HttpStatus.NOT_FOUND);
        }
        // check if stock exists
        Stock existingStock = stockRepository.findByProductId(product.getId());
        if (existingStock == null) {
            throw new ApiRequestException("Stock does not exist for product id: " + product.getId(), HttpStatus.NOT_FOUND);
        }

        if (stock.getQuantity() < 0) {
            throw new ApiRequestException("Stock quantity cannot be negative", HttpStatus.BAD_REQUEST);
        }

        existingStock.setQuantity(stock.getQuantity());
        return stockRepository.save(existingStock);
    }

    @Override
    public void deleteOne(Long id) {
        Stock existingStock = stockRepository.findById(id).orElse(null);
        if (existingStock == null) {
            throw new ApiRequestException("Stock not found with id: " + id, HttpStatus.NOT_FOUND);
        }

        stockRepository.deleteById(id);
    }

    @Override
    public List<Stock> findAllStocks() {
        return stockRepository.findAll();
    }
}
