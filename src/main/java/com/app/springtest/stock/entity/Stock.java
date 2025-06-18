package com.app.springtest.stock.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "products",
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
    private long quantity;

    public Stock create(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
        return this;
    }

    public void updatePlusQuantity(long quantity) {
        this.quantity += quantity;
    }

    public void decrease(long quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("Insufficient stock quantity");
        }
        this.quantity -= quantity;
    }
}
