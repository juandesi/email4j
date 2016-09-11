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
package desi.juan.email;

import static desi.juan.email.EmailTestUtils.EMAIL_CONTENT;
import static desi.juan.email.EmailTestUtils.EMAIL_SUBJECT;
import static desi.juan.email.EmailTestUtils.GOKU_EMAIL;
import static desi.juan.email.EmailTestUtils.setUpServer;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import desi.juan.email.api.Email;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public abstract class Email4JTestCase {

  protected static final String PASSWORD = "pass";
  protected static final String HOST = "127.0.0.1";
  protected static final int PORT = 10000;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  protected GreenMail server;
  protected GreenMailUser user;

  @Before
  public void setup() throws Exception {
    ServerSetup serverSetup = setUpServer(PORT, getProtocol());
    server = new GreenMail(serverSetup);
    server.start();
    user = server.setUser(GOKU_EMAIL, GOKU_EMAIL, PASSWORD);
  }

  @After
  public void dispose() throws Exception {
    assertThat(server, is(not(nullValue())));
    server.stop();
  }

  protected void assertBodyContent(Email email) {
    assertThat(email.getBody().getContent(), is(EMAIL_CONTENT));
    assertThat(email.getBody().getContentType(), startsWith("text/"));
  }

  protected void assertSubject(Email email) {
    assertThat(email.getSubject(), is(EMAIL_SUBJECT));
  }

  public abstract String getProtocol();
}
