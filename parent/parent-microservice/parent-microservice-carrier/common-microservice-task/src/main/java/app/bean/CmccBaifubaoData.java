package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CmccBaifubaoData {
	
	private String operator;
	
	private String area;
	
	private String area_operator;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getArea_operator() {
		return area_operator;
	}

	public void setArea_operator(String area_operator) {
		this.area_operator = area_operator;
	} 
	
	
	

}
