package hansung.cse.withSpace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WithSpaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithSpaceApplication.class, args);
	}

}
