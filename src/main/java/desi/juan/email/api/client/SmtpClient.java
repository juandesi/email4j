package desi.juan.email.api.client;


import static desi.juan.email.api.EmailConstants.DEFAULT_TIMEOUT;
import static desi.juan.email.internal.EmailProtocol.SMTP;
import static desi.juan.email.internal.EmailProtocol.SMTPS;

import java.util.Map;

import desi.juan.email.api.Email;
import desi.juan.email.api.security.TlsConfiguration;
import desi.juan.email.internal.commands.SendCommand;
import desi.juan.email.internal.connection.SenderConnection;

public class SmtpClient {

  private SenderConnection connection;
  private SendCommand sendCommand;

  public SmtpClient(String username,
                    String password,
                    String host,
                    String port,
                    long connectionTimeout,
                    long readTimeout,
                    long writeTimeout,
                    Map<String, String> properties,
                    TlsConfiguration tlsConfiguration)
  {
    this.connection = new SenderConnection(tlsConfiguration.isSecureEnabled() ? SMTPS : SMTP,
                                           username,
                                           password,
                                           host,
                                           port,
                                           connectionTimeout,
                                           readTimeout,
                                           writeTimeout,
                                           properties);
  }

  public SmtpClient(String username,
                    String password,
                    String host,
                    String port,
                    Map<String, String> properties,
                    TlsConfiguration tlsConfiguration)
  {
    this(username, password, host, port, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, properties, tlsConfiguration);
  }

  public void send(Email email)
  {
    sendCommand.send(connection, email);
  }
}
