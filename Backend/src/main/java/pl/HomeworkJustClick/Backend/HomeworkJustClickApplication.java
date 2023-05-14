package pl.HomeworkJustClick.Backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition
public class HomeworkJustClickApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeworkJustClickApplication.class, args);
	}

}
