/**
 * 
 */
package com.crawler.monitor.json.tasker;

import com.crawler.housingfund.json.TaskHousingfund;

/**
 * @author sln
 * @date 2018年9月12日下午6:42:42
 * @Description: 
 */
public class MonitorHousingTaskerBean {
	private TaskHousingfund taskHousing;
	private int recordCount;   //爬取的记录总数（流水信息）、
	public TaskHousingfund getTaskHousing() {
		return taskHousing;
	}
	public void setTaskHousing(TaskHousingfund taskHousing) {
		this.taskHousing = taskHousing;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
}
