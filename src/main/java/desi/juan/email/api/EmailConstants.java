/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.api;

/**
 * Common email constant values.
 */
public final class EmailConstants {

  public static final long DEFAULT_TIMEOUT = 5000;

  /**
   * Default folder name for all the mailboxes.
   */
  public static final String INBOX_FOLDER = "INBOX";

  /**
   * defines all the multipart content types
   */
  public static final String MULTIPART = "multipart/*";

  /**
   * defines all the text content types
   */
  public static final String TEXT = "text/*";

  /**
   * Default email body content type
   */
  public static final String TEXT_PLAIN = "text/plain";

  /**
   * Default email body charset
   */
  public static final String UTF8 = "UTF-8";

  /**
   * returns the default content type.
   */
  public static final String DEFAULT_CONTENT_TYPE = TEXT_PLAIN + "; charset=" + UTF8 ;

  /**
   * Default port value for SMTP servers.
   */
  public static final String SMTP_PORT = "25";

  /**
   * Default port value for SMTPS servers.
   */
  public static final String SMTPS_PORT = "465";


  /**
   * Hide constructor
   */
  private EmailConstants() {}

}