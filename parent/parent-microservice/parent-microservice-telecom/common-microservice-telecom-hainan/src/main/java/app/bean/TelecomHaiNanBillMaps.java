package app.bean;

import java.util.List;

public class TelecomHaiNanBillMaps {
	private String zhouqi;

	private List<TelecomHaiNanBillChongzis> chongzis;

	private String searchtime;

	private String current;

	private String resultcode;

	public void setZhouqi(String zhouqi) {
		this.zhouqi = zhouqi;
	}

	public String getZhouqi() {
		return this.zhouqi;
	}

	public void setChongzis(List<TelecomHaiNanBillChongzis> telecomHaiNanBillChongzis) {
		this.chongzis = telecomHaiNanBillChongzis;
	}

	public List<TelecomHaiNanBillChongzis> getChongzis() {
		return this.chongzis;
	}

	public void setSearchtime(String searchtime) {
		this.searchtime = searchtime;
	}

	public String getSearchtime() {
		return this.searchtime;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getCurrent() {
		return this.current;
	}

	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}

	public String getResultcode() {
		return this.resultcode;
	}
}
