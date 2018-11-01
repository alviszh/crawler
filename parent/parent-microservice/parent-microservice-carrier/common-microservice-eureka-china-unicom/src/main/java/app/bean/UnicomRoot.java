package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomPayMsgStatusResult;
import com.microservice.dao.entity.crawler.unicom.UnicomUserInfo;

public class UnicomRoot {
	
    private UnicomErrorMessage errorMessage;
	
	public UnicomErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(UnicomErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	private String alltotalfee;

	private String querynowdate;

	private String callTotaltime;

	private String queryDateScope;

	private UnicomPageMap<?> pageMap;

	private boolean isSuccess;

	private UnicomUserInfo userInfo;
	
    private List<UnicomPayMsgStatusResult> totalResult;

	private int totalRecord;

	private String msg;

	private boolean isFixNum;

	private boolean timeresult;
	
	public List<UnicomPayMsgStatusResult> getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(List<UnicomPayMsgStatusResult> totalResult) {
		this.totalResult = totalResult;
	}

	public void setAlltotalfee(String alltotalfee) {
		this.alltotalfee = alltotalfee;
	}

	public String getAlltotalfee() {
		return this.alltotalfee;
	}

	public void setQuerynowdate(String querynowdate) {
		this.querynowdate = querynowdate;
	}

	public String getQuerynowdate() {
		return this.querynowdate;
	}

	public void setCallTotaltime(String callTotaltime) {
		this.callTotaltime = callTotaltime;
	}

	public String getCallTotaltime() {
		return this.callTotaltime;
	}

	public void setQueryDateScope(String queryDateScope) {
		this.queryDateScope = queryDateScope;
	}

	public String getQueryDateScope() {
		return this.queryDateScope;
	}

	public void setPageMap(UnicomPageMap<?> pageMap) {
		this.pageMap = pageMap;
	}

	public UnicomPageMap<?> getPageMap() {
		return this.pageMap;
	}

	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public boolean getIsSuccess() {
		return this.isSuccess;
	}

	public void setUserInfo(UnicomUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public UnicomUserInfo getUserInfo() {
		return this.userInfo;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getTotalRecord() {
		return this.totalRecord;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setIsFixNum(boolean isFixNum) {
		this.isFixNum = isFixNum;
	}

	public boolean getIsFixNum() {
		return this.isFixNum;
	}

	public void setTimeresult(boolean timeresult) {
		this.timeresult = timeresult;
	}

	public boolean getTimeresult() {
		return this.timeresult;
	}

	@Override
	public String toString() {
		return "UnicomRoot [errorMessage=" + errorMessage + ", alltotalfee=" + alltotalfee + ", querynowdate="
				+ querynowdate + ", callTotaltime=" + callTotaltime + ", queryDateScope=" + queryDateScope
				+ ", pageMap=" + pageMap + ", isSuccess=" + isSuccess + ", userInfo=" + userInfo + ", totalRecord="
				+ totalRecord + ", msg=" + msg + ", isFixNum=" + isFixNum + ", timeresult=" + timeresult + "]";
	}

	
}
