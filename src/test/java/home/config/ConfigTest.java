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
package home.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import home.TestUtils;
import home.model.ConnectionModel;
import home.model.ModeType;

final class ConfigTest {

    @Test
    void validConfigTest() {
        try {
            readConfig("valid_cfg.yaml");

            checkCurrentConfig();
        } catch (Exception e) {
            fail("Errors while read config: " + e.getMessage());
        }
    }

    @Test
    void validConfigDirTest() throws Exception {
        String path = getResourcePath("valid_cfg.yaml");
        String dir = Paths.get(path).getParent().resolve("valid_cfg_dir").toString();
        TestUtils.readConfig(dir);

        checkCurrentConfig();
    }

    @Test
    void notExistsFileConfigTest() {
        assertThrows(NoSuchFileException.class, () -> TestUtils.readConfig("not_exists.yaml"));
    }

    @ParameterizedTest(name = "[{0}]: {1}")
    @CsvSource(delimiter = '|', value = {
            // fileName................|.expectedErrorMsg
            "missed_connection_name    | Missed required param: 'name'",
            "missed_connection_host    | Missed required param: 'host'",
            "missed_connection_db      | Missed required param: 'database'",
            "missed_connection_user    | Missed required param: 'user'",
            "missed_connection_pass    | Missed required param: 'pass'",
            "missed_connections        | Missed required param: 'connections'",
            "missed_settings_mode      | Missed required param: 'mode'",
            "unknown_connections_param | Unknown 'connections' param: unknown_conn_param_1,unknown_conn_param_2",
            "unknown_root_param        | Unknown 'root' param: unknown_root_param",
            "unknown_settings_param    | Unknown 'settings' param: unknown_settings_param",
    })
    void brokenConfigTest(String fileName, String expectedErrorMsg) throws IOException {
        checkErrorMessage(fileName + ".yaml", expectedErrorMsg);
    }

    @Test
    void brokenYamlTest() throws IOException {
        String expectedErrorMsg = """
                error during parsing: while constructing a mapping
                 in 'reader', line 6, column 5:
                        name : first1
                        ^
                found duplicate key database
                 in 'reader', line 9, column 5:
                        database : dbname1
                        ^
                """.strip();
        checkErrorMessage("duplicate_param.yaml", expectedErrorMsg);
    }

    @Test
    void unsupportedConfigFileExtensionTest() throws IOException {
        checkErrorMessage("unsupported_extension.txt", "Unsupported file extension: 'unsupported_extension.txt'");
    }

    private void checkCurrentConfig() {
        Config actualConfig = Config.getCurrent();

        String expectedModeStr = "mirror";
        assertEquals(expectedModeStr, actualConfig.getMode().getTypeName());
        assertEquals(ModeType.getModeType(expectedModeStr), actualConfig.getMode());

        Map<String, ConnectionModel> actualConnections = actualConfig.getConnections();
        assertTrue(actualConnections.size() == 2);

        String connectionName = "first1";
        ConnectionModel actualConnection = actualConnections.get(connectionName);
        assertConnection(
                // expected values
                connectionName,
                "jdbc:postgresql://127.0.0.1:5432/dbname1",
                "username1",
                "password1",
                // actual connection
                actualConnection);

        connectionName = "second2";
        actualConnection = actualConnections.get(connectionName);
        assertConnection(
                // expected values
                connectionName,
                "jdbc:postgresql://localhost:5433/dbname2",
                "username2",
                "password2",
                // actual connection
                actualConnection);
    }

    private void assertConnection(String expectedName, String expectedUrl,
            String expectedUser, String expectedPass, ConnectionModel actualConnection) {
        assertEquals(expectedName, actualConnection.getName());
        assertEquals(expectedUrl,  actualConnection.getUrl());
        assertEquals(expectedUser, actualConnection.getUser());
        assertEquals(expectedPass, actualConnection.getPassword());
    }

    private void checkErrorMessage(String fileName, String expectedErrorMsg) throws IOException {
        try {
            readConfig(fileName);
            fail("Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.");
        } catch (IllegalArgumentException e) {
            String actualErrorMsg = e.getMessage().strip();
            if (!actualErrorMsg.endsWith(expectedErrorMsg)) {
                assertEquals(expectedErrorMsg, actualErrorMsg);
            }
        }
    }

    private void readConfig(String name) throws IOException {
        TestUtils.readConfig(getResourcePath(name));
    }

    private String getResourcePath(String name) {
        return TestUtils.getResourcePath(name, getClass());
    }
}
