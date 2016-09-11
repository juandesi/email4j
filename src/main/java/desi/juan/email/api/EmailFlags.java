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

/**
 * The {@link EmailFlags} class represents the set of flags on a Message. Flags are composed of predefined system flags that most
 * folder implementations are expected to support.
 */
public class EmailFlags {

  public enum EmailFlag {
    SEEN,
    DRAFT,
    ANSWERED,
    RECENT,
    DELETED
  }

  /**
   * Specifies if the email message has been answered or not.
   */
  private final boolean answered;

  /**
   * Specifies if the email message has been deleted or not.
   */
  private final boolean deleted;

  /**
   * Specifies if the email message is a draft or not.
   */
  private final boolean draft;

  /**
   * Specifies if the email message is recent or not.
   */
  private final boolean recent;

  /**
   * Specifies if the email message has been seen or not.
   */
  private final boolean seen;

  public EmailFlags(boolean answered, boolean deleted, boolean draft, boolean recent, boolean seen) {
    this.answered = answered;
    this.deleted = deleted;
    this.draft = draft;
    this.recent = recent;
    this.seen = seen;
  }

  /**
   * @return if this message has been answered.
   */
  public boolean isAnswered() {
    return answered;
  }

  /**
   * @return if this message has been isDeleted.
   */
  public boolean isDeleted() {
    return deleted;
  }

  /**
   * @return if this message is a isDraft.
   */
  public boolean isDraft() {
    return draft;
  }

  /**
   * @return if this message is isRecent. Folder implementations set this flag to indicate that this message is new to this
   *         folder, that is, it has arrived since the last time this folder was opened.
   */
  public boolean isRecent() {
    return recent;
  }

  /**
   * @return if this message has been isSeen. This flag is implicitly set by the implementation when the the email content is
   *         returned to the client in some form.
   */
  public boolean isSeen() {
    return seen;
  }

}
