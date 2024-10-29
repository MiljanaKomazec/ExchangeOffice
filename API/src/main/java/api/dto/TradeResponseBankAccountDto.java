package api.dto;

public class TradeResponseBankAccountDto {

	private String message;
	
	private double amountEur;
	private double amountRsd;
	private double amountUsd;
	private double amountChf;
	private double amountGbp;
	
	
	public TradeResponseBankAccountDto() {
	
	}
	
	public TradeResponseBankAccountDto(String message, double amountEur, double amountRsd, double amountUsd, double amountChf,
			double amountGbp, double amountBTC, double amountLTC, double amountXMR) {
		super();
		this.message = message;
		this.amountEur = amountEur;
		this.amountRsd = amountRsd;
		this.amountUsd = amountUsd;
		this.amountChf = amountChf;
		this.amountGbp = amountGbp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
