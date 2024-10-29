package api.dto;

public class CryptoConversionResponseDto {

	private String message;
	private double amountBTC;
	private double amountLTC;
	private double amountXMR;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
