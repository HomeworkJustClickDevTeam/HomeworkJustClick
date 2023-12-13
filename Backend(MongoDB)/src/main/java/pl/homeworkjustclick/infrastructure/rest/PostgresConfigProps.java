package pl.homeworkjustclick.infrastructure.rest;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "restapi.postgres")
@Getter
@Setter
public class PostgresConfigProps {
    @NotEmpty
    private String url;
}
