package currencyExchange.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currency_exchange")
public class CurrencyExchangeModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int id;
	@Column(name = "exchange_from", length = 3)
	private String from;
	@Column(name = "exchange_to", length = 3)
	private String to;
	@Column(name = "exchange_value", precision = 10, scale = 5)
	private BigDecimal exchangeValue;
	
	
	public CurrencyExchangeModel() {
		super();
		// TODO Auto-generated constructor stub
	}


	public CurrencyExchangeModel(int id, String from, String to, BigDecimal exchangeValue) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.exchangeValue = exchangeValue;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String getTo() {
		return to;
	}


	public void setTo(String to) {
		this.to = to;
	}


	public BigDecimal getExchangeValue() {
		return exchangeValue;
	}


	public void setExchangeValue(BigDecimal exchangeValue) {
		this.exchangeValue = exchangeValue;
	}
	
	

}
