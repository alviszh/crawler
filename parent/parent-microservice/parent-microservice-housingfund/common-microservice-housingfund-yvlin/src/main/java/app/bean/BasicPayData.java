/**
  * Copyright 2018 bejson.com 
  */
package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.housing.yvlin.HousingBasicPayResult;

/**
 * Auto-generated: 2018-01-11 16:44:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BasicPayData {

	private int pageSize;
	private int pageCount;
	private int currentPage;
	private int rows;
	private String tableid;
	private List<HousingBasicPayResult> data;
	private int totalCount;

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getRows() {
		return rows;
	}

	public void setTableid(String tableid) {
		this.tableid = tableid;
	}

	public String getTableid() {
		return tableid;
	}

	public void setData(List<HousingBasicPayResult> data) {
		this.data = data;
	}

	public List<HousingBasicPayResult> getData() {
		return data;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public String toString() {
		return "BasicPayData [pageSize=" + pageSize + ", pageCount=" + pageCount + ", currentPage=" + currentPage
				+ ", rows=" + rows + ", tableid=" + tableid + ", data=" + data + ", totalCount=" + totalCount + "]";
	}

}