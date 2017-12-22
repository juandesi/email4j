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

import javax.mail.Folder;
import javax.mail.Header;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.copyOf;

/**
 * Generic contract for Email implementations.
 */
public abstract class Email {

  /**
   * @return the number is the relative position of this Email in its {@link Folder}.
   *
   * Valid message numbers start at 1. Emails that do not belong to any {@link Folder} (like newly composed or derived messages) have 0 as
   * their message number.
   */
  public abstract int getNumber();

  /**
   * Get the unique id of this Email.
   *
   * @return this Email's id
   */
  public abstract long getId();

  /**
   * The subject of this {@link Email}.
   */
  private final String subject;

  /**
   * @return the subject of this Email.
   */
  public final String getSubject() {
    return subject;
  }

  /**
   * The recipient addresses of "To" (primary) type.
   */
  private final ImmutableList<String> toAddresses;

  /**
   * @return a {@link ImmutableList} with all the recipient addresses of "To" (primary) type.
   */
  public final ImmutableList<String> getToAddresses() {
    return toAddresses;
  }

  /**
   * The recipient addresses of "Bcc" (blind carbon copy) type.
   */
  private final ImmutableList<String> bccAddresses;

  /**
   * @return a {@link ImmutableList} with all the recipient addresses of "Bcc" (blind carbon copy) type.
   */
  public final ImmutableList<String> getBccAddresses() {
    return bccAddresses;
  }

  /**
   * The recipient addresses of "Cc" (carbon copy) type.
   */
  private final ImmutableList<String> ccAddresses;

  /**
   * @return a {@link ImmutableList} with all the recipient addresses of "Cc" (carbon copy) type.
   */
  public ImmutableList<String> getCcAddresses() {
    return ccAddresses;
  }

  /**
   * The address(es) of the person(s) who sent this Email. This is usually just one person but may be multiple.
   *
   * This field can be faked and does not confirm the person is who they claim to be.
   */
  private final ImmutableList<String> fromAddresses;

  /**
   * Get the address(es) of the person(s) who sent this Email.
   *
   * @return a {@link ImmutableList} with the from addresses.
   */
  public ImmutableList<String> getFromAddresses() {
    return fromAddresses;
  }

  /**
   * The addresses to which this Email should reply.
   */
  private final ImmutableList<String> replyToAddresses;

  /**
   * Get the addresses to which replies should be directed. This will usually be the same as fromAddresses, but may be different.
   *
   * @return all the recipient addresses of replyTo type.
   */
  public final ImmutableList<String> getReplyToAddresses() {
    return replyToAddresses;
  }

  /**
   * Get the {@link LocalDateTime} this Email was received.
   *
   * @return the {@link LocalDateTime} this Email was received.
   */
  public abstract Optional<LocalDateTime> getReceivedDate();

  /**
   * The time where the email was sent.
   *
   * Different {@link Folder} implementations may assign this value or not.
   */
  private final LocalDateTime sentDate;

  /**
   * Get the {@link LocalDateTime} this Email was sent.
   *
   * @return the {@link LocalDateTime} this Email was sent.
   */
  public final Optional<LocalDateTime> getSentDate() {
    return Optional.ofNullable(sentDate);
  }

  /**
   * @return an {@link EmailFlags} containing the flags set in the Email.
   */
  public abstract EmailFlags getFlags();

  /**
   * The body of this Email
   */
  private final EmailBody body;

  /**
   * Get the body of this Email.
   *
   * @return the {@link EmailBody} of this Email.
   */
  public final EmailBody getBody() {
    return body;
  }

  /**
   * A {@link ImmutableList} with all the {@link EmailAttachment}s of this Email.
   */
  private final ImmutableList<EmailAttachment> attachments;

  /**
   * Get the attachments of this Email. An empty {@link ImmutableList} is returned when there are no {@link EmailAttachment}s.
   *
   * @return a {@link ImmutableList} with the {@link EmailAttachment}s of this Email.
   */
  public final ImmutableList<EmailAttachment> getAttachments() {
    return attachments;
  }

  /**
   * The headers of this Email.
   */
  private final ImmutableList<Header> headers;

  /**
   * @return all the @{link Header}s of this Email.
   */
  public final ImmutableList<Header> getHeaders() {
    return headers;
  }

  /**
   * Creates a new instance.
   *
   * @param subject
   * @param fromAddresses
   * @param toAddresses
   * @param bccAddresses
   * @param ccAddresses
   * @param replyToAddresses
   * @param body
   * @param attachments
   * @param headers
   */
  protected Email(final String subject,
                  final ImmutableList<String> fromAddresses,
                  final ImmutableList<String> toAddresses,
                  final ImmutableList<String> ccAddresses,
                  final ImmutableList<String> bccAddresses,
                  final ImmutableList<String> replyToAddresses,
                  final LocalDateTime sentDate,
                  final EmailBody body,
                  final ImmutableList<EmailAttachment> attachments,
                  final ImmutableList<Header> headers) {
    this.subject = subject;
    this.sentDate = sentDate;
    this.toAddresses = copyOf(toAddresses);
    this.ccAddresses = copyOf(ccAddresses);
    this.bccAddresses = copyOf(bccAddresses);
    this.fromAddresses = copyOf(fromAddresses);
    this.replyToAddresses = copyOf(replyToAddresses);
    this.body = body;
    this.attachments = copyOf(attachments);
    this.headers = copyOf(headers);
  }
}
