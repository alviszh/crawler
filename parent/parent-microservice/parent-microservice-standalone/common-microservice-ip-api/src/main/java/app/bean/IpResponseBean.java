package app.bean;

import java.util.List;

public class IpResponseBean {
	
	public String msg;
	
	public Integer code;
	
	public Boolean success;
	
	public List<IPbean> data;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public List<IPbean> getData() {
		return data;
	}

	public void setData(List<IPbean> data) {
		this.data = data;
	}


}
