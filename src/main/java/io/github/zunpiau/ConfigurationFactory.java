package io.github.zunpiau;

import io.github.zunpiau.utils.CharEditor;
import io.github.zunpiau.utils.FileWatcher;
import io.github.zunpiau.utils.PropertyUtil;
import io.github.zunpiau.utils.ReflectionUtil;

import java.beans.PropertyEditorManager;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationFactory {

    private final Map<Path, Class> classMap;
    private final HashMap<Class, Object> instanceMap;
    private FileWatcher fileWatcher;

    public ConfigurationFactory() {
        classMap = new HashMap<>(4);
        instanceMap = new HashMap<>();
        if (PropertyEditorManager.findEditor(Character.class) == null) {
            PropertyEditorManager.registerEditor(Character.class, CharEditor.class);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> clazz) throws IllegalAccessException, InstantiationException, IOException {
        T t;
        if (instanceMap.containsKey(clazz)) {
            t = (T) instanceMap.get(clazz);
        } else {
            t = clazz.newInstance();
            config(clazz, t);
        }
        return t;
    }

    public void config(Object o) throws IllegalAccessException, IOException {
        config(o.getClass(), o);
    }

    private <T> void config(Class<?> clazz, T t) throws IllegalAccessException, IOException {
        AutoConfiguration autoConfiguration = getAutoConfigurationAnnotation(clazz);
        Path path = PropertyUtil.parsePath(autoConfiguration.value());
        config(clazz, t, autoConfiguration, path);
        if (autoConfiguration.autoRefresh()) {
            if (fileWatcher == null) {
                initFileWatcher();
            }
            fileWatcher.register(path);
        }
        classMap.put(path, clazz);
        instanceMap.put(clazz, t);
    }

    private void initFileWatcher() throws IOException {
        fileWatcher = new FileWatcher(new FileWatcher.FileListener() {
            @Override
            public void onDelete(Path path) {
                throw new IllegalStateException("[" + path + "] has delete");
            }

            @Override
            public void onModify(Path path) {
                Class clazz = classMap.get(path);
                try {
                    config(clazz, instanceMap.get(clazz), getAutoConfigurationAnnotation(clazz), path);
                } catch (IOException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private <T> void config(Class<?> clazz, T t, AutoConfiguration autoConfiguration, Path path) throws IOException, IllegalAccessException {
        Properties properties = PropertyUtil.getProperties(path);
        String prefix = autoConfiguration.prefix();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Value valueAnnotation = ReflectionUtil.getAnnotation(field, Value.class);
            if (valueAnnotation == null) {
                continue;
            }
            String name = valueAnnotation.value();
            if (name.equals(""))
                name = field.getName();
            String key = prefix + name;
            if (!properties.containsKey(key)) {
                throw new IllegalArgumentException("[" + key + "] not found in [" + path + "]");
            }
            Class filedType = field.getType();
            if (filedType.isArray()) {
                ReflectionUtil.setValue(field, t, properties.getProperty(key).split(","), filedType.getComponentType());
            } else {
                ReflectionUtil.setValue(field, t, properties.getProperty(key), filedType);
            }
        }
    }

    private AutoConfiguration getAutoConfigurationAnnotation(Class<?> clazz) {
        try {
            return clazz.getAnnotation(AutoConfiguration.class);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("[" + clazz + "] don't has @AutoConfiguration annotation");
        }
    }

}
