/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.api;

import static java.util.Collections.emptyMap;

import java.util.Map;

/**
 * Represents and enables the construction of an email attachment.
 */
public final class EmailAttachment {

  /**
   * the name of the attachment.
   */
  private String id;
  /**
   * the content of the attachment.
   */
  private Object content;

  /**
   * the content type of the attachment content.
   */
  private String contentType;

  /**
   * the headers bounded to the attachment
   */
  private final Map<String, String> headers;

  public EmailAttachment(String id, Object content, String contentType) {
    this(id, content, contentType, emptyMap());
  }

  public EmailAttachment(String id, Object content, String contentType, Map<String, String> headers) {
    this.id = id;
    this.content = content;
    this.contentType = contentType;
    this.headers = headers;
  }

  /**
   * @return the name of the attachment.
   */
  public String getId() {
    return id;
  }

  /**
   * @return the content of the attachment.
   */
  public Object getContent() {
    return content;
  }

  /**
   * @return the content type of the attachment content.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @return a set of headers that are specific for this attachment.
   * Such as Content-Type or Content-Disposition.
   */
  public Map<String, String> getHeaders() {
    return headers;
  }
}
