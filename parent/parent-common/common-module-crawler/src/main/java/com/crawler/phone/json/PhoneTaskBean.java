package com.crawler.phone.json;

import java.io.Serializable;

/**   
*    
* 项目名称：common-microservice-client   
* 类名称：aa   
* 类描述：   
* 创建人：zhaohui
* 创建时间：2018年10月9日 下午4:40:20   
* @version        
*/
public class PhoneTaskBean implements Serializable{
	
	private String taskid;

	private String phone;
	
	@Override
	public String toString() {
		return "PhoneTaskBean [ taskid=" + taskid + ", phone=" + phone + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
