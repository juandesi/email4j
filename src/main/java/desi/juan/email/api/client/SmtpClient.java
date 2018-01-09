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


import desi.juan.email.api.Email;
import desi.juan.email.api.client.configuration.ClientConfiguration;
import desi.juan.email.internal.commands.SendOperations;
import desi.juan.email.internal.connection.SenderConnection;

import static desi.juan.email.internal.EmailProtocol.SMTP;
import static desi.juan.email.internal.EmailProtocol.SMTPS;

/**
 * Encapsulates all the functionality necessary to send emails through an SMTP server.
 *
 * This class takes care of all low level details of interacting with an SMTP server and provides a convenient higher level interface.
 */
public class SmtpClient extends SenderConnection implements SendOperations {

  /**
   * Default port value for SMTP servers.
   */
  public static final String DEFAULT_SMTP_PORT = "25";

  /**
   * Default port value for SMTPS servers.
   */
  public static final String DEFAULT_SMTPS_PORT = "587";

  /**
   * {@inheritDoc}
   */
  public SmtpClient(final String username,
                    final String password,
                    final String host,
                    final int port,
                    final ClientConfiguration config) {
    super(config.getTlsConfig().isPresent() ? SMTPS : SMTP,
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
   * @param email
   * @see SendOperations#send(SenderConnection, Email)
   */
  //TODO: should this be OutgoingEmail
  public void send(final Email email) {
    send(this, email);
  }
}
