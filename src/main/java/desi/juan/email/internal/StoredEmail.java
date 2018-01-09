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
import com.sun.mail.pop3.POP3Folder;
import desi.juan.email.api.Email;
import desi.juan.email.api.EmailAttachment;
import desi.juan.email.api.EmailBody;
import desi.juan.email.api.EmailFlags;

import javax.mail.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static javax.mail.Flags.Flag.*;
import static javax.mail.Message.RecipientType.*;

/**
 * An implementation of an {@link Email} to be used for stored messages.
 */
public class StoredEmail extends Email {

  /**
   * The number is the relative position of the {@link Email} in its {@link Folder}. Note that the id for a particular {@link Email} can change during a
   * session if other {@link Email}s in the {@link Folder} are isDeleted and expunged.
   * <p>
   * Valid message numbers start at 1. {@link Email}s that do not belong to any {@link Folder} (like newly composed or derived messages) have 0 as
   * their message number.
   */
  private final int number;

  /**
   * {@inheritDoc}
   */
  @Override
  public int getNumber() {
    return number;
  }

  /**
   * The unique identifier of the {@link Email} in a {@link Folder}, this id can be used to retrieve {@link Email}s from some mailbox {@link Folder}s, but
   * keep in mind that not all the {@link Folder}s can retrieve {@link Email}s by id ({@link POP3Folder}} for example).
   */
  private final long id;

  /**
   * {@inheritDoc}
   */
  @Override
  public long getId() {
    return id;
  }

  /**
   * The time where the {@link Email} was received.
   * <p>
   * Different {@link Folder} implementations may assign this value or not.
   * <p>
   * If this is a sent {@link Email} this will be null.
   */
  private final LocalDateTime receivedDate;

  @Override
  public Optional<LocalDateTime> getReceivedDate() {
    return Optional.ofNullable(receivedDate);
  }

  /**
   * The {@link EmailFlags} bounded on the {@link Email}.
   */
  private final EmailFlags flags;

  @Override
  public EmailFlags getFlags() {
    return flags;
  }

  /**
   * Creates a new instance.
   *
   * If the {@code readContent} parameter is true then the {@link Email} is going to be opened
   * and flagged as SEEN, otherwise the {@link EmailBody} and {@link EmailAttachment}s are not going to be obtained.
   *
   * @param message
   * @param readContent
   */
  StoredEmail(Message message, long id, boolean readContent) throws MessagingException {
    super(message.getSubject(),
        parseAddressArray(message.getFrom()),
        parseAddressArray(message.getRecipients(TO)),
        parseAddressArray(message.getRecipients(CC)),
        parseAddressArray(message.getRecipients(BCC)),
        parseAddressArray(message.getReplyTo()),
        parseDate(message.getSentDate()),
        getBody(message, readContent),
        getAttachments(message, readContent),
        ImmutableList.copyOf(Collections.list(message.getAllHeaders())));

    this.id = id;
    this.number = message.getMessageNumber();
    this.receivedDate = parseDate(message.getReceivedDate());
    this.flags = parseFlags(message.getFlags());
  }

  /**
   * Get the {@link EmailAttachment}s.
   *
   * @param message
   * @param readContent
   * @return a {@link ImmutableList} of {@link EmailAttachment}s.
   */
  private static ImmutableList<EmailAttachment> getAttachments(Message message, boolean readContent) {
    if (readContent) {
      EmailContentProcessor processor = EmailContentProcessor.getInstance(message);
      return processor.getAttachments();
    } else {
      return ImmutableList.of();
    }
  }

  /**
   * @param message
   * @param readContent
   * @return EmailBody which contains the body from the message. EmailBody will be empty if readContent is false
   * @throws MessagingException
   */
  private static EmailBody getBody(Message message, boolean readContent) throws MessagingException {
    if (readContent) {
      EmailContentProcessor processor = EmailContentProcessor.getInstance(message);
      return new EmailBody(processor.getBody(), message.getContentType());
    } else {
      return new EmailBody();
    }
  }

  /**
   *
   * @return ImmutableList with the {@link Address}es as {@link String}s.
   */
    return ImmutableList.of();
  }
}
}
