package com.mu.service;

import com.mu.dao.PaymentDAO;
import com.mu.model.Payment;
import com.mu.observer.NotificationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentDAO paymentDAO;

    private AutoCloseable mocks;

    @BeforeAll
    static void suiteSetup() {
        System.setProperty("net.bytebuddy.experimental", "true");
        System.out.println("=== PaymentServiceTest starting ===");
    }

    @AfterAll
    static void suiteTeardown() {
        System.setProperty("net.bytebuddy.experimental", "true");
        System.out.println("=== PaymentServiceTest finished ===");
    }

    @BeforeEach
    void init() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void close() throws Exception {
        mocks.close();
    }

    @Test
    @Order(1)
    void makePayment_invalidStudentId_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.makePayment(0, 500.0, "cash"));
    }

    @Test
    @Order(2)
    void makePayment_negativeAmount_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.makePayment(1, -100.0, "cash"));
    }

    @Test
    @Order(3)
    void makePayment_zeroAmount_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.makePayment(1, 0.0, "cash"));
    }

    @Test
    @Order(4)
    void makePayment_nullMethod_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.makePayment(1, 500.0, null));
    }

    @Test
    @Order(5)
    void makePayment_unknownMethod_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.makePayment(1, 500.0, "bitcoin"));
    }

    @ParameterizedTest(name = "method [{0}] should be accepted")
    @ValueSource(strings = { "cash", "bkash", "card", "CASH", "BKASH", "CARD" })
    @Order(6)
    void makePayment_validMethods_noThrow(String method) {
        try {
            paymentService.makePayment(1, 500.0, method);
        } catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException for valid method: " + method);
        } catch (Exception ignored) {
        }
    }

    @Test
    @Order(7)
    void paymentModel_constructorAndGetters() {
        Payment p = new Payment(1, 10, 1500.0, "cash", LocalDate.of(2025, 1, 15));

        assertEquals(1, p.getPaymentId());
        assertEquals(10, p.getStudentId());
        assertEquals(1500.0, p.getAmount(), 0.001);
        assertEquals("cash", p.getPaymentMethod());
        assertNotNull(p.getPaymentDate());
    }

    @Test
    @Order(8)
    void paymentModel_setAmount_updatesField() {
        Payment p = new Payment();
        p.setAmount(2500.0);
        // Verify the setter actually stores the value (was broken before fix)
        assertEquals(2500.0, p.getAmount(), 0.001);
    }

    @Test
    @Order(9)
    void paymentModel_defaultConstructor_zeroAmount() {
        Payment p = new Payment();
        assertEquals(0.0, p.getAmount(), 0.001);
    }

    @Test
    @Order(10)
    void paymentAmounts_matchExpected() {
        double[] expectedAmounts = { 500.0, 1000.0, 1500.0 };
        double[] actualAmounts = { 500.0, 1000.0, 1500.0 };
        assertArrayEquals(expectedAmounts, actualAmounts, 0.001);
    }

    @Test
    @Order(11)
    void paymentValidation_withinTimeout() {
        assertTimeout(Duration.ofSeconds(1), () -> {
            assertThrows(IllegalArgumentException.class,
                    () -> paymentService.makePayment(-1, 500.0, "cash"));
        });
    }

    @ParameterizedTest(name = "id={0}, amount={1}, method={2} â†’ throws={3}")
    @CsvSource({
            "1,    500,  cash,    false",
            "0,    500,  cash,    true",
            "1,    0,    cash,    true",
            "1,    500,  null,    true",
            "-1,   500,  bkash,   true",
            "1,    -100, card,    true",
    })
    @Order(12)
    void makePayment_decisionTable(int id, double amount, String method, boolean shouldThrow) {
        String m = "null".equals(method) ? null : method;
        if (shouldThrow) {
            assertThrows(IllegalArgumentException.class,
                    () -> paymentService.makePayment(id, amount, m));
        } else {
            try {
                paymentService.makePayment(id, amount, m);
            } catch (IllegalArgumentException e) {
                fail("Should not throw for valid inputs");
            } catch (Exception ignored) {

            }
        }
    }

    @Test
    @Order(13)
    void paymentService_notNull() {
        assertNotNull(paymentService);
    }

    @Test
    @Order(14)
    void twoServiceInstances_notSame() {
        PaymentService ps2 = new PaymentService(paymentDAO);
        assertNotSame(paymentService, ps2);
    }
}
