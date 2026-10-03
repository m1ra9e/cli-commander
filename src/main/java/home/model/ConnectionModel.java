/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2025 Lenar Shamsutdinov
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
 *******************************************************************************/
package home.model;

import java.util.Objects;
import java.util.Properties;

public final class ConnectionModel {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String URL_TEMPLATE = "jdbc:postgresql://%s:%d/%s";

    private static final int DEFAULT_PORT = 5432;

    private static final int TIMEOUT = 30;           // sec
    private static final int QUERY_TIMEOUT = 15_000; // ms
    private static final int LOCK_TIMEOUT = 10_000;  // ms

    private final String name;
    private final String url;
    private final Properties connProps;

    public ConnectionModel(String name, String host, int port, String database, String user, String pass) {
        this.name = name;
        port = port != 0 ? port : DEFAULT_PORT;
        url = URL_TEMPLATE.formatted(host, port, database);
        connProps = createConnectionProperties(user, pass);
    }

    private Properties createConnectionProperties(String user, String pass) {
        var props = new Properties();

        props.setProperty("user", user);
        props.setProperty("password", pass);
        props.setProperty("reWriteBatchedInserts", "true");

        String timeout = String.valueOf(TIMEOUT);
        props.setProperty("loginTimeout", timeout);
        props.setProperty("connectTimeout", timeout);
        props.setProperty("cancelSignalTimeout", timeout);
        props.setProperty("socketTimeout", timeout);

        props.setProperty("options", "-c statement_timeout=%d -c lock_timeout=%d"
                .formatted(QUERY_TIMEOUT, LOCK_TIMEOUT));

        return props;
    }

    public String getDriver() {
        return JDBC_DRIVER;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return connProps.getProperty("user");
    }

    public String getPassword() {
        return connProps.getProperty("password");
    }

    public Properties getConnectionProperties(boolean isReadOnly) {
        if (isReadOnly) {
            connProps.setProperty("readOnlyMode", "always");
        }

        return connProps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConnectionModel other)) {
            return false;
        }
        return Objects.equals(name, other.name)
                && Objects.equals(url, other.url);
    }
}
