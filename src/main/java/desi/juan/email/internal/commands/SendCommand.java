/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package desi.juan.email.internal.commands;


import static desi.juan.email.api.EmailConstants.MULTIPART;
import static java.lang.String.format;
import static javax.mail.Message.RecipientType.BCC;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Part.ATTACHMENT;
import static javax.mail.Part.INLINE;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import desi.juan.email.api.Email;
import desi.juan.email.api.EmailAttachment;
import desi.juan.email.api.EmailBody;
import desi.juan.email.internal.connection.SenderConnection;
import desi.juan.email.internal.exception.EmailException;
import desi.juan.email.internal.exception.SendEmailException;

/**
 * Represents the send operation.
 */
public final class SendCommand {

  /**
   * Sends an email message. The message will be sent to all recipient {@code to}, {@code cc},
   * {@code bcc} specified in the message.
   */
  public void send(SenderConnection connection, Email email) {
    try {

      Message m = new MimeMessage(connection.getSession());
      m.setFrom(toAddress(email.getFromAddresses().get(0)));
      m.setRecipients(TO, toAddresses(email.getToAddresses()));
      m.setRecipients(CC, toAddresses(email.getCcAddresses()));
      m.setRecipients(BCC, toAddresses(email.getBccAddresses()));
      m.setSentDate(Calendar.getInstance().getTime());
      m.setSubject(email.getSubject());
      m.setReplyTo(toAddresses(email.getReplyToAddresses()));

      for (Map.Entry<String, String> entry : email.getHeaders().entrySet()) {
        m.addHeader(entry.getKey(), entry.getValue());
      }

      EmailBody body = email.getBody();
      List<EmailAttachment> attachments = email.getAttachments();
      if (attachments.isEmpty()) {
        m.setDisposition(INLINE);
        m.setContent(body.getContent(), body.getContentType());
      } else {
        MimeMultipart multipart = buildMultipart(body, attachments);
        m.setContent(multipart, MULTIPART);
      }

      Transport.send(m);
    } catch (MessagingException e) {
      throw new SendEmailException("Error while sending the email: " + e.getMessage(), e);
    }
  }

  private MimeMultipart buildMultipart(EmailBody body, List<EmailAttachment> attachments) throws MessagingException {
    MimeMultipart multipart = new MimeMultipart();
    MimeBodyPart bodyPart = new MimeBodyPart();
    bodyPart.setDisposition(INLINE);
    bodyPart.setContent(body.getContent(), body.getContentType());
    multipart.addBodyPart(bodyPart);

    MimeBodyPart attachmentPart;
    for (EmailAttachment attachment : attachments) {
      try {
        attachmentPart = new MimeBodyPart();
        attachmentPart.setDisposition(ATTACHMENT);
        attachmentPart.setFileName(attachment.getId());
        DataHandler attachmentDataHandler = new DataHandler(attachment.getContent(), attachment.getContentType());
        attachmentPart.setDataHandler(attachmentDataHandler);
        multipart.addBodyPart(attachmentPart);
      } catch (Exception e) {
        throw new EmailException("Error while adding attachment: " + attachment, e);
      }
    }
    return multipart;
  }


  /**
   * Converts a {@link List} of {@link String}s representing email addresses into an {@link Address} array.
   */
  private Address[] toAddresses(List<String> addresses) {
    return addresses.stream().map(this::toAddress).toArray(Address[]::new);
  }

  /**
   * Converts a simple {@link String} representing an address into an {@link Address} instance
   */
  private Address toAddress(String address) {
    try {
      return new InternetAddress(address);
    } catch (AddressException e) {
      throw new EmailException(format("Error while creating %s InternetAddress", address), e);
    }
  }
}
