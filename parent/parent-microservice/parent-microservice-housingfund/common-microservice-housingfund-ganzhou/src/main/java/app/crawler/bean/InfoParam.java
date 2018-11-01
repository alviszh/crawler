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
	
	private String cxyd;
	// 当前状态
	private String zgzt;
	
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

	public String getCxyd() {
		return cxyd;
	}
	public void setCxyd(String cxyd) {
		this.cxyd = cxyd;
	}
	public String getZgzt() {
		return zgzt;
	}

	public InfoParam() {
		super();
		// TODO Auto-generated constructor stub
	}


	public void setZgzt(String zgzt) {
		this.zgzt = zgzt;
	}
	public InfoParam(String zgzh, String sfzh, String zgxm, String dwbm,  String zgzt) {
		super();
		this.zgzh = zgzh;
		this.sfzh = sfzh;
		this.zgxm = zgxm;
		this.dwbm = dwbm;
		this.zgzt = zgzt;
	}

	public InfoParam(String zgzh, String sfzh, String zgxm, String dwbm, String cxyd, String zgzt) {
		super();
		this.zgzh = zgzh;
		this.sfzh = sfzh;
		this.zgxm = zgxm;
		this.dwbm = dwbm;
		this.cxyd = cxyd;
		this.zgzt = zgzt;
	}
    
}
