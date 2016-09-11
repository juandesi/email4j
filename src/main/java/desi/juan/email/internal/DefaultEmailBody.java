/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.internal;


import static java.lang.String.format;

import desi.juan.email.api.EmailBody;

/**
 * Represents and enables the construction of the content of an email with a body of type "text/*" and a specific character
 * encoding.
 */
public class DefaultEmailBody implements EmailBody {

  /**
   * Text body of the message content
   */
  private String body;

  /**
   * ContentType of the body text. Example: "text/html" or "text/plain".
   */
  private String contentType;

  /**
   * The character encoding of the body. If not specified, it defaults to the default charset in the mule configuration
   */
  private String charset;


  public DefaultEmailBody(String body, String contentType, String charset) {
    this.body = body;
    this.contentType = contentType;
    this.charset = charset;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getContent() {
    return body;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getContentType() {
    return format("%s; charset=%s", contentType, charset);
  }
}
