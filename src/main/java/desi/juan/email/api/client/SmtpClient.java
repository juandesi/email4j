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


import static desi.juan.email.internal.EmailProtocol.SMTP;
import static desi.juan.email.internal.EmailProtocol.SMTPS;

import java.util.Optional;

import desi.juan.email.api.Email;
import desi.juan.email.api.client.configuration.ClientConfiguration;
import desi.juan.email.api.security.TlsConfiguration;
import desi.juan.email.internal.commands.SendCommand;
import desi.juan.email.internal.connection.SenderConnection;

/**
 * Encapsulates all the functionality necessary to send emails through an SMTP server.
 * <p>
 * This class takes care of all low level details of interacting with an SMTP server and provides a
 * convenient higher level interface
 */
public class SmtpClient {

  private SenderConnection connection;
  private SendCommand sendCommand = new SendCommand();

  public SmtpClient(String username,
                    String password,
                    String host,
                    int port,
                    ClientConfiguration configuration) {
    Optional<TlsConfiguration> tls = configuration.getTlsConfig();
    this.connection = new SenderConnection(tls.isPresent() ? SMTPS : SMTP,
                                           username,
                                           password,
                                           host,
                                           port,
                                           configuration.getConnectionTimeout(),
                                           configuration.getReadTimeout(),
                                           configuration.getWriteTimeout(),
                                           configuration.getProperties());
  }

  public void send(Email email) {
    sendCommand.send(connection, email);
  }
}
