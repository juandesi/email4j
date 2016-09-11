/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.internal;

import static com.google.common.collect.ImmutableList.copyOf;
import static java.time.LocalDateTime.now;

import com.google.common.collect.ImmutableMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.Folder;

import desi.juan.email.api.Email;
import desi.juan.email.api.EmailAttachment;
import desi.juan.email.api.EmailBody;
import desi.juan.email.api.EmailFlags;

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
  private final Map<String, String> headers;

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
                       Map<String, String> headers) {
    this.subject = subject;
    this.sentDate = now();
    this.toAddresses = copyOf(toAddresses);
    this.ccAddresses = copyOf(ccAddresses);
    this.bccAddresses = copyOf(bccAddresses);
    this.fromAddresses = copyOf(fromAddresses);
    this.replyToAddresses = copyOf(replyToAddresses);
    this.body = body;
    this.attachments = copyOf(attachments);
    this.headers = ImmutableMap.copyOf(headers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getId() {
    throw new UnsupportedOperationException("Outgoing emails does not contain id");
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
    return Optional.of(sentDate);
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
    throw new UnsupportedOperationException("Outgoing emails does not contain a received date");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmailFlags getFlags() {
    throw new UnsupportedOperationException("Outgoing emails are not flagged");
  }
}
