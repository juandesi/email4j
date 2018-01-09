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
package desi.juan.email.internal.commands;

import desi.juan.email.api.EmailFlags;
import desi.juan.email.api.EmailFlags.EmailFlag;
import desi.juan.email.internal.exception.RetrieveEmailException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static desi.juan.email.api.EmailFlags.EmailFlag.DELETED;
import static java.lang.String.format;
import static javax.mail.Flags.Flag;
import static javax.mail.Folder.READ_WRITE;

/**
 * Represents an operation that marks an email with one of the {@link EmailFlags}. This operation can only be used on IMAP Mailboxes.
 * POP3 does not support flags.
 */
public interface FlagOperations {

  static void markById(final UIDFolder folder, final EmailFlag flag, final long uid) {
    final Message message;
    try {
      message = folder.getMessageByUID(uid);
    } catch (final MessagingException e) {
      throw new RetrieveEmailException(format("Error while retrieving the email of id:[%s]", uid, flag));
    }
    markByMessage(message, flag);
  }

  static void markByMessage(final Message message, final EmailFlag flag) {
    //TODO: is this necessary? does setFlag method already check this?
    // Doing this check may harm performance (especially if called in loop where messages are all in same folder)
    verifyFolderMode(message.getFolder(), READ_WRITE);
    try {
      message.setFlag(FlagMap.get(flag), true);
    } catch (final MessagingException e) {
      throw new RetrieveEmailException(format("Error while marking the email:[%s] as [%s]", message, flag));
    }
  }

  /**
   * Mark an email with a flag
   *
   * @param messages
   * @param flag
   */
  static void markByMessage(final Message[] messages, final EmailFlag flag) {
    for (final Message currentMessage : messages) {
      markByMessage(currentMessage, flag);
    }
  }

  /**
   * Deletes a message and expunges folder.
   *
   * @param message
   */
  static void delete(final Message message) {
    markByMessage(message, DELETED);
    expungeFolder(message.getFolder());
  }

  /**
   * Deletes a message and expunges folder of first message.
   *
   * @param messages
   */
  static void delete(final Message[] messages) {
    markByMessage(messages, DELETED);
    //TODO: messages could be in separate folders all of which may need to be expunged
    expungeFolder(messages[0].getFolder());
  }

  /**
   * Calls expunge on folder.
   *
   * @param folder
   */
  static void expungeFolder(final Folder folder) {
    try {
      folder.expunge();
    } catch (final MessagingException e) {
      throw new RetrieveEmailException(format("Error while expunging folder:[%s]", folder));
    }
  }

  /**
   * Verify that folder mode matches
   *
   * @param folder
   * @param openMode mode to check against folder
   */
  static void verifyFolderMode(final Folder folder, final int openMode) {
    if (folder.getMode() != openMode)
      throw new IllegalStateException(format("Folder mode must be in mode:[%s]", openMode));
  }

  enum FlagMap {

    ANSWERED(EmailFlag.ANSWERED, Flag.ANSWERED),
    DELETED(EmailFlag.DELETED, Flag.DELETED),
    DRAFT(EmailFlag.DRAFT, Flag.DRAFT),
    RECENT(EmailFlag.RECENT, Flag.RECENT),
    SEEN(EmailFlag.SEEN, Flag.SEEN);

    private final EmailFlag emailFlag;
    private final Flag flag;
    private static final Map<EmailFlag, Flag> map = Collections.unmodifiableMap(initializeMapping());

    FlagMap(final EmailFlag emailFlag, final Flag flag) {
      this.emailFlag = emailFlag;
      this.flag = flag;
    }

    private static Flag get(final EmailFlag emailFlag) {
      if (map.containsKey(emailFlag)) {
        return map.get(emailFlag);
      }
      return null;
    }

    private static Map<EmailFlag, Flag> initializeMapping() {
      final Map<EmailFlag, Flag> map = new HashMap<>();
      for (final FlagMap s : FlagMap.values()) {
        map.put(s.emailFlag, s.flag);
      }
      return map;
    }
  }
}
