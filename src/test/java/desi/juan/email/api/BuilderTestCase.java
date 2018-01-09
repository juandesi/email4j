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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static desi.juan.email.api.BaseTestCase.EMAIL_CONTENT;
import static desi.juan.email.api.BaseTestCase.EMAIL_SUBJECT;
import static desi.juan.email.api.BaseTestCase.PERSON_1;
import static desi.juan.email.api.BaseTestCase.PERSON_2;
import static desi.juan.email.api.EmailConstants.NO_SUBJECT;
import static desi.juan.email.api.EmailConstants.TEXT_HTML;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BuilderTestCase {

  @Rule
  public ExpectedException ee = ExpectedException.none();

  private OutgoingEmail.Builder builder;

  @Before
  public void before() {
    builder = new OutgoingEmail.Builder();
  }

  @Test
  public void onlyBody() {
    final String html = "<h1>Html Text</h1>";
    final Email email = builder
        .to(PERSON_1)
        .from(PERSON_2)
        .body(new EmailBody(html, Charsets.UTF_8, TEXT_HTML))
        .build();

    assertThat(email.getSubject(), is(NO_SUBJECT));
    assertThat(email.getTo(), containsInAnyOrder(PERSON_1));
    assertThat(email.getFrom(), containsInAnyOrder(PERSON_2));
    assertThat(email.getCc().size(), is(0));
    assertThat(email.getBcc().size(), is(0));
    assertThat(email.getBody().getContent(), is(html));
    assertThat(email.getAttachments().size(), is(0));
    assertThat(email.getHeaders().size(), is(0));
  }

  @Test
  public void noFrom() {
    ee.expect(IllegalStateException.class);
    ee.expectMessage(containsString(Email.ERROR_FROM));
    builder
        .to(PERSON_1)
        .body(EMAIL_CONTENT)
        .build();
  }

  @Test
  public void noBody() {
    ee.expect(IllegalStateException.class);
    ee.expectMessage(containsString(Email.ERROR_BODY));
    builder
        .to(PERSON_1)
        .from(PERSON_2)
        .build();
  }

  @Test
  public void noTo() {
    ee.expect(IllegalStateException.class);
    ee.expectMessage(containsString(Email.ERROR_RECIPIENTS));
    builder
        .from(EMAIL_SUBJECT)
        .body(EMAIL_CONTENT)
        .build();
  }
}
