package desi.juan.email.api;

import com.google.common.collect.ImmutableList;
import desi.juan.email.internal.connection.AbstractConnection;
import desi.juan.email.internal.connection.MailboxManagerConnection;
import org.junit.After;
import org.junit.Test;

import javax.mail.MessagingException;

public abstract class ReceiveTestCase extends BaseTestCase {

  @Test
  public void singlePart() throws MessagingException {
    sendInitialEmailBatch(0);
    final ImmutableList<Email> emails = getEmails();
    doSinglePartReceiveTests(emails);
  }

  @Test
  public void multiPart() throws MessagingException {
    sendInitialEmailBatch(1);
    final ImmutableList<Email> emails = getEmails();
    doMultiPartReceiveTests(emails);
  }

  @After
  public void after() {
    final AbstractConnection ac = getClient();
    if (ac instanceof MailboxManagerConnection) {
      final MailboxManagerConnection mmc = (MailboxManagerConnection) ac;
      mmc.disconnect();
    }
  }

  protected abstract ImmutableList<Email> getEmails();
}
