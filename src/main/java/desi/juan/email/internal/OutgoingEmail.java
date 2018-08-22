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

import com.google.common.collect.ImmutableList;
import desi.juan.email.api.Email;
import desi.juan.email.api.EmailAttachment;
import desi.juan.email.api.EmailBody;
import desi.juan.email.api.EmailFlags;

import javax.mail.Header;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static desi.juan.email.api.EmailConstants.NO_SUBJECT;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

/**
 * An implementation of an {@link Email} to be used for outgoing messages.
 */
public class OutgoingEmail extends Email {

  /**
   * /**
   * Creates a new instance.
   *
   * @param subject
   * @param from
   * @param to
   * @param cc
   * @param bcc
   * @param replyTo
   * @param body
   * @param attachments
   * @param headers
   */
  OutgoingEmail(final String subject,
                final ImmutableList<String> from,
                final ImmutableList<String> replyTo,
                final ImmutableList<String> to,
                final ImmutableList<String> cc,
                final ImmutableList<String> bcc,
                final EmailBody body,
                final ImmutableList<EmailAttachment> attachments,
                final ImmutableList<Header> headers) {
    super(subject, now(), from, replyTo, to, cc, bcc, body, attachments, headers);
    this.bcc = bcc;
  }

  /**
   * Builder to help build a new OutgoingEmail.
   */
  public static class Builder {
    private String subject = NO_SUBJECT;
    private final ImmutableList.Builder<String> from = new ImmutableList.Builder<>();
    private ImmutableList.Builder<String> to = new ImmutableList.Builder<>();
    private ImmutableList.Builder<String> bcc = new ImmutableList.Builder<>();
    private ImmutableList.Builder<String> cc = new ImmutableList.Builder<>();
    private ImmutableList.Builder<String> replyTo = new ImmutableList.Builder<>();
    private EmailBody body;
    private ImmutableList.Builder<EmailAttachment> attachments = new ImmutableList.Builder<>();
    private ImmutableList.Builder<Header> headers = new ImmutableList.Builder<>();

    /**
     * Sets the subject of the email that is being built.
     *
     * @param subject the email subject to be set.
     * @return this {@link Builder}
     */
    public Builder subject(final String subject) {
      this.subject = subject;
      return this;
    }

    /**
     * Adds an {@link Email} "From" address
     *
     * @param from the from address to be added.
     * @return this {@link Builder}
     */
    public Builder from(final String from) {
      this.from.add(from);
      return this;
    }

    /**
     * Adds an {@link Email} "From" address
     *
     * @param from the from addresses to be added.
     * @return this {@link Builder}
     */
    public Builder from(final List<String> from) {
      this.from.addAll(from);
      return this;
    }

    /**
     * Adds a single "ReplyTo" address to the {@link Email} that is being built.
     *
     * @param replyTo the "replyTo" address to be added.
     * @return this {@link Builder}
     */
    public Builder replyTo(final String replyTo) {
      this.replyTo.add(replyTo);
      return this;
    }

    /**
     * Adds "ReplyTo" addresses to the {@link Email} that is being built.
     *
     * @param replyTo the "replyTo" addresses to be added.
     * @return this {@link Builder}
     */
    public Builder replyTo(final List<String> replyTo) {
      this.replyTo.addAll(replyTo);
      return this;
    }

    /**
     * Adds a single "To" (primary) recipient to the {@link Email} that is being built.
     *
     * @param to the "to" address to be added.
     * @return this {@link Builder}
     */
    public Builder to(final String to) {
      this.to.add(to);
      return this;
    }

    /**
     * Adds "To" (primary) recipients to the {@link Email} that is being built.
     *
     * @param to the "to" addresses to be added.
     * @return this {@link Builder}
     */
    public Builder to(final List<String> to) {
      this.to.addAll(to);
      return this;
    }

    /**
     * Adds a single "Cc" (carbon copy) recipient to the {@link Email} that is being built.
     *
     * @param cc the "cc" address to be added.
     * @return this {@link Builder}
     */
    public Builder cc(final String cc) {
      this.cc.add(cc);
      return this;
    }

