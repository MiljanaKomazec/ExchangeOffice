package cryptoWalletService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"util.exceptions", "cryptoWalletService"})
public class CryptoWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoWalletApplication.class, args);
	}

}
