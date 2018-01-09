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
package desi.juan.email.api.client.configuration;

import com.google.common.collect.ImmutableMap;
import desi.juan.email.api.security.TlsConfiguration;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents a configuration with advanced which enables the use of TLS for encrypted
 * transactions and more advanced configurations for the underlying procotol by injecting specific
 * properties.
 */
public class ClientConfiguration {

  private final long connectionTimeout;
  private final Map<String, String> properties;
  private final long readTimeout;
  private final TlsConfiguration tlsConfig;
  private final long writeTimeout;

  /**
   * Default timeouts are 10 seconds
   *
   * @return
   */
  public ClientConfiguration() {
    this(10, Collections.emptyMap(), 10, null, 10);
  }

  public ClientConfiguration(final long connectionTimeout,
                             final Map<String, String> properties,
                             final long readTimeout,
                             final TlsConfiguration tlsConfig,
                             final long writeTimeout) {
    this.connectionTimeout = connectionTimeout;
    this.properties = ImmutableMap.copyOf(properties);
    this.readTimeout = readTimeout;
    this.tlsConfig = tlsConfig;
    this.writeTimeout = writeTimeout;
  }

  /**
   * @return
   */
  public long getConnectionTimeout() {
    return connectionTimeout;
  }

  /**
   * @return
   */
  public Map<String, String> getProperties() {
    return properties;
  }

  /**
   * @return
   */
  public long getReadTimeout() {
    return readTimeout;
  }

  /**
   * @return
   */
  public Optional<TlsConfiguration> getTlsConfig() {
    return Optional.ofNullable(tlsConfig);
  }

  /**
   * @return
   */
  public long getWriteTimeout() {
    return writeTimeout;
  }
}
