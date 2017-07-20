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

import static desi.juan.email.api.EmailConstants.TEXT;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static javax.mail.Message.RecipientType.BCC;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;

import com.google.common.collect.ImmutableMap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import desi.juan.email.api.Email;
import desi.juan.email.api.EmailAttachment;
import desi.juan.email.api.EmailBody;
import desi.juan.email.api.EmailFlags;
import desi.juan.email.internal.exception.RetrieveEmailException;

/**
 * Contains all the metadata of an email, it carries information such as the subject of the email, the id in the mailbox and the
 * recipients between others.
 */
public class StoredEmail implements Email {

  /**
   * The number is the relative position of the email in its Folder. Note that the id for a particular email can change during a
   * session if other emails in the Folder are isDeleted and expunged.
   * <p>
   * Valid message numbers start at 1. Emails that do not belong to any folder (like newly composed or derived messages) have 0 as
   * their message number.
   */
  private final int number;

  /**
   * The unique identifier of the email in a folder, this id can be used to retrieve emails from some mailbox folders, but
   * take in mind that not all the folders can retrieve emails by id (POP3 folders for example).
   */
  private final long id;

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
  private final Map<String, String> headers;

  /**
   * The subject of the email.
   */
  private final String subject;

  /**
   * The time where the email was received.
   * <p>
   * Different {@link Folder} implementations may assign this value or not.
   * <p>
   * If this is a sent email this will be null.
   */
  private final LocalDateTime receivedDate;

  /**
   * The time where the email was sent.
   * <p>
   * Different {@link Folder} implementations may assign this value or not.
   */
  private final LocalDateTime sentDate;

  /**
   * The flags bounded on the email.
   */
  private final EmailFlags flags;

  /**
   * The body of the email
   */
  private final EmailBody body;

  /**
   * A list with all the attachments bounded to this email.
   */
  private final List<EmailAttachment> attachments;

  /**
   * Creates a new instance.
   *
   * If the {@code readContent} parameter is true then the email is going to be opened
   * and flagged as SEEN, otherwise the body and attachments are not going to be obtained.
   */
  public StoredEmail(Message message, long id, boolean readContent) {
    try {
      this.id = id;
      this.number = message.getMessageNumber();
      this.subject = message.getSubject();
      this.sentDate = parseDate(message.getSentDate());
      this.receivedDate = parseDate(message.getReceivedDate());
      this.toAddresses = parseAddressArray(message.getRecipients(TO));
      this.ccAddresses = parseAddressArray(message.getRecipients(CC));
      this.bccAddresses = parseAddressArray(message.getRecipients(BCC));
      this.fromAddresses = parseAddressArray(message.getFrom());
      this.replyToAddresses = parseAddressArray(message.getReplyTo());
      this.headers = parseHeaders(message.getAllHeaders());
      this.flags = parseFlags(message.getFlags());

      if (readContent) {
        EmailContentProcessor processor = EmailContentProcessor.getInstance(message);
        this.body = new DefaultEmailBody(processor.getBody(), TEXT, "");
        this.attachments = processor.getAttachments();
      } else {
        this.body = new EmptyEmailBody();
        this.attachments = emptyList();
      }

    } catch (MessagingException e) {
      throw new RetrieveEmailException("Error while wrapping incoming email", e);
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int getNumber() {
    return number;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getId() {
    return id;
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
  public Optional<LocalDateTime> getReceivedDate() {
    return Optional.ofNullable(receivedDate);
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
  public Map<String, String> getHeaders() {
    return headers != null ? ImmutableMap.copyOf(headers) : ImmutableMap.of();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmailFlags getFlags() {
    return flags;
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
   * Parses an array of {@link Address}es to a {@link List}.
   */
  private List<String> parseAddressArray(Address[] toAddresses) {
    if (toAddresses != null) {
      return stream(toAddresses).map(Object::toString).collect(toList());
    }
    return emptyList();
  }

  /**
   * Parses all the {@link Message} headers to a {@link Map}
   */
  private Map<String, String> parseHeaders(Enumeration headers) {
    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    while (headers.hasMoreElements()) {
      Header header = (Header) headers.nextElement();
      builder.put(header.getName(), header.getValue());
    }
    return builder.build();
  }

  /**
   * Parses the flags of a {@link Message} to an {@link EmailFlags} instance.
   */
  private EmailFlags parseFlags(Flags flags) {
    return new EmailFlags(flags.contains(Flag.ANSWERED),
                          flags.contains(Flag.DELETED),
                          flags.contains(Flag.DRAFT),
                          flags.contains(Flag.RECENT),
                          flags.contains(Flag.SEEN));
  }

  /**
   * Parses a not null {@link Date} to a {@link LocalDateTime} instance.
   */
  private LocalDateTime parseDate(Date date) {
    if (date == null) {
      return null;
    }
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }
}
