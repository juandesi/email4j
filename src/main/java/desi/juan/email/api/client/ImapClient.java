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


import desi.juan.email.api.client.configuration.ClientConfiguration;
import desi.juan.email.internal.commands.DeleteOperations;
import desi.juan.email.internal.commands.FolderOperations;
import desi.juan.email.internal.connection.MailboxManagerConnection;
import desi.juan.email.internal.exception.EmailException;

import javax.mail.Folder;
import javax.mail.UIDFolder;

import static desi.juan.email.internal.EmailProtocol.IMAP;
import static desi.juan.email.internal.EmailProtocol.IMAPS;
import static java.lang.String.format;

/**
 * Encapsulates all the functionality necessary to manage IMAP mailboxes.
 */
public class ImapClient extends MailboxManagerConnection implements DeleteOperations, FolderOperations {

  /**
   * Default port value for IMAP servers.
   */
  public static final String DEFAULT_IMAP_PORT = "143";

  /**
   * Default port value for IMAPS servers.
   */
  public static final String DEFAULT_IMAPS_PORT = "993";

  /**
   * {@inheritDoc}
   */
  public ImapClient(final String username,
                    final String password,
                    final String host,
                    final int port,
                    final ClientConfiguration config) {
    super(config.getTlsConfig().isPresent() ? IMAPS : IMAP,
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
   * @param openMode
   * @return
   */
  public UIDFolder getUIDFolder(final String folder, final int openMode) {
    final Folder uidFolder = getFolder(folder, openMode);
    if (uidFolder instanceof UIDFolder) {
      return (UIDFolder) uidFolder;
    }
    throw new EmailException(format("the specified folder:[%s] does not support fetching emails by id", folder));
  }
}
