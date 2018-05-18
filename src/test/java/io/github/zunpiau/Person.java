package io.github.zunpiau;

import static io.github.zunpiau.Person.PREFIX;
import static io.github.zunpiau.Person.VALUE;

@SuppressWarnings({"WeakerAccess", "unused"})
@AutoConfiguration(value = VALUE, prefix = PREFIX)
public class Person {

    static final String PREFIX = "person.";
    static final String VALUE = "classpath:person.properties";
    @Value
    private Integer age;
    @Value
    private String name;
    @Value("short")
    private short aShort;
    @Value("double")
    private double aDouble;
    @Value("char")
    private Character aChar;
    @Value("boolean")
    private boolean aBoolean;

    private long ignore;

    public Person() {
    }

    public long getIgnore() {
        return ignore;
    }

    public boolean isBoolean() {
        return aBoolean;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public short getShort() {
        return aShort;
    }

    public double getDouble() {
        return aDouble;
    }

    public char getChar() {
        return aChar;
    }

}
