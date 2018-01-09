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
import com.google.common.collect.ImmutableMap;
import desi.juan.email.api.EmailAttachment;
import desi.juan.email.internal.exception.EmailException;
import org.apache.commons.io.IOUtils;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static desi.juan.email.api.EmailConstants.TEXT;
import static javax.mail.Part.ATTACHMENT;

/**
 * Given a {@link Message} introspects its content to obtain the {@link EmailBody} an the {@link EmailAttachment}s if any.
 */
class EmailContentProcessor {

  private static final String ERROR_PROCESSING_MESSAGE = "Error while processing message content.";

  private final List<EmailAttachment> attachmentParts = new LinkedList<>();
  private final StringJoiner body = new StringJoiner("\n");

  /**
   * Creates an instance and getInstance the message content.
   * <p>
   * Hided constructor, can only get a new instance out of this class using the factory method
   * {@link EmailContentProcessor#getInstance(Message)}.
   *
   * @param message the {@link Message} to be processed.
   */
  private EmailContentProcessor(Message message) {
        processPart(message);
  }

  /**
   * Factory method to get a new instance of {@link EmailContentProcessor} and getInstance a {@link Message}.
   *
   * @param message the {@link Message} to be processed.
   * @return a new {@link EmailContentProcessor} instance.
   */
  static EmailContentProcessor getInstance(Message message) {
    return new EmailContentProcessor(message);
  }

  /**
   * @return the text body of the message.
   */
  public String getBody() {
    return body.toString().trim();
  }

  /**
   * @return an {@link ImmutableMap} with the attachments of an email message.
   */
  public List<EmailAttachment> getAttachments() {
    return ImmutableList.copyOf(attachmentParts);
  }

  /**
   * Processes a {@link Multipart} which may contain INLINE content and ATTACHMENTS.
   *
   * @param part the part to be processed
   */
  private void processMultipartPart(Part part) {
    try {
      Multipart mp = (Multipart) part.getContent();
      for (int i = 0; i < mp.getCount(); i++) {
        processPart(mp.getBodyPart(i));
      }
    } catch (MessagingException | IOException e) {
      throw new EmailException(ERROR_PROCESSING_MESSAGE, e);
    }
  }

  /**
   * Processes a single {@link Part} and adds it to the body of the message or as a new attachment depending on it's disposition
   * type.
   *
   * @param part the part to be processed
   */
  private void processPart(Part part) {
    try {
      Object content = part.getContent();
      if (isMultipart(content)) {
        processMultipartPart(part);
      }

      if (isAttachment(part)) {
        Map<String, String> headers = new HashMap<>();
        final Enumeration allHeaders = part.getAllHeaders();
        while (allHeaders.hasMoreElements()) {
          Header h = (Header) allHeaders.nextElement();
          headers.put(h.getName(), h.getValue());
        }

        attachmentParts.add(new EmailAttachment(part.getFileName(), part.getContent(), part.getContentType(), headers));

      } else {
        if (isText(content)) {
          body.add((String) content);
        }

        if (content instanceof InputStream && isInline(part) && part.isMimeType(TEXT)) {
          String inline = IOUtils.toString((InputStream) content);
          body.add(inline);
        }
      }
    } catch (MessagingException | IOException e) {
      throw new EmailException(ERROR_PROCESSING_MESSAGE, e);
    }
  }

  /**
   * Evaluates whether the disposition of the {@link Part} is INLINE or not.
   *
   * @param part the part to be validated.
   * @return true is the part is dispositioned as inline, false otherwise
   * @throws MessagingException
   */
  private boolean isInline(Part part) throws MessagingException {
    return part.getDisposition().equalsIgnoreCase(Part.INLINE);
  }


  /**
   * Evaluates whether a {@link Part} is an attachment or not.
   *
   * @param part the part to be validated.
   * @return true is the part is dispositioned as an attachment, false otherwise
   * @throws MessagingException
   */
  private boolean isAttachment(Part part) throws MessagingException {
    return part.getFileName() != null && (part.getDisposition() == null || part.getDisposition().equals(ATTACHMENT));
  }

  /**
   * Evaluates whether a content is multipart or not.
   *
   * @param content the content to be evaluated.
   * @return true if is multipart, false otherwise
   */
  private boolean isMultipart(Object content) {
    return content instanceof Multipart;
  }

  /**
   * Evaluates whether a content is text or not.
   *
   * @param content the content to be evaluated.
   * @return true if is text, false otherwise
   */
  private boolean isText(Object content) {
    return content instanceof String;
  }
}
