/**
 * 
 */
package app.bean;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

/**
 * @author sln
 * @date 2018年9月12日下午6:42:42
 * @Description: 
 */
public class MonitorHousingTaskerBean {
	private TaskHousing taskHousing;
	private int recordCount;   //爬取的记录总数（流水信息）、
	public TaskHousing getTaskHousing() {
		return taskHousing;
	}
	public void setTaskHousing(TaskHousing taskHousing) {
		this.taskHousing = taskHousing;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public MonitorHousingTaskerBean(TaskHousing taskHousing, int recordCount) {
		super();
		this.taskHousing = taskHousing;
		this.recordCount = recordCount;
	}
	public MonitorHousingTaskerBean() {
		super();
	}
}
