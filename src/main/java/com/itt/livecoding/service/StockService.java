package com.itt.livecoding.service;

import com.itt.livecoding.entities.Stock;
import com.itt.livecoding.repository.StockRepository;

import java.util.Objects;

public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public boolean isInStock(String productId) {
        Stock stock = stockRepository.findStockByProductId(productId);
        if (Objects.isNull(stock)) {
            return false;
        }
        return stock.quantity > 0;
    }

    public void updateStock(String productId, int quantity) {
        stockRepository.updateStock(productId, quantity);
    }

}
