package pl.homeworkjustclick.infrastructure.rest;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "restapi.mongo")
@Getter
@Setter
public class MongoConfigProps {
    @NotEmpty
    private String url;
}
