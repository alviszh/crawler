package com.crawler.eureka.listen;

public class ClientBean {

	private String name;// 所属的项目名称
	// 爬虫端--->middle
	private String serverip;// serverip
	private String clienttype;// 0：开始使用 1：使用完成
	// middle--->爬虫端
	private String leisureip;// 空闲serverip

	private String msg;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerip() {
		return serverip;
	}

	public void setServerip(String serverip) {
		this.serverip = serverip;
	}

	public String getClienttype() {
		return clienttype;
	}

	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}

	public String getLeisureip() {
		return leisureip;
	}

	public void setLeisureip(String leisureip) {
		this.leisureip = leisureip;
	}

	@Override
	public String toString() {
		return "ClientBean [name=" + name + ", serverip=" + serverip + ", clienttype=" + clienttype + ", leisureip="
				+ leisureip + ", msg=" + msg + "]";
	}

}
