/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.internal.connection;

import java.util.Map;

import desi.juan.email.internal.EmailProtocol;

/**
 * A connection with a mail server for sending emails.
 */
public final class SenderConnection extends AbstractConnection {

  /**
   * Creates a new instance.
   */
  public SenderConnection(EmailProtocol protocol,
                          String username,
                          String password,
                          String host,
                          String port,
                          long connectionTimeout,
                          long readTimeout,
                          long writeTimeout,
                          Map<String, String> properties) {
    super(protocol, username, password, host, port, connectionTimeout, readTimeout, writeTimeout, properties);
  }
}
