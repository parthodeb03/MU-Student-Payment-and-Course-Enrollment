# MU Student Payment and Course Enrollment

A Java Swing application for managing student registrations, course enrollments, and payments at a university. The project demonstrates design patterns, database integration, and unit testing.

## Features

- Student registration and login with required batch information
- Separate, clearly labelled student and administrator login/registration paths
- Password visibility controls and password confirmation on registration forms
- Inline validation messages, required-field indicators, and email/batch helper text
- Course management and enrollment
- Payment processing with multiple strategies (cash, card, Bkash)
- Notification observer pattern for event updates
- Input validation and session management
- Unit tests for services and design patterns

## Technologies

- Java
- Swing for UI
- JDBC and MySQL (or local database) for persistence
- JUnit 5 for testing
- Mockito for mocking dependencies

## Usage

1. Build the project with Maven.
2. Run the `Main.java` GUI application.
3. Register students with their batch, manage courses, enroll, and process payments.

## Account access

- Use **Register** to create a student account and **Login** to access the student portal.
- Use **Admin Login** for an existing administrator account or **Create Admin Account** to register a new administrator.
- Registration requires password confirmation. Use the **Show password(s)** checkbox to review entries before submitting.

## Notes

This repository includes pattern-based test coverage for payment strategy and observer notifications, plus service-level unit tests.
