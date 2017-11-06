/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Juan Desimoni
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

import com.google.common.base.Charsets;

import java.nio.charset.Charset;

import static desi.juan.email.api.EmailConstants.DEFAULT_CONTENT_TYPE;
import static java.lang.String.format;

/**
 * Represents and enables the construction of the content of an email with a body of type "text/*" and a specific character
 * encoding.
 */
public class EmailBody {

  /**
   * @return the body content of the email as a {@link String} value.
   */
  public String getContent() {
    return body;
  }

  /**
   * @return the content type of the body, one of text/plain or text/html
   */
  public String getContentType() {
    return format("%s; charset=%s", contentType, charset);
  }

  /**
   * Text body of the message content
   */
  private final String body;

  /**
   * ContentType of the body text. Example: "text/html" or "text/plain".
   */
  private final String contentType;

  /**
   * The character encoding of the body.
   */
  private final Charset charset;

  public EmailBody() {
    this("", Charsets.UTF_8, DEFAULT_CONTENT_TYPE);
  }

  public EmailBody(String body, Charset charset, String contentType) {
    if (body == null || contentType == null || charset == null) {
      throw new IllegalArgumentException("param cannot be null");
    }
    this.body = body;
    this.contentType = contentType;
    this.charset = charset;
  }
}
