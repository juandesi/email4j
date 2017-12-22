package desi.juan.email.api;

import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static desi.juan.email.api.EmailConstants.DEFAULT_CHARSET;

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
}
