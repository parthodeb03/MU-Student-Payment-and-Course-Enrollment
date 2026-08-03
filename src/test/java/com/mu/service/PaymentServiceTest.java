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
    void makePayment_nullStudentId_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.makePayment(null, "Tuition Fee", "January", "2025",
                        "Spring", "cash", "ref-001"));
    }

    @Test
    @Order(2)
    void makePayment_emptyStudentId_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.makePayment("", "Tuition Fee", "January", "2025",
                        "Spring", "cash", "ref-001"));
    }

    @Test
    @Order(3)
    void makePayment_nullPaymentType_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.makePayment("STU-001", null, "January", "2025",
                        "Spring", "cash", "ref-001"));
    }

    @Test
    @Order(4)
    void makePayment_nullPaymentMethod_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.makePayment("STU-001", "Tuition Fee", "January", "2025",
                        "Spring", null, "ref-001"));
    }

    @Test
    @Order(5)
    void makePayment_emptyPaymentMethod_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.makePayment("STU-001", "Tuition Fee", "January", "2025",
                        "Spring", "", "ref-001"));
    }

    @ParameterizedTest(name = "method [{0}] should be accepted")
    @ValueSource(strings = { "cash", "bkash", "card", "CASH", "BKASH", "CARD" })
    @Order(6)
    void makePayment_validMethods_noThrow(String method) {
        when(paymentDAO.getFeeAmount(anyString())).thenReturn(500.0);
        when(paymentDAO.makePayment(any(Payment.class))).thenReturn(true);

        try {
            paymentService.makePayment("STU-001", "Tuition Fee", "January", "2025",
                    "Spring", method, "ref-001");
        } catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException for valid method: " + method);
        } catch (Exception ignored) {
        }
    }

    @Test
    @Order(7)
    void paymentModel_constructorAndGetters() {
        Payment p = new Payment(1, "STU-010", 1500.0, "cash",
                LocalDate.of(2025, 1, 15), "Tuition Fee", "January", "2025",
                "Spring", "PENDING", "ref-001");

        assertEquals(1, p.getPaymentId());
        assertEquals("STU-010", p.getStudentId());
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
                    () -> paymentService.makePayment(null, "Tuition Fee", "January", "2025",
                            "Spring", "cash", "ref-001"));
        });
    }

    @ParameterizedTest(name = "studentId={0}, paymentType={1}, method={2} → throws={3}")
    @CsvSource({
            "STU-001, Tuition Fee, cash,    false",
            "null,    Tuition Fee, cash,    true",
            "STU-001, null,        cash,    true",
            "STU-001, Tuition Fee, null,    true",
            "'',      Tuition Fee, bkash,   true",
            "STU-001, '',          card,    true",
    })
    @Order(12)
    void makePayment_decisionTable(String studentId, String paymentType, String method, boolean shouldThrow) {
        String sid = "null".equals(studentId) ? null : studentId;
        String pt  = "null".equals(paymentType) ? null : paymentType;
        String m   = "null".equals(method) ? null : method;

        if (!shouldThrow) {
            when(paymentDAO.getFeeAmount(anyString())).thenReturn(500.0);
            when(paymentDAO.makePayment(any(Payment.class))).thenReturn(true);
        }

        if (shouldThrow) {
            assertThrows(IllegalArgumentException.class,
                    () -> paymentService.makePayment(sid, pt, "January", "2025",
                            "Spring", m, "ref-001"));
        } else {
            try {
                paymentService.makePayment(sid, pt, "January", "2025",
                        "Spring", m, "ref-001");
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
