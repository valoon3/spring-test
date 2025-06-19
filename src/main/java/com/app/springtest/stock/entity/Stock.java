package com.app.springtest.stock.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "stock",
        indexes = {

        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long productId;

    @Column(nullable = false)
    private int quantity;

    public static Stock create(long productId, int quantity) {
        Stock stock = new Stock();
        stock.productId = productId;
        stock.quantity = quantity;
        return stock;
    }

    public void updatePlusQuantity(long quantity) {
        this.quantity += quantity;
    }

    public void decrease(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Invalid quantity: " + quantity);
        }

        if (this.quantity < quantity) {
            throw new IllegalArgumentException("Insufficient stock quantity");
        }
        this.quantity -= quantity;
    }
}
