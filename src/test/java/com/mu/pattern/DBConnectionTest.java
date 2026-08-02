package com.mu.pattern;

import com.mu.config.DBConnection;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DBConnectionSingletonTest {

    @BeforeAll
    static void suiteSetup() {
        System.out.println("=== DBConnectionSingletonTest starting ===");
    }

    @AfterAll
    static void suiteTeardown() {
        System.out.println("=== DBConnectionSingletonTest finished ===");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("--- Running test ---");
    }

    @AfterEach
    void afterEach() {
        System.out.println("--- Test done ---");
    }

    @Test
    @Order(1)
    void getInstance_calledTwice_returnsSameInstance() {
        DBConnection instance1 = DBConnection.getInstance();
        DBConnection instance2 = DBConnection.getInstance();


        assertSame(instance1, instance2, "Singleton violation: two calls returned different instances!");
    }

    @Test
    @Order(2)
    void getInstance_returnsNonNull() {
        DBConnection instance = DBConnection.getInstance();
        assertNotNull(instance);
    }


    @Test
    @Order(3)
    void getInstance_returnsCorrectType() {
        Object instance = DBConnection.getInstance();
        assertTrue(instance instanceof DBConnection);
    }

    @Test
    @Order(4)
    void singleton_notSameAsArbitraryObject() {
        DBConnection instance = DBConnection.getInstance();
        Object other = new Object();
        assertNotSame(instance, other);
    }


    @Test
    @Order(5)
    void getInstance_completesWithinTimeout() {
        assertTimeout(Duration.ofMillis(50), () -> {
            DBConnection instance = DBConnection.getInstance();
            assertNotNull(instance);
        });
    }


    @Test
    @Order(6)
    void getInstance_multipleThreads_sameHashCode() throws InterruptedException {
        int[] hashCodes = new int[5];
        Thread[] threads = new Thread[5];

        for (int i = 0; i < 5; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                hashCodes[idx] = DBConnection.getInstance().hashCode();
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        int expected = hashCodes[0];
        for (int code : hashCodes) {
            assertEquals(expected, code,
                    "Thread got a different singleton instance!");
        }
    }

    @Test
    @Order(7)
    void getInstance_twoCallsAreNotUnequal() {
        DBConnection i1 = DBConnection.getInstance();
        DBConnection i2 = DBConnection.getInstance();
        assertFalse(i1 != i2, "Instances should be the same reference");
    }

    @Test
    @Order(8)
    void getInstance_arrayOfCalls_sameHashCode() {
        int[] codes = new int[3];
        for (int i = 0; i < 3; i++) {
            codes[i] = DBConnection.getInstance().hashCode();
        }
        assertEquals(codes[0], codes[1]);
        assertEquals(codes[1], codes[2]);
        assertArrayEquals(new int[]{codes[0], codes[0], codes[0]}, codes);
    }
}