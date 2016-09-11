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

import static java.util.Collections.emptyMap;

import java.util.Map;

/**
 * Represents and enables the construction of an email attachment.
 */
public final class EmailAttachment {

  /**
   * the name of the attachment.
   */
  private String id;
  /**
   * the content of the attachment.
   */
  private Object content;

  /**
   * the content type of the attachment content.
   */
  private String contentType;

  /**
   * the headers bounded to the attachment
   */
  private final Map<String, String> headers;

  public EmailAttachment(String id, Object content, String contentType) {
    this(id, content, contentType, emptyMap());
  }

  public EmailAttachment(String id, Object content, String contentType, Map<String, String> headers) {
    this.id = id;
    this.content = content;
    this.contentType = contentType;
    this.headers = headers;
  }

  /**
   * @return the name of the attachment.
   */
  public String getId() {
    return id;
  }

  /**
   * @return the content of the attachment.
   */
  public Object getContent() {
    return content;
  }

  /**
   * @return the content type of the attachment content.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @return a set of headers that are specific for this attachment.
   * Such as Content-Type or Content-Disposition.
   */
  public Map<String, String> getHeaders() {
    return headers;
  }
}
