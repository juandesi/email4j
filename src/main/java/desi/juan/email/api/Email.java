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
import desi.juan.email.internal.OutgoingEmail;

import javax.mail.Folder;
import javax.mail.Header;
import java.time.LocalDateTime;
import java.util.Optional;

import static desi.juan.email.api.EmailConstants.NO_SUBJECT;
import static java.lang.String.format;

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
   * The address(es) of the person(s) who sent this Email. This is usually just one person but may be multiple.
   *
   * This field can be faked and does not confirm the person is who they claim to be.
   */
  private final ImmutableList<String> from;

  /**
   * Get the address(es) of the person(s) who sent this Email.
   *
   * @return a {@link ImmutableList} with the from addresses.
   */
  public ImmutableList<String> getFrom() {
    return from;
  }

  /**
   * The addresses to which this Email should reply.
   */
  private final ImmutableList<String> replyTo;

  /**
   * Get the addresses to which replies should be directed. This will usually be the same as fromAddresses, but may be different.
   *
   * @return all the recipient addresses of replyTo type.
   */
  public final ImmutableList<String> getReplyTo() {
    return replyTo;
  }

  /**
   * The recipient addresses of "To" (primary) type.
   */
  private final ImmutableList<String> to;

  /**
   * @return a {@link ImmutableList} with all the recipient addresses of "To" (primary) type.
   */
  public final ImmutableList<String> getTo() {
    return to;
  }

  /**
   * The recipient addresses of "Cc" (carbon copy) type.
   */
  private final ImmutableList<String> cc;

  /**
   * @return a {@link ImmutableList} with all the recipient addresses of "Cc" (carbon copy) type.
   */
  public ImmutableList<String> getCc() {
    return cc;
  }

  /**
   * @return a {@link ImmutableList} with all the recipient addresses of "Bcc" (blind carbon copy) type.
   */
  public abstract ImmutableList<String> getBcc();

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
   * Get the {@link LocalDateTime} this Email was received.
   *
   * @return the {@link LocalDateTime} this Email was received.
   */
  public abstract Optional<LocalDateTime> getReceivedDate();

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
   * The {@link Header}s of this Email.
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
   * @param sentDate
   * @param from
   * @param replyTo
   * @param to
   * @param cc
   * @param bcc
   * @param body
   * @param attachments
   * @param headers
   */
  protected Email(final String subject,
                  final LocalDateTime sentDate,
                  final ImmutableList<String> from,
                  final ImmutableList<String> replyTo,
                  final ImmutableList<String> to,
                  final ImmutableList<String> cc,
                  final ImmutableList<String> bcc,
                  final EmailBody body,
                  final ImmutableList<EmailAttachment> attachments,
                  final ImmutableList<Header> headers) {

    if (from.isEmpty()) {
      throw new IllegalStateException(format("%s%s", CANNOT_BUILD, ERROR_FROM));
    }

    if (body == null) {
      throw new IllegalStateException(format("%s%s", CANNOT_BUILD, ERROR_BODY));
    }

    // For an OutgoingEmail - TO, CC, or BCC must be present
    if (getClass().equals(OutgoingEmail.class)) {

      if (to.isEmpty() && cc.isEmpty() && bcc.isEmpty()) {
        throw new IllegalStateException(format("%s%s", CANNOT_BUILD, ERROR_RECIPIENTS));
      }
    }

    // For a StoredEmail - TO, CC and BCC may all be empty. This is because the TO and CC may have originally been
    // blank and we received it because we were BCC'd.

    //TODO: should check RFCs to determine what can and cannot be empty/null here - e.g. could headers ever be empty

    if (subject == null) {
      this.subject = NO_SUBJECT;
    } else {
      this.subject = subject;
    }

    this.sentDate = sentDate;

    this.from = from;
    this.replyTo = replyTo;

    this.to = to;
    this.cc = cc;

    this.body = body;

    this.attachments = attachments;

    this.headers = headers;
  }

  private static final String CANNOT_BUILD = "Cannot build an Email with no ";
  public static final String ERROR_FROM = "FROM address(es)";
  public static final String ERROR_RECIPIENTS = "TO, CC or BCC address(es). One of these must be present";
  public static final String ERROR_BODY = "body";
}
