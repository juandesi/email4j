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

import com.google.common.collect.ImmutableList;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import desi.juan.email.internal.EmailProtocol;
import desi.juan.email.internal.OutgoingEmail;
import desi.juan.email.internal.StoredEmail;
import desi.juan.email.internal.connection.AbstractConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import javax.activation.DataHandler;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.Properties;

import static desi.juan.email.api.EmailConstants.DEFAULT_CONTENT_TYPE;
import static desi.juan.email.api.EmailConstants.TEXT_PLAIN;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Part.ATTACHMENT;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public abstract class BaseTestCase {

  protected static final String EMAIL_SUBJECT = "Test subject";
  protected static final String EMAIL_CONTENT = "Test content";

  protected static final String PERSON_1 = "person1@test.com";
  protected static final String PERSON_2 = "person2@test.com";
  protected static final String PERSON_3 = "person3@test.com";
  protected static final String PERSON_4 = "person4@test.com";
  protected static final String PERSON_5 = "person5@test.com";

  protected static final String HEADER_KEY = "This is a Header key";
  protected static final String HEADER_VAL = "This is a Header value";

  protected static final EmailAttachment ATTACHMENT_1 = new EmailAttachment("id1", "Content from attachment 1", DEFAULT_CONTENT_TYPE);
  protected static final EmailAttachment ATTACHMENT_2 = new EmailAttachment("id2", "Content from attachment 2", DEFAULT_CONTENT_TYPE);

  protected static final String USER = PERSON_1;
  protected static final String PASSWORD = "pass";
  protected static final String HOST = "127.0.0.1";
  protected static final int PORT = 10000;
  private static final long SERVER_STARTUP_TIMEOUT = 5000;

  protected static final int NUM_OF_EMAILS_TO_TEST = 5;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private GreenMail server;
  private GreenMailUser user;

  @Before
  public void setupServer() {
    final ServerSetup serverSetup = new ServerSetup(PORT, null, getProtocol().getName());
    serverSetup.setServerStartupTimeout(SERVER_STARTUP_TIMEOUT);
    server = new GreenMail(serverSetup);
    server.start();
    user = server.setUser(USER, USER, PASSWORD);
    System.out.println("Setup GreenMail test server");
  }

  @After
  public void dispose() {
    assertThat(server, is(not(nullValue())));
    server.stop();
    System.out.println("Stopped GreenMail test server");
  }

  //@Test
  public void testProtocol() {
    assertThat(getClient().getProtocol(), is(getProtocol()));
  }

  protected abstract EmailProtocol getProtocol();

  protected abstract AbstractConnection getClient();

  void sendInitialEmailBatch(final int a) throws MessagingException {
    // TODO: fix to send method in
    for (int i = 0; i < NUM_OF_EMAILS_TO_TEST; i++) {
      final MimeMessage message;
      if (a == 0) {
        message = getSinglePartTestMessage();
      } else {
        message = getMultiPartTestMessage();
      }
      user.deliver(message);
    }
  }

  protected ImmutableList<Email> getReceivedMessages() {
    return EmailTools.toStoredList(server.getReceivedMessages(), true);
  }

  private static final String TEXT_PLAIN_ATTACHMENT_CONTENT = "Plain text attachment content";
  private static final String TEXT_PLAIN_ATTACHMENT_NAME = "attachment.txt";
  private static final String JSON_ATTACHMENT_CONTENT = "{\"json\": \"content\"}";
  private static final String JSON_ATTACHMENT_NAME = "attachment.json";

  private static final Session testSession = Session.getDefaultInstance(new Properties());

  private static MimeMessage getMultiPartTestMessage() throws MessagingException {
    final MimeBodyPart body = new MimeBodyPart();
    body.setContent(EMAIL_CONTENT, TEXT_PLAIN);

    final MimeBodyPart textAttachment = new MimeBodyPart();
    textAttachment.setDisposition(ATTACHMENT);
    textAttachment.setFileName(TEXT_PLAIN_ATTACHMENT_NAME);
    textAttachment.setContent(TEXT_PLAIN_ATTACHMENT_CONTENT, TEXT_PLAIN);

    final MimeBodyPart jsonAttachment = new MimeBodyPart();
    jsonAttachment.setDisposition(ATTACHMENT);
    jsonAttachment.setFileName(JSON_ATTACHMENT_NAME);

    try {
      final DataHandler dh = new DataHandler(new ByteArrayDataSource(JSON_ATTACHMENT_CONTENT, "application/json"));
      jsonAttachment.setDataHandler(dh);
    } catch (final IOException e) {
      throw new MessagingException(e.getMessage());
    }

    final Multipart multipart = new MimeMultipart();

    multipart.addBodyPart(body);
    multipart.addBodyPart(textAttachment);
    multipart.addBodyPart(jsonAttachment);

    final MimeMessage message = new MimeMessage(testSession);
    message.setContent(multipart);
    setSubjectAndRecipient(message);
    return message;
  }

  private static MimeMessage getSinglePartTestMessage() throws MessagingException {
    final MimeMessage message = new MimeMessage(testSession);
    message.setText(EMAIL_CONTENT);
    setSubjectAndRecipient(message);
    return message;
  }

  private static void setSubjectAndRecipient(final MimeMessage message) throws MessagingException {
    message.setFrom(PERSON_2);
    message.setSubject(EMAIL_SUBJECT);
    message.setRecipient(TO, new InternetAddress(PERSON_1));
  }

  static void doSinglePartReceiveTests(final ImmutableList<Email> emails) {
    doAssertsForBothParts(emails);
    emails.forEach(BaseTestCase::assertBodyContent);
    emails.forEach(BaseTestCase::assertContentTypeText);
  }

  static void doMultiPartReceiveTests(final ImmutableList<Email> emails) {
    doAssertsForBothParts(emails);
    emails.forEach(BaseTestCase::assertBodyContent);
    emails.forEach(BaseTestCase::assertContentTypeMultipart);
  }

  private static void doAssertsForBothParts(final ImmutableList<Email> emails) {
    assertThat(emails.size(), is(NUM_OF_EMAILS_TO_TEST));
    emails.forEach(email -> {
      assertStoredEmail(email);
      assertSubject(email);
      assertHeaders(email);
    });
  }

  protected static void assertStoredEmail(final Email email) {
    assertThat(email, is(instanceOf(StoredEmail.class)));
  }

  protected static void assertOutgoingEmail(final Email email) {
    assertThat(email, is(instanceOf(OutgoingEmail.class)));
  }

  protected static void assertSubject(final Email email) {
    assertThat(email.getSubject(), is(EMAIL_SUBJECT));
  }

  protected static void assertFrom(final Email email) {
    assertThat(email.getFrom(), containsInAnyOrder(PERSON_4));
  }

  protected static void assertRecipients(final Email email) {
    assertThat(email.getTo(), containsInAnyOrder(PERSON_1));
    assertThat(email.getCc(), containsInAnyOrder(PERSON_2, PERSON_3));

    // BCC for received will always be empty
    assertThat(email.getBcc(), empty());
  }

  protected static void assertAttachments(final Email email) {

    final ImmutableList<EmailAttachment> attachments = email.getAttachments();

    assertAttachmentIdAndContent(attachments.get(0), ATTACHMENT_1);
    assertAttachmentIdAndContent(attachments.get(1), ATTACHMENT_2);
  }

  private static void assertAttachmentIdAndContent(final EmailAttachment receivedAttachment,
                                                   final EmailAttachment sentAttachment) {
    assertThat(receivedAttachment.getId(), equalTo(sentAttachment.getId()));
    assertThat(receivedAttachment.getContent(), equalTo(sentAttachment.getContent()));
  }

  protected static void assertHeaders(final Email email) {
    final ImmutableList<Header> headers = email.getHeaders();

    final ImmutableList.Builder<String> headerNamesBuilder = new ImmutableList.Builder<>();
    for (final Header h : headers) {
      headerNamesBuilder.add(h.getName());
    }

    final ImmutableList<String> headerNames = headerNamesBuilder.build();
    assertThat(headerNames, hasItems("Date", "To", "Message-ID", "Subject", "MIME-Version", "Content-Type"));

    if (email.getBody().getContentType().startsWith("text/")) {
      assertThat(headerNames, hasItem("Content-Transfer-Encoding"));
    }
  }

  protected static void assertBodyContent(final Email email) {
    assertThat(email.getBody().getContent(), is(EMAIL_CONTENT));
  }

  private static void assertContentTypeText(final Email email) {
    assertContentType(email, "text/");
  }

  private static void assertContentTypeMultipart(final Email email) {
    assertContentType(email, "multipart/mixed");
  }

  private static void assertContentType(final Email email, final String contentType) {
    assertThat(email.getBody().getContentType(), startsWith(contentType));
  }
}
