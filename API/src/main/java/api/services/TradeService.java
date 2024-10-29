package api.services;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

public interface TradeService {
	@GetMapping("/trade-service")
	ResponseEntity<?> getConversion(@RequestParam String from, 
			@RequestParam String to, @RequestParam BigDecimal quantity,
			HttpServletRequest request);
}
