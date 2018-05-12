package io.github.zunpiau;

import io.github.zunpiau.utils.PropertyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AutoRefreshTest {

    private Properties properties;
    private File propertiesFile;

    @Before
    public void setup() throws IOException {
        AutoConfiguration autoConfiguration = Config.class.getAnnotation(AutoConfiguration.class);
        propertiesFile = PropertyUtil.parsePath(autoConfiguration.value()).toFile();
        properties = PropertyUtil.getProperties(Paths.get("src/test/resources/config.properties"));
        storeProperties(properties, propertiesFile);
    }

    @Test
    public void testModifyProperties() throws IllegalAccessException, IOException, InstantiationException, InterruptedException {
        Config config = new ConfigurationFactory().create(Config.class);
        assertTrue(config.enable);
        modifyConfig();
        Thread.sleep(100);
        assertFalse(config.enable);
    }

    private void modifyConfig() throws IOException {
        Properties propertiesModified = (Properties) properties.clone();
        propertiesModified.setProperty("autoRefresh.enable", "false");
        storeProperties(propertiesModified, propertiesFile);
    }

    private void storeProperties(Properties properties, File file) throws IOException {
        properties.store(new FileOutputStream(file), "");
    }

    @After
    public void cleanup() throws IOException {
        storeProperties(properties, propertiesFile);
    }

    @AutoConfiguration(value = "classpath:config.properties", prefix = "autoRefresh.", autoRefresh = true)
    public static class Config {

        @Value
        private boolean enable;
    }

}
