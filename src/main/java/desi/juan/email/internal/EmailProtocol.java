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
package desi.juan.email.internal;

import java.net.Socket;

import static java.lang.String.format;

/**
 * Represents an Email Protocol.
 *
 * Each protocol have a set of properties that need to be configured to establish a connection with a mail server.
 *
 * The full list of properties available for protocols can be found at:
 *
 * @see com.sun.mail.pop3
 * @see com.sun.mail.imap
 * @see com.sun.mail.smtp
 */
public enum EmailProtocol {

  /**
   * represents the Simple Mail Transfer Protocol.
   */
  SMTP("smtp", false),

  /**
   * represents the secured Simple Mail Transfer Protocol.
   */
  SMTPS("smtp", true),

  /**
   * represents the Internet Message Access Protocol.
   */
  IMAP("imap", false),

  /**
   * represents the secured Internet Message Access Protocol.
   */
  IMAPS("imaps", true),

  /**
   * represents the Post Office Protocol.
   */
  POP3("pop3", false),

  /**
   * represents the secured Post Office Protocol.
   */
  POP3S("pop3s", true);

  private final String name;
  private final boolean secure;

  /**
   * Creates an instance.
   *
   * @param name   the name of the protocol.
   * @param secure
   */
  EmailProtocol(final String name, final boolean secure) {
    this.name = name;
    this.secure = secure;
  }

  /**
   * The name of the protocol.
   *
   * @return the name of the protocol.
   */
  public String getName() {
    return name;
  }

  /**
   * If the protocol is secure or not.
   *
   * @return whether a protocol is secured by SSL/TLS or not.
   */
  public boolean isSecure() {
    return secure;
  }

  /**
   * The host name of the mail server.
   *
   * @return the protocol host name property.
   */
  public String getHostProperty() {
    return unmaskProperty("mail.%s.host");
  }

  /**
   * The port number of the mail server.
   *
   * @return the protocol port property.
   */
  public String getPortProperty() {
    return unmaskProperty("mail.%s.port");
  }

  /**
   * Indicates if should attempt to authorize or not. Defaults to false.
   *
   * @return the protocol mail auth property.
   */
  public String getMailAuthProperty() {
    return unmaskProperty("mail.%s.auth");
  }

  /**
   * Defines the default mime charset to use when none has been specified for the message.
   *
   * @return the mime charset property.
   */
  public String getMailMimeCharsetProperty() {
    return unmaskProperty("mail.mime.charset");
  }

  /**
   * Whether to use {@link Socket} as a fallback if the initial connection fails or not.
   *
   * @return the protocol socket factory fallback property.
   */
  public String getSocketFactoryFallbackProperty() {
    return unmaskProperty("mail.%s.socketFactory.fallback");
  }

  /**
   * Specifies the port to connect to when using a socket factory.
   *
   * @return the protocol socket factory port property.
   */
  public String getSocketFactoryPortProperty() {
    return unmaskProperty("mail.%s.socketFactory.port");
  }

  /**
   * @return the protocol socket factory property.
   */
  public String getSocketFactoryProperty() {
    return unmaskProperty("mail.%s.ssl.socketFactory");
  }

  /**
   * Specifies the SSL ciphersuites that will be enabled for SSL connections.
   *
   * @return the protocol ssl ciphersuites property.
   */
  public String getSslCiphersuitesProperty() {
    return unmaskProperty("mail.%s.ssl.ciphersuites");
  }

  /**
   * Specifies the SSL protocols that will be enabled for SSL connections.
   *
   * @return the protocol ssl enabled protocols property.
   */
  public String getSslProtocolsProperty() {
    return unmaskProperty("mail.%s.ssl.protocols");
  }

  /**
   * Specifies if ssl is enabled or not.
   *
   * @return the ssl enable property.
   */
  public String getSslEnableProperty() {
    return unmaskProperty("mail.%s.ssl.enable");
  }

  /**
   * Specifies the trusted hosts.
   *
   * @return the protocol ssl trust property.
   */
  public String getSslTrustProperty() {
    return unmaskProperty("mail.%s.ssl.trust");
  }

  /**
   * Indicates if the STARTTLS command shall be used to initiate a TLS-secured connection.
   *
   * @return the protocol start tls property.
   */
  public String getStartTlsProperty() {
    return unmaskProperty("mail.%s.starttls.enable");
  }

  /**
   * Specifies the default transport name.
   *
   * @return the protocol name property.
   */
  public String getTransportProtocolProperty() {
    return "mail.transport.name";
  }

  /**
   * Socket read timeout value in milliseconds. Default is infinite timeout.
   *
   * @return the protocol read timeout property.
   */
  public String getReadTimeoutProperty() {
    return unmaskProperty("mail.%s.timeout");
  }

  /**
   * Socket connection timeout value in milliseconds. Default is infinite timeout.
   *
   * @return the protocol connection timeout property.
   */
  public String getConnectionTimeoutProperty() {
    return unmaskProperty("mail.%s.connectiontimeout");
  }

  /**
   * Socket write timeout value in milliseconds. Default is infinite timeout.
   *
   * @return the protocol write timeout property.
   */
  public String getWriteTimeoutProperty() {
    return unmaskProperty("mail.%s.writetimeout");
  }

  private String unmaskProperty(final String property) {
    return format(property, name);
  }
}
