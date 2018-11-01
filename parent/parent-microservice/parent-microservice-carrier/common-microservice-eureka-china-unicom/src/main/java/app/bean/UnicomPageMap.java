package app.bean;

import java.util.List;

public class UnicomPageMap<T> {
	private List<T> result;

	private int totalCount;

	private int pageNo;

	private List<UnicomPages> pages;

	private int pageSize;

	private int totalPages;

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount() {
		return this.totalCount;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPages(List<UnicomPages> pages) {
		this.pages = pages;
	}

	public List<UnicomPages> getPages() {
		return this.pages;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalPages() {
		return this.totalPages;
	}

}