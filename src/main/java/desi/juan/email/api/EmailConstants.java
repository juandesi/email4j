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

import com.google.common.base.Charsets;

import java.nio.charset.Charset;

/**
 * Common email constant values.
 */
public final class EmailConstants {

  /**
   * No subject.
   */
  public static final String NO_SUBJECT = "[No Subject]";

  /**
   * Default folder name for all the mailboxes.
   */
  public static final String INBOX_FOLDER = "INBOX";

  /**
   * Defines all the multipart content types.
   */
  public static final String MULTIPART = "multipart/*";

  /**
   * Defines all the text content types.
   */
  public static final String TEXT = "text/*";

  /**
   * Plain text format.
   */
  public static final String TEXT_PLAIN = "text/plain";

  /**
   * HTML format.
   */
  public static final String TEXT_HTML = "text/html";

  /**
   * The default charset.
   */
  public static final Charset DEFAULT_CHARSET = Charsets.US_ASCII;

  /**
   * The default content type.
   */
  public static final String DEFAULT_CONTENT_TYPE = TEXT_PLAIN + "; charset=" + DEFAULT_CHARSET;

  /**
   * Hide constructor.
   */
  private EmailConstants() {
  }
}
