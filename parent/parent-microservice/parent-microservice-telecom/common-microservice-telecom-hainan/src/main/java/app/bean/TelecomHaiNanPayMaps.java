package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanPayResult;

public class TelecomHaiNanPayMaps {
	private String zhouqi;

	private String message;

	private List<TelecomHaiNanPayResult> chongzis;

	private String searchtime;

	private String resultcode;

	public String getZhouqi() {
		return zhouqi;
	}

	public void setZhouqi(String zhouqi) {
		this.zhouqi = zhouqi;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<TelecomHaiNanPayResult> getChongzis() {
		return chongzis;
	}

	public void setChongzis(List<TelecomHaiNanPayResult> chongzis) {
		this.chongzis = chongzis;
	}

	public String getSearchtime() {
		return searchtime;
	}

	public void setSearchtime(String searchtime) {
		this.searchtime = searchtime;
	}

	public String getResultcode() {
		return resultcode;
	}

	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}

	@Override
	public String toString() {
		return "Maps [zhouqi=" + zhouqi + ", message=" + message + ", chongzis=" + chongzis + ", searchtime="
				+ searchtime + ", resultcode=" + resultcode + "]";
	}

	
}
