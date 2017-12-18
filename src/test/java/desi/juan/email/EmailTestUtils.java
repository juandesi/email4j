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
package desi.juan.email;

import com.icegreen.greenmail.util.ServerSetup;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.URL;
import java.util.Properties;

import static desi.juan.email.api.EmailConstants.TEXT;
import static java.lang.Thread.currentThread;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Part.ATTACHMENT;

public class EmailTestUtils {

  public static final String EMAIL_SUBJECT = "DBZ Email";
  public static final String EMAIL_CONTENT = "Dragon Ball GT sucks";
  public static final String TEXT_PLAIN_ATTACHMENT_CONTENT = "Dragon Ball Z";
  public static final String TEXT_PLAIN_ATTACHMENT_NAME = "dbz-title";
  public static final String JSON_ATTACHMENT_CONTENT = "{\"goku\": \"ssj\"}";
  public static final String JSON_ATTACHMENT_NAME = "dbz.json";

  public static final String GOKU_EMAIL = "goku@dbz.com";
  public static final String TRUNKS_EMAIL = "truks.purplehair@dbz.com";
  public static final String VEGETA_EMAIL = "vegeta@dbz.com";
  public static final String GOHAN_EMAIL = "gohan@dbz.com";
  public static final String ROSHI_EMAIL = "roshi.pervert@dbz.com";

  public static final String HEADER_KEY = "HeaderKey";
  public static final String HEADER_VAL = "HeaderValue";

  public static final long SERVER_STARTUP_TIMEOUT = 5000;

  public static final Session testSession = Session.getDefaultInstance(new Properties());


  public static MimeMessage getMultipartTestMessage() throws Exception {
    MimeBodyPart body = new MimeBodyPart();
    body.setContent(EMAIL_CONTENT, TEXT);

    MimeBodyPart textAttachment = new MimeBodyPart();
    textAttachment.setDisposition(ATTACHMENT);
    textAttachment.setFileName(TEXT_PLAIN_ATTACHMENT_NAME);
    textAttachment.setContent(TEXT_PLAIN_ATTACHMENT_CONTENT, TEXT);

    MimeBodyPart jsonAttachment = new MimeBodyPart();
    URL resource = currentThread().getContextClassLoader().getResource(JSON_ATTACHMENT_NAME);
    jsonAttachment.setFileName(JSON_ATTACHMENT_NAME);
    jsonAttachment.setDataHandler(new DataHandler(resource));

    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(body);
    multipart.addBodyPart(textAttachment);
    multipart.addBodyPart(jsonAttachment);

    MimeMessage message = new MimeMessage(testSession);
    message.setContent(multipart);
    message.setSubject(EMAIL_SUBJECT);
    message.setRecipient(TO, new InternetAddress(GOHAN_EMAIL));
    return message;
  }

  public static Message getSinglePartTestMessage() throws MessagingException {
    Message message = new MimeMessage(testSession);
    message.setText(EMAIL_CONTENT);
    message.setSubject(EMAIL_SUBJECT);
    message.setRecipient(TO, new InternetAddress(GOHAN_EMAIL));
    return message;
  }

  public static ServerSetup setUpServer(int port, String protocol) {
    ServerSetup serverSetup = new ServerSetup(port, null, protocol);
    serverSetup.setServerStartupTimeout(SERVER_STARTUP_TIMEOUT);
    return serverSetup;
  }
}
