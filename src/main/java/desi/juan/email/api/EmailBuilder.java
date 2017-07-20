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

import static desi.juan.email.api.EmailConstants.TEXT_PLAIN;
import static desi.juan.email.api.EmailConstants.UTF8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
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
  private Multimap<String, String> headers = ImmutableMultimap.of();
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
   * sets the subject of the email that is being built.
   *
   * @param subject the email subject to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * adds an email from address
   *
   * @param fromAddress the from address to be added.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder from(String fromAddress) {
    this.from.add(fromAddress);
    return this;
  }

  /**
   * adds "To" (primary) recipients to the email that is being built.
   *
   * @param toAddresses the "to" addresses to be added.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder to(List<String> toAddresses) {
    this.to.addAll(toAddresses);
    return this;
  }

  /**
   * adds "Bcc" (blind carbon copy) recipients to the email that is being built.
   *
   * @param bccAddresses the "bcc" addresses to be added.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder bcc(List<String> bccAddresses) {
    this.bcc.addAll(bccAddresses);
    return this;
  }

  /**
   * adds a "Bcc" (blind carbon copy) recipients to the email that is being built.
   *
   * @param bcc the "bcc" address to be added.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder bcc(String bcc) {
    this.bcc.add(bcc);
    return this;
  }

  /**
   * adds a "to" (primary) recipients to the email that is being built.
   *
   * @param to the "to" address to be added.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder to(String to) {
    this.to.add(to);
    return this;
  }

  /**
   * adds "Cc" (carbon copy) recipients to the email that is being built.
   *
   * @param ccAddresses the "cc" addresses to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder cc(List<String> ccAddresses) {
    this.cc.addAll(ccAddresses);
    return this;
  }

  /**
   * adds a single "Cc" (carbon copy) recipient to the email that is being built.
   *
   * @param cc the "cc" address to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder cc(String cc) {
    this.cc.add(cc);
    return this;
  }

  /**
   * sets additional headers to the email that is being built.
   *
   * @param headers the headers to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder withHeaders(Multimap<String, String> headers) {
    this.headers.putAll(headers);
    return this;
  }  
  
  /**
   * sets an additional header of the email that is being built.
   *
   * @param key the key name of the header.
   * @param val the value of the header.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder withHeader(String key, String val) {
    this.headers.put(key, val);
    return this;
  }

  /**
   * adds "ReplyTo" addresses to the email that is being built.
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
    this.attachments.addAll(attachments);
    return this;
  }

  /**
   * adds an attachment to the email that is being built.
   */
  public EmailBuilder withAttachment(EmailAttachment attachment){
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

    if (to.isEmpty())
    {
      throw new IllegalStateException("Cannot build a Email message with no TO recipient addresses");
    }

    if (from.isEmpty())
    {
      throw new IllegalStateException("Cannot build a Email message with no from address");
    }

    return new OutgoingEmail(subject, from, to, bcc, cc, replyTo, body, attachments, headers);
  }
}

