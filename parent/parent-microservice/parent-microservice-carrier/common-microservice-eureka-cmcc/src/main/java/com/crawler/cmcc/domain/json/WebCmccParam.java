package com.crawler.cmcc.domain.json;

import java.util.List;

import com.crawler.cmcc.domain.json.LoginAuthJson;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.cmcc.CmccCheckMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccPayMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccSMSMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccUserCallResult;
import com.microservice.dao.entity.crawler.cmcc.CmccUserInfo;

public class WebCmccParam {
	
	public WebClient webClient;

	public LoginAuthJson loginAuthJson;
	
	public String code_second;
	
	public List<CmccUserCallResult> results;
	
	public List<CmccPayMsgResult> cmccPayMsgResults;
	

	public Integer code;
	
	public CmccUserInfo cmccUserInfo;
	
	public List<CmccSMSMsgResult> cmccSMSMsgResults;
	
	public List<CmccCheckMsgResult> cmccCheckMsgResults;
	
	public Integer pointValueCode;
	
	public Integer accountMsgStatus;
	
	public Integer busiPlanStatus;
	
	public Integer payMsgStatus;

	public Integer getPayMsgStatus() {
		return payMsgStatus;
	}

	public void setPayMsgStatus(Integer payMsgStatus) {
		this.payMsgStatus = payMsgStatus;
	}

	public List<CmccPayMsgResult> getCmccPayMsgResults() {
		return cmccPayMsgResults;
	}
	
	public void setCmccPayMsgResults(List<CmccPayMsgResult> cmccPayMsgResults) {
		this.cmccPayMsgResults = cmccPayMsgResults;
	}
	public Integer getBusiPlanStatus() {
		return busiPlanStatus;
	}

	public void setBusiPlanStatus(Integer busiPlanStatus) {
		this.busiPlanStatus = busiPlanStatus;
	}

	public Integer getAccountMsgStatus() {
		return accountMsgStatus;
	}

	public void setAccountMsgStatus(Integer accountMsgStatus) {
		this.accountMsgStatus = accountMsgStatus;
	}

	public Integer getPointValueCode() {
		return pointValueCode;
	}

	public void setPointValueCode(Integer pointValueCode) {
		this.pointValueCode = pointValueCode;
	}

	public List<CmccCheckMsgResult> getCmccCheckMsgResults() {
		return cmccCheckMsgResults;
	}

	public void setCmccCheckMsgResults(List<CmccCheckMsgResult> cmccCheckMsgResults) {
		this.cmccCheckMsgResults = cmccCheckMsgResults;
	}

	public List<CmccSMSMsgResult> getCmccSMSMsgResults() {
		return cmccSMSMsgResults;
	}

	public void setCmccSMSMsgResults(List<CmccSMSMsgResult> cmccSMSMsgResults) {
		this.cmccSMSMsgResults = cmccSMSMsgResults;
	}

	public CmccUserInfo getCmccUserInfo() {
		return cmccUserInfo;
	}

	public void setCmccUserInfo(CmccUserInfo cmccUserInfo) {
		this.cmccUserInfo = cmccUserInfo;
	}

	public List<CmccUserCallResult> getResults() {
		return results;
	}

	public void setResults(List<CmccUserCallResult> results) {
		this.results = results;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getCode_second() {
		return code_second;
	}
	
	public void setCode_second(String code_second) {
		this.code_second = code_second;
	}
	
	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public LoginAuthJson getLoginAuthJson() {
		return loginAuthJson;
	}

	public void setLoginAuthJson(LoginAuthJson loginAuthJson) {
		this.loginAuthJson = loginAuthJson;
	}

}
