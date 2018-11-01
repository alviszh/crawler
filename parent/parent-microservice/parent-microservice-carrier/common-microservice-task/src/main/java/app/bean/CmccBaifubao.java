package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CmccBaifubao {

	private CmccBaifubaoData data;

	public CmccBaifubaoData getData() {
		return data;
	}

	public void setData(CmccBaifubaoData data) {
		this.data = data;
	}
	
	
	
}
