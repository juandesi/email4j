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

import com.google.common.base.Charsets;
import desi.juan.email.internal.OutgoingEmail;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static desi.juan.email.EmailTestUtils.*;
import static desi.juan.email.api.EmailBuilder.newEmail;
import static desi.juan.email.api.EmailConstants.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmailBuilderTestCase {

  @Rule
  public ExpectedException ee = ExpectedException.none();

  private final EmailAttachment attachment1 = new EmailAttachment("id1", "content", DEFAULT_CONTENT_TYPE);
  private final EmailAttachment attachment2 = new EmailAttachment("id2", "content", DEFAULT_CONTENT_TYPE);

  @Test
  public void completeEmail() {
    Email email = newEmail()
        .withAttachment(attachment1)
        .withAttachment(attachment2)
        .withSubject(EMAIL_SUBJECT)
        .bcc(asList(GOHAN_EMAIL, VEGETA_EMAIL))
        .cc(TRUNKS_EMAIL)
        .to(GOKU_EMAIL)
        .from(ROSHI_EMAIL)
        .withBody(EMAIL_CONTENT)
        .withHeader(HEADER_KEY, HEADER_VAL)
        .build();
    
    assertThat(email, is(instanceOf(OutgoingEmail.class)));
    assertThat(email.getBccAddresses(), hasItems(GOHAN_EMAIL, VEGETA_EMAIL));
    assertThat(email.getCcAddresses(), hasItems(TRUNKS_EMAIL));
    assertThat(email.getToAddresses(), hasItems(GOKU_EMAIL));
    assertThat(email.getAttachments().size(), is(2));
    assertThat(email.getAttachments(), hasItems(attachment1, attachment2));
    assertThat(email.getBody().getContent(), is(EMAIL_CONTENT));
    assertThat(email.getBody().getContentType(), is(DEFAULT_CONTENT_TYPE));
    assertThat(email.getSubject(), is(EMAIL_SUBJECT));
    assertThat(email.getHeaders().size(), is(1));
    assertThat(email.getHeaders().get(HEADER_KEY), hasItems(HEADER_VAL));
  }

  @Test
  public void onlyBodyEmail() {
    String html = "<h1>Html Text</h1>";
    Email email = newEmail()
        .to(singletonList(GOKU_EMAIL))
        .from(ROSHI_EMAIL)
        .withBody(new EmailBody(html, Charsets.UTF_8, TEXT_HTML))
        .build();

    assertThat(email.getHeaders().size(), is(0));
    assertThat(email.getBccAddresses().size(), is(0));
    assertThat(email.getCcAddresses().size(), is(0));
    assertThat(email.getBody().getContent(), is(html));
    assertThat(email.getSubject(), is(NO_SUBJECT));
  }

  @Test
  public void noBodyEmail() {
    ee.expect(IllegalStateException.class);
    ee.expectMessage(containsString("with no body"));
    newEmail()
        .withAttachment(attachment2)
        .withSubject(EMAIL_SUBJECT)
        .withHeader(HEADER_KEY, HEADER_VAL)
        .build();
  }

  @Test
  public void noToEmail() {
    ee.expect(IllegalStateException.class);
    ee.expectMessage(containsString("with no TO address(es)"));
    newEmail()
        .withAttachment(attachment2)
        .withSubject(EMAIL_SUBJECT)
        .withBody("")
        .withHeader(HEADER_KEY, HEADER_VAL)
        .build();
  }

  @Test
  public void noFromEmail() {
    ee.expect(IllegalStateException.class);
    ee.expectMessage(containsString("with no from address"));
    newEmail()
        .withAttachment(attachment2)
        .withSubject(EMAIL_SUBJECT)
        .withBody("")
        .to(singletonList(ROSHI_EMAIL))
        .withHeader(HEADER_KEY, HEADER_VAL)
        .build();
  }
}
