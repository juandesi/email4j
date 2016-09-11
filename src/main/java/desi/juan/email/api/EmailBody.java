package desi.juan.email.api;

public interface EmailBody {

  /**
   * @return the body content of the email as a {@link String} value.
   */
  String getContent();

  /**
   * @return the content type of the body, one of text/plain or text/html
   */
  String getContentType();
}
