package pl.projewski.bitcoin.common.configuration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ModuleConfiguration {
    private final String name;

    public int getInt(final String propertyName, final int defaultValue) {
        final ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        final String conf = configurationManager.getConfiguration(name, propertyName, null);
        return conf == null ? defaultValue : Integer.valueOf(conf);
    }

    public String getString(final String propertyName, final String defaultValue) {
        final ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        return configurationManager.getConfiguration(name, propertyName, defaultValue);
    }
}
