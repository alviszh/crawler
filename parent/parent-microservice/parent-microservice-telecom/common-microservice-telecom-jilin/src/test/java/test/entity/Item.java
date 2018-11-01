package test.entity;

public class Item {

	private String bonusInfo ;
	private String cardNo ;
	private String rechargeAmount ;
	private String rechargeFlag;
	private String rechargeFlowAmount;
	private String rechargeTime;
	private String rechargeUnit;
	private String rechargeUser;
	private String rechargeUserType;
	private String result;
	public String getBonusInfo() {
		return bonusInfo;
	}
	public void setBonusInfo(String bonusInfo) {
		this.bonusInfo = bonusInfo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getRechargeAmount() {
		return rechargeAmount;
	}
	public void setRechargeAmount(String rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
	public String getRechargeFlag() {
		return rechargeFlag;
	}
	public void setRechargeFlag(String rechargeFlag) {
		this.rechargeFlag = rechargeFlag;
	}
	public String getRechargeFlowAmount() {
		return rechargeFlowAmount;
	}
	public void setRechargeFlowAmount(String rechargeFlowAmount) {
		this.rechargeFlowAmount = rechargeFlowAmount;
	}
	public String getRechargeTime() {
		return rechargeTime;
	}
	public void setRechargeTime(String rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
	public String getRechargeUnit() {
		return rechargeUnit;
	}
	public void setRechargeUnit(String rechargeUnit) {
		this.rechargeUnit = rechargeUnit;
	}
	public String getRechargeUser() {
		return rechargeUser;
	}
	public void setRechargeUser(String rechargeUser) {
		this.rechargeUser = rechargeUser;
	}
	public String getRechargeUserType() {
		return rechargeUserType;
	}
	public void setRechargeUserType(String rechargeUserType) {
		this.rechargeUserType = rechargeUserType;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "Item [bonusInfo=" + bonusInfo + ", cardNo=" + cardNo + ", rechargeAmount=" + rechargeAmount
				+ ", rechargeFlag=" + rechargeFlag + ", rechargeFlowAmount=" + rechargeFlowAmount + ", rechargeTime="
				+ rechargeTime + ", rechargeUnit=" + rechargeUnit + ", rechargeUser=" + rechargeUser
				+ ", rechargeUserType=" + rechargeUserType + ", result=" + result + "]";
	}
	 
	
}
