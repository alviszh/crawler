package app.bean;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;

/**
 * @description:运营商定时任务bean
 * @author: sln 
 */
public class MonitorCarrierTempBean {
	private TaskMobile taskMobile;
	private int taskCallCount;   //运营商中爬取的通话记录总数
	private int etlTreatCallCount;  //etl处理的通话记录总数
	private String developer;    //网站负责人
	private String isNeedSms;    //是否需要短信验证码
	private int proTreatCallCount;  //产品化处理的通话记录总数
	public TaskMobile getTaskMobile() {
		return taskMobile;
	}
	public void setTaskMobile(TaskMobile taskMobile) {
		this.taskMobile = taskMobile;
	}
	public int getTaskCallCount() {
		return taskCallCount;
	}
	public void setTaskCallCount(int taskCallCount) {
		this.taskCallCount = taskCallCount;
	}
	public int getEtlTreatCallCount() {
		return etlTreatCallCount;
	}
	public void setEtlTreatCallCount(int etlTreatCallCount) {
		this.etlTreatCallCount = etlTreatCallCount;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public MonitorCarrierTempBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getIsNeedSms() {
		return isNeedSms;
	}
	public void setIsNeedSms(String isNeedSms) {
		this.isNeedSms = isNeedSms;
	}
	
	public int getProTreatCallCount() {
		return proTreatCallCount;
	}
	public void setProTreatCallCount(int proTreatCallCount) {
		this.proTreatCallCount = proTreatCallCount;
	}
	public MonitorCarrierTempBean(TaskMobile taskMobile, int taskCallCount, int etlTreatCallCount, String developer,
			String isNeedSms, int proTreatCallCount) {
		super();
		this.taskMobile = taskMobile;
		this.taskCallCount = taskCallCount;
		this.etlTreatCallCount = etlTreatCallCount;
		this.developer = developer;
		this.isNeedSms = isNeedSms;
		this.proTreatCallCount = proTreatCallCount;
	}
	
}
