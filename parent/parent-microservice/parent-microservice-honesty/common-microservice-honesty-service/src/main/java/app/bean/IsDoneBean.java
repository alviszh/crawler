package app.bean;
/**   
*    
* 项目名称：common-microservice-search-task   
* 类名称：isDoneBean   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月26日 下午3:35:34   
* @version        
*/
public class IsDoneBean {

	
	private String taskid;
	
	private int totalnum;
	private int unfinishednum;
	private int finishednum;
	private int crawleringnum;
	private int errornum;
	
	private String errormessage;
		
	
	public String getErrormessage() {
		return errormessage;
	}
	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public int getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}
	public int getUnfinishednum() {
		return unfinishednum;
	}
	public void setUnfinishednum(int unfinishednum) {
		this.unfinishednum = unfinishednum;
	}
	public int getFinishednum() {
		return finishednum;
	}
	public void setFinishednum(int finishednum) {
		this.finishednum = finishednum;
	}
	public int getCrawleringnum() {
		return crawleringnum;
	}
	public void setCrawleringnum(int crawleringnum) {
		this.crawleringnum = crawleringnum;
	}
	public int getErrornum() {
		return errornum;
	}
	public void setErrornum(int errornum) {
		this.errornum = errornum;
	}
	@Override
	public String toString() {
		return "IsDoneBean [taskid=" + taskid + ", totalnum=" + totalnum + ", unfinishednum=" + unfinishednum
				+ ", finishednum=" + finishednum + ", crawleringnum=" + crawleringnum + ", errornum=" + errornum
				+ ", errormessage=" + errormessage + "]";
	}
	
	
}
