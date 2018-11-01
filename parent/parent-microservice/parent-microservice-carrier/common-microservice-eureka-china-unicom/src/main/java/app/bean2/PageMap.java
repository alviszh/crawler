package app.bean2;

import java.util.List;

public class PageMap {
	private List<Result> result;

	private int totalCount;

	private int pageNo;

	private List<Pages> pages;

	private int pageSize;

	private int totalPages;

	public void setResult(List<Result> result) {
		this.result = result;
	}

	public List<Result> getResult() {
		return this.result;
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

	public void setPages(List<Pages> pages) {
		this.pages = pages;
	}

	public List<Pages> getPages() {
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
