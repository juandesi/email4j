package desi.juan.email.api.client;


import static desi.juan.email.api.EmailConstants.DEFAULT_TIMEOUT;
import static desi.juan.email.internal.EmailProtocol.IMAP;
import static desi.juan.email.internal.EmailProtocol.IMAPS;

import java.util.List;
import java.util.Map;

import desi.juan.email.api.Email;
import desi.juan.email.api.security.TlsConfiguration;

public class ImapClient extends AbstractRetrieverClient {

  /**
   * Default port value for IMAP servers.
   */
  public static final String DEFAULT_IMAP_PORT = "143";

  /**
   * Default port value for IMAPS servers.
   */
  public static final String DEFAULT_IMAPS_PORT = "993";


  public ImapClient(String username,
                    String password,
                    String host,
                    String port,
                    long connectionTimeout,
                    long readTimeout,
                    long writeTimeout,
                    Map<String, String> properties,
                    TlsConfiguration tlsConfiguration) {
    super(tlsConfiguration.isSecureEnabled() ? IMAPS : IMAP,
          username,
          password,
          host,
          port,
          connectionTimeout,
          readTimeout,
          writeTimeout,
          properties,
          tlsConfiguration);
  }

  public ImapClient(String username,
                    String password,
                    String host,
                    String port,
                    Map<String, String> properties,
                    TlsConfiguration tlsConfiguration) {
    this(username, password, host, port, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, properties, tlsConfiguration);
  }


  public List<Email> retrieve(String folder, boolean readContent) {
    return retrieveCommand.retrieve(connection, folder, readContent);
  }

}
