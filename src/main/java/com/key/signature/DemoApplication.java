package com.key.signature;

import com.key.signature.domain.User;
import com.key.signature.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RequestMapping("/principal")
	public Principal user(Principal user) {
		return user;
	}

	@Bean
	public CommandLineRunner demo(UserRepository repository) {
		return (args) -> {
			User u1 = new User("Pavel","123321");
			u1.setActivated(true);
			u1.setRole(User.Role.ADMIN);
			User u2 = new User("Vova","123");
			User u3 = new User("Alex","123");

			u1=repository.save(u1);
			u2=repository.save(u2);
			u3=repository.save(u3);

			System.out.println(u1);
			System.out.println(u2);
			System.out.println(u3);
		};
	}

}
