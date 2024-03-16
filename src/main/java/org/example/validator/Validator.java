package org.example.validator;

public interface Validator <T> {
    ValidationResult isValid(T object);
}
