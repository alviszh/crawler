package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiIntergentResult;

public class TelecomQingHaiIntergentRootBean {

	private List<TelecomQingHaiIntergentResult> telecomQingHaiIntergentResultlist;

	public List<TelecomQingHaiIntergentResult> getIntergent() {
		return telecomQingHaiIntergentResultlist;
	}

	public void setIntergent(List<TelecomQingHaiIntergentResult> telecomQingHaiIntergentResultlist) {
		this.telecomQingHaiIntergentResultlist = telecomQingHaiIntergentResultlist;
	}

	@Override
	public String toString() {
		return "TelecomQingHaiIntergentRootBean [telecomQingHaiIntergentResultlist=" + telecomQingHaiIntergentResultlist
				+ "]";
	}
	
	

}
