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

import com.google.common.collect.ImmutableList;
import desi.juan.email.api.Email;
import desi.juan.email.api.EmailTools;
import desi.juan.email.internal.StoredEmail;
import desi.juan.email.internal.exception.RetrieveEmailException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;
import javax.mail.search.SearchTerm;
import java.util.Date;

import static java.lang.String.format;

/**
 * Interface for folder operations.
 */
public interface FolderOperations {

  int ALL_MESSAGES = Integer.MAX_VALUE;

  /**
   * Retrieve emailby UID
   *
   * @param folder
   * @param uid
   * @return Email
   */
  default Email retrieveById(final UIDFolder folder, final long uid) {
    try {
      final Message message = folder.getMessageByUID(uid);
      final StoredEmail.Builder builder = new StoredEmail.Builder();
      builder.message(message).id(uid).readContent(true).build();
      return builder.build();
    } catch (final MessagingException e) {
      throw new RetrieveEmailException(format("Cannot retrieve email id:[%s] from folder [%s]", uid, folder));
    }
  }

  /**
   * Helper method. Retrieves all {@link Email}s in a {@link Folder}.
   *
   * @param folder
   * @param readContent
   * @return ImmutableList<Email>
   * @see #retrieve(Folder, boolean, int)
   */
  default ImmutableList<Email> retrieve(final Folder folder, final boolean readContent) {
    return retrieve(folder, readContent, ALL_MESSAGES);
  }

  /**
   * Helper method.
   *
   * @param folder
   * @param readContent
   * @param numToRetrieve
   * @return immutable list of Emails.
   * @see EmailTools#getMessages(Folder, boolean, int)
   * @see EmailTools#toStoredList(Message[], boolean)
   */
  default ImmutableList<Email> retrieve(final Folder folder, final boolean readContent, final int numToRetrieve) {
    final Message[] emails = EmailTools.getMessages(folder, readContent, numToRetrieve);
    return EmailTools.toStoredList(emails, readContent);
  }

  /**
   * Helper method.
   *
   * @param folder
   * @param readContent
   * @param terms
   * @return immutable list of Emails.
   * @see EmailTools#search(Folder, SearchTerm...)
   * @see EmailTools#toStoredList(Message[], boolean)
   */
  default ImmutableList<Email> search(final Folder folder, final boolean readContent, final SearchTerm... terms) throws MessagingException {
    final Message[] emails = EmailTools.search(folder, terms);
    return EmailTools.toStoredList(emails, readContent);
  }

  /**
   * Helper method.
   *
   * @param folder
   * @param readContent
   * @param olderThan
   * @param newerThan
   * @return immutable list of Emails
   * @see EmailTools#search(Folder, Date, Date)
   * @see EmailTools#toStoredList(Message[], boolean)
   */
  default ImmutableList<Email> search(final Folder folder, final boolean readContent, final Date olderThan, final Date newerThan) throws MessagingException {
    final Message[] emails = EmailTools.search(folder, olderThan, newerThan);
    return EmailTools.toStoredList(emails, readContent);
  }

  /**
   * Helper method.
   *
   * @param fromFolder
   * @param readContent
   * @param numToRetrieve
   * @param moveToFolder
   * @return immutable list of Emails.
   * @see EmailTools#getMessages(Folder, boolean, int)
   * @see EmailTools#move(Folder, Message[], Folder)
   * @see EmailTools#toStoredList(Message[], boolean)
   */
  default ImmutableList<Email> retrieveAndMove(final Folder fromFolder, final boolean readContent, final int numToRetrieve, final Folder moveToFolder) throws MessagingException {
    final Message[] emails = EmailTools.getMessages(fromFolder, readContent, numToRetrieve);
    final ImmutableList<Email> storedEmails = EmailTools.toStoredList(emails, readContent);
    if (emails.length > 1) {
      EmailTools.move(fromFolder, emails, moveToFolder);
    }
    return storedEmails;
  }

  /**
   * Helper method.
   *
   * @param searchInfolder
   * @param readContent
   * @param terms
   * @param moveToFolder
   * @return immutable list of Emails.
   * @see EmailTools#search(Folder, SearchTerm...)
   * @see EmailTools#move(Folder, Message[], Folder)
   * @see EmailTools#toStoredList(Message[], boolean)
   */
  default ImmutableList<Email> searchAndMove(final Folder searchInfolder, final boolean readContent, final Folder moveToFolder, final SearchTerm... terms) throws MessagingException {
    final Message[] emails = EmailTools.search(searchInfolder, terms);
    final ImmutableList<Email> storedEmails = EmailTools.toStoredList(emails, readContent);
    if (emails.length > 1) {
      EmailTools.move(searchInfolder, emails, moveToFolder);
    }
    return storedEmails;
  }

  /**
   * Helper method
   *
   * @param searchInfolder
   * @param readContent
   * @param moveToFolder
   * @param newerThan
   * @param olderThan
   * @return immutable list of Emails.
   * @see EmailTools#search(Folder, Date, Date)
   * @see EmailTools#move(Folder, Message[], Folder)
   * @see EmailTools#toStoredList(Message[], boolean)
   */
  default ImmutableList<Email> searchAndMove(final Folder searchInfolder, final boolean readContent, final Folder moveToFolder, final Date olderThan, final Date newerThan) throws MessagingException {
    final Message[] emails = EmailTools.search(searchInfolder, olderThan, newerThan);
    final ImmutableList<Email> storedEmails = EmailTools.toStoredList(emails, readContent);
    if (emails.length > 1) {
      EmailTools.move(searchInfolder, emails, moveToFolder);
    }
    return storedEmails;
  }

  /**
   * Helper method.
   *
   * @param fromFolder
   * @param readContent
   * @param numToRetrieve
   * @param toFolder
   * @see EmailTools#move(Folder, boolean, int, Folder)
   */
  default void move(final Folder fromFolder, final boolean readContent, final int numToRetrieve, final Folder toFolder) throws MessagingException {
    EmailTools.move(fromFolder, readContent, numToRetrieve, toFolder);
  }
}
