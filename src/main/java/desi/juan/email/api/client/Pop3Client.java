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
package desi.juan.email.api.client;


import com.google.common.collect.ImmutableList;
import desi.juan.email.api.Email;
import desi.juan.email.api.client.configuration.ClientConfiguration;
import desi.juan.email.internal.commands.DeleteOperations;
import desi.juan.email.internal.commands.FolderOperations;
import desi.juan.email.internal.connection.MailboxManagerConnection;

import javax.mail.Folder;
import javax.mail.MessagingException;

import static desi.juan.email.internal.EmailProtocol.POP3;
import static desi.juan.email.internal.EmailProtocol.POP3S;
import static javax.mail.Folder.READ_ONLY;

/**
 * Encapsulates all the functionality necessary to manage POP3 mailboxes.
 */
public class Pop3Client extends MailboxManagerConnection implements DeleteOperations, FolderOperations {

  /**
   * Default port value for POP3 servers.
   */
  public static final String DEFAULT_POP3_PORT = "110";

  /**
   * Default port value for POP3S servers.
   */
  public static final String DEFAULT_POP3S_PORT = "995";

  /**
   * {@inheritDoc}
   */
  public Pop3Client(final String username,
                    final String password,
                    final String host,
                    final int port,
                    final ClientConfiguration config) {
    super(config.getTlsConfig().isPresent() ? POP3S : POP3,
        username,
        password,
        host,
        port,
        config.getConnectionTimeout(),
        config.getReadTimeout(),
        config.getWriteTimeout(),
        config.getProperties());
  }

  /**
   * @param folder
   * @param isDeleteAfterRetrieve
   * @param numToRetrieve
   * @return
   */
  public ImmutableList<Email> retrieve(final String folder, final boolean isDeleteAfterRetrieve, final int numToRetrieve) {
    final Folder readOnlyFolder = getFolder(folder, READ_ONLY);
    final ImmutableList<Email> emails = retrieve(readOnlyFolder, true, numToRetrieve);
    if (isDeleteAfterRetrieve) {
      //TODO: check input streams after the emails have been deleted
      emails.forEach(e -> {
        try {
          deleteByNumber(readOnlyFolder, e.getNumber());
        } catch (final MessagingException e1) {

          e1.printStackTrace();
          //TODO: this needs to be better logged
        }
      });
    }
    return emails;
  }

  public ImmutableList<Email> retrieve(final String folder, final boolean isDeleteAfterRetrieve) {
    return retrieve(folder, isDeleteAfterRetrieve, FolderOperations.ALL_MESSAGES);
  }

  public ImmutableList<Email> retrieve(final String folder) {
    return retrieve(folder, false);
  }

  public ImmutableList<Email> retrieve(final String folder, final int numToRetrieve) {
    return retrieve(folder, false, numToRetrieve);
  }
}
