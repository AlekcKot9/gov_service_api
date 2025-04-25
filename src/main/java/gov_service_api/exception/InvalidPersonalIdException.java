package gov_service_api.exception;

public class InvalidPersonalIdException extends RuntimeException {
    public InvalidPersonalIdException(String message) {
        super(message);
    }
}
