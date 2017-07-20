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
package desi.juan.email.api.client;


import static desi.juan.email.internal.EmailProtocol.POP3;
import static desi.juan.email.internal.EmailProtocol.POP3S;
import static javax.mail.Folder.READ_ONLY;

import java.util.List;

import javax.mail.Folder;

import desi.juan.email.api.Email;
import desi.juan.email.api.client.configuration.ClientConfiguration;

/**
 * Encapsulates all the functionality necessary to manage POP3 mailboxes.
 */
public class Pop3Client extends AbstractMailboxManagerClient {

  /**
   * Default port value for POP3 servers.
   */
  public static final String DEFAULT_POP3_PORT = "110";

  /**
   * Default port value for POP3S servers.
   */
  public static final String DEFAULT_POP3S_PORT = "995";


  public Pop3Client(String username,
                    String password,
                    String host,
                    int port,
                    ClientConfiguration config)
  {
    super(config.getTlsConfig().isPresent() ? POP3S : POP3, username, password, host, port, config);
  }

  public List<Email> retrieve(String folder, boolean deleteAfterRetrieve) {
    Folder f = connection.getFolder(folder, READ_ONLY);
    List<Email> emails = retriever.retrieve(f, true);
    if (deleteAfterRetrieve) {
      //TODO: check input streams after the emails has been deleted
      emails.forEach(e -> deleter.deleteByNumber(f, e.getNumber()));
    }
    return emails;
  }

}
