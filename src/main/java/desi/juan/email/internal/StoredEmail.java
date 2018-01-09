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
import desi.juan.email.internal.exception.RetrieveEmailException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static desi.juan.email.api.EmailTools.parseAddressArray;
import static desi.juan.email.api.EmailTools.parseDate;
import static desi.juan.email.api.EmailTools.parseFlags;
import static javax.mail.Message.RecipientType.BCC;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;

/**
 * An implementation of an {@link Email} to be used for stored messages.
 */
public class StoredEmail extends Email {

  /**
   * Creates a new instance.
   *
   * If the {@code readContent} parameter is true then the {@link Email} is going to be opened and flagged as SEEN, otherwise the {@link EmailBody} and {@link EmailAttachment}s are not going to be obtained.
   *
   * @param message
   * @param readContent
   */
  StoredEmail(final Message message, final long id, final boolean readContent) throws MessagingException {
    super(message.getSubject(),
        parseDate(message.getSentDate()),
        parseAddressArray(message.getFrom()),
        parseAddressArray(message.getReplyTo()),
        parseAddressArray(message.getRecipients(TO)),
        parseAddressArray(message.getRecipients(CC)),
        parseAddressArray(message.getRecipients(BCC)),
        //TODO: this is bad to call getEmailContentProcessor twice since it is executing logic twice
        EmailContentProcessor.getInstance(message, readContent).getBody(),
        EmailContentProcessor.getInstance(message, readContent).getAttachments(),
        ImmutableList.copyOf(Collections.list(message.getAllHeaders())));

    this.id = id;
    this.number = message.getMessageNumber();
    this.receivedDate = parseDate(message.getReceivedDate());
    this.flags = parseFlags(message.getFlags());
  }

  /**
   * Builder to help build a new StoredEmail.
   */
  public static class Builder {

    private Message message;
    private long id;
    private boolean readContent;

    public Builder message(final Message message) {
      this.message = message;
      return this;
    }

    public Builder id(final long id) {
      this.id = id;
      return this;
    }

    public Builder readContent(final boolean readContent) {
      this.readContent = readContent;
      return this;
    }

    /**
     * Builds a new {@link Email} instance.
     */
    public Email build() {
      try {
        return new StoredEmail(message, id, readContent);
      } catch (final MessagingException e) {
        throw new RetrieveEmailException("Error while wrapping incoming email", e);
      }
    }
  }

  /**
   * The number is the relative position of the {@link Email} in its {@link Folder}. Note that the id for a particular {@link Email} can change during a
   * session if other {@link Email}s in the {@link Folder} are isDeleted and expunged.
   * <p>
   * Valid message numbers start at 1. {@link Email}s that do not belong to any {@link Folder} (like newly composed or derived messages) have 0 as
   * their message number.
   */
  private final int number;

  @Override
  public int getNumber() {
    return number;
  }

  /**
   * The unique identifier of the {@link Email} in a {@link Folder}, this id can be used to retrieve {@link Email}s from some mailbox {@link Folder}s, but
   * keep in mind that not all the {@link Folder}s can retrieve {@link Email}s by id ({@link POP3Folder}} for example).
   */
  private final long id;

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

  //TODO: find RFC

  /**
   * This will always be empty per RFC
   *
   * @return
   */
  @Override
  public ImmutableList<String> getBcc() {
    return ImmutableList.of();
  }
}
