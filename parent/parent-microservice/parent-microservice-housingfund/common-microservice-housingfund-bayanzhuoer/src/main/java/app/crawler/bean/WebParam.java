package app.crawler.bean;


import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.microservice.dao.entity.crawler.housing.bayanzhuoer.HousingBayanzhuoerUserInfo;

public class WebParam<T> {
	
	public Page page;
	public Integer code;
	
	public String url;
	
	public String html;
	
	public String text;
	
	public List<T> list;
	
	private String cookies;
	
	private InfoParam infoParam;
	
	private HousingBayanzhuoerUserInfo housingBayanzhuoerUserInfo;
	
	private List<String> datalist;
	
	private String loginType;
	public List<String> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<String> datalist) {
		this.datalist = datalist;
	}
	public HousingBayanzhuoerUserInfo getHousingBayanzhuoerUserInfo() {
		return housingBayanzhuoerUserInfo;
	}

	public void setHousingBayanzhuoerUserInfo(HousingBayanzhuoerUserInfo housingBayanzhuoerUserInfo) {
		this.housingBayanzhuoerUserInfo = housingBayanzhuoerUserInfo;
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

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	
}
