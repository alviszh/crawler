package com.crawler.car.insurance.bean;

public enum CarInsuranceStatusCode {
	
	CAR_INSURANCE_VERIFY_DOING("信息验证中，请稍后...","VERIFY","DOING",100),
	CAR_INSURANCE_VERIFY_SUCCESS("信息验证成功.","VERIFY","SUCCESS",200),
	CAR_INSURANCE_VERIFY_ERROR("信息验证失败，请核实输入信息。","VERIFY","ERROR",400),
	CAR_INSURANCE_CRAWLER_DOING("数据开始采集，请稍后...","CRAWLER","DOING",100),
	CAR_INSURANCE_CRAWLER_SUCCESS("数据采集成功.","CRAWLER","SUCCESS",200),
	CAR_INSURANCE_SEND_CODE_DONING ("短信验证码发送中....","SEND_CODE", "DONING",0),
	CAR_INSURANCE_WAIT_CODE_SUCCESS ("短信验证码已发送，请注意查收","WAIT_CODE", "SUCCESS",0),
	
	//LZH PingAn
	CAR_INSURANCE_WAIT_CODE_ERROR ("短信验证码发送失败","WAIT_CODE", "ERROR",0),
	CAR_INSURANCE_WAIT_CODE_DONING("短信验证码验证中.....","WAIT_CODE", "DONING",2),
	CAR_INSURANCE_LOGIN_LOADING("正在认证，请耐心等待...","LOGIN", "DOING",1),
	CAR_INSURANCE_LOGIN_SUCCESS ("认证成功！","LOGIN","SUCCESS",0),
	CAR_INSURANCE_LOGIN_ERROR ("认证失败","LOGIN", "FAIL",2);
	private String description;
	
	private String phase;
	
	private String phasestatus;//步骤状态
	
	private Integer error_code;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getPhasestatus() {
		return phasestatus;
	}

	public void setPhasestatus(String phasestatus) {
		this.phasestatus = phasestatus;
	}

	public Integer getError_code() {
		return error_code;
	}

	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}

	private CarInsuranceStatusCode(String description,String phase, String phasestatus,Integer error_code) {
		this.description = description;
		this.phase = phase;
		this.phasestatus = phasestatus;
		this.error_code = error_code;
	}
	
}
