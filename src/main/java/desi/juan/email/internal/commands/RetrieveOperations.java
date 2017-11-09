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
package desi.juan.email.internal.commands;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

import com.google.common.collect.ImmutableList;
import com.sun.mail.pop3.POP3Folder;

import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;

import desi.juan.email.api.Email;
import desi.juan.email.internal.StoredEmail;
import desi.juan.email.internal.exception.RetrieveEmailException;

/**
 * Class that contains all the retrieve emails operations.
 */
public interface RetrieveOperations {

  int ALL_MESSAGES = Integer.MAX_VALUE;

  /**
   * Retrieves limited number of the emails in the specified {@code folderName}.
   * <p>
   * For folder implementations (like IMAP) that support fetching without reading the content, if the content should NOT be read
   * ({@code readContent} = false) the SEEN flag is not going to be set.
   */
  default List<Email> retrieve(Folder folder, boolean readContent, int numToRetrieve) {
    ImmutableList.Builder<Email> emailsBuilder = ImmutableList.builder();
    try {
      // if supposed to retrieve all messages, set numToRetrieve to number of messages in folder
      if (numToRetrieve == ALL_MESSAGES) {
        numToRetrieve = folder.getMessageCount();
      }
      for (Message message : folder.getMessages(1, numToRetrieve)) {
        long uid = getEmailUid(folder, message);
        emailsBuilder.add(new StoredEmail(message, uid, readContent));
      }
      return emailsBuilder.build();
    } catch (MessagingException me) {
      throw new RetrieveEmailException("Error while retrieving emails", me);
    }
  }

  /**
   * Retrieves all the emails in the specified {@code folderName}.
   * <p>
   * For folder implementations (like IMAP) that support fetching without reading the content, if the content should NOT be read
   * ({@code readContent} = false) the SEEN flag is not going to be set.
   */
  default List<Email> retrieve(Folder folder, boolean readContent) {
    return retrieve(folder, readContent, ALL_MESSAGES);
  }

  default Email retrieveById(UIDFolder folder, long uid) {
    try {
      return new StoredEmail(folder.getMessageByUID(uid), uid, true);
    } catch (MessagingException e) {
      throw new RetrieveEmailException(format("Cannot retrieve email id:[%s] from folder [%s]", uid, folder));
    }
  }

  default long getEmailUid(Folder folder, Message message) throws MessagingException {
    if (folder instanceof POP3Folder) {
      return parseLong(((POP3Folder) folder).getUID(message));
    }
    if (folder instanceof UIDFolder) {
      return ((UIDFolder) folder).getUID(message);
    }
    //TODO: maybe this should fail instead.
    return -1;
  }
}
