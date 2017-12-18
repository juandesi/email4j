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
package desi.juan.email.internal;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import desi.juan.email.api.Email;
import desi.juan.email.api.EmailAttachment;
import desi.juan.email.api.EmailBody;
import desi.juan.email.api.EmailFlags;

import javax.mail.Folder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.copyOf;
import static java.time.LocalDateTime.now;

/**
 * Contains all the metadata of an email, it carries information such as the subject of the email, the id in the mailbox and the
 * recipients between others.
 */
public class OutgoingEmail implements Email {

  /**
   * The address(es) of the person(s) which sent the email.
   * <p>
   * This will usually be the sender of the email, but some emails may direct replies to a different address
   */
  private final List<String> fromAddresses;

  /**
   * The recipient addresses of "To" (primary) type.
   */
  private final List<String> toAddresses;

  /**
   * The recipient addresses of "Cc" (carbon copy) type
   */
  private final List<String> ccAddresses;

  /**
   * The recipient addresses of "Bcc" (blind carbon copy) type
   */
  private final List<String> bccAddresses;

  /**
   * The email addresses to which this email should reply.
   */
  private final List<String> replyToAddresses;

  /**
   * The headers that this email carry.
   */
  private final Multimap<String, String> headers;

  /**
   * The subject of the email.
   */
  private final String subject;

  /**
   * The time where the email was sent.
   * <p>
   * Different {@link Folder} implementations may assign this value or not.
   */
  private final LocalDateTime sentDate;

  /**
   * the attachments bounded to be sent with the email.
   */
  private final List<EmailAttachment> attachments;

  /**
   * the text body of the email.
   */
  private final EmailBody body;

  /**
   * Creates a new instance.
   */
  public OutgoingEmail(String subject,
                       List<String> fromAddresses,
                       List<String> toAddresses,
                       List<String> bccAddresses,
                       List<String> ccAddresses,
                       List<String> replyToAddresses,
                       EmailBody body,
                       List<EmailAttachment> attachments,
                       Multimap<String, String> headers) {
    this.subject = subject;
    this.sentDate = now();
    this.toAddresses = copyOf(toAddresses);
    this.ccAddresses = copyOf(ccAddresses);
    this.bccAddresses = copyOf(bccAddresses);
    this.fromAddresses = copyOf(fromAddresses);
    this.replyToAddresses = copyOf(replyToAddresses);
    this.body = body;
    this.attachments = copyOf(attachments);
    this.headers = ImmutableMultimap.copyOf(headers);
  }

  @Override
  public int getNumber() {
    throw new UnsupportedOperationException("Outgoing emails does not contain number, the number is set when the message arrives to a folder");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getId() {
    throw new UnsupportedOperationException("Outgoing emails does not contain id, the id is set when the message arrives to a folder");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getReplyToAddresses() {
    return replyToAddresses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSubject() {
    return subject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getToAddresses() {
    return toAddresses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getBccAddresses() {
    return bccAddresses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getCcAddresses() {
    return ccAddresses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getFromAddresses() {
    return fromAddresses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<LocalDateTime> getSentDate() {
    return Optional.ofNullable(sentDate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Multimap<String, String> getHeaders() {
    return headers != null ? ImmutableMultimap.copyOf(headers) : ImmutableMultimap.of();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmailBody getBody() {
    return body;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EmailAttachment> getAttachments() {
    return attachments;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<LocalDateTime> getReceivedDate() {
    throw new UnsupportedOperationException("Outgoing emails does not contain a received date, the received date is set when the message arrives to a folder");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmailFlags getFlags() {
    throw new UnsupportedOperationException("Outgoing emails are not flagged, flags are set when the message arrives to a folder");
  }
}
