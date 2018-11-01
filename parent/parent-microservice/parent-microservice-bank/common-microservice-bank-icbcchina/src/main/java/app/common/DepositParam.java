package app.common;

public class DepositParam {

	private String cardnum;
	private String acctcode;
	private String acctnum;
	private String accttype;
	private String name;
	private String methodSelList;
	private String agrFlag_Acct;
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	public String getAcctcode() {
		return acctcode;
	}
	public void setAcctcode(String acctcode) {
		this.acctcode = acctcode;
	}
	public String getAcctnum() {
		return acctnum;
	}
	public void setAcctnum(String acctnum) {
		this.acctnum = acctnum;
	}
	public String getAccttype() {
		return accttype;
	}
	public void setAccttype(String accttype) {
		this.accttype = accttype;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMethodSelList() {
		return methodSelList;
	}
	public void setMethodSelList(String methodSelList) {
		this.methodSelList = methodSelList;
	}
	public String getAgrFlag_Acct() {
		return agrFlag_Acct;
	}
	public void setAgrFlag_Acct(String agrFlag_Acct) {
		this.agrFlag_Acct = agrFlag_Acct;
	}
	@Override
	public String toString() {
		return "DepositParam [cardnum=" + cardnum + ", acctcode=" + acctcode + ", acctnum=" + acctnum + ", accttype="
				+ accttype + ", name=" + name + ", methodSelList=" + methodSelList + ", agrFlag_Acct=" + agrFlag_Acct
				+ "]";
	}
	
}
