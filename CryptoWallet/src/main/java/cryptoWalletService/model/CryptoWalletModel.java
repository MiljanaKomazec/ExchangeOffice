package cryptoWalletService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "crypto_wallet")
public class CryptoWalletModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	private double amountBTC;
	private double amountLTC;
	private double amountXMR;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getAmountBTC() {
		return amountBTC;
	}
	public void setAmountBTC(double amountBTC) {
		this.amountBTC = amountBTC;
	}
	public double getAmountLTC() {
		return amountLTC;
	}
	public void setAmountLTC(double amountLTC) {
		this.amountLTC = amountLTC;
	}
	public double getAmountXMR() {
		return amountXMR;
	}
	public void setAmountXMR(double amountXMR) {
		this.amountXMR = amountXMR;
	}
	
	

}
