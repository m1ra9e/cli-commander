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

import static home.config.ConfigConst.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import home.model.ConnectionModel;
import home.model.ModeType;

final class ConfigParser {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigParser.class);

    private final Config config;

    static void readConfigs(String configFiles, Config config) throws IOException {
        new ConfigParser(config).parseFiles(configFiles);
    }

    private ConfigParser(Config config) {
        this.config = config;
    }

    private void parseFiles(String fileOrDirStrPaths) throws IOException {
        var dataMaps = new HashMap<String, Map<String, Object>>();
        for (String strPath : convertToPaths(fileOrDirStrPaths)) {
            Map<String, Object> dataMap = readYamlFile(strPath);
            if (dataMap != null) {
                dataMaps.computeIfAbsent(strPath, k -> new HashMap<>()).putAll(dataMap);
            }
        }

        dataMaps.forEach((path, dataMap) -> parseData(path, () -> readConfig(dataMap)));

        verify();
    }

    /**
     * Convert input path to list of nested configuration files
     *
     * @param fileOrDirStrPaths list of file/directory paths, separated by commas
     * @return sorted list of files
     * @throws IOException if error occurs when opening the directory
     */
    private List<String> convertToPaths(String fileOrDirStrPaths) throws IOException {
        var strPaths = new ArrayList<String>();
        for (String fileOrDirStrPath : fileOrDirStrPaths.split(",")) {
            Path path = Paths.get(fileOrDirStrPath);
            String shortName = path.getFileName().toString();
            if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                LOG.info("Reading directory: {}", shortName);
                collectPaths(path, strPaths);
            } else if (ConfigUtils.endsWithIgnoreCase(fileOrDirStrPath, ".yaml")) {
                LOG.info("Reading yaml-file: {}", shortName);
                strPaths.add(fileOrDirStrPath);
            } else {
                throw new IllegalArgumentException("Unsupported file extension: '%s'".formatted(shortName));
            }
        }

        strPaths.sort(Comparator.comparing(s -> s.substring(s.lastIndexOf(File.separator) + 1)));

        return strPaths;
    }

    /**
     * Collects a list of nested configuration files paths from the input path
     *
     * @param dirOrFilePath path to directory or yaml-file
     * @param strPaths      list with yaml-file paths
     * @throws IOException if error occurs when opening the directory
     */
    private void collectPaths(Path dirOrFilePath, List<String> strPaths) throws IOException {
        try (Stream<Path> filesStream = Files.list(dirOrFilePath)) {
            for (Path dirOrFile : getDirsAndYamlFiles(filesStream)) {
                if (Files.isDirectory(dirOrFile)) {
                    collectPaths(dirOrFile, strPaths);
                } else {
                    strPaths.add(dirOrFile.toString());
                }
            }
        }
    }

    /**
     * Returns paths to directories and yaml-files
     *
     * @param filesStream - stream containing paths to directories and files
     * @return paths to directories and yaml-files
     */
    private Iterable<Path> getDirsAndYamlFiles(Stream<Path> filesStream) {
        return (Iterable<Path>) filesStream
                .filter(p -> Files.isDirectory(p) || ConfigUtils.endsWithIgnoreCase(p.toString(), ".yaml"))::iterator;
    }

    /**
     * Reads the yaml-file
     *
     * @param filePath yaml-file path
     * @return map with data read from yaml-file
     * @throws IOException              if file read error occurs
     * @throws IllegalArgumentException if error in file filling format
     */
    private Map<String, Object> readYamlFile(String filePath) throws IOException {
        LOG.info("Reading file: {}", filePath);
        try {
            var options = new LoaderOptions();
            options.setAllowDuplicateKeys(false);
            try (InputStream inputStream = getInputStreamFromFile(filePath)) {
                return new Yaml(options).load(inputStream);
            }
        } catch (YAMLException | ClassCastException e) {
            throw new IllegalArgumentException(
                    "%s: error during parsing: %s".formatted(filePath, e.getMessage()), e);
        }
    }

    /**
     * Parsing data object from yaml-file
     *
     * @param fileName      file name
     * @param dataObjReader data object reader
     * @throws IllegalArgumentException configuration error
     */
    private void parseData(String fileName, Runnable dataObjReader) {
        LOG.debug("Reading data with settings: {}", fileName);
        try {
            dataObjReader.run();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(fileName + ": error during parsing: " + e.getMessage(), e);
        }
    }

    /**
     * Getting stream for a given file First, the file is searched for among system
     * resources, then by its full path.
     *
     * @param filePath file path
     * @return input stream from file
     * @throws IOException if file read error occurs
     */
    private InputStream getInputStreamFromFile(String filePath) throws IOException {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(filePath);
        return inputStream != null ? inputStream : Files.newInputStream(Paths.get(filePath));
    }

    /**
     * Processing the root data map and writing processed data to storage
     *
     * @param dataMap root data map
     * @throws IllegalArgumentException configuration error
     */
    private void readConfig(Map<String, Object> dataMap) {
        dataMap.forEach((entity, value) -> {
            switch (entity) {
                case SETTINGS -> readSettings(ConfigUtils.castToMap(value, SETTINGS));
                case CONNECTIONS -> {
                    for (Object connection : ConfigUtils.castToList(value, CONNECTIONS)) {
                        readConnection(ConfigUtils.castToMap(connection, CONNECTIONS));
                    }
                }
                default -> throw new IllegalArgumentException("Unknown 'root' param: " + entity);
            }
        });
    }

    private void readSettings(Map<?, ?> dataMap) {
        Map<String, String> settingsMap = ConfigUtils.convertWildMapToStringMap(dataMap);

        String mode = removeRequiredFromMap(MODE, settingsMap);

        checkUnknownParams(settingsMap, SETTINGS);

        config.setMode(ModeType.getModeType(mode));
    }

    private void readConnection(Map<?, ?> dataMap) {
        String name = removeRequiredFromMap(NAME, dataMap);

        if (config.getConnection(name) != null) {
            throw new IllegalArgumentException("Duplicate connection name: " + name);
        }

        String host     = removeRequiredFromMap(HOST,     dataMap);
        String database = removeRequiredFromMap(DATABASE, dataMap);
        String user     = removeRequiredFromMap(USER,     dataMap);
        String pass     = removeRequiredFromMap(PASS,     dataMap);

        int port = parseInt(removeFromMap(PORT, dataMap), PORT);

        checkUnknownParams(dataMap, CONNECTIONS);

        config.addConnection(new ConnectionModel(name, host, port, database, user, pass));
    }

    private String removeRequiredFromMap(String key, Map<?, ?> dataMap) {
        return removeFromMap(key, dataMap, true);
    }

    private String removeFromMap(String key, Map<?, ?> dataMap) {
        return removeFromMap(key, dataMap, false);
    }

    private String removeFromMap(String key, Map<?, ?> dataMap, boolean isRequired) {
        Object value = dataMap.remove(key);
        if (value == null) {
            if (isRequired) {
                throwMissedRequiredParam(key);
            }

            return null;
        }

        return value.toString();
    }

    private int parseInt(String value, String key) {
        try {
            return value == null ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Incorrect value of int param '%s': %s".formatted(key, value));
        }
    }

    private void checkUnknownParams(Map<?, ?> dataMap, String scope) {
        Set<?> keys = dataMap.keySet();
        if (!keys.isEmpty()) {
            String params = keys.stream().map(Object::toString).collect(Collectors.joining(","));
            throw new IllegalArgumentException("Unknown '%s' param: %s".formatted(scope, params));
        }
    }

    private void verify() {
        if (config.getMode() == null) {
            throwMissedRequiredParam(MODE);
        }

        if (config.getConnections().isEmpty()) {
            throwMissedRequiredParam(CONNECTIONS);
        }
    }

    private void throwMissedRequiredParam(String param) {
        throw new IllegalArgumentException("Missed required param: '%s'".formatted(param));
    }
}
