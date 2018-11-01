package app.bean;

import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonPointsAndCharges;

public class TelecomCommonPointsAndChargesRootBean {

	private String code;
	private String status;
	private String msg;
	private TelecomCommonPointsAndCharges obj;
	private String url;

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	
	public TelecomCommonPointsAndCharges getObj() {
		return obj;
	}

	public void setObj(TelecomCommonPointsAndCharges obj) {
		this.obj = obj;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}