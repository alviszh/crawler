package com.crawler.monitor.json;

public class MonitorRancherChange {
	private String ip;
	private String nodename;
	private String netstate;
	private String diskprop;   //硬盘使用占比
	private String swapfree;     //硬盘内存不足之后，内存被占用，还剩余多少（百分比）
	private String selflink;    //主机对应的api链接，用于邮件中加超链接
	private String loadavg;     //cpu使用占比，认为只要一次超过阈值，就报警
	private String envirtype;    //所属rancher环境
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNodename() {
		return nodename;
	}
	public void setNodename(String nodename) {
		this.nodename = nodename;
	}
	public String getNetstate() {
		return netstate;
	}
	public void setNetstate(String netstate) {
		this.netstate = netstate;
	}
	public String getDiskprop() {
		return diskprop;
	}
	public void setDiskprop(String diskprop) {
		this.diskprop = diskprop;
	}
	public String getSwapfree() {
		return swapfree;
	}
	public void setSwapfree(String swapfree) {
		this.swapfree = swapfree;
	}
	public String getSelflink() {
		return selflink;
	}
	public void setSelflink(String selflink) {
		this.selflink = selflink;
	}
	public String getEnvirtype() {
		return envirtype;
	}
	public void setEnvirtype(String envirtype) {
		this.envirtype = envirtype;
	}
	public String getLoadavg() {
		return loadavg;
	}
	public void setLoadavg(String loadavg) {
		this.loadavg = loadavg;
	}
	
}
