package com.app.springtest.stock.service;

import com.app.springtest.stock.entity.Stock;
import com.app.springtest.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional
    public  void decrease(final Long id, final Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new RuntimeException("Stock not found with id: " + id));
        stock.decrease(quantity);
    }

}
