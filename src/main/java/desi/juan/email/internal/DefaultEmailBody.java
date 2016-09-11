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
package desi.juan.email.internal;


import static java.lang.String.format;

import desi.juan.email.api.EmailBody;

/**
 * Represents and enables the construction of the content of an email with a body of type "text/*" and a specific character
 * encoding.
 */
public class DefaultEmailBody implements EmailBody {

  /**
   * Text body of the message content
   */
  private String body;

  /**
   * ContentType of the body text. Example: "text/html" or "text/plain".
   */
  private String contentType;

  /**
   * The character encoding of the body.
   */
  private String charset;


  public DefaultEmailBody(String body, String contentType, String charset) {
    this.body = body;
    this.contentType = contentType;
    this.charset = charset;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getContent() {
    return body;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getContentType() {
    return format("%s; charset=%s", contentType, charset);
  }
}
