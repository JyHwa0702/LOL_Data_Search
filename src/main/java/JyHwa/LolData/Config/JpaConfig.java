package JyHwa.LolData.Config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "JyHwa.LolData.Repository")
@EntityScan(basePackages = "JyHwa.LolData.Entity")
public class JpaConfig {
}
