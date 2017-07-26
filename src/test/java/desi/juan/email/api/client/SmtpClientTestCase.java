/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Juan Desimoni
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

import static desi.juan.email.EmailTestUtils.EMAIL_CONTENT;
import static desi.juan.email.EmailTestUtils.EMAIL_SUBJECT;
import static desi.juan.email.EmailTestUtils.GOHAN_EMAIL;
import static desi.juan.email.EmailTestUtils.GOKU_EMAIL;
import static desi.juan.email.EmailTestUtils.HEADER_KEY;
import static desi.juan.email.EmailTestUtils.HEADER_VAL;
import static desi.juan.email.EmailTestUtils.TRUNKS_EMAIL;
import static desi.juan.email.EmailTestUtils.VEGETA_EMAIL;
import static desi.juan.email.api.EmailBuilder.newEmail;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import desi.juan.email.Email4JTestCase;
import desi.juan.email.api.Email;
import desi.juan.email.api.client.configuration.ClientConfiguration;
import desi.juan.email.internal.EmailProtocol;
import org.junit.Before;
import org.junit.Test;

public class SmtpClientTestCase extends Email4JTestCase {

  private SmtpClient client;

  @Before
  public void createClient() {
    client = new SmtpClient(GOKU_EMAIL, PASSWORD, HOST, PORT, new ClientConfiguration());
  }

  @Test
  public void sendSimple() throws IOException, MessagingException {
    Email email = buildSimpleEmail();
    client.send(email);
    List<MimeMessage> messages = getReceivedMessages();
    assertThat(messages, hasSize(3));
    MimeMessage message = messages.get(0);
    assertThat(message.getContent().toString().trim(), is(EMAIL_CONTENT));
    assertThat(message.getSubject(), is(EMAIL_SUBJECT));
    assertRecipients(message.getRecipients(TO), GOHAN_EMAIL);
    assertRecipients(message.getRecipients(CC), VEGETA_EMAIL, TRUNKS_EMAIL);
    assertThat(message.getHeader(HEADER_KEY)[0], is(HEADER_VAL));
  }

  @Test
  public void sendBatch() throws IOException, MessagingException {
    Email email = buildSimpleEmail();
    for (int i = 0; i < 100; i++) {
      client.send(email);
    }
    List<MimeMessage> messages = getReceivedMessages();
    assertThat(messages, hasSize(300));
    messages.forEach(m -> {
      try {
        assertThat(m.getContent().toString().trim(), is(EMAIL_CONTENT));
        assertThat(m.getSubject(), is(EMAIL_SUBJECT));
      } catch (Exception e) {
        fail(e.getMessage());
      }
    });

  }

  @Override
  public String getProtocol() {
    return EmailProtocol.SMTP.getName();
  }

  private Email buildSimpleEmail() {
    return newEmail()
      .to(GOHAN_EMAIL)
      .cc(VEGETA_EMAIL)
      .cc(TRUNKS_EMAIL)
      .withBody(EMAIL_CONTENT)
      .withSubject(EMAIL_SUBJECT)
      .withHeader(HEADER_KEY, HEADER_VAL)
      .from(GOKU_EMAIL)
      .build();
  }

  private List<MimeMessage> getReceivedMessages() {
    return asList(server.getReceivedMessages());
  }

  private void assertRecipients(Address[] recipients, String... emails) {
    List<String> addresses = stream(recipients).map(Address::toString).collect(toList());
    assertThat(addresses, containsInAnyOrder(emails));
  }
}
