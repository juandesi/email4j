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

import static java.lang.String.format;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;

import desi.juan.email.internal.exception.RetrieveEmailException;

/**
 * Class that contains the delete email operations.
 */
public final class DeleteOperations {

  public void deleteById(UIDFolder folder, long uid) {
    try {
      Message message = folder.getMessageByUID(uid);
      message.setFlag(Flag.DELETED, true);
      ((Folder)folder).expunge();
    } catch (MessagingException e) {
      throw new RetrieveEmailException(format("Error while deleting email of id:[%s] from folder", uid));
    }
  }

  public void deleteByNumber(Folder folder, int number) {
    try {
      Message message = folder.getMessage(number);
      message.setFlag(Flag.DELETED, true);
      folder.expunge();
    } catch (MessagingException e) {
      throw new RetrieveEmailException(format("Error while deleting email number:[%s] from folder [%s]", number, folder.getName()));
    }
  }
}
