package com.itranzition.alex.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rabbit", ignoreUnknownFields = false)
public class RabbitConfigurationProperties {
    private String queue;
    private String routing;
    private String exchange;
}
