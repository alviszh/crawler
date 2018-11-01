package app.bean;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.honesty.shixin.ShiXinBean;


/**
 * 
 * 项目名称：common-module-dao 类名称：SearchTask 类描述： 创建人：hyx 创建时间：2018年1月18日 上午11:44:00
 * 
 * @version
 */
public class HonestyShiXinBean  {
	
	private String taskid;

	private String pName;//人名或公司名
	
	private String pCardNum;//身份证号或组织机构代码

    private Date updateTime;
    
    private List<ShiXinBean> list;
           
	private String message;
    
	private String status;// 状态
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ShiXinBean> getList() {
		return list;
	}

	public void setList(List<ShiXinBean> list) {
		this.list = list;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getpCardNum() {
		return pCardNum;
	}

	public void setpCardNum(String pCardNum) {
		this.pCardNum = pCardNum;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "HonestyShiXinBean [taskid=" + taskid + ", pName=" + pName + ", pCardNum=" + pCardNum + ", updateTime="
				+ updateTime + ", list=" + list + ", message=" + message + ", status=" + status + "]";
	}
	
	

}
