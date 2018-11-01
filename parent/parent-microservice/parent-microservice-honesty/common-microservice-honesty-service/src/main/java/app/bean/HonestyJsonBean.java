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
public class HonestyJsonBean {

	private String taskid;
	
	private List<Honestybean> keys; //查询的关键字
	 
	private List<HonestyShiXinBean> result; //查询结果
	
	private Integer pagenum; //查询页数
	
	private Integer pagesize;//查询每页数量
	
	
	
		
	public List<HonestyShiXinBean> getResult() {
		return result;
	}

	public void setResult(List<HonestyShiXinBean> result) {
		this.result = result;
	}

	public Integer getPagenum() {
		return pagenum;
	}

	public void setPagenum(Integer pagenum) {
		this.pagenum = pagenum;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}

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

	public List<Honestybean> getKeys() {
		return keys;
	}

	public void setKeys(List<Honestybean> keys) {
		this.keys = keys;
	}

	@Override
	public String toString() {
		return "HonestyJsonBean [taskid=" + taskid + ", keys=" + keys + ", result=" + result + ", pagenum=" + pagenum
				+ ", pagesize=" + pagesize + ", priority=" + priority + "]";
	}
	
}
