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

import com.google.common.collect.ImmutableList;

import javax.mail.Header;

/**
 * Represents and enables the construction of an email attachment.
 */
public final class EmailAttachment extends EmailData {

  /**
   * The name of the attachment.
   */
  private final String id;

  /**
   * @return The name of the attachment.
   */
  public String getId() {
    return id;
  }

  /**
   * The {@link Header}s for this attachment.
   */
  private final ImmutableList<Header> headers;

  /**
   * @return a {@link ImmutableList} of {@link Header}s that are specific for this attachment. Examples: Content-Type or Content-Disposition.
   */
  public ImmutableList<Header> getHeaders() {
    return headers;
  }

  /**
   * Creates a new instance. {@link Header}s will be empty.
   *
   * @param id
   * @param content
   * @param contentType
   */
  public EmailAttachment(final String id, final Object content, final String contentType) {
    this(id, content, contentType, ImmutableList.of());
  }

  /**
   * Creates a new instance.
   *
   * @param id
   * @param content
   * @param contentType
   * @param headers
   */
  public EmailAttachment(final String id, final Object content, final String contentType, final ImmutableList<Header> headers) {
    super(content, contentType);
    this.headers = headers;
    this.id = id;
  }
}
