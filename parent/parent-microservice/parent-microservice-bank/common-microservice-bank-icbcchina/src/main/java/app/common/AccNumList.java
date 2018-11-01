package app.common;

public class AccNumList {

	private String cardNum;
	
	private String acctNo0;
	
	private String skFlag;
	
	private String cardType;
	
	private String acctCode;
	
	private String areaCode;
	
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getAcctNo0() {
		return acctNo0;
	}

	public void setAcctNo0(String acctNo0) {
		this.acctNo0 = acctNo0;
	}

	public String getSkFlag() {
		return skFlag;
	}

	public void setSkFlag(String skFlag) {
		this.skFlag = skFlag;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getAcctCode() {
		return acctCode;
	}

	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Override
	public String toString() {
		return "AccNumList [cardNum=" + cardNum + ", acctNo0=" + acctNo0 + ", skFlag=" + skFlag + ", cardType="
				+ cardType + ", acctCode=" + acctCode + ", areaCode=" + areaCode + "]";
	}

	
	
}
