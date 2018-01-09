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
package desi.juan.email.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import desi.juan.email.api.Email;
import desi.juan.email.api.EmailAttachment;
import desi.juan.email.api.EmailBody;
import desi.juan.email.api.EmailTools;
import desi.juan.email.internal.exception.EmailException;

import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.StringJoiner;

import static desi.juan.email.api.EmailConstants.TEXT;

/**
 * Given a {@link Message} introspects its content to obtain the {@link EmailBody} an the {@link EmailAttachment}s if any.
 */
class EmailContentProcessor {

  private static final String ERROR_PROCESSING_MESSAGE = "Error while processing message content.";

  private final ImmutableList.Builder<EmailAttachment> attachmentParts = ImmutableList.builder();
  private final StringJoiner body = new StringJoiner("\n");
  private final String contentType;

  /**
   * Creates an instance and getInstance the message content.
   * <p>
   * Hide constructor, can only get a new instance out of this class using the factory method
   * {@link EmailContentProcessor#getInstance(Message, boolean)}.
   *
   * @param message the {@link Message} to be processed.
   */
  private EmailContentProcessor(final Message message, final boolean readContent) {
    try {
      if (readContent) {
        processPart(message);
        contentType = message.getContentType();
      } else {
        contentType = null;
      }
    } catch (MessagingException | IOException e) {
      throw new EmailException(ERROR_PROCESSING_MESSAGE, e);
    }
  }

  /**
   * Factory method to get a new instance of {@link EmailContentProcessor} and getInstance a {@link Message}.
   *
   * @param message the {@link Message} to be processed.
   * @return a new {@link EmailContentProcessor} instance.
   */
  static EmailContentProcessor getInstance(final Message message, final boolean readContent) {
    return new EmailContentProcessor(message, readContent);
  }

  /**
   * @return the {@link EmailBody}. EmailBody will be empty if readContent is false
   */
  public EmailBody getBody() {
    if (body.length() > 0) {
      return new EmailBody(body.toString().trim(), contentType);
    } else {
      return new EmailBody();
    }
  }

  /**
   * @return an {@link ImmutableList} with the {@link EmailAttachment}s of an {@link Email}.
   */
  public ImmutableList<EmailAttachment> getAttachments() {
    return attachmentParts.build();
  }

  /**
   * Processes a {@link Multipart} which may contain INLINE content and ATTACHMENTS.
   *
   * @param part the Multipart to be processed
   */
  private void processMultipartPart(final Multipart part) throws MessagingException, IOException {
    for (int i = 0; i < part.getCount(); i++) {
      final BodyPart bp = part.getBodyPart(i);
      processPart(bp);
    }
  }

  /**
   * Processes a single {@link Part} and adds it to the body of the message or as a new attachment depending on its disposition
   * type.
   *
   * @param part the part to be processed
   */
  private void processPart(final Part part) throws MessagingException, IOException {
    final Object content = part.getContent();
    if (isMultipart(content)) {
      processMultipartPart((Multipart) content);
    }

    if (isAttachment(part)) {
      addAttachment(part);
    } else {
      if (isText(content)) {
        body.add((String) content);
      } else {
        if (content instanceof InputStream && isInline(part) && part.isMimeType(TEXT)) {
          final InputStream is = (InputStream) content;
          final String contentType = part.getContentType();
          final Charset cs = EmailTools.getCharset(EmailTools.toContentType(contentType));
          final String inline = new String(ByteStreams.toByteArray(is), cs);
          body.add(inline);
        } else {
          // TODO: need to do something with this??
        }
      }
    }
  }

  private void addAttachment(final Part part) throws MessagingException, IOException {
    final String fileName = part.getFileName();
    final Object content = part.getContent();
    final String contentType = part.getContentType();
    final Enumeration<Header> allHeaders = part.getAllHeaders();
    attachmentParts.add(new EmailAttachment(fileName, content, contentType, ImmutableList.copyOf(Collections.list(allHeaders))));
  }

  /**
   * Evaluates whether a {@link Part} is an attachment or not.
   *
   * @param part the part to be validated.
   * @return true if the part is dispositioned as an attachment, false otherwise
   * @throws MessagingException
   */
  private boolean isAttachment(final Part part) throws MessagingException {
    final String disposition = part.getDisposition();
    final String fileName = part.getFileName();
    return fileName != null && (disposition == null || disposition.equalsIgnoreCase(Part.ATTACHMENT));
  }

  /**
   * Evaluates whether the disposition of the {@link Part} is INLINE or not.
   *
   * @param part the part to be validated.
   * @return true if the part is dispositioned as inline, false otherwise
   * @throws MessagingException
   */
  private boolean isInline(final Part part) throws MessagingException {
    final String disposition = part.getDisposition();
    return disposition != null && disposition.equalsIgnoreCase(Part.INLINE);
  }

  /**
   * Evaluates whether a content is multipart or not.
   *
   * @param content the content to be evaluated.
   * @return true if is multipart, false otherwise
   */
  private boolean isMultipart(final Object content) {
    return content instanceof Multipart;
  }

  /**
   * Evaluates whether a content is text or not.
   *
   * @param content the content to be evaluated.
   * @return true if is text, false otherwise
   */
  private boolean isText(final Object content) {
    return content instanceof String;
  }
}
