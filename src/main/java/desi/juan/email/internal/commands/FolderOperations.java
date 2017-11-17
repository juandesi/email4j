/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Juan Desimoni
 * Copyright (c) 2017 Jonathan Hult
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

import static java.lang.String.format;

import java.util.Date;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;
import javax.mail.search.SearchTerm;

import desi.juan.email.api.Email;
import desi.juan.email.internal.StoredEmail;
import desi.juan.email.internal.exception.RetrieveEmailException;

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
   *
   * @return Email
   */
  default Email retrieveById(UIDFolder folder, long uid) {
    try {
      Message email = folder.getMessageByUID(uid);
      return new StoredEmail(email, uid, true);
    } catch (MessagingException e) {
      throw new RetrieveEmailException(format("Cannot retrieve email id:[%s] from folder [%s]", uid, folder));
    }
  }

  /**
   * Helper method. Retrieves all messages in folder.
   *
   * @see #retrieve(Folder, boolean, int)
   *
   * @param folder
   * @param readContent
   *
   * @return List<Email>
   */
  default List<Email> retrieve(Folder folder, boolean readContent) {
    return retrieve(folder, readContent, ALL_MESSAGES);
  }

  /**
   *
   * Helper method
   * @see FolderOperationsInternal#getMessages(Folder, boolean, int)
   * @see FolderOperationsInternal#toStoredList(Message[], boolean)
   *
   * @param folder
   * @param readContent
   * @param numToRetrieve
   *
   * @return List<Email>
   *
   */
  default List<Email> retrieve(Folder folder, boolean readContent, int numToRetrieve) {
    Message[] emails = FolderOperationsInternal.getMessages(folder, readContent, numToRetrieve);
    return FolderOperationsInternal.toStoredList(emails, readContent);
  }

  /**
   * Helper method
   *
   * @see FolderOperationsInternal#search(Folder, SearchTerm...)
   * @see FolderOperationsInternal#toStoredList(Message[], boolean)
   *
   * @param folder
   * @param readContent
   * @param terms
   *
   * @return List<Email>
   */
  default List<Email> search(Folder folder, boolean readContent, SearchTerm... terms) throws MessagingException {
    Message[] emails = FolderOperationsInternal.search(folder, terms);
    return FolderOperationsInternal.toStoredList(emails, readContent);
  }

  /**
   * Helper method
   *
   * @see FolderOperationsInternal#search(Folder, Date, Date)
   * @see FolderOperationsInternal#toStoredList(Message[], boolean)
   *
   * @param folder
   * @param readContent
   * @param olderThan
   * @param newerThan
   *
   * @return List<Email>
   */
  default List<Email> search(Folder folder, boolean readContent, Date olderThan, Date newerThan) throws MessagingException {
    Message[] emails = FolderOperationsInternal.search(folder, olderThan, newerThan);
    return FolderOperationsInternal.toStoredList(emails, readContent);
  }

  /**
   * Helper method
   * @see FolderOperationsInternal#getMessages(Folder, boolean, int)
   * @see FolderOperationsInternal#move(Folder, Message[], Folder)
   * @see FolderOperationsInternal#toStoredList(Message[], boolean)
   *
   * @param fromFolder
   * @param readContent
   * @param numToRetrieve
   * @param moveToFolder
   *
   * @return List<Email>
   */
  default List<Email> retrieveAndMove(Folder fromFolder, boolean readContent, int numToRetrieve, Folder moveToFolder) throws MessagingException {
    Message[] emails = FolderOperationsInternal.getMessages(fromFolder, readContent, numToRetrieve);
    List<Email> storedEmails = FolderOperationsInternal.toStoredList(emails, readContent);
    if (emails.length > 1) {
      FolderOperationsInternal.move(fromFolder, emails, moveToFolder);
    }
    return storedEmails;
  }

  /**
   * Helper method
   * @see FolderOperationsInternal#search(Folder, SearchTerm...)
   * @see FolderOperationsInternal#move(Folder, Message[], Folder)
   * @see FolderOperationsInternal#toStoredList(Message[], boolean)
   *
   * @param searchInfolder
   * @param readContent
   * @param terms
   * @param moveToFolder
   *
   * @return List<Email>
   */
  default List<Email> searchAndMove(Folder searchInfolder, boolean readContent, Folder moveToFolder, SearchTerm... terms) throws MessagingException {
    Message[] emails = FolderOperationsInternal.search(searchInfolder, terms);
    List<Email> storedEmails = FolderOperationsInternal.toStoredList(emails, readContent);
    if (emails.length > 1) {
      FolderOperationsInternal.move(searchInfolder, emails, moveToFolder);
    }
    return storedEmails;
  }

  /**
   * Helper method
   * @see FolderOperationsInternal#search(Folder, Date, Date)
   * @see FolderOperationsInternal#move(Folder, Message[], Folder)
   * @see FolderOperationsInternal#toStoredList(Message[], boolean)
   *
   * @param searchInfolder
   * @param readContent
   * @param moveToFolder
   * @param newerThan
   * @param olderThan
   *
   * @return List<Email>
   */
  default List<Email> searchAndMove(Folder searchInfolder, boolean readContent, Folder moveToFolder, Date olderThan, Date newerThan) throws MessagingException {
    Message[] emails = FolderOperationsInternal.search(searchInfolder, olderThan, newerThan);
    List<Email> storedEmails = FolderOperationsInternal.toStoredList(emails, readContent);
    if (emails.length > 1) {
      FolderOperationsInternal.move(searchInfolder, emails, moveToFolder);
    }
    return storedEmails;
  }

  /**
   * Helper method
   * @see FolderOperationsInternal#move(Folder, boolean, int, Folder)
   *
   * @param fromFolder
   * @param readContent
   * @param numToRetrieve
   * @param toFolder
   */
  default void move(Folder fromFolder, boolean readContent, int numToRetrieve, Folder toFolder) throws MessagingException {
    FolderOperationsInternal.move(fromFolder, readContent, numToRetrieve, toFolder);
  }
}
