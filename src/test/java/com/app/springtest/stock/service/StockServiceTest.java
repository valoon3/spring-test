package com.app.springtest.stock.service;

import com.app.springtest.stock.entity.Stock;
import com.app.springtest.stock.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockServiceTest {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @BeforeEach
    void setUp() {
        Stock stock = Stock.create(1L, 100);
        stockRepository.saveAndFlush(stock);
    }

    @Test
    @DisplayName("동시성에 10번의 요청 테스트")
    void 수량_동시성_테스트() throws InterruptedException {
        int threadCount = 10;
        // 멀티스레드 이용 ExecutorService : 비동기를 단순하게 처리할 수 있또록 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(); // 성공 카운트
        AtomicInteger failCount = new AtomicInteger();   // 실패 카운트

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 10);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    // 어떤 예외가 발생하는지 로그로 확인
//                    System.out.println("Exception occurred: " + e.getClass().getName());
                    System.out.println("Exception occurred: " + e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        //다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌
        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertThat(stock.getQuantity()).isEqualTo(100 - successCount.get() * 10);
        assertThat(stock.getQuantity()).isEqualTo(0);
    }

    @Test
    void 기본키_생성_테스트() {
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertNotNull(stock.getId());
        assertTrue(stock.getId() > 0);
    }

    @Test
    void testDecreaseThrowsOnNegativeQuantity() {
        Stock stock = stockRepository.findById(1L).orElseThrow();
        Long stockId = stock.getId();
        int negativeQuantity = -10;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            stockService.decrease(stockId, negativeQuantity);
        });
        // Optionally, check the exception message or type if business logic is updated to throw a specific exception

        assertTrue(exception.getMessage().contains("Invalid quantity"));
    }

    @Test
    void testRetrieveStockInfoWithValidSymbol() {
        Stock stock = stockRepository.findById(1L).orElseThrow();
        Optional<Stock> found = stockRepository.findById(stock.getId());
        assertTrue(found.isPresent());
        assertEquals(stock.getProductId(), found.get().getProductId());
        assertEquals(stock.getQuantity(), found.get().getQuantity());
    }

    @Test
    void testUpdateStockQuantityWithValidRequest() {
        Stock stock = stockRepository.findById(1L).orElseThrow();
        Long stockId = stock.getId();
        Long addQuantity = 50L;
        Stock found = stockRepository.findById(stockId).orElseThrow();
        found.updatePlusQuantity(addQuantity);
        stockRepository.save(found);
        Stock updated = stockRepository.findById(stockId).orElseThrow();
        assertEquals(150L, updated.getQuantity());
    }

    @Test
    void testGetAllStocks() {
        Stock stock = stockRepository.findById(1L).orElseThrow();
        List<Stock> stocks = stockRepository.findAll();
        assertFalse(stocks.isEmpty());
        assertEquals(1, stocks.size());
        assertEquals(stock.getProductId(), stocks.get(0).getProductId());
    }

    @Test
    void testRetrieveStockInfoWithInvalidSymbol() {
        Optional<Stock> found = stockRepository.findById(-999L);
        assertFalse(found.isPresent());
    }

    @Test
    void testGetAllStocksWhenDatabaseIsEmpty() {
        stockRepository.deleteAll();
        List<Stock> stocks = stockRepository.findAll();
        assertTrue(stocks.isEmpty());
    }

    @Test
    void testDecreaseStockWithSufficientQuantity() {
        Stock stock = stockRepository.findById(1L).orElseThrow();
        Long stockId = stock.getId();
        stockService.decrease(stockId, 30);
        Stock updated = stockRepository.findById(stockId).orElseThrow();
        assertEquals(70L, updated.getQuantity());
    }

    @Test
    void testDecreaseFetchesAndUpdatesCorrectStock() {
        Stock anotherStock = Stock.create(2L, 200);
        stockRepository.save(anotherStock);

        Stock stock = stockRepository.findById(1L).orElseThrow();
        stockService.decrease(stock.getId(), 10);

        Stock updated = stockRepository.findById(stock.getId()).orElseThrow();
        Stock untouched = stockRepository.findById(anotherStock.getId()).orElseThrow();

        assertEquals(90L, updated.getQuantity());
        assertEquals(200L, untouched.getQuantity());
    }

    @Test
    void testFindByIdCalledOnceDuringDecrease() {
        // Since we cannot use mocks, we check the effect: decrease should only affect the correct stock
        Stock anotherStock = Stock.create(2L, 200);
        stockRepository.save(anotherStock);

        Stock stock = stockRepository.findById(1L).orElseThrow();
        stockService.decrease(stock.getId(), 5);

        Stock updated = stockRepository.findById(stock.getId()).orElseThrow();
        Stock untouched = stockRepository.findById(anotherStock.getId()).orElseThrow();

        assertEquals(95L, updated.getQuantity());
        assertEquals(200L, untouched.getQuantity());
    }

    @Test
    void testDecreaseThrowsWhenStockNotFound() {
        Long nonExistentId = -12345L;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            stockService.decrease(nonExistentId, 10);
        });
        assertTrue(exception.getMessage().contains("Stock not found with id"));
    }

    @Test
    void testDecreaseThrowsOnInsufficientQuantity() {
        Stock stock = stockRepository.findById(1L).orElseThrow();
        Long stockId = stock.getId();
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.decrease(stockId, 200);
        });
    }
}
