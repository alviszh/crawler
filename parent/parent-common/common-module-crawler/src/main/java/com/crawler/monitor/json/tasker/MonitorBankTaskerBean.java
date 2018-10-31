/**
 * 
 */
package com.crawler.monitor.json.tasker;

import com.crawler.bank.json.TaskBank;

public class MonitorBankTaskerBean {
	private TaskBank taskBank;
	private int recordCount;   //爬取的记录总数（流水信息）、
	public TaskBank getTaskBank() {
		return taskBank;
	}
	public void setTaskBank(TaskBank taskBank) {
		this.taskBank = taskBank;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public MonitorBankTaskerBean() {
		super();
	}
	public MonitorBankTaskerBean(TaskBank taskBank, int recordCount) {
		super();
		this.taskBank = taskBank;
		this.recordCount = recordCount;
	}
}
