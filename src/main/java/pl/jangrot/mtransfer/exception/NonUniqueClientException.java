package pl.jangrot.mtransfer.exception;

public class NonUniqueClientException extends IllegalStateException {

    public NonUniqueClientException(String message) {
        super(message);
    }
}
