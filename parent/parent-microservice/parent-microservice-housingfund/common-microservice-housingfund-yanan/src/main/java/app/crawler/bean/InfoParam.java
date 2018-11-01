package app.crawler.bean;

public class InfoParam {
	// 职工账号
	private String zgzh;
	// 身份证号
	private String sfzh;
	// 职工姓名
	private String zgxm;
	// 单位编码
	private String dwbm;
	
	public String getZgzh() {
		return zgzh;
	}


	public void setZgzh(String zgzh) {
		this.zgzh = zgzh;
	}


	public String getSfzh() {
		return sfzh;
	}


	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}


	public String getZgxm() {
		return zgxm;
	}


	public void setZgxm(String zgxm) {
		this.zgxm = zgxm;
	}


	public String getDwbm() {
		return dwbm;
	}

	public void setDwbm(String dwbm) {
		this.dwbm = dwbm;
	}
	
	public InfoParam() {
		super();
		// TODO Auto-generated constructor stub
	}


	public InfoParam(String zgzh, String sfzh, String zgxm, String dwbm) {
		super();
		this.zgzh = zgzh;
		this.sfzh = sfzh;
		this.zgxm = zgxm;
		this.dwbm = dwbm;
	}
	@Override
	public String toString() {
		return "InfoParam [zgzh=" + zgzh + ", sfzh=" + sfzh + ", zgxm=" + zgxm + ", dwbm=" + dwbm + "]";
	}
	
}
