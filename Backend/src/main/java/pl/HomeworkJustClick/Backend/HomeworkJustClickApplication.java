package pl.HomeworkJustClick.Backend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class HomeworkJustClickApplication {

    @PostConstruct
	public static void main(String[] args) {
		SpringApplication.run(HomeworkJustClickApplication.class, args);
	}

}
