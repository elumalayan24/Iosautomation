package com.automation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.InputStream;
import java.util.Map;

public final class ConfigReader {

    private static volatile ConfigReader instance;
    private final Map<String, Object> config;

    @SuppressWarnings("unchecked")
    private ConfigReader() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("config.yaml")) {
            if (stream == null) {
                throw new IllegalStateException("config.yaml not found in classpath");
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            config = mapper.readValue(stream, Map.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load config.yaml", e);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public String getString(String path) {
        String[] keys = path.split("\\.");
        Object current = config;
        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(key);
            } else {
                throw new IllegalArgumentException("Invalid config path: " + path);
            }
        }
        return current != null ? current.toString() : null;
    }

    @SuppressWarnings("unchecked")
    public int getInt(String path) {
        String value = getString(path);
        if (value == null) {
            throw new IllegalArgumentException("Config key not found: " + path);
        }
        return Integer.parseInt(value);
    }

    @SuppressWarnings("unchecked")
    public boolean getBoolean(String path) {
        String value = getString(path);
        if (value == null) {
            throw new IllegalArgumentException("Config key not found: " + path);
        }
        return Boolean.parseBoolean(value);
    }

    public String getWithDefault(String path, String defaultValue) {
        try {
            String value = getString(path);
            return value != null ? value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getSection(String section) {
        return (T) config.get(section);
    }
}
