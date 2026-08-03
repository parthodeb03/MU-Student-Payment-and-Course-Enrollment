package com.mu.pattern;

import com.mu.factory.BkashPayment;
import com.mu.factory.CardPayment;
import com.mu.factory.CashPayment;
import com.mu.factory.PaymentFactory;
import com.mu.factory.PaymentStrategy;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentStrategyTest {

    @BeforeAll
    static void setup() {
        System.out.println("=== PaymentStrategyTest starting ===");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("=== PaymentStrategyTest finished ===");
    }

    @Test
    @Order(1)
    void factory_cash_returnsCashPayment() {
        PaymentStrategy strategy = PaymentFactory.createPayment("cash");
        assertNotNull(strategy);
        assertTrue(strategy instanceof CashPayment);
    }

    @Test
    @Order(2)
    void factory_bkash_returnsBkashPayment() {
        PaymentStrategy strategy = PaymentFactory.createPayment("bkash");
        assertNotNull(strategy);
        assertTrue(strategy instanceof BkashPayment);
    }

    @Test
    @Order(3)
    void factory_card_returnsCardPayment() {
        PaymentStrategy strategy = PaymentFactory.createPayment("card");
        assertNotNull(strategy);
        assertTrue(strategy instanceof CardPayment);
    }

    @Test
    @Order(4)
    void factory_unknownMethod_returnsNull() {
        PaymentStrategy strategy = PaymentFactory.createPayment("bitcoin");
        assertNull(strategy);
    }

    @Test
    @Order(5)
    void factory_nullMethod_returnsNull() {
        PaymentStrategy strategy = PaymentFactory.createPayment(null);
        assertNull(strategy);
    }

    @Test
    @Order(6)
    void factory_twoCashCalls_notSameReference() {
        PaymentStrategy s1 = PaymentFactory.createPayment("cash");
        PaymentStrategy s2 = PaymentFactory.createPayment("cash");
        assertNotSame(s1, s2); // Different object references
        assertEquals(s1.getClass(), s2.getClass()); // But same class
    }

    @Test
    @Order(7)
    void factory_differentMethods_differentClasses() {
        PaymentStrategy cash = PaymentFactory.createPayment("cash");
        PaymentStrategy bkash = PaymentFactory.createPayment("bkash");
        assertNotEquals(cash.getClass(), bkash.getClass());
    }

    @ParameterizedTest(name = "method [{0}] → expected non-null: {1}")
    @CsvSource({
            "cash,  true",
            "bkash, true",
            "card,  true",
            "CASH,  true",
            "CARD,  true",
            "wire,  false",
    })
    @Order(8)
    void factory_variousMethods(String method, boolean expectedNonNull) {
        PaymentStrategy strategy = PaymentFactory.createPayment(method);
        if (expectedNonNull) {
            assertNotNull(strategy);
        } else {
            assertNull(strategy);
        }
    }

    @Test
    @Order(9)
    void cashPay_completesWithinTimeout() {
        CashPayment cash = new CashPayment();
        assertTimeout(Duration.ofMillis(200), () -> cash.pay(500.0));
    }

    @Test
    @Order(10)
    void bkashPay_completesWithinTimeout() {
        BkashPayment bkash = new BkashPayment();
        assertTimeout(Duration.ofMillis(200), () -> bkash.pay(1000.0));
    }

    @Test
    @Order(11)
    void nullStrategy_isNotValid() {
        PaymentStrategy strategy = PaymentFactory.createPayment("unknown");
        assertFalse(strategy != null);
    }
}
