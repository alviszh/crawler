package app.bean;

import com.gargoylesoftware.htmlunit.Page;

public class PageBean {
	
	public Page pages;
	public String startYear;
	public int m;
	public int total;
	public String time;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Page getPages() {
		return pages;
	}
	public int getM() {
		return m;
	}
	public void setM(int m) {
		this.m = m;
	}
	public void setPages(Page pages) {
		this.pages = pages;
	}
	public String getStartYear() {
		return startYear;
	}
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}
	@Override
	public String toString() {
		return "PageBean [pages=" + pages + ", startYear=" + startYear + ", m=" + m + ", total=" + total + "]";
	}
	
	

}
