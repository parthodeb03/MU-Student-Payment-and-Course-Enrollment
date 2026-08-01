package com.mu.pattern;

import com.mu.observer.NotificationService;
import com.mu.observer.Observer;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    private Observer capturingObserver() {
        return message -> capturedMessages.add(message);
    }

    @Test
    @Order(1)
    void service_instantiated_notNull() {
        assertNotNull(service);
    }

    @Test
    @Order(2)
    void addObserver_notify_observerReceivesMessage() {
        service.addObserver(capturingObserver());
        service.notifyObservers("Payment received");
        assertTrue(capturedMessages.contains("Payment received"));
    }

    @Test
    @Order(3)
    void notify_correctMessageDelivered() {
        service.addObserver(capturingObserver());
        service.notifyObservers("Test message 123");

        assertEquals("Test message 123", capturedMessages.get(0));
    }

    @Test
    @Order(4)
    void removeObserver_noLongerReceivesMessages() {
        Observer obs = capturingObserver();
        service.addObserver(obs);
        service.removeObserver(obs);

        service.notifyObservers("Should not arrive");

        assertFalse(capturedMessages.contains("Should not arrive"));
    }

    @Test
    @Order(5)
    void twoServices_notSameReference() {
        NotificationService s2 = new NotificationService();
        assertNotSame(service, s2);
    }

    @Test
    @Order(6)
    void multipleMessages_arriveinOrder() {
        service.addObserver(capturingObserver());
        service.notifyObservers("First");
        service.notifyObservers("Second");
        service.notifyObservers("Third");

        String[] expected = { "First", "Second", "Third" };
        String[] actual = capturedMessages.toArray(new String[0]);

        assertArrayEquals(expected, actual);
    }

    @Test
    @Order(7)
    void twoMessages_notEqual() {
        assertNotEquals("Payment Done", "Payment Failed");
    }
}
