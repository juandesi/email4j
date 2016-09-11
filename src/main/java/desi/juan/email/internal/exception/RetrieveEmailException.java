package desi.juan.email.internal.exception;

public class RetrieveEmailException extends RuntimeException {

  public RetrieveEmailException() {
    super();
  }

  public RetrieveEmailException(String message) {
    super(message);
  }

  public RetrieveEmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
