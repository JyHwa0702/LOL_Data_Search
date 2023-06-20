package JyHwa.LolData;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableJpaRepositories(basePackages = "JyHwa.LolData.Repository")
public class LolDataApplication {
//	dd

	public static void main(String[] args) {
		SpringApplication.run(LolDataApplication.class, args);
	}

}
