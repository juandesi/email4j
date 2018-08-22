package desi.juan.email.api;

import com.google.common.collect.ImmutableList;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import desi.juan.email.internal.StoredEmail;
import desi.juan.email.internal.commands.FlagOperations;
import desi.juan.email.internal.exception.RetrieveEmailException;

import javax.mail.Address;
import javax.mail.Flags;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static desi.juan.email.api.EmailConstants.DEFAULT_CHARSET;
import static desi.juan.email.internal.commands.FolderOperations.ALL_MESSAGES;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static javax.mail.Flags.Flag.ANSWERED;
import static javax.mail.Flags.Flag.DELETED;
import static javax.mail.Flags.Flag.DRAFT;
import static javax.mail.Flags.Flag.RECENT;
import static javax.mail.Flags.Flag.SEEN;

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
  public static Charset getCharset(final ContentType contentType) {
    final String csParam = contentType.getParameter("charset");
    // TODO: verify logic
    if (csParam == null) {
      return DEFAULT_CHARSET;
    }
    try {
      final String csStr = MimeUtility.decodeText(csParam);
      return Charset.forName(csStr);
    } catch (final UnsupportedEncodingException e) {
      return DEFAULT_CHARSET;
    }
  }

  /**
   * @param contentType
   * @return a ContentType instance for the given contentType string.
   */
  public static ContentType toContentType(final String contentType) {
    try {
      return new ContentType(contentType);
    } catch (final Exception e) {
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
  public static String getFormat(final ContentType contentType) {
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
  public static ImmutableList<Email> toStoredList(final Message[] messages, final boolean readContent) {
    final ImmutableList.Builder<Email> list = ImmutableList.builder();
    for (final Message message : messages) {
      final long uid = getEmailUid(message.getFolder(), message);
      final StoredEmail.Builder builder = new StoredEmail.Builder();
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
  public static Message[] getMessages(final Folder folder, final boolean readContent, int numToRetrieve) {
    //TODO: how is readContent being used
    try {
      // if supposed to retrieve all messages, set numToRetrieve to number of messages in folder
      if (numToRetrieve == ALL_MESSAGES) {
        numToRetrieve = folder.getMessageCount();
      }
      return folder.getMessages(1, numToRetrieve);
    } catch (final MessagingException me) {
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
  public static long getEmailUid(final Folder folder, final Message message) {
    try {
      if (folder instanceof POP3Folder) {
        return parseLong(((POP3Folder) folder).getUID(message));
      }
      if (folder instanceof UIDFolder) {
        return ((UIDFolder) folder).getUID(message);
      }
    } catch (final MessagingException e) {
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
  public static Message[] search(final Folder folder, final SearchTerm... terms) throws MessagingException {
    final SearchTerm andTerm = new AndTerm(terms);
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
  public static Message[] search(final Folder folder, final Date olderThan, final Date newerThan) throws MessagingException {
    final SearchTerm ot = new ReceivedDateTerm(ComparisonTerm.LT, olderThan);
    final SearchTerm nt = new ReceivedDateTerm(ComparisonTerm.GT, newerThan);

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
  public static void move(final Folder fromFolder, final Message[] messages, final Folder toFolder) throws MessagingException {
    if (messages.length < 1) {
      throw new MessagingException("No messages to move");
    }
    //try {
    if (fromFolder instanceof IMAPFolder) {
      final IMAPFolder imapFolder = (IMAPFolder) fromFolder;
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
  public static void move(final Folder fromFolder, final Message message, final Folder toFolder) throws MessagingException {
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
  public static void move(final Folder fromFolder, final boolean readContent, final int numToRetrieve, final Folder toFolder) throws MessagingException {
    move(fromFolder, getMessages(fromFolder, readContent, numToRetrieve), toFolder);
  }

  /**
   * Parses an array of {@link Address}es to a {@link ImmutableList}.
   *
   * @param addresses
   * @return ImmutableList with the {@link Address}es as {@link String}s.
   */
  public static ImmutableList<String> parseAddressArray(final Address[] addresses) {
    if (addresses == null) {
      return ImmutableList.of();
    }
    return ImmutableList.copyOf(stream(addresses).map(Object::toString).collect(toList()));
  }

  /**
   * Parses the {@link Flags} of a {@link Message} to an {@link EmailFlags} instance.
   *
   * @param flags
   * @return {@link EmailFlags}
   */
  public static EmailFlags parseFlags(final Flags flags) {
    return new EmailFlags(flags.contains(ANSWERED),
        flags.contains(DELETED),
        flags.contains(DRAFT),
        flags.contains(RECENT),
        flags.contains(SEEN));
  }

  /**
   * Parses a not null {@link Date} to a {@link LocalDateTime} instance.
   *
   * @param date
   * @return date in {@link LocalDateTime} format.
   */
  public static LocalDateTime parseDate(final Date date) {
    if (date == null) {
      return null;
    }
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }
}
