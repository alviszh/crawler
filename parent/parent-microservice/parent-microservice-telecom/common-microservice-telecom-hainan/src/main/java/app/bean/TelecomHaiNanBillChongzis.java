package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanBillResult;

public class TelecomHaiNanBillChongzis {
	private String charge;

	private List<TelecomHaiNanBillResult> details;

	private String showlevel;

	private String chargeName;

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getCharge() {
		return this.charge;
	}

	public void setDetails(List<TelecomHaiNanBillResult> details) {
		this.details = details;
	}

	public List<TelecomHaiNanBillResult> getDetails() {
		return this.details;
	}

	public void setShowlevel(String showlevel) {
		this.showlevel = showlevel;
	}

	public String getShowlevel() {
		return this.showlevel;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getChargeName() {
		return this.chargeName;
	}

	@Override
	public String toString() {
		return "TelecomHaiNanBillChongzis [charge=" + charge + ", details=" + details + ", showlevel=" + showlevel
				+ ", chargeName=" + chargeName + "]";
	}
	
}
