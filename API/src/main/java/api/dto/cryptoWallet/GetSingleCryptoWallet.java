package api.dto.cryptoWallet;

public class GetSingleCryptoWallet {
	
	private int id;
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
