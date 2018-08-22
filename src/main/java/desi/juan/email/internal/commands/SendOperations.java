/**
 * The MIT License (MIT)
 *
 * Original work Copyright (c) 2016 Juan Desimoni
 * Modified work Copyright (c) 2017 yx91490
 * Modified work Copyright (c) 2017 Jonathan Hult
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package desi.juan.email.internal.commands;


import com.google.common.collect.ImmutableList;
import desi.juan.email.api.Email;
import desi.juan.email.api.EmailAttachment;
import desi.juan.email.api.EmailBody;
import desi.juan.email.internal.connection.SenderConnection;
import desi.juan.email.internal.exception.EmailException;
import desi.juan.email.internal.exception.SendEmailException;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Calendar;

import static desi.juan.email.api.EmailConstants.MULTIPART;
import static java.lang.String.format;
import static javax.mail.Message.RecipientType.BCC;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Part.ATTACHMENT;
import static javax.mail.Part.INLINE;

/**
 * Interface for the send operation.
 */
public interface SendOperations {

  /**
   * Sends an email message. The message will be sent to all recipient {@code to}, {@code cc},
   * {@code bcc} specified in the message.
   */
  default void send(final SenderConnection connection, final Email email) {
    try {

      final Message m = new MimeMessage(connection.getSession());
      m.setFrom(toAddress(email.getFrom().get(0)));
      m.setRecipients(TO, toAddresses(email.getTo()));
      m.setRecipients(CC, toAddresses(email.getCc()));
      m.setRecipients(BCC, toAddresses(email.getBcc()));
      m.setSentDate(Calendar.getInstance().getTime());
      m.setSubject(email.getSubject());
      m.setReplyTo(toAddresses(email.getReplyTo()));

      for (final Header header : email.getHeaders()) {
        m.addHeader(header.getName(), header.getValue());
      }

      final EmailBody body = email.getBody();
      final ImmutableList<EmailAttachment> attachments = email.getAttachments();
      if (attachments.isEmpty()) {
        m.setDisposition(INLINE);
        m.setContent(body.getContent(), body.getContentType());
      } else {
        final MimeMultipart multipart = buildMultipart(body, attachments);
        m.setContent(multipart, MULTIPART);
      }

      Transport.send(m);
    } catch (final MessagingException e) {
      throw new SendEmailException("Error while sending the email: " + e.getMessage(), e);
    }
  }

  static MimeMultipart buildMultipart(final EmailBody body, final ImmutableList<EmailAttachment> attachments) throws MessagingException {
    final MimeMultipart multipart = new MimeMultipart();
    final MimeBodyPart bodyPart = new MimeBodyPart();
    bodyPart.setDisposition(INLINE);
    bodyPart.setContent(body.getContent(), body.getContentType());
    multipart.addBodyPart(bodyPart);

    MimeBodyPart attachmentPart;
    for (final EmailAttachment attachment : attachments) {
      try {
        attachmentPart = new MimeBodyPart();
        attachmentPart.setDisposition(ATTACHMENT);
        attachmentPart.setFileName(attachment.getId());
        final DataHandler attachmentDataHandler = new DataHandler(attachment.getContent(), attachment.getContentType());
        attachmentPart.setDataHandler(attachmentDataHandler);
        multipart.addBodyPart(attachmentPart);
      } catch (final Exception e) {
        throw new EmailException("Error while adding attachment: " + attachment, e);
      }
    }
    return multipart;
  }

  /**
   * Converts a {@link ImmutableList} of {@link String}s representing email addresses into an {@link Address} array.
   */
  default Address[] toAddresses(final ImmutableList<String> addresses) {
    return addresses.stream().map(this::toAddress).toArray(Address[]::new);
  }

  /**
   * Converts a simple {@link String} representing an address into an {@link Address} instance
   */
  default Address toAddress(final String address) {
    try {
      return new InternetAddress(address);
    } catch (final AddressException e) {
      throw new EmailException(format("Error while creating %s InternetAddress", address), e);
    }
  }
}
