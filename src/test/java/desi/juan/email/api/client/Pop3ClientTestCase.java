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
import desi.juan.email.api.ReceiveTestCase;
import desi.juan.email.api.client.configuration.ClientConfiguration;
import desi.juan.email.internal.EmailProtocol;
import desi.juan.email.internal.connection.MailboxManagerConnection;
import org.junit.Before;

import static desi.juan.email.api.EmailConstants.INBOX_FOLDER;

public class Pop3ClientTestCase extends ReceiveTestCase {

  private Pop3Client client;

  @Before
  public void before() {
    client = new Pop3Client(USER, PASSWORD, HOST, PORT, new ClientConfiguration());
  }

  @Override
  protected EmailProtocol getProtocol() {
    return EmailProtocol.POP3;
  }

  @Override
  protected ImmutableList<Email> getEmails() {
    return client.retrieve(INBOX_FOLDER, false);
  }

  @Override
  protected MailboxManagerConnection getClient() {
    return client;
  }
}
