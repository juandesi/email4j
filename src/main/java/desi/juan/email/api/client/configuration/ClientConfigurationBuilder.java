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
package desi.juan.email.api.client.configuration;

import static desi.juan.email.api.client.configuration.ClientConfiguration.DEFAULT_TIMEOUT;

import java.util.LinkedHashMap;
import java.util.Map;

import desi.juan.email.api.security.TlsConfiguration;

/**
 * Implementation of the builder design patter to build {@link ClientConfiguration}s.
 */
public class ClientConfigurationBuilder {

  private TlsConfiguration tlsConfig;
  private long connectionTimeout = DEFAULT_TIMEOUT;
  private long writeTimeout = DEFAULT_TIMEOUT;
  private long readTimeout = DEFAULT_TIMEOUT;
  private Map<String, String> properties = new LinkedHashMap<>();

  private ClientConfigurationBuilder() {
  }

  public static ClientConfigurationBuilder getInstance() {
    return new ClientConfigurationBuilder();
  }

  public static ClientConfiguration getDefaultConfiguration() {
    return new BasicConfiguration();
  }

  public void withTlsConfig(TlsConfiguration tlsConfig) {
    this.tlsConfig = tlsConfig;
  }

  public void setConnectionTimeout(long connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public void setWriteTimeout(long writeTimeout) {
    this.writeTimeout = writeTimeout;
  }

  public void setReadTimeout(long readTimeout) {
    this.readTimeout = readTimeout;
  }

  public void addProperty(String key, String value) {
    this.properties.put(key, value);
  }

  public ClientConfiguration build()
  {
    return new DefaultClientConfiguration(connectionTimeout, writeTimeout, readTimeout, properties, tlsConfig);
  }
}
