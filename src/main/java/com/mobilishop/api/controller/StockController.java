package com.mobilishop.api.controller;

import com.mobilishop.api.model.Stock;
import com.mobilishop.api.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public List<Stock> getStock() {
        return stockService.findAllStocks();
    }

    @GetMapping("/{id}")
    public Stock getStockById(@PathVariable Long id) {
        return stockService.findByProductId(id);
    }

    @GetMapping("/name")
    public Stock getStockByProductName(@RequestBody String productName) {
        return stockService.findByProductName(productName);
    }

    @PostMapping
    public Stock createStock(@RequestBody Stock stock) {
        return stockService.createOne(stock);
    }

    @PutMapping
    public Stock updateStock(@RequestBody Stock stock) {
        return stockService.updateOne(stock);
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        stockService.deleteOne(id);
    }

}
