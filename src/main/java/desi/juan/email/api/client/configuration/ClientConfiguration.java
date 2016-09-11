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

import java.util.Map;
import java.util.Optional;

import desi.juan.email.api.security.TlsConfiguration;

/**
 * This interface represents a configuration with advanced attributes for a client, you can always go
 * to the {@link BasicConfiguration} for basic cases, but this enables the use of TLS for encrypted
 * transactions and more advanced configurations for the underlying procotol by injecting specific
 * properties.
 */
public interface ClientConfiguration {

  long DEFAULT_TIMEOUT = 5000;

  Optional<TlsConfiguration> getTlsConfig();

  long getConnectionTimeout();

  long getWriteTimeout();

  long getReadTimeout();

  Map<String, String> getProperties();
}
