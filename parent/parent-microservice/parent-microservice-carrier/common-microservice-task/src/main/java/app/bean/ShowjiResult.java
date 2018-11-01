package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ShowjiResult {

	private String Mobile;

	private String QueryResult;

	private String TO;

	private String Corp;

	private String Province;

	private String City;

	private String AreaCode;

	private String PostCode;

	private String VNO;

	private String Card;

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getQueryResult() {
		return QueryResult;
	}

	public void setQueryResult(String queryResult) {
		QueryResult = queryResult;
	}

	public String getTO() {
		return TO;
	}

	public void setTO(String tO) {
		TO = tO;
	}

	public String getCorp() {
		return Corp;
	}

	public void setCorp(String corp) {
		Corp = corp;
	}

	public String getProvince() {
		return Province;
	}

	public void setProvince(String province) {
		Province = province;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getAreaCode() {
		return AreaCode;
	}

	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}

	public String getPostCode() {
		return PostCode;
	}

	public void setPostCode(String postCode) {
		PostCode = postCode;
	}

	public String getVNO() {
		return VNO;
	}

	public void setVNO(String vNO) {
		VNO = vNO;
	}

	public String getCard() {
		return Card;
	}

	public void setCard(String card) {
		Card = card;
	}

	@Override
	public String toString() {
		return "ShowjiResult [Mobile=" + Mobile + ", QueryResult=" + QueryResult + ", TO=" + TO + ", Corp=" + Corp
				+ ", Province=" + Province + ", City=" + City + ", AreaCode=" + AreaCode + ", PostCode=" + PostCode
				+ ", VNO=" + VNO + ", Card=" + Card + "]";
	}

	


	

}
