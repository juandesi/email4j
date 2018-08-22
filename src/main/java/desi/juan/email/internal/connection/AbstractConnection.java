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
package desi.juan.email.internal.connection;

import desi.juan.email.internal.EmailProtocol;
import desi.juan.email.internal.exception.EmailConnectionException;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Map;
import java.util.Properties;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Generic implementation for an email connection of a connector which operates over the SMTP, IMAP, POP3 and its secure versions
 * protocols.
 * <p>
 * Performs the creation of a persistent set of properties that are used to configure the {@link Session} instance.
 */
public abstract class AbstractConnection {

  private static final String ERROR = "%s provided but no %s was specified.";

  final Session session;

  private final EmailProtocol protocol;

  public final EmailProtocol getProtocol() {
    return protocol;
  }

  /**
   * @param protocol
   * @param username
   * @param password
   * @param host
   * @param port
   * @param connectionTimeout Socket connection timeout value in seconds
   * @param readTimeout       Socket read timeout value in seconds
   * @param writeTimeout      Socket write timeout value in seconds
   * @param properties
   * @throws EmailConnectionException
   */
  AbstractConnection(final EmailProtocol protocol,
                     final String username,
                     final String password,
                     final String host,
                     final int port,
                     final long connectionTimeout,
                     final long readTimeout,
                     final long writeTimeout,
                     final Map<String, String> properties) throws EmailConnectionException {
    this.protocol = protocol;
    final Properties sessionProperties = buildBasicSessionProperties(host, port, connectionTimeout, readTimeout, writeTimeout);

    if (properties != null) {
      sessionProperties.putAll(properties);
    }

    UserPassAuthenticator authenticator = null;
    if (shouldAuthenticate(username, password)) {
      sessionProperties.setProperty(protocol.getMailAuthProperty(), "true");
      authenticator = new UserPassAuthenticator(username, password);
    }

    session = Session.getInstance(sessionProperties, authenticator);
  }


  /**
   * Creates a new {@link Properties} instance and set all the basic properties required by the specified {@code protocol}.
   */
  private Properties buildBasicSessionProperties(final String host,
                                                 final int port,
                                                 final long connectionTimeout,
                                                 final long readTimeout,
                                                 final long writeTimeout) throws EmailConnectionException {
    final Properties props = new Properties();
    props.setProperty(protocol.getPortProperty(), Integer.toString(port));
    props.setProperty(protocol.getHostProperty(), host);
    props.setProperty(protocol.getReadTimeoutProperty(), Long.toString(SECONDS.toMillis(readTimeout)));
    props.setProperty(protocol.getConnectionTimeoutProperty(), Long.toString(SECONDS.toMillis(connectionTimeout)));
    props.setProperty(protocol.getWriteTimeoutProperty(), Long.toString(SECONDS.toMillis(writeTimeout)));
    props.setProperty(protocol.getTransportProtocolProperty(), protocol.getName());
    return props;
  }

  /**
   * @return the email {@link Session} used by the connection.
   */
  public Session getSession() {
    return session;
  }

  /**
   * Checks the consistency of the username and password parameters and returns whether we should authenticate with the server or
   * not.
   *
   * @param username the specified username.
   * @param password the specified password.
   */
  private boolean shouldAuthenticate(final String username, final String password) {
    if (username == null && password != null) {
      throw new EmailConnectionException(String.format(ERROR, "Password", "username"));
    }
    if (username != null && password == null) {
      throw new EmailConnectionException(String.format(ERROR, "Username", "password"));
    }
    return username != null;
  }

  /**
   * An {@link Authenticator} implementation, that enables the authentication with
   * a server through username and password.
   */
  private final class UserPassAuthenticator extends Authenticator {

    private String username;
    private String password;

    UserPassAuthenticator(final String username, final String password) {
      this.username = username;
      this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(username, password);
    }
  }
}
