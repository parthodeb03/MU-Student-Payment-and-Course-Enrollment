package com.mu.service;

import com.mu.dao.StudentDAO;
import com.mu.model.Student;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegistrationServiceTest {

    private StudentDAO mockDAO;
    private RegistrationService service;

    @BeforeAll
    static void suiteSetup() {
        System.setProperty("net.bytebuddy.experimental", "true");
    }

    @BeforeEach
    void setup() {
        mockDAO = mock(StudentDAO.class);
        service = new RegistrationService(mockDAO);
        when(mockDAO.register(any(Student.class))).thenReturn(true);
    }

    @AfterEach
    void teardown() {
        clearInvocations(mockDAO);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "A"})
    @Order(1)
    void register_invalidName_throws(String name) {
        Student s = new Student(0, name, "valid@mu.edu", "password123");
        assertThrows(IllegalArgumentException.class, () -> service.register(s));
        verify(mockDAO, never()).register(any(Student.class));
    }

    @ParameterizedTest
    @CsvSource({
            "Alice Rahman,alice@mu.edu,password123,true",
            "Bob,bob@mu.edu,pass12,true",
            "A,a@mu.edu,password123,false",
            "Alice,invalid-email,password,false",
            "Alice,alice@mu.edu,short,false"
    })
    @Order(2)
    void register_combinationsTable(String name, String email, String pass, boolean shouldSucceed) {
        Student s = new Student(0, name, email, pass);

        if (shouldSucceed) {
            assertDoesNotThrow(() -> service.register(s));
            verify(mockDAO).register(s);
        } else {
            assertThrows(IllegalArgumentException.class, () -> service.register(s));
            verify(mockDAO, never()).register(any(Student.class));
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/valid_students.csv", numLinesToSkip = 1)
    @Order(3)
    void register_csvFileSource_allValid(String name, String email, String password) {
        Student s = new Student(0, name, email, password);

        assertDoesNotThrow(() -> service.register(s));

        verify(mockDAO).register(s);
    }

    @ParameterizedTest
    @MethodSource("validStudentProvider")
    @Order(4)
    void register_methodSource_validStudents(Student student) {
        assertDoesNotThrow(() -> service.register(student));
        verify(mockDAO).register(student);
    }

    static Stream<Student> validStudentProvider() {
        return Stream.of(
                new Student(0, "Dana Islam", "dana@mu.edu", "dana1234"),
                new Student(0, "Evan Hossain", "evan@mu.edu", "evan5678"),
                new Student(0, "Fara Begum", "fara@mu.edu", "fara9012")
        );
    }

    @Test
    @Order(5)
    void register_password5Chars_throws() {
        Student s = new Student(0, "Test User", "test@mu.edu", "12345");

        assertThrows(IllegalArgumentException.class, () -> service.register(s));

        verify(mockDAO, never()).register(any(Student.class));
    }

    @Test
    @Order(6)
    void register_password6Chars_succeeds() {
        Student s = new Student(0, "Test User", "test@mu.edu", "123456");

        assertDoesNotThrow(() -> service.register(s));

        verify(mockDAO).register(s);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "notanemail",
            "missing@",
            "@nodomain",
            "no-at-sign.com"
    })
    @Order(7)
    void causeEffect_invalidEmail_throwsException(String badEmail) {
        Student s = new Student(0, "Valid Name", badEmail, "password123");

        assertThrows(IllegalArgumentException.class, () -> service.register(s));

        verify(mockDAO, never()).register(any(Student.class));
    }

    @Test
    @Order(8)
    void register_success_returnsTrue() {
        Student s = new Student(0, "Gina Noor", "gina@mu.edu", "ginaspass");

        boolean result = service.register(s);

        assertTrue(result);
        assertNotNull(s.getName());

        verify(mockDAO).register(s);
    }
}