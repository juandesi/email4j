/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.api;

import static desi.juan.email.api.EmailConstants.TEXT_PLAIN;
import static desi.juan.email.api.EmailConstants.UTF8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import desi.juan.email.internal.DefaultEmailBody;
import desi.juan.email.internal.OutgoingEmail;

/**
 * Implementation of the builder design pattern to create a new {@link Email} instance.
 */
public final class EmailBuilder {

  private String subject = "[No Subject]";
  private List<String> from = new ArrayList<>();
  private List<String> to = new ArrayList<>();
  private List<String> bcc = new ArrayList<>();
  private List<String> cc = new ArrayList<>();
  private Map<String, String> headers = new HashMap<>();
  private List<String> replyTo = new ArrayList<>();
  private List<EmailAttachment> attachments = new ArrayList<>();
  private EmailBody body;

  /**
   * Hide constructor.
   */
  private EmailBuilder() {}

  /**
   * @return an instance of this {@link EmailBuilder}.
   */
  public static EmailBuilder newEmail() {
    return new EmailBuilder();
  }

  /**
   * sets the subject of the  that is being built.
   *
   * @param subject the email subject to be set in the attributes
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * sets the email from addresses
   *
   * @param fromAddresses the addresses to be set in the attributes.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder from(List<String> fromAddresses) {
    this.from = fromAddresses;
    return this;
  }

  /**
   * sets the "To" (primary) recipients of the  that is being built.
   *
   * @param toAddresses the "to" addresses to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder to(List<String> toAddresses) {
    this.to = toAddresses;
    return this;
  }

  /**
   * sets the "Bcc" (blind carbon copy) recipients of the  that is being built.
   *
   * @param bccAddresses the "bcc" addresses to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder bcc(List<String> bccAddresses) {
    this.bcc = bccAddresses;
    return this;
  }

  /**
   * sets the "Cc" (carbon copy) recipients of the  that is being built.
   *
   * @param ccAddresses the "cc" addresses to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder cc(List<String> ccAddresses) {
    this.cc = ccAddresses;
    return this;
  }

  /**
   * sets the additional headers of the  that is being built.
   *
   * @param headers the headers to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder setHeaders(Map<String, String> headers) {
    this.headers = headers;
    return this;
  }

  /**
   * sets the "ReplyTo" addresses of the  that is being built.
   *
   * @param replyToAddresses the "replyTo" addresses to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder replyTo(List<String> replyToAddresses) {
    this.replyTo = replyToAddresses;
    return this;
  }

  /**
   * sets the specified body to the email that is being built.
   */
  public EmailBuilder withBody(EmailBody body){
    this.body = body;
    return this;
  }

  /**
   * sets a plain text body to the email that is being built.
   */
  public EmailBuilder withBody(String body){
    this.body = new DefaultEmailBody(body, TEXT_PLAIN, UTF8);
    return this;
  }

  /**
   * sets a list of attachments to bound in the email that is being built.
   */
  public EmailBuilder withAttachments(List<EmailAttachment> attachments){
    this.attachments = attachments;
    return this;
  }

  /**
   * adds an attachment to the email that is being built.
   */
  public EmailBuilder addAttachments(EmailAttachment attachment){
    this.attachments.add(attachment);
    return this;
  }

  /**
   * builds the new {@link Email} instance.
   */
  public Email build() {

    if (body == null) {
      throw new IllegalStateException("Cannot build a Email message with no body");
    }

    return new OutgoingEmail(subject, from, to, bcc, cc, replyTo, body, attachments, headers);
  }
}

