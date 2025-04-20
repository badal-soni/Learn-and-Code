package com.itt.livecoding.repository;

import com.itt.livecoding.entities.Stock;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class StockRepository {

    private static final Map<String, Stock> stockDatabase = new HashMap<>();

    public void addStock(Stock stock) {
        stockDatabase.putIfAbsent(stock.productId, stock);
    }

    public Stock updateStock(String productId, int quantity) {
        Stock stock = stockDatabase.get(productId);
        stock.updatedAt = LocalDateTime.now();
        stock.quantity += quantity;
        return stock;
    }

    public Stock findStockByProductId(String productId) {
        return stockDatabase.get(productId);
    }

}
