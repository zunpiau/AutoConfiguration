package io.github.zunpiau;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ConfigurationFactoryTest {

    @Test
    public void testCreate() throws IllegalAccessException, IOException, InstantiationException {
        Person person = new ConfigurationFactory().create(Person.class);
        doAssert(person);
    }

    @Test
    public void testConfig() throws IOException, IllegalAccessException {
        Person otherPerson = new Person();
        new ConfigurationFactory().config(otherPerson);
        doAssert(otherPerson);
    }

    @Test
    public void testSingleton() throws IllegalAccessException, IOException, InstantiationException, InterruptedException {
        ConfigurationFactory factory = new ConfigurationFactory();
        assertSame(factory.create(Person.class), factory.create(Person.class));
        ConfigurationFactory otherFactor = new ConfigurationFactory();
        assertNotSame(factory.create(Person.class), otherFactor.create(Person.class));
    }

    private void doAssert(Person person) {
        assertEquals(21, person.getAge());
        assertEquals("Bob", person.getName());
        assertEquals('c', person.getChar());
        assertEquals(1.2, person.getDouble(), 0);
        assertEquals(1, person.getShort());
        assertTrue(person.isBoolean());
        assertEquals(0, person.getIgnore());
    }
}