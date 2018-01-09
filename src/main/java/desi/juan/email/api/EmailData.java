package desi.juan.email.api;

import javax.mail.internet.ContentType;
import java.nio.charset.Charset;

import static java.lang.String.format;

/**
 * Internal class which stores data for either an {@link EmailBody} or an {@link EmailAttachment} with a specific charset and format.
 */
class EmailData {

  /**
   * Format of data. Example: "text/html" or "text/plain".
   */
  private final String format;

  /**
   * @return format of data. Example: "text/html" or "text/plain".
   */
  public String getFormat() {
    return format;
  }

  /**
   * The charset the data.
   */
  private final Charset charset;

  /**
   * @return the charset of the data.
   */
  public Charset getCharset() {
    return charset;
  }

  /**
   * @return the content type of the data, includes email format and charset
   */
  public String getContentType() {
    if (charset == null) {
      return format;
    } else {
      return format("%s; charset=%s", format, charset);
    }
  }

  /**
   * The content of either the data.
   */
  private Object content;

  /**
   * @return the content the data.
   */
  public Object getContent() {
    return content;
  }

  /**
   * Creates a new instance of EmailBody.
   *
   * @param content
   * @param contentType
   * @see #EmailData(Object, ContentType)
   */
  EmailData(final Object content, final String contentType) {
    this(content, EmailTools.toContentType(contentType));
  }

  /**
   * Creates a new instance of EmailBody.
   *
   * @param content
   * @param contentType
   * @see #EmailData(Object, Charset, String)
   */
  private EmailData(final Object content, final ContentType contentType) {
    this(content, EmailTools.getCharset(contentType), EmailTools.getFormat(contentType));
  }

  /**
   * Creates a new instance of EmailBody.
   *
   * @param content
   * @param charset
   * @param emailFormat
   */
  EmailData(final Object content, final Charset charset, final String emailFormat) {
    if (content == null || emailFormat == null) {
      throw new IllegalArgumentException("No param can be null");
    }
    this.content = content;
    this.charset = charset;
    this.format = emailFormat.toLowerCase();
  }
}
