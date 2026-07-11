package home;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import home.config.Config;

public final class TestUtils {

    public static String getResourcePath(String name, Class<?> clazz) {
        URL resource = clazz.getResource(name);
        if (resource == null) {
            fail(name + " not found");
        }

        return toFilePath(resource);
    }

    private static String toFilePath(URL url) {
        return new File(url.getPath()).toString();
    }

    public static void readConfig(String fileName) throws IOException {
        Config.readConfigs(fileName);
    }

    private TestUtils() {
    }
}