    /**
     * Adds "Cc" (carbon copy) recipients to the {@link Email} that is being built.
     *
     * @param cc the "cc" addresses to be added.
     * @return this {@link Builder}
     */
    public Builder cc(final List<String> cc) {
      this.cc.addAll(cc);
      return this;
    }

    /**
     * Adds a single "Bcc" (blind carbon copy) recipient to the {@link Email} that is being built.
     *
     * @param bcc the "bcc" address to be added.
     * @return this {@link Builder}
     */
    public Builder bcc(final String bcc) {
      this.bcc.add(bcc);
      return this;
    }

    /**
     * Adds "Bcc" (blind carbon copy) recipients to the {@link Email} that is being built.
     *
     * @param bcc the "bcc" addresses to be added.
     * @return this {@link Builder}
     */
    public Builder bcc(final List<String> bcc) {
      this.bcc.addAll(bcc);
      return this;
    }

    /**
     * Adds a Header of the {@link Email} that is being built.
     *
     * @param key the key name of the header.
     * @param val the value of the header.
     * @return this {@link Builder}
     */
    public Builder header(final String key, final String val) {
      this.headers.add(new Header(key, val));
      return this;
    }

    /**
     * Sets the specified {@link EmailBody} to the {@link Email} that is being built.
     *
     * @param body
     * @return this {@link Builder}
     */
    public Builder body(final EmailBody body) {
      this.body = body;
      return this;
    }

    /**
     * Sets a plain text {@link EmailBody} to the {@link Email} that is being built.
     *
     * @param body
     * @return this {@link Builder}
     */
    public Builder body(final String body) {
      return body(new EmailBody(body));
    }

    /**
     * Adds an {@link EmailAttachment} to the {@link Email} that is being built.
     *
     * @return this {@link Builder}
     */
    public Builder attachment(final EmailAttachment attachment) {
      this.attachments.add(attachment);
      return this;
    }

    /**
     * Adds a {@link List} of {@link EmailAttachment}s to the {@link Email} that is being built.
     *
     * @return this {@link Builder}
     */
    public Builder attachment(final List<EmailAttachment> attachments) {
      this.attachments.addAll(attachments);
      return this;
    }

    /**
     * Builds a new {@link Email} instance.
     */
    public Email build() {
      return new OutgoingEmail(
          subject,
          from.build(),
          replyTo.build(),
          to.build(),
          cc.build(),
          bcc.build(),
          body,
          attachments.build(),
          headers.build()
      );
    }
  }

  /**
   * The recipient addresses of "Bcc" (blind carbon copy) type.
   */
  private final ImmutableList<String> bcc;

  /**
   * The recipient addresses of "Bcc" (blind carbon copy) type.
   */
  @Override
  public ImmutableList<String> getBcc() {
    return bcc;
  }

  @Override
  public int getNumber() {
    throw new UnsupportedOperationException(format("%s%s%s", ERROR_DO_NOT_CONTAIN, "a number which is", ERROR_SET_WHEN));
  }

  @Override
  public long getId() {
    throw new UnsupportedOperationException(format("%s%s%s", ERROR_DO_NOT_CONTAIN, "an id which is", ERROR_SET_WHEN));
  }

  @Override
  public Optional<LocalDateTime> getReceivedDate() {
    throw new UnsupportedOperationException(format("%s%s%s", ERROR_DO_NOT_CONTAIN, "a received date which is", ERROR_SET_WHEN));
  }

  @Override
  public EmailFlags getFlags() {
    throw new UnsupportedOperationException(format("%s%s%s", ERROR_DO_NOT_CONTAIN, "flags, which are", ERROR_SET_WHEN));
  }

  private static final String ERROR_EMAILS = "OutgoingEmails";
  private static final String ERROR_DO_NOT_CONTAIN = ERROR_EMAILS + " do not contain ";
  private static final String ERROR_SET_WHEN = " set when the Email arrives to a folder.";
}
