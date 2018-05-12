package io.github.zunpiau.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyUtil {

    private static final String CLASSPATH_PREFIX = "classpath:";

    public static Path parsePath(String source) {
        String path;
        if (source.startsWith(CLASSPATH_PREFIX)) {
            URL url = PropertyUtil.class.getClassLoader().getResource(source.substring(CLASSPATH_PREFIX.length()));
            if (url == null) {
                throw new IllegalArgumentException("[" + source + "] not exists");
            }
            path = url.getPath();
        } else
            path = source;
        return Paths.get(path);
    }

    public static Properties getProperties(Path source) throws IOException {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(source.toFile())) {
            properties.load(is);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("[" + source + "] not exists");
        }
        return properties;
    }

}
