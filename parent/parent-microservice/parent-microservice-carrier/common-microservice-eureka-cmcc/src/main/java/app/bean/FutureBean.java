package app.bean;

import java.util.List;

public class FutureBean {
	
	public List<PageBean> getBeans() {
		return beans;
	}
	public void setBeans(List<PageBean> beans) {
		this.beans = beans;
	}
	public List<PageBean> beans;
	public boolean isCrawler = true;

}
