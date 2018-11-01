package app;

import java.util.List;

import com.microservice.dao.entity.crawler.housing.handan.HousingHandanPay;

public class PayBean {
	
	public List<HousingHandanPay> result;
	public String recode;
	public String totalnum;
	
	public List<HousingHandanPay> getResult() {
		return result;
	}
	public void setResult(List<HousingHandanPay> result) {
		this.result = result;
	}
	public String getRecode() {
		return recode;
	}
	public void setRecode(String recode) {
		this.recode = recode;
	}
	public String getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(String totalnum) {
		this.totalnum = totalnum;
	}
	

}
