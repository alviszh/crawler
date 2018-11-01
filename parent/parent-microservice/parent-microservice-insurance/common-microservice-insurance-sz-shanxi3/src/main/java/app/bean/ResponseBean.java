package app.bean;

import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

public class ResponseBean {
	
	public WebClient webClient;
	public TaskInsurance taskInsurance;
	public Integer code;
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public TaskInsurance getTaskInsurance() {
		return taskInsurance;
	}
	public void setTaskInsurance(TaskInsurance taskInsurance) {
		this.taskInsurance = taskInsurance;
	}

}
