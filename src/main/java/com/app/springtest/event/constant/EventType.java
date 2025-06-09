package com.app.springtest.event.constant;

import lombok.Getter;

@Getter
public enum EventType {
    DISCOUNT_1000_WON("1,000원 할인 쿠폰", 1000, 0, 1000, 0),
    DISCOUNT_5000_WON("5,000원 할인 쿠폰", 5000, 0, 50000, 0),
    DISCOUNT_10_PERCENT("10% 할인 쿠폰", 0, 10, 0, 20000),
    DISCOUNT_20_PERCENT("20% 할인 쿠폰", 0, 20, 30000, 10000)

    ;

    private final String description;
    private final int discountAmount; // 고정 할인 금액
    private final int discountRate;   // 할인율 (%)
    private final int minPurchaseAmount; // 최소 결제 금액
    private final int maxDiscountAmount; // 최대 할인 금액

    EventType(String description, int discountAmount, int discountRate, int minPurchaseAmount, int maxDiscountAmount) {
        this.description = description;
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
        this.minPurchaseAmount = minPurchaseAmount;
        this.maxDiscountAmount = maxDiscountAmount;
    }

    /**
     * 실제 할인 금액을 계산하는 로직
     * @param originalPrice 원가
     * @return 할인 적용된 금액
     */
    public int calculateDiscount(int originalPrice) {
        if (originalPrice < this.minPurchaseAmount) {
            return -1; // 최소 결제 금액 미달 시 -1 반환
        }

        int discount = 0;
        if (this.discountRate > 0) {
            discount = (int) (originalPrice * (this.discountRate / 100.0));
            if (this.maxDiscountAmount > 0 && discount > this.maxDiscountAmount) {
                discount = this.maxDiscountAmount;
            }
        } else {
            discount = this.discountAmount;
        }
        return discount;
    }
}
