package io.github.zunpiau.utils;

import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class PropertyUtilTest {

    @Test
    public void testClasspath() throws IOException {
        Properties properties = PropertyUtil.getProperties(PropertyUtil.parsePath("classpath:person.properties"));
        doAssert(properties);
    }

    @Test
    public void testFile() throws IOException {
        Properties properties = PropertyUtil.getProperties(PropertyUtil.parsePath("src/test/resources/person.properties"));
        doAssert(properties);
    }

    private void doAssert(Properties properties) {
        assertEquals("21", properties.getProperty("person.age"));
        assertEquals("Bob", properties.getProperty("person.name"));
    }
}