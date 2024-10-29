package apiGateway.routing;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfiguration {
	
	
	@Bean
	RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		return builder.routes()
			.route(p -> p.path("/currency-exchange").uri("lb://currency-exchange"))
			.route(p -> p.path("/crypto-exchange").uri("lb://crypto-exchange"))
			.route(p -> p.path("/currency-conversion").uri("lb://currency-conversion"))
			.route(p -> p.path("/crypto-conversion").uri("lb://crypto-conversion"))
			.route(p -> p.path("/trade-service").uri("lb://trade-service"))
			.route(p-> p.path("/users").uri("lb://users-service"))
			.route(p-> p.path("/bank-account").uri("lb://bank-account-service"))
			.route(p-> p.path("/bank-account/{id}").uri("lb://bank-account-service"))
			.route(p-> p.path("/crypto-wallet").uri("lb://crypto-wallet-service"))
			.route(p-> p.path("/crypto-wallet/{id}").uri("lb://crypto-wallet-service"))
			.route(p-> p.path("/users/newAdmin").uri("lb://users-service"))
			.route(p-> p.path("/users/newUser").uri("lb://users-service"))
			.route(p-> p.path("/users/updateUser").uri("lb://users-service"))
			.route(p-> p.path("/users/updateAdmin").uri("lb://users-service"))
			.route(p-> p.path("/users/{id}").uri("lb://users-service"))
			.route(p-> p.path("/crypto-wallet/my-wallet").uri("lb://crypto-wallet-service"))
			.route(p-> p.path("/bank-account/my-account").uri("lb://bank-account-service"))
			.build();
		
	}

}
