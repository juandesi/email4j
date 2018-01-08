package desi.juan.email.api;

import com.google.common.collect.ImmutableList;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import desi.juan.email.internal.StoredEmail;
import desi.juan.email.internal.commands.FlagOperations;
import desi.juan.email.internal.exception.RetrieveEmailException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;

import static desi.juan.email.api.EmailConstants.DEFAULT_CHARSET;
import static desi.juan.email.internal.commands.FolderOperations.ALL_MESSAGES;
import static java.lang.Long.parseLong;
import static java.lang.String.format;

public class EmailTools {

  /**
   * Hide constructor.
   */
  private EmailTools() {
  }

  /**
   * @param contentType
   * @return charset for the contentType.
   */
  public static Charset getCharset(ContentType contentType) {
    String csParam = contentType.getParameter("charset");
    // TODO: verify logic
    if (csParam == null) {
      System.out.println("charset was null; using default for contentType: " + contentType);
      return DEFAULT_CHARSET;
    }
    try {
      String csStr = MimeUtility.decodeText(csParam);
      return Charset.forName(csStr);
    } catch (UnsupportedEncodingException e) {
      return DEFAULT_CHARSET;
    }
  }

  /**
   * @param contentType
   * @return a ContentType instance for the given contentType string.
   */
  public static ContentType toContentType(String contentType) {
    try {
      return new ContentType(contentType);
    } catch (Exception e) {
      System.out.println("empty contenttype");
      // TODO: should this do something else?
      return new ContentType();
    }
  }

  /**
   * Convert a ContentType to just the format
   *
   * @param contentType
   * @return the format of the contentType.
   * @{see ContentType. #ContentType}.
   */
  public static String getFormat(ContentType contentType) {
    return contentType.getBaseType();
  }

  /**
   * Transform array of messages into {@link ImmutableList<Email>}.
   *
   * @param messages
   * @param readContent
   * @return list of stored emails.
   * @see #getEmailUid(Folder, Message)
   * @see Message#getFolder()
   */
  public static ImmutableList<Email> toStoredList(Message[] messages, boolean readContent) {
    ImmutableList.Builder<Email> list = ImmutableList.builder();
    for (Message message : messages) {
      long uid = getEmailUid(message.getFolder(), message);
      StoredEmail.Builder builder = new StoredEmail.Builder();
      builder.message(message).id(uid).readContent(readContent).build();
      list.add(builder.build());
    }
    return list.build();
  }

  /**
   * Retrieves limited number of the emails in the specified {@code folderName}.
   *
   * For {@link Folder} implementations (like IMAP) that support fetching without reading the content, if the content should NOT be read
   * ({@code readContent} = false) the SEEN flag is not going to be set.
   *
   * @param folder
   * @param readContent
   * @param numToRetrieve
   * @return array of Messages
   * @see Folder#getMessages(int, int)
   */
  public static Message[] getMessages(Folder folder, boolean readContent, int numToRetrieve) {
    //TODO: how is readContent being used
    try {
      // if supposed to retrieve all messages, set numToRetrieve to number of messages in folder
      if (numToRetrieve == ALL_MESSAGES) {
        numToRetrieve = folder.getMessageCount();
      }
      return folder.getMessages(1, numToRetrieve);
    } catch (MessagingException me) {
      throw new RetrieveEmailException("Error while retrieving emails", me);
    }
  }

  /**
   * Get UID of email message.
   *
   * @param folder
   * @param message
   * @return UID of email message
   */
  public static long getEmailUid(Folder folder, Message message) {
    try {
      if (folder instanceof POP3Folder) {
        return parseLong(((POP3Folder) folder).getUID(message));
      }
      if (folder instanceof UIDFolder) {
        return ((UIDFolder) folder).getUID(message);
      }
    } catch (MessagingException e) {
      throw new RetrieveEmailException(format("Cannot retrieve email:[%s] from folder [%s]", message, folder));
    }
    //TODO: maybe this should fail instead.
    return -1;
  }

  /**
   * Search in {@link Folder} using {@link SearchTerm} combined with AND.
   *
   * @param folder
   * @param terms
   * @return array of Messages.
   * @see Folder#search(SearchTerm)
   */
  public static Message[] search(Folder folder, SearchTerm... terms) throws MessagingException {
    SearchTerm andTerm = new AndTerm(terms);
    return folder.search(andTerm);
  }

  /**
   * Helper method
   *
   * @param folder
   * @param olderThan
   * @param newerThan
   * @return array of Messages
   * @throws MessagingException
   * @see #search(Folder, Date, Date)
   */
  public static Message[] search(Folder folder, Date olderThan, Date newerThan) throws MessagingException {
    SearchTerm ot = new ReceivedDateTerm(ComparisonTerm.LT, olderThan);
    SearchTerm nt = new ReceivedDateTerm(ComparisonTerm.GT, newerThan);

    return search(folder, ot, nt);
  }

  /**
   * Move messages from one {@link Folder} to another.
   *
   * @param fromFolder
   * @param messages
   * @param toFolder
   * @throws MessagingException
   * @see Folder#copyMessages(Message[], Folder)
   * @see FlagOperations#delete(Message[])
   */
  public static void move(Folder fromFolder, Message[] messages, Folder toFolder) throws MessagingException {
    if (messages.length < 1) {
      throw new MessagingException("No messages to move");
    }
    //try {
    if (fromFolder instanceof IMAPFolder) {
      IMAPFolder imapFolder = (IMAPFolder) fromFolder;
      imapFolder.moveMessages(messages, toFolder);
    } else {
      fromFolder.copyMessages(messages, toFolder);
      //TODO: should we wait?
      //fromFolder.wait(2000);
      FlagOperations.delete(messages);
    }
    // } catch (InterruptedException e) {
    //  e.printStackTrace();
    // }
  }

  /**
   * Helper method.
   *
   * @param fromFolder
   * @param message
   * @param toFolder
   * @see #move(Folder, Message[], Folder)
   */
  public static void move(Folder fromFolder, Message message, Folder toFolder) throws MessagingException {
    move(fromFolder, new Message[]{message}, toFolder);
  }

  /**
   * Helper method.
   *
   * @param fromFolder
   * @param readContent
   * @param numToRetrieve
   * @param toFolder
   * @throws MessagingException
   * @see #move(Folder, Message[], Folder)
   * @see #getMessages(Folder, boolean, int)
   */
  public static void move(Folder fromFolder, boolean readContent, int numToRetrieve, Folder toFolder) throws MessagingException {
    move(fromFolder, getMessages(fromFolder, readContent, numToRetrieve), toFolder);
  }
}
