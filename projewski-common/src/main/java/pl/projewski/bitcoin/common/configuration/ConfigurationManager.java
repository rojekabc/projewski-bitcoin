package pl.projewski.bitcoin.common.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {
    private final static String configurationFileName = "pr-bitcoin.properties";

    private static final ConfigurationManager instance = new ConfigurationManager();

    private Properties properties;
    private long lastModified = 0;

    static ConfigurationManager getInstance() {
        return instance;
    }

    public static ModuleConfiguration getInstance(final String moduleName) {
        return new ModuleConfiguration(moduleName);
    }

    private ConfigurationManager() {
    }

    String getConfiguration(final String moduleName, final String propertyName, final String defaultValue) {
        loadCondiguration();
        return properties.getProperty(moduleName + '.' + propertyName, defaultValue);
    }


    private void loadCondiguration() {
        final File propertiesFile = new File(configurationFileName);
        final boolean propertiesExists = propertiesFile.exists();

        if (!propertiesExists) {
            properties = new Properties();
            lastModified = 0;
            return;
        } else if (properties == null) {
            properties = new Properties();
        }

        if (lastModified != propertiesFile.lastModified()) {
            try (InputStream is = new FileInputStream(propertiesFile)) {
                properties.load(is);
                lastModified = propertiesFile.lastModified();
            } catch (final IOException e) {
                e.printStackTrace();
            }

        }

    }
}
