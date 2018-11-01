package app.gongan.bean.request;

import java.io.Serializable;

public class TokenStatusRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -560822934924245672L;

	private String token; 
	
	private String appKey;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Override
	public String toString() {
		return "TokenStatusRequest [token=" + token + ", appKey=" + appKey + "]";
	}
	
	
	

}
