package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanBear;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanInjury;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanMedical;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanPension;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanUnemployment;

public class WebParam<T> {
	
	public HtmlPage htmlPage;
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	public List<InsuranceSZHunanPension> listPension;
	public List<InsuranceSZHunanMedical> listMedical;
	public List<InsuranceSZHunanBear> listBear;
	public List<InsuranceSZHunanInjury> listInjury;
	public List<InsuranceSZHunanUnemployment> listUnemployment;
	public HtmlPage getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(HtmlPage htmlPage) {
		this.htmlPage = htmlPage;
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
	public List<InsuranceSZHunanPension> getListPension() {
		return listPension;
	}
	public void setListPension(List<InsuranceSZHunanPension> listPension) {
		this.listPension = listPension;
	}
	public List<InsuranceSZHunanMedical> getListMedical() {
		return listMedical;
	}
	public void setListMedical(List<InsuranceSZHunanMedical> listMedical) {
		this.listMedical = listMedical;
	}
	public List<InsuranceSZHunanBear> getListBear() {
		return listBear;
	}
	public void setListBear(List<InsuranceSZHunanBear> listBear) {
		this.listBear = listBear;
	}
	public List<InsuranceSZHunanInjury> getListInjury() {
		return listInjury;
	}
	public void setListInjury(List<InsuranceSZHunanInjury> listInjury) {
		this.listInjury = listInjury;
	}
	public List<InsuranceSZHunanUnemployment> getListUnemployment() {
		return listUnemployment;
	}
	public void setListUnemployment(List<InsuranceSZHunanUnemployment> listUnemployment) {
		this.listUnemployment = listUnemployment;
	}
	@Override
	public String toString() {
		return "WebParam [htmlPage=" + htmlPage + ", page=" + page + ", code=" + code + ", url=" + url + ", html="
				+ html + ", list=" + list + ", listPension=" + listPension + ", listMedical=" + listMedical
				+ ", listBear=" + listBear + ", listInjury=" + listInjury + ", listUnemployment=" + listUnemployment
				+ "]";
	}
	
	
}
