package app.bean;

import java.util.List;

/**   
*    
* 项目名称：common-microservice-search   
* 类名称：SanWangJsonBean   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月31日 下午3:54:20   
* @version        
*/
public class SanWangJsonBean {

	private String taskid;
	
	private List<String> keys;
	
	private int pagenum;
	
	private int  priority; //优先级
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	@Override
	public String toString() {
		return "SanWangJsonBean [taskid=" + taskid + ", keys=" + keys + ", pagenum=" + pagenum + ", priority="
				+ priority + "]";
	}
	
	
}
