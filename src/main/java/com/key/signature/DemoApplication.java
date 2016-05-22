package com.key.signature;

import com.key.signature.domain.User;
import com.key.signature.domain.UserExeption;
import com.key.signature.domain.UserRepository;
import com.key.signature.domain.UserValidator;
import com.key.signature.filter.KeyHandWritingFilter;
import com.key.signature.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@SpringBootApplication
public class DemoApplication {


	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RequestMapping(value = "api/logIn",method =RequestMethod.POST)
	public Map<String,Boolean> user(@RequestBody User u) {
		User u1 = userRepository.findByUserName(u.getUserName());
		if(u1==null)  throw new UserExeption("Incorrect user");

		List<Double> res1 = Arrays
				.asList(u1.getKeyHandWriting().split(","))
				.stream()
				.mapToDouble(Double::valueOf).boxed().collect(Collectors.toList());
		System.out.println(res1);
		List<Double> res2 = Arrays
				.asList(u.getKeyHandWriting().split(","))
				.stream()
				.mapToDouble(Double::valueOf).boxed().collect(Collectors.toList());
		System.out.println(res2);
		double S1 =  MathUtil.sampleVariance(MathUtil.filterError(res1));
		double S2 =  MathUtil.sampleVariance(MathUtil.filterError(res2));
		System.out.println(S1);
		System.out.println(S2);

		System.out.println(Math.max(S1,S2)/Math.min(S1,S2));
		System.out.println(MathUtil.getTheoreticalFisher(MathUtil.filterError(res1).size()));
		if(Math.max(S1,S2)/Math.min(S1,S2) >  MathUtil.getTheoreticalFisher(MathUtil.filterError(res1).size()))  throw  new UserExeption("Bad credential");

		return Collections.singletonMap("authenticated",true);
	}

	@RequestMapping(value = "api/logOut",method =RequestMethod.POST)
	public Map<String,Boolean> user() {
		return Collections.singletonMap("authenticated",false);
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



	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		source.registerCorsConfiguration("/**", config);
		final FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	@Bean
	public FilterRegistrationBean keyValidationFilter(UserRepository userRepository){
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
		KeyHandWritingFilter filter = new KeyHandWritingFilter(userRepository);
		filterRegBean.setFilter(filter);
		List<String> urlPatterns = new ArrayList<>();
		urlPatterns.add("/login");
		filterRegBean.setUrlPatterns(urlPatterns);
		return filterRegBean;
	}

	@RequestMapping("/principal")
	public Principal principal(Principal user) {
		return user;
	}


	@Configuration
	public static class CustomRepositoryRestConfigurer extends RepositoryRestConfigurerAdapter {

		@Override
		public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
			validatingListener.addValidator("beforeCreate", new UserValidator());
		}

	}

}
