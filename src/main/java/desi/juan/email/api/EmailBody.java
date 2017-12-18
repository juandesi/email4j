/**
 * The MIT License (MIT)
 *
 * Original work Copyright (c) 2016 Juan Desimoni
 * Modified work Copyright (c) 2017 yx91490
 * Modified work Copyright (c) 2017 Jonathan Hult
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package desi.juan.email.api;

import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static desi.juan.email.api.EmailConstants.DEFAULT_CHARSET;
import static desi.juan.email.api.EmailConstants.DEFAULT_CONTENT_TYPE;
import static java.lang.String.format;

/**
 * Represents and enables the construction of the content of an email with a body of type "text/*" and a specific character
 * encoding.
 */
public class EmailBody {

  /**
   * Text body of the message content.
   */
  private final String content;

  /**
   * @return the body content of the email as a {@link String} value.
   */

  public String getContent() {
    return content;
  }

  /**
   * Format of the body text. Example: "text/html" or "text/plain".
   */
  private final String format;

  /**
   * @return format of the body text. Example: "text/html" or "text/plain".
   */
  public String getFormat() {
    return format;
  }

  /**
   * The charset of the body text
   */
  private final Charset charset;

  /**
   * @return the charset of the body text
   */
  public Charset getCharset() {
    return charset;
  }

  /**
   * @return the content type of the body, includes email format and charset
   */
  public String getContentType() {
    return format("%s; charset=%s", format, charset);
  }

  /**
   * EmailBody constructor
   *
   * @param content
   * @param charset
   * @param emailFormat
   */
  public EmailBody(String content, Charset charset, String emailFormat) {
    if (content == null || charset == null || emailFormat == null) {
      throw new IllegalArgumentException("No param can be null");
    }
    this.content = content;
    this.charset = charset;
    this.format = emailFormat.toLowerCase();
  }

  /**
   * Body with DEFAULT_CONTENT_TYPE.
   *
   * @see #EmailBody(String, String)
   */
  public EmailBody(String content) {
    this(content, DEFAULT_CONTENT_TYPE);
  }

  /**
   * Empty body with DEFAULT_CONTENT_TYPE.
   *
   * @see #EmailBody(String)
   */
  public EmailBody() {
    this("");
  }

  /**
   * @param content
   * @param contentType
   * @see #EmailBody(String, ContentType)
   */
  public EmailBody(String content, String contentType) {
    this(content, toContentType(contentType));
  }

  /**
   * @param content
   * @param contentType
   * @see #EmailBody(String, Charset, String)
   */
  protected EmailBody(String content, ContentType contentType) {
    this(content, getCharset(contentType), getFormat(contentType));
  }

  /**
   * @param contentType
   * @return
   */
  private static Charset getCharset(ContentType contentType) {
    String csParam = contentType.getParameter("charset");
    try {
      String csStr = MimeUtility.decodeText(csParam);
      return Charset.forName(csStr);
    } catch (UnsupportedEncodingException e) {
      return DEFAULT_CHARSET;
    }
  }

  /**
   * @param contentType
   * @return
   */
  private static ContentType toContentType(String contentType) {
    try {
      return new ContentType(contentType);
    } catch (Exception e) {
      // TODO: should this do something else?
      return new ContentType();
    }
  }

  /**
   * @param contentType
   * @return
   */
  private static String getFormat(ContentType contentType) {
    return contentType.getBaseType();
  }
}
