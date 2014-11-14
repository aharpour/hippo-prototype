package com.tdclighthouse.prototype.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class SpringConfigurablePropertyPlaceholder extends PropertyPlaceholderConfigurer {

    private String systemPropertyName;
    private String defaultPropertiesFileLocation;

    public String getSystemPropertyName() {
        return systemPropertyName;
    }

    public void setSystemPropertyName(String systemPropertyName) {
        this.systemPropertyName = systemPropertyName;
    }

    public String getDefaultPropertiesFileLocation() {
        return defaultPropertiesFileLocation;
    }

    public void setDefaultPropertiesFileLocation(String defaultPropertiesFileLocation) {
        this.defaultPropertiesFileLocation = defaultPropertiesFileLocation;
    }

    @Override
    public Properties mergeProperties() throws IOException {
        return super.mergeProperties();
    }

    @Override
    protected void loadProperties(Properties props) throws IOException {
        Resource location = null;
        if (StringUtils.isNotEmpty(systemPropertyName)) {
            String propertyFilePath = System.getProperties().getProperty(systemPropertyName);
            if (StringUtils.isNotBlank(propertyFilePath)) {
                location = new FileSystemResource(propertyFilePath);
            } else {
                location = new ClassPathResource(defaultPropertiesFileLocation);
            }

        }

        setLocation(location);
        super.loadProperties(props);
    }
}