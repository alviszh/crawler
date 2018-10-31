/**
 * 
 */
package com.crawler.monitor.json.tasker;

import com.crawler.insurance.json.TaskInsurance;

public class MonitorInsuranceTaskerBean {
	private TaskInsurance taskInsurance;
	private int recordCount;   //爬取的记录总数（流水信息）、
	public TaskInsurance getTaskInsurance() {
		return taskInsurance;
	}
	public void setTaskInsurance(TaskInsurance taskInsurance) {
		this.taskInsurance = taskInsurance;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public MonitorInsuranceTaskerBean() {
		super();
	}
	public MonitorInsuranceTaskerBean(TaskInsurance taskInsurance, int recordCount) {
		super();
		this.taskInsurance = taskInsurance;
		this.recordCount = recordCount;
	}
}
