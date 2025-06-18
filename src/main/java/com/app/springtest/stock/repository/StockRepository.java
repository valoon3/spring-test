package com.app.springtest.stock.repository;

import com.app.springtest.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
