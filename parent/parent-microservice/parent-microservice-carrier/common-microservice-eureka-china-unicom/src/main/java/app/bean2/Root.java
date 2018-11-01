package app.bean2;

import java.util.List;

public class Root {
	private PaymentRecordResult PaymentRecordResult;

	private List<TotalResult> totalResult;

	private String bizCode;

	private PageMap pageMap;

	private String transid;

	private UserInfo userInfo;

	public void setPaymentRecordResult(PaymentRecordResult PaymentRecordResult) {
		this.PaymentRecordResult = PaymentRecordResult;
	}

	public PaymentRecordResult getPaymentRecordResult() {
		return this.PaymentRecordResult;
	}

	public void setTotalResult(List<TotalResult> totalResult) {
		this.totalResult = totalResult;
	}

	public List<TotalResult> getTotalResult() {
		return this.totalResult;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getBizCode() {
		return this.bizCode;
	}

	public void setPageMap(PageMap pageMap) {
		this.pageMap = pageMap;
	}

	public PageMap getPageMap() {
		return this.pageMap;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public String getTransid() {
		return this.transid;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public UserInfo getUserInfo() {
		return this.userInfo;
	}

}