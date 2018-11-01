package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingCallThremResult;



public class CallThremBean {

	
	private List<TelecomBeijingCallThremResult> result;
	
	private int pagenum;

	public List<TelecomBeijingCallThremResult> getResult() {
		return result;
	}

	public void setResult(List<TelecomBeijingCallThremResult> result) {
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
		return "CallThremBean [result=" + result + ", pagenum=" + pagenum + "]";
	}

	
	
}
