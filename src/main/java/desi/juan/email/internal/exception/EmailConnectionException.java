package desi.juan.email.internal.exception;

public class EmailConnectionException extends RuntimeException {

  public EmailConnectionException() {
    super();
  }

  public EmailConnectionException(String message) {
    super(message);
  }

  public EmailConnectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
