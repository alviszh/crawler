package app.bean;

public class RequestParam {
	
	public String idnum;
	public String taskid;
	
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "RequestParam [idnum=" + idnum + ", taskid=" + taskid + "]";
	}
	
	
}
