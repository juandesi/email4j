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
package desi.juan.email.api.security;

/**
 * Generic contract for classes that enables and centralise all the work of TLS/SSL configuration
 * in order to be used in secure connections.
 *
 * //TODO: CREATE IMPLEMENTATIONS
 */
public interface TlsConfiguration {

  /**
   * The list of ciphers that must be used to restrict the creation of the SSL Sockets
   *
   * @return ths list of enabled cipher suites
   */
  default String[] getEnabledCipherSuites() {
    return new String[0];
  }

  /**
   * The list of enabled protocols that must be used to restrict the creation of the SSL Sockets
   *
   * @return the list of enabled protocols
   */
  default String[] getEnabledProtocols() {
    return new String[0];
  }

  /**
   * @return true if the keystore was configured, false otherwise
   */
  default boolean isKeyStoreConfigured() {
    return false;
  }

  /**
   * @return true if the trust store was configured, false otherwise
   */
  default boolean isTrustStoreConfigured() {
    return false;
  }

  /**
   * @return An object with the configuration of the key store.
   */
  default TlsContextKeyStoreConfiguration getKeyStoreConfiguration() {
    return null;
  }

  /**
   * @return An object with the configuration of the trust store.
   */
  default TlsContextTrustStoreConfiguration getTrustStoreConfiguration() {
    return null;
  }
}
