/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.internal.exception;

public class EmailException extends RuntimeException {

  public EmailException(Throwable cause) {
    super(cause);
  }

  public EmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
