/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.internal;


import static desi.juan.email.api.EmailConstants.DEFAULT_CONTENT_TYPE;

import desi.juan.email.api.EmailBody;

/**
 * Represents an empty email body.
 */
class EmptyEmailBody implements EmailBody {

  EmptyEmailBody() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getContent() {
    return "";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getContentType() {
    return DEFAULT_CONTENT_TYPE;
  }
}
