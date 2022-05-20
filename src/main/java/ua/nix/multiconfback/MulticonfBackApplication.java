package ua.nix.multiconfback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
public class MulticonfBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MulticonfBackApplication.class, args);
    }

}
