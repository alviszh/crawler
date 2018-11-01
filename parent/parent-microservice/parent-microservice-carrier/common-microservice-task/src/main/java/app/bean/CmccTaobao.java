package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CmccTaobao {
	
	private String province;
	
	private String catName;
	
	private String telString;
	
	private String areaVid;
	
	private String ispVid;
	
	private String carrier;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getTelString() {
		return telString;
	}

	public void setTelString(String telString) {
		this.telString = telString;
	}

	public String getAreaVid() {
		return areaVid;
	}

	public void setAreaVid(String areaVid) {
		this.areaVid = areaVid;
	}

	public String getIspVid() {
		return ispVid;
	}

	public void setIspVid(String ispVid) {
		this.ispVid = ispVid;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	 
	

}
