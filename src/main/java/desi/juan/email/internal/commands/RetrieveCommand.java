/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.internal.commands;

import static javax.mail.Folder.READ_ONLY;

import com.google.common.collect.ImmutableList;

import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import desi.juan.email.api.Email;
import desi.juan.email.internal.IncomingEmail;
import desi.juan.email.internal.connection.RetrieverConnection;
import desi.juan.email.internal.exception.RetrieveEmailException;

/**
 * Represents the retrieve emails operation.
 */
public final class RetrieveCommand {

  /**
   * Retrieves all the emails in the specified {@code folderName}.
   * <p>
   * For folder implementations (like IMAP) that support fetching without reading the content, if the content should NOT be read
   * ({@code readContent} = false) the SEEN flag is not going to be set.
   */
  public List<Email> retrieve(RetrieverConnection connection, String folderName, boolean readContent) {
    ImmutableList.Builder<Email> emailsBuilder = ImmutableList.builder();
    try {
      Folder folder = connection.getFolder(folderName, READ_ONLY);
      for (Message m : folder.getMessages()) {
        emailsBuilder.add(new IncomingEmail(m, readContent));
      }
      return emailsBuilder.build();
    } catch (MessagingException me) {
      throw new RetrieveEmailException("Error while retrieving emails", me);
    }
  }
}
