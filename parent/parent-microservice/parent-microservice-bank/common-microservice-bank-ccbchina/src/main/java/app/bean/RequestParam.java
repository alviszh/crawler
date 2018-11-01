package app.bean;

public class RequestParam {
	
	@Override
	public String toString() {
		return "RequestParam [skey=" + skey + ", branchid=" + branchid + "]";
	}
	public String skey;
	public String branchid;	
	public String userid;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSkey() {
		return skey;
	}
	public void setSkey(String skey) {
		this.skey = skey;
	}
	public String getBranchid() {
		return branchid;
	}
	public void setBranchid(String branchid) {
		this.branchid = branchid;
	}

}
