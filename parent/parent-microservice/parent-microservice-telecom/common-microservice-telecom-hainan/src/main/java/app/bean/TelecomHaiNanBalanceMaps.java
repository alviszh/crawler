package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanBalanceResult;

public class TelecomHaiNanBalanceMaps {
	private String zhouqi;

	private List<TelecomHaiNanBalanceResult> keyongs;

	private String searchtime;

	private String resultcode;

	public String getZhouqi() {
		return zhouqi;
	}

	public void setZhouqi(String zhouqi) {
		this.zhouqi = zhouqi;
	}

	public List<TelecomHaiNanBalanceResult> getKeyongs() {
		return keyongs;
	}

	public void setKeyongs(List<TelecomHaiNanBalanceResult> keyongs) {
		this.keyongs = keyongs;
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
		return "Maps [zhouqi=" + zhouqi + ", keyongs=" + keyongs + ", searchtime=" + searchtime + ", resultcode="
				+ resultcode + "]";
	}

	
}
