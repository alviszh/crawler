package app.crawler.bean;


import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.microservice.dao.entity.crawler.housing.rizhao.HousingRizhaoUserInfo;

public class WebParam<T> {
	
	public Page page;
	public Integer code;
	
	public String url;
	
	public String html;
	
	public String text;
	
	public List<T> list;
	
	private String cookies;
	
	private InfoParam infoParam;
	
	private HousingRizhaoUserInfo housingRizhaoUserInfo;
	
	private List<String> datalist;
	
	public List<String> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<String> datalist) {
		this.datalist = datalist;
	}
	public HousingRizhaoUserInfo getHousingRizhaoUserInfo() {
		return housingRizhaoUserInfo;
	}

	public void setHousingRizhaoUserInfo(HousingRizhaoUserInfo housingRizhaoUserInfo) {
		this.housingRizhaoUserInfo = housingRizhaoUserInfo;
	}


	public InfoParam getInfoParam() {
		return infoParam;
	}


	public void setInfoParam(InfoParam infoParam) {
		this.infoParam = infoParam;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public Page getPage() {
		return page;
	}


	public void setPage(Page page) {
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


	public List<T> getList() {
		return list;
	}


	public void setList(List<T> list) {
		this.list = list;
	}


	public String getCookies() {
		return cookies;
	}


	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	
}
