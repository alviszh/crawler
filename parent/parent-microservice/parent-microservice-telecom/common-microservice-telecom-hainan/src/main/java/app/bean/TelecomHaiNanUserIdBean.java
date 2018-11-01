package app.bean;

public class TelecomHaiNanUserIdBean {

	private String phonetype;
	private String phone ;
	private String weizhiphonetype ;//50 代表手机
	private String weizhi2 ;//3238789407
	private String weizhi3 ;//无
	private String canshu ;//aWTUafTzjWLbzgHrGZxTLQ==
	
	private String userid;
	
	private String html;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getPhonetype() {
		return phonetype;
	}

	public void setPhonetype(String phonetype) {
		this.phonetype = phonetype;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWeizhiphonetype() {
		return weizhiphonetype;
	}

	public void setWeizhiphonetype(String weizhiphonetype) {
		this.weizhiphonetype = weizhiphonetype;
	}

	public String getWeizhi2() {
		return weizhi2;
	}

	public void setWeizhi2(String weizhi2) {
		this.weizhi2 = weizhi2;
	}

	public String getWeizhi3() {
		return weizhi3;
	}

	public void setWeizhi3(String weizhi3) {
		this.weizhi3 = weizhi3;
	}

	public String getCanshu() {
		return canshu;
	}

	public void setCanshu(String canshu) {
		this.canshu = canshu;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "TelecomHaiNanUserIdBean [phonetype=" + phonetype + ", phone=" + phone + ", weizhiphonetype="
				+ weizhiphonetype + ", weizhi2=" + weizhi2 + ", weizhi3=" + weizhi3 + ", canshu=" + canshu + ", userid="
				+ userid + "]";
	}

	
	
	
}
