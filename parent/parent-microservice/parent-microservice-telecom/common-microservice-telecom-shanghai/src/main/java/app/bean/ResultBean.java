package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiCallRec;

public class ResultBean {
	
	public List<TelecomShanghaiCallRec> pagedResult;
	public String devNo;
	public boolean result;
	
	public List<TelecomShanghaiCallRec> getPagedResult() {
		return pagedResult;
	}
	public void setPagedResult(List<TelecomShanghaiCallRec> pagedResult) {
		this.pagedResult = pagedResult;
	}
	public String getDevNo() {
		return devNo;
	}
	public void setDevNo(String devNo) {
		this.devNo = devNo;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}

}
