package re.ethernitydev.mcquest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;  // ← import ajouté

@SpringBootApplication
@EnableScheduling
public class McquestApplication {

    public static void main(String[] args) {
        SpringApplication.run(McquestApplication.class, args);
    }

}
