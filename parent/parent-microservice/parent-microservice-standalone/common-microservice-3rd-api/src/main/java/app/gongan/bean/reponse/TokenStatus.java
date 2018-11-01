package app.gongan.bean.reponse;

public class TokenStatus {
	
	//appkey
	private String client_id;
	
	//过期时间  单位秒
	private Integer exp;
	
	//过期时间  单位秒
	private String message;

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "TokenStatus [client_id=" + client_id + ", exp=" + exp + ", message=" + message + "]";
	}

	
	
	

}
