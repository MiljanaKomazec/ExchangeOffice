package apiGateway.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;

import api.dto.UserDto;


@Configuration
@EnableWebFluxSecurity
public class ApiGatewayAuthentication {
	
	@Bean
	SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeExchange(exchange -> exchange.pathMatchers(HttpMethod.POST).hasAnyRole("OWNER", "ADMIN")
				.pathMatchers("/currency-exchange").permitAll()
				.pathMatchers("/crypto-exchange").permitAll()
				.pathMatchers("/currency-conversion").hasRole("USER")
				.pathMatchers("/crypto-conversion").hasRole("USER")
				.pathMatchers("/trade-service").hasRole("USER")
				.pathMatchers("/users").hasRole("OWNER")
				.pathMatchers("/bank-account").hasRole("ADMIN")
				.pathMatchers("/bank-account/{id}").hasRole("ADMIN")
				.pathMatchers("/crypto-wallet").hasRole("ADMIN")
				.pathMatchers("/crypto-wallet/{id}").hasRole("ADMIN")
				.pathMatchers("/users/newAdmin").hasRole("OWNER")
				.pathMatchers("/users/updateAdmin").hasRole("OWNER")
				.pathMatchers("/users/newUser").hasAnyRole("OWNER", "ADMIN")
				.pathMatchers("/users/updateUser").hasAnyRole("OWNER", "ADMIN")
				.pathMatchers("/users/{id}").hasRole("OWNER")
				.pathMatchers("/crypto-wallet/my-wallet").hasRole("USER")
				.pathMatchers("/bank-account/my-account").hasRole("USER")
				).httpBasic(Customizer.withDefaults());
		return http.build();
	}
	
	@Bean
	MapReactiveUserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {
		ResponseEntity<List<UserDto>> response = 
				//Obratiti pažnju prilikom rada sa Dockerom(sad je promeenjeno)
				new RestTemplate().exchange("http://host.docker.internal:8770/users", HttpMethod.GET,
						null, new ParameterizedTypeReference<List<UserDto>>() {});
		List<UserDetails> users = new ArrayList<UserDetails>();
		for(UserDto user : response.getBody()) {
			users.add(
				User.withUsername(user.getEmail())
				.password(encoder.encode(user.getPassword()))
				.roles(user.getRole())
				.build());
		}
		return new MapReactiveUserDetailsService(users);
		
	}
	
	@Bean
	BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

}
