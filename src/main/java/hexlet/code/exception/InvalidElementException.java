package hexlet.code.exception;

import org.springframework.http.HttpStatus;

public class InvalidElementException extends RuntimeException {

    private final HttpStatus status;

    public InvalidElementException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static InvalidElementException invalidElement(String message) {
        return new InvalidElementException(message, HttpStatus.NOT_FOUND);
    }
}
