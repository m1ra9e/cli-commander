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

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import home.model.ConnectionModel;
import home.model.ModeType;

public final class Config {

    private static Config config;

    private Config() {
    }

    public static void readConfigs(String configFiles) throws IOException {
        config = new Config();
        ConfigParser.readConfigs(configFiles, config);
    }

    public static Config getCurrent() {
        return config;
    }

    private ModeType mode;

    private final Map<String, ConnectionModel> connectionModels = new HashMap<>();

    void setMode(ModeType mode) {
        this.mode = mode;
    }

    public ModeType getMode() {
        return mode;
    }

    void addConnection(ConnectionModel connectionModel) {
        connectionModels.put(connectionModel.getName(), connectionModel);
    }

    public ConnectionModel getConnection(String name) {
        return connectionModels.get(name);
    }

    public Map<String, ConnectionModel> getConnections() {
        return Collections.unmodifiableMap(connectionModels);
    }

    public Collection<ConnectionModel> getConnectionsForCurrentMode() {
        return getConnectionsForMode(mode);
    }

    public Collection<ConnectionModel> getConnectionsForMode(ModeType modeType) {
        Collection<ConnectionModel> connections = getConnections().values();

        return switch (modeType) {
            case MIRROR -> connections;
            case SINGLE -> Collections.singletonList(connections.iterator().next());
            default -> throw new IllegalArgumentException("Unexpected value: " + mode);
        };
    }
}
