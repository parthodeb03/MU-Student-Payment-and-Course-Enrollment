package com.mu.pattern;

import com.mu.observer.NotificationService;
import com.mu.observer.Observer;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NotificationServiceTest – tests the Observer design pattern.
 *
 * DESIGN PATTERN: Observer
 *   - Subject: NotificationService
 *   - Observers: any class implementing Observer interface
 *
 * WHY Observer Pattern: When a payment is made, multiple parties
 *   (student, admin, logger) need to be notified without PaymentService
 *   needing to know each one explicitly.
 *
 * WHEN TO USE: When state changes in one object must trigger updates
 *   in other objects, without tight coupling.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationServiceTest {

    private NotificationService service;
    private List<String> capturedMessages;

    @BeforeAll
    static void suiteStart() {
        System.out.println("=== NotificationServiceTest starting ===");
    }

    @AfterAll
    static void suiteEnd() {
        System.out.println("=== NotificationServiceTest finished ===");
    }

    @BeforeEach
    void init() {
        service = new NotificationService();
        capturedMessages = new ArrayList<>();
    }

    @AfterEach
    void cleanup() {
        capturedMessages.clear();
    }

    // helper: a test-only observer that captures messages
    private Observer capturingObserver() {
        return message -> capturedMessages.add(message);
    }

    /**
     * WHY assertNotNull: service must be instantiated properly.
     */
    @Test
    @Order(1)
    void service_instantiated_notNull() {
        assertNotNull(service);
    }

    /**
     * WHY assertTrue: after adding an observer, notification must reach it.
     */
    @Test
    @Order(2)
    void addObserver_notify_observerReceivesMessage() {
        service.addObserver(capturingObserver());
        service.notifyObservers("Payment received");

        // The service already adds StudentObserver internally (+1), so we added +1 = 2 total
        // Our custom observer should have captured the message
        assertTrue(capturedMessages.contains("Payment received"));
    }

    /**
     * WHY assertEquals: the exact message must arrive unchanged.
     */
    @Test
    @Order(3)
    void notify_correctMessageDelivered() {
        service.addObserver(capturingObserver());
        service.notifyObservers("Test message 123");

        assertEquals("Test message 123", capturedMessages.get(0));
    }

    /**
     * WHY assertFalse: after removal, observer must not receive messages.
     */
    @Test
    @Order(4)
    void removeObserver_noLongerReceivesMessages() {
        Observer obs = capturingObserver();
        service.addObserver(obs);
        service.removeObserver(obs);

        service.notifyObservers("Should not arrive");

        assertFalse(capturedMessages.contains("Should not arrive"));
    }

    /**
     * WHY assertNotSame: two separate NotificationService instances are different.
     */
    @Test
    @Order(5)
    void twoServices_notSameReference() {
        NotificationService s2 = new NotificationService();
        assertNotSame(service, s2);
    }

    /**
     * WHY assertArrayEquals: multiple messages arrive in correct order.
     */
    @Test
    @Order(6)
    void multipleMessages_arriveinOrder() {
        service.addObserver(capturingObserver());
        service.notifyObservers("First");
        service.notifyObservers("Second");
        service.notifyObservers("Third");

        String[] expected = {"First", "Second", "Third"};
        String[] actual = capturedMessages.toArray(new String[0]);

        assertArrayEquals(expected, actual);
    }

    /**
     * WHY assertNotEquals: different messages are not equal.
     */
    @Test
    @Order(7)
    void twoMessages_notEqual() {
        assertNotEquals("Payment Done", "Payment Failed");
    }
}
