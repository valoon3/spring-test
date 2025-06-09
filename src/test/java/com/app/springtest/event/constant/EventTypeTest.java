package com.app.springtest.event.constant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EventTypeTest {

    EventType discount1000Won;
    EventType discount5000Won;
    EventType discount10Percent;
    EventType discount20Percent;

    @BeforeEach
    public void setUp() {
        discount1000Won = EventType.DISCOUNT_1000_WON;
        discount5000Won = EventType.DISCOUNT_5000_WON;
        discount10Percent = EventType.DISCOUNT_10_PERCENT;
        discount20Percent = EventType.DISCOUNT_20_PERCENT;
    }

    @Nested
    @DisplayName("DISCOUNT_1000_WON 은 결제 금액이 1000 원 이상일때 사용가능")
    public class DISCOUNT_1000_WON_test {
        @Test
        @DisplayName("결제 금액이 1,000원이면 1,000원을 할인한다 (성공)")
        void 결제_금액이_1000_원이면_1000_원을_할인한다_성공() {
            // Given (주어진 상황)
            int originalPrice = 1000;

            // When (무엇을 할 때)
            int discountAmount = discount1000Won.calculateDiscount(originalPrice);

            // Then (결과)
            assertThat(discountAmount).isEqualTo(1000);
        }

        @Test
        @DisplayName("결제 금액이 1,000원 이상이면 1,000원을 할인한다 (성공)")
        void shouldApplyDiscount_whenPriceIsOverMinimum() {
            // Given (주어진 상황)
            int originalPrice = 5000;

            // When (무엇을 할 때)
            int discountAmount = discount1000Won.calculateDiscount(originalPrice);

            // Then (결과)
            assertThat(discountAmount).isEqualTo(1000);
        }

        @Test
        @DisplayName("결제 금액이 1,000원 미만이면 할인이 적용되지 않는다 (실패)")
        void shouldNotApplyDiscount_whenPriceIsBelowMinimum() {
            // Given (주어진 상황)
            int originalPrice = 999;

            // When (무엇을 할 때)
            int discountAmount = discount1000Won.calculateDiscount(originalPrice);

            // Then (결과)
            assertThat(discountAmount).isEqualTo(-1); // -1을 반환하는지 확인
        }
    }

}