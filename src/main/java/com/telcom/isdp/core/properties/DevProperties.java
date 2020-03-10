package com.telcom.isdp.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix="remote", ignoreUnknownFields = false)
@PropertySource(value={"classpath:META-INF/spring-devtools.properties"}, encoding = "utf-8")
@Data
@Component
public class DevProperties {
    private String httpUrl;

    public String getHttpUrl() {
        return httpUrl;
    }
}
