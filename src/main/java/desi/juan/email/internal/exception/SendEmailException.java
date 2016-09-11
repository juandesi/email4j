package desi.juan.email.internal.exception;

public class SendEmailException extends RuntimeException {

  public SendEmailException() {
    super();
  }

  public SendEmailException(String message) {
    super(message);
  }

  public SendEmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
