package br.com.api.streaming;

import br.com.api.streaming.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamingApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(StreamingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}
