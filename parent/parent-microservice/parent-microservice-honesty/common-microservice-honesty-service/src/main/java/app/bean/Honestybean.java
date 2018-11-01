package app.bean;

/**
 * Auto-generated: 2017-08-08 10:55:53
 *
 * @author www.jsons.cn
 * @website http://www.jsons.cn/json2java/
 */
public class Honestybean {

	private String taskid;
	
	private String pName;
	
	private String pCardNum;
	
	private Integer pagenum;
	
	private Integer pagesize;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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

	@Override
	public String toString() {
		return "Honestybean [taskid=" + taskid + ", pName=" + pName + ", pCardNum=" + pCardNum + ", pagenum=" + pagenum
				+ ", pagesize=" + pagesize + "]";
	}
	
	

}