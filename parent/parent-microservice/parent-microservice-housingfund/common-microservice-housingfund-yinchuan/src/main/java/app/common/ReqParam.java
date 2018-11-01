package app.common;

public class ReqParam {

	public String companyNum;
	public String companyName;
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@Override
	public String toString() {
		return "reqParam [companyNum=" + companyNum + ", companyName=" + companyName + "]";
	}
	
}
