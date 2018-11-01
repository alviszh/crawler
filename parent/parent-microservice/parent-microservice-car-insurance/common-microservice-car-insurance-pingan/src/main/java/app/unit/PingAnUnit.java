package app.unit;

import com.gargoylesoftware.htmlunit.Page;

public class PingAnUnit {

	public Page page;
	
	public String state;
	
	private String verifyCode;
	
	

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "PingAnUnit [page=" + page + ", state=" + state + ", verifyCode=" + verifyCode + "]";
	}

	
	
}
