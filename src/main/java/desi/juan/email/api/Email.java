package desi.juan.email.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generic contract for email messages implementations.
 */
public interface Email {
  
  /**
   * Get the Message id of the email.
   *
   * @return the message id
   */
  int getId();

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
  Map<String, String> getHeaders();

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
