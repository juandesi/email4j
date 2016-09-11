/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.api.client;

import java.util.Map;

import desi.juan.email.api.security.TlsConfiguration;
import desi.juan.email.internal.EmailProtocol;
import desi.juan.email.internal.commands.RetrieveCommand;
import desi.juan.email.internal.connection.RetrieverConnection;

class AbstractRetrieverClient {

  protected RetrieverConnection connection;
  protected RetrieveCommand retrieveCommand;

  AbstractRetrieverClient(EmailProtocol protocol,
                          String username,
                          String password,
                          String host,
                          String port,
                          long connectionTimeout,
                          long readTimeout,
                          long writeTimeout,
                          Map<String, String> properties,
                          TlsConfiguration tlsConfiguration) {
    connection = new RetrieverConnection(protocol,
                                         username,
                                         password,
                                         host,
                                         port,
                                         connectionTimeout,
                                         readTimeout,
                                         writeTimeout,
                                         properties);
  }

  public void disconnect() {
    connection.disconnect();
  }
}
