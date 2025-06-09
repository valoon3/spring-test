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
    class DISCOUNT_1000_WON_test {
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

    @Nested
    @DisplayName("10% 할인 쿠폰은")
    class Discount10PercentTest {

        @Test
        @DisplayName("할인 금액이 최대 할인 금액(20,000원)을 넘지 않으면 정상적으로 10%를 할인한다 (성공)")
        void shouldApply10PercentDiscount_whenDiscountIsBelowMax() {
            // Given: 10% 할인 시 5,000원
            int originalPrice = 50000;

            // When
            int discountAmount = discount10Percent.calculateDiscount(originalPrice);

            // Then
            assertThat(discountAmount).isEqualTo(5000);
        }

        @Test
        @DisplayName("할인 금액이 최대 할인 금액(20,000원)을 초과하면 최대 할인 금액으로 적용된다 (성공)")
        void shouldApplyMaxDiscount_whenDiscountExceedsMax() {
            // Given: 10% 할인 시 30,000원이지만 최대 할인 금액(20,000원)을 초과
            int originalPrice = 300000;

            // When
            int discountAmount = discount10Percent.calculateDiscount(originalPrice);

            // Then: 최대 할인 금액인 20,000원이 적용되어야 함
            assertThat(discountAmount).isEqualTo(20000);
        }

        @Test
        @DisplayName("할인 금액이 최대 할인 금액(20,000원)과 정확히 일치하면 해당 금액으로 할인된다 (성공)")
        void shouldApplyExactMaxDiscount_whenDiscountIsEqualToMax() {
            // Given: 10% 할인 시 정확히 20,000원
            int originalPrice = 200000;

            // When
            int discountAmount = discount10Percent.calculateDiscount(originalPrice);

            // Then
            assertThat(discountAmount).isEqualTo(20000);
        }
    }

    @Nested
    @DisplayName("5,000원 할인 쿠폰은")
    class Discount5000WonTest {

        @Test
        @DisplayName("결제 금액이 50,000원 미만이면 할인이 적용되지 않는다 (실패)")
        void shouldNotApplyDiscount_whenPriceIsBelowMinimum() {
            // Given: 최소 결제 금액 미달
            int originalPrice = 49999;

            // When
            int discountAmount = discount5000Won.calculateDiscount(originalPrice);

            // Then
            assertThat(discountAmount).isEqualTo(-1);
        }

        @Test
        @DisplayName("결제 금액이 50,000원이면 5,000원을 할인한다 (성공)")
        void shouldApplyDiscount_whenPriceIsEqualToMinimum() {
            // Given: 최소 결제 금액과 동일
            int originalPrice = 50000;

            // When
            int discountAmount = discount5000Won.calculateDiscount(originalPrice);

            // Then
            assertThat(discountAmount).isEqualTo(5000);
        }

        @Test
        @DisplayName("결제 금액이 50,000원 이상이면 5,000원을 할인한다 (성공)")
        void shouldApplyDiscount_whenPriceIsOverMinimum() {
            // Given: 최소 결제 금액 초과
            int originalPrice = 100000;

            // When
            int discountAmount = discount5000Won.calculateDiscount(originalPrice);

            // Then
            assertThat(discountAmount).isEqualTo(5000);
        }
    }

    @Nested
    @DisplayName("20% 할인 쿠폰은")
    class Discount20PercentTest {

        @Test
        @DisplayName("결제 금액이 30,000원 미만이면 할인이 적용되지 않는다 (실패)")
        void shouldNotApplyDiscount_whenPriceIsBelowMinimum() {
            // Given: 최소 결제 금액(30,000원) 미달
            int originalPrice = 29999;

            // When
            int discountAmount = discount20Percent.calculateDiscount(originalPrice);

            // Then
            assertThat(discountAmount).isEqualTo(-1);
        }

        @Test
        @DisplayName("30,000원 이상 결제 시, 할인액이 최대 할인액(10,000원) 미만이면 20%를 할인한다 (성공)")
        void shouldApply20PercentDiscount_whenDiscountIsBelowMax() {
            // Given: 20% 할인액(8,000원) < 최대 할인액(10,000원)
            int originalPrice = 40000;

            // When
            int discountAmount = discount20Percent.calculateDiscount(originalPrice);

            // Then
            assertThat(discountAmount).isEqualTo(8000); // 40,000 * 20%
        }

        @Test
        @DisplayName("할인액이 최대 할인액(10,000원)을 초과하면 최대 할인액만 적용된다 (성공)")
        void shouldApplyMaxDiscount_whenDiscountExceedsMax() {
            // Given: 20% 할인액(12,000원) > 최대 할인액(10,000원)
            int originalPrice = 60000;

            // When
            int discountAmount = discount20Percent.calculateDiscount(originalPrice);

            // Then
            assertThat(discountAmount).isEqualTo(10000); // 최대 할인액
        }
    }

}