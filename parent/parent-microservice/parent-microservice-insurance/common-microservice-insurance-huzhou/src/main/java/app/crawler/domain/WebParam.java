package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouBasicinfo;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouRecords;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouUserInfo;

public class WebParam {

	public HtmlPage page;
	public Integer code;	
	public InsuranceHuzhouUserInfo userInfo;	
	public String url;//存储请求页面的url
	public String html;//存储请求页面的html代码
	public List<InsuranceHuzhouBasicinfo>  basicinfoList;
	public List<InsuranceHuzhouRecords>  recordList;
	//currentPage: 2, next: 3, pageEnd: 40, pageSize: 20, pageStart: 21, pageSum: 3, prev: 1, totalRows: 46 
	//返回的页面参数 ，当做下次查询的参数
	public String currentPage;
	public String next;
	public String pageEnd;
	public String pageSize;
	public String pageStart;
	public String pageSum;
	public String prev;
	public String totalRows;
	public String errMsg;
	public WebClient webClient;
	public HtmlPage getPage() {
		return page;
	}
	public void setPage(HtmlPage page) {
		this.page = page;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public InsuranceHuzhouUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(InsuranceHuzhouUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public List<InsuranceHuzhouBasicinfo> getBasicinfoList() {
		return basicinfoList;
	}
	public void setBasicinfoList(List<InsuranceHuzhouBasicinfo> basicinfoList) {
		this.basicinfoList = basicinfoList;
	}
	public List<InsuranceHuzhouRecords> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<InsuranceHuzhouRecords> recordList) {
		this.recordList = recordList;
	}
	public String getPageSum() {
		return pageSum;
	}
	public void setPageSum(String pageSum) {
		this.pageSum = pageSum;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public String getPageEnd() {
		return pageEnd;
	}
	public void setPageEnd(String pageEnd) {
		this.pageEnd = pageEnd;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getPageStart() {
		return pageStart;
	}
	public void setPageStart(String pageStart) {
		this.pageStart = pageStart;
	}
	public String getPrev() {
		return prev;
	}
	public void setPrev(String prev) {
		this.prev = prev;
	}
	public String getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", code=" + code + ", userInfo=" + userInfo + ", url=" + url + ", html="
				+ html + ", basicinfoList=" + basicinfoList + ", recordList=" + recordList + ", currentPage="
				+ currentPage + ", next=" + next + ", pageEnd=" + pageEnd + ", pageSize=" + pageSize + ", pageStart="
				+ pageStart + ", pageSum=" + pageSum + ", prev=" + prev + ", totalRows=" + totalRows + ", errMsg="
				+ errMsg + ", webClient=" + webClient + "]";
	}
	
}
