package bankAccountService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bank_account")
public class BankAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	private double amountEur;
	private double amountRsd;
	private double amountUsd;
	private double amountChf;
	private double amountGbp;
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
	public double getAmountEur() {
		return amountEur;
	}
	public void setAmountEur(double amountEur) {
		this.amountEur = amountEur;
	}
	public double getAmountRsd() {
		return amountRsd;
	}
	public void setAmountRsd(double amountRsd) {
		this.amountRsd = amountRsd;
	}
	public double getAmountUsd() {
		return amountUsd;
	}
	public void setAmountUsd(double amountUsd) {
		this.amountUsd = amountUsd;
	}
	public double getAmountChf() {
		return amountChf;
	}
	public void setAmountChf(double amountChf) {
		this.amountChf = amountChf;
	}
	public double getAmountGbp() {
		return amountGbp;
	}
	public void setAmountGbp(double amountGbp) {
		this.amountGbp = amountGbp;
	}

	
}
