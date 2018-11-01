/**
  * Copyright 2017 bejson.com 
  */
package app.bean;

/**
 * Auto-generated: 2017-10-31 14:58:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Header {

	private String local;
	private String agent;
	@Override
	public String toString() {
		return "Header [local=" + local + ", agent=" + agent + ", version=" + version + ", device=" + device
				+ ", platform=" + platform + ", plugins=" + plugins + ", page=" + page + ", ext=" + ext + "]";
	}

	private String version;
	private String device;
	private String platform;
	private String plugins;
	private String page;
	private String ext;

	public void setLocal(String local) {
		this.local = local;
	}

	public String getLocal() {
		return local;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAgent() {
		return agent;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getDevice() {
		return device;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlugins(String plugins) {
		this.plugins = plugins;
	}

	public String getPlugins() {
		return plugins;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPage() {
		return page;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getExt() {
		return ext;
	}

}