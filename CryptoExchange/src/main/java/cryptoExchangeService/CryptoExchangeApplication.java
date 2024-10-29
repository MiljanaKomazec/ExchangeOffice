package cryptoExchangeService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"util.exceptions", "cryptoExchangeService"})
public class CryptoExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoExchangeApplication.class, args);
	}

}
