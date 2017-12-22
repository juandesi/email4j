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
import java.util.Optional;

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
   * @param fromAddresses
   * @param toAddresses
   * @param bccAddresses
   * @param ccAddresses
   * @param replyToAddresses
   * @param body
   * @param attachments
   * @param headers
   */
  OutgoingEmail(String subject,
                final ImmutableList<String> fromAddresses,
                final ImmutableList<String> toAddresses,
                final ImmutableList<String> bccAddresses,
                final ImmutableList<String> ccAddresses,
                final ImmutableList<String> replyToAddresses,
                EmailBody body,
                final ImmutableList<EmailAttachment> attachments,
                final ImmutableList<Header> headers) {
    super(subject, fromAddresses, toAddresses, ccAddresses, bccAddresses, replyToAddresses, now(), body, attachments, headers);
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
