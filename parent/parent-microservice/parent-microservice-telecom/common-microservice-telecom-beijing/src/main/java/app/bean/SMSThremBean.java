package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingSMSThremResult;

public class SMSThremBean {

	
	private List<TelecomBeijingSMSThremResult> result;
	
	private int pagenum;

	public List<TelecomBeijingSMSThremResult> getResult() {
		return result;
	}

	public void setResult(List<TelecomBeijingSMSThremResult> result) {
		this.result = result;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	@Override
	public String toString() {
		return "SMSThremBean [result=" + result + ", pagenum=" + pagenum + "]";
	}


	
	
}
