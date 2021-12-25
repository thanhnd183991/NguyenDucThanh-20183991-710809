package entity.payment;

import java.sql.Timestamp;

/**
 * nguyen duc thanh
 * @author ADMIN
 *
 */
public class DomesticCreditCard extends PaymentCard{	
	protected String owner;
	protected String dateExpired;
	private String issuingBank;
	private String cardNumber;
	
	public DomesticCreditCard(String issuingBank, String owner, String cardNumber, String dateExpired) {
		this.owner = owner;
		this.dateExpired = dateExpired;
		this.issuingBank = issuingBank;
		this.cardNumber = cardNumber;
	}

	public String getIssuingBank() {
		return issuingBank;
	}

	public void setIssuingBank(String issuingBank) {
		this.issuingBank = issuingBank;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	
}
