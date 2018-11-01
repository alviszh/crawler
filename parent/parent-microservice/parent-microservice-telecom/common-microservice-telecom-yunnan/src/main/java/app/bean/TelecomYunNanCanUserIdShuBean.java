package app.bean;

public class TelecomYunNanCanUserIdShuBean {

	private String areacode;
	private String weizhi ;
	private String weizhi1 ;
	private String weizhi2 ;
	private String userid ;
	
	public String getWeizhi1() {
		return weizhi1;
	}
	public void setWeizhi1(String weizhi1) {
		this.weizhi1 = weizhi1;
	}
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getWeizhi() {
		return weizhi;
	}
	public void setWeizhi(String weizhi) {
		this.weizhi = weizhi;
	}
	public String getWeizhi2() {
		return weizhi2;
	}
	public void setWeizhi2(String weizhi2) {
		this.weizhi2 = weizhi2;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Override
	public String toString() {
		return "TelecomYunNanCanShuBean [areacode=" + areacode + ", weizhi=" + weizhi + ", weizhi1=" + weizhi1
				+ ", weizhi2=" + weizhi2 + ", userid=" + userid + "]";
	}
	
	
}
