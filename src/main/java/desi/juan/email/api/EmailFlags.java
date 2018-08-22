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

import javax.mail.Folder;

/**
 * This class represents the set of flags on an {@link Email}. Flags are composed of predefined system flags that most
 * {@link Folder} implementations are expected to support.
 */
public class EmailFlags {

  public enum EmailFlag {
    ANSWERED,
    DELETED,
    DRAFT,
    RECENT,
    SEEN
  }

  /**
   * Specifies if this {@link Email} has been answered or not.
   */
  private final boolean isAnswered;

  /**
   * @return if this {@link Email} has been answered or not.
   */
  public boolean isAnswered() {
    return isAnswered;
  }

  /**
   * Specifies if this {@link Email} has been deleted or not.
   */
  private final boolean isDeleted;

  /**
   * @return if this {@link Email} has been deleted or not.
   */
  public boolean isDeleted() {
    return isDeleted;
  }

  /**
   * Specifies if this {@link Email} is a draft or not.
   */
  private final boolean isDraft;

  /**
   * @return if this {@link Email} is a draft or not.
   */
  public boolean isDraft() {
    return isDraft;
  }

  /**
   * Specifies if this {@link Email} is isRecent or not.
   */
  private final boolean isRecent;

  /**
   * @return if this {@link Email} is recent. {@link Folder} implementations set this flag to indicate that this {@link Email} is new to this
   * {@link Folder}, that is, it has arrived since the last time this {@link Folder} was opened.
   */
  public boolean isRecent() {
    return isRecent;
  }

  /**
   * Specifies if this {@link Email} has been seen or not.
   */
  private final boolean isSeen;

  /**
   * @return if this {@link Email} has been isSeen. This flag is implicitly set by the implementation when the the {@link Email} content is
   * returned to the client in some form.
   */
  public boolean isSeen() {
    return isSeen;
  }

  /**
   * Creates a new instance.
   *
   * @param isAnswered
   * @param isDeleted
   * @param isDraft
   * @param isRecent
   * @param isSeen
   */
  public EmailFlags(final boolean isAnswered, final boolean isDeleted, final boolean isDraft, final boolean isRecent, final boolean isSeen) {
    this.isAnswered = isAnswered;
    this.isDeleted = isDeleted;
    this.isDraft = isDraft;
    this.isRecent = isRecent;
    this.isSeen = isSeen;
  }
}
