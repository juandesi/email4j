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
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import desi.juan.email.api.Email;
import desi.juan.email.internal.StoredEmail;
import desi.juan.email.internal.exception.RetrieveEmailException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import java.util.Date;
import java.util.List;

import static desi.juan.email.internal.commands.FolderOperations.ALL_MESSAGES;
import static java.lang.Long.parseLong;
import static java.lang.String.format;

/**
 * Class that contains the folder email operations.
 */
public class FolderOperationsInternal {
  /**
   * Retrieves limited number of the emails in the specified {@code folderName}.
   * <p>
   * For folder implementations (like IMAP) that support fetching without reading the content, if the content should NOT be read
   * ({@code readContent} = false) the SEEN flag is not going to be set.
   *
   * @param folder
   * @param readContent
   * @param numToRetrieve
   * @return Message[]
   * @see Folder#getMessages(int, int)
   */
  protected static Message[] getMessages(Folder folder, boolean readContent, int numToRetrieve) {
    //TODO: how is readContent being used
    try {
      // if supposed to retrieve all messages, set numToRetrieve to number of messages in folder
      if (numToRetrieve == ALL_MESSAGES) {
        numToRetrieve = folder.getMessageCount();
      }
      return folder.getMessages(1, numToRetrieve);
    } catch (MessagingException me) {
      throw new RetrieveEmailException("Error while retrieving emails", me);
    }
  }

  /**
   * Transform array of messages into List<Email>.
   *
   * @param messages
   * @param readContent
   * @return List<Email>
   * @see #getEmailUid(Folder, Message)
   * @see Message#getFolder()
   */
  public static List<Email> toStoredList(Message[] messages, boolean readContent) {
    ImmutableList.Builder<Email> emailsBuilder = ImmutableList.builder();
    for (Message message : messages) {
      long uid = getEmailUid(message.getFolder(), message);
      emailsBuilder.add(new StoredEmail(message, uid, readContent));
    }
    return emailsBuilder.build();
  }

  /**
   * Get UID of email message.
   *
   * @param folder
   * @param message
   * @return UID of email message
   */
  protected static long getEmailUid(Folder folder, Message message) {
    try {
      if (folder instanceof POP3Folder) {
        return parseLong(((POP3Folder) folder).getUID(message));
      }
      if (folder instanceof UIDFolder) {
        return ((UIDFolder) folder).getUID(message);
      }
    } catch (MessagingException e) {
      throw new RetrieveEmailException(format("Cannot retrieve email:[%s] from folder [%s]", message, folder));
    }
    //TODO: maybe this should fail instead.
    return -1;
  }

  /**
   * Search in folder using terms combined with AND.
   *
   * @param folder
   * @param terms
   * @return Message[]
   * @see Folder#search(SearchTerm)
   */
  protected static Message[] search(Folder folder, SearchTerm... terms) throws MessagingException {
    SearchTerm andTerm = new AndTerm(terms);
    return folder.search(andTerm);
  }

  /**
   * Helper method
   *
   * @param folder
   * @param olderThan
   * @param newerThan
   * @return Message[]
   * @throws MessagingException
   * @see #search(Folder, Date, Date)
   */
  protected static Message[] search(Folder folder, Date olderThan, Date newerThan) throws MessagingException {
    SearchTerm ot = new ReceivedDateTerm(ComparisonTerm.LT, olderThan);
    SearchTerm nt = new ReceivedDateTerm(ComparisonTerm.GT, newerThan);

    return search(folder, ot, nt);
  }

  /**
   * Move messages from one folder to another.
   *
   * @param fromFolder
   * @param messages
   * @param toFolder
   * @throws MessagingException
   * @see Folder#copyMessages(Message[], Folder)
   * @see FlagOperations#delete(Message[])
   */
  public static void move(Folder fromFolder, Message[] messages, Folder toFolder) throws MessagingException {
    if (messages.length < 1) {
      throw new MessagingException("No messages to move");
    }
    //try {
    if (fromFolder instanceof IMAPFolder) {
      IMAPFolder imapFolder = (IMAPFolder) fromFolder;
      imapFolder.moveMessages(messages, toFolder);
    } else {
      fromFolder.copyMessages(messages, toFolder);
      //TODO: should we wait?
      //fromFolder.wait(2000);
      FlagOperations.delete(messages);
    }
    // } catch (InterruptedException e) {
    //  e.printStackTrace();
    // }
  }

  /**
   * Helper method
   *
   * @param fromFolder
   * @param message
   * @param toFolder
   * @see #move(Folder, Message[], Folder)
   */
  protected static void move(Folder fromFolder, Message message, Folder toFolder) throws MessagingException {
    move(fromFolder, new Message[]{message}, toFolder);
  }

  /**
   * Helper method
   *
   * @param fromFolder
   * @param readContent
   * @param numToRetrieve
   * @param toFolder
   * @throws MessagingException
   * @see #move(Folder, Message[], Folder)
   * @see #getMessages(Folder, boolean, int)
   */
  protected static void move(Folder fromFolder, boolean readContent, int numToRetrieve, Folder toFolder) throws MessagingException {
    move(fromFolder, getMessages(fromFolder, readContent, numToRetrieve), toFolder);
  }
}
