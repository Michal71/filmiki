package starwars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class StarWarsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(StarWarsApplication.class, args);
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
