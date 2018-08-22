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
package desi.juan.email.api.client;

import com.google.common.collect.ImmutableList;
import desi.juan.email.api.BaseTestCase;
import desi.juan.email.api.Email;
import desi.juan.email.api.client.configuration.ClientConfiguration;
import desi.juan.email.internal.EmailProtocol;
import desi.juan.email.internal.OutgoingEmail;
import desi.juan.email.internal.connection.AbstractConnection;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class SmtpClientTestCase extends BaseTestCase {

  private SmtpClient client;

  @Before
  public void before() {
    client = new SmtpClient(USER, PASSWORD, HOST, PORT, new ClientConfiguration());
    send();
  }

  @Override
  protected EmailProtocol getProtocol() {
    return EmailProtocol.SMTP;
  }

  @Override
  protected AbstractConnection getClient() {
    return client;
  }

  @Test
  public void test() {
    final ImmutableList<Email> receivedEmails = getReceivedMessages();

    // NUM_OF_EMAILS_TO_TEST * 4 = 20
    // 4 because of To, CC, CC and BCC
    assertThat(receivedEmails, hasSize(20));

    receivedEmails.forEach(SmtpClientTestCase::assertEmailData);
  }

  private void send() {
    final Email emailToSend = buildEmail();
    for (int i = 0; i < NUM_OF_EMAILS_TO_TEST; i++) {
      client.send(emailToSend);
      assertOutgoingEmail(emailToSend);
    }
  }

  private static void assertEmailData(final Email email) {
    try {
      assertStoredEmail(email);
      assertSubject(email);
      assertFrom(email);
      assertRecipients(email);
      assertBodyContent(email);
      assertAttachments(email);
      assertHeaders(email);
    } catch (final Exception ex) {
      fail(ex.getMessage());
    }
  }

  private Email buildEmail() {
    return new OutgoingEmail.Builder()
        .subject(EMAIL_SUBJECT)
        .from(PERSON_4)
        .to(PERSON_1)
        .cc(PERSON_2)
        .cc(PERSON_3)
        .bcc(PERSON_5)
        .body(EMAIL_CONTENT)
        .header(HEADER_KEY, HEADER_VAL)
        .attachment(ATTACHMENT_1)
        .attachment(ATTACHMENT_2)
        .build();
  }
}
