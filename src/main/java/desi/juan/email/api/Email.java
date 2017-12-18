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
package desi.juan.email.api;

import com.google.common.collect.Multimap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Generic contract for email messages implementations.
 */
public interface Email {

  /**
   * @return the number is the relative position of the email in its Folder.
   * <p>
   * Valid message numbers start at 1. Emails that do not belong to any folder (like newly composed or derived messages) have 0 as
   * their message number.
   */
  int getNumber();

  /**
   * Get the unique id of the email.
   *
   * @return the message id
   */
  long getId();

  /**
   * Get the addresses to which replies should be directed. This will usually be the sender of the email, but some emails may
   * direct replies to a different address
   *
   * @return all the recipient addresses of replyTo type.
   */
  List<String> getReplyToAddresses();

  /**
   * @return the subject of the email.
   */
  String getSubject();

  /**
   * @return all the recipient addresses of "To" (primary) type.
   */
  List<String> getToAddresses();

  /**
   * @return all the recipient addresses of "Bcc" (blind carbon copy) type.
   */
  List<String> getBccAddresses();

  /**
   * @return all the recipient addresses of "Cc" (carbon copy) type.
   */
  List<String> getCcAddresses();

  /**
   * Get the identity of the person(s) who wished this message to be sent.
   *
   * @return all the from addresses.
   */
  List<String> getFromAddresses();

  /**
   * Get the date this message was received.
   *
   * @return the date this message was received.
   */
  Optional<LocalDateTime> getReceivedDate();

  /**
   * Get the date this message was sent.
   *
   * @return the date this message was sent.
   */
  Optional<LocalDateTime> getSentDate();

  /**
   * @return all the headers of this email message.
   */
  Multimap<String, String> getHeaders();

  /**
   * @return an {@link EmailFlags} object containing the flags setted in the email.
   */
  EmailFlags getFlags();

  /**
   * Get the body of the email, a text content of type "text/*";
   *
   * @return the body content of the email.
   */
  EmailBody getBody();

  /**
   * Get the attachments of the email, an empty list is returned when there are no attachments.
   *
   * @return a {@link List} with the attachments of the email.
   */
  List<EmailAttachment> getAttachments();
}
