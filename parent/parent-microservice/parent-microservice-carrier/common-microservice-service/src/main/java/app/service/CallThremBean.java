package app.service;

import java.util.List;

import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingCallThremResult;

public class CallThremBean {
	
	private int pagenum; 

	private List<TelecomBeijingCallThremResult> result;

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public List<TelecomBeijingCallThremResult> getResult() {
		return result;
	}

	public void setResult(List<TelecomBeijingCallThremResult> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "CallThremBean [pagenum=" + pagenum + ", result=" + result + "]";
	}
	
}