package app.crawler.domain;


import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduUserInfo;
import com.microservice.dao.entity.crawler.insurance.fuzhou3.InsuranceFuZhou3Account;
import com.microservice.dao.entity.crawler.insurance.fuzhou3.InsuranceFuZhou3Html;
import com.microservice.dao.entity.crawler.insurance.shiyan.InsuranceShiYanHtml;
import com.microservice.dao.entity.crawler.insurance.shiyan.InsuranceShiYanMedical;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanHtml;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanMedicalCare;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanPensionDetail;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanPensionAccount;

public class WebParam<T> {
	
	public HtmlPage page;
	public Integer code;
	
	public InsuranceShiYanMedical insuranceShiYanMedical;
	public InsuranceShiYanHtml insuranceShiYanHtml;
	
	public String url;
	public String html;
	
	
	public List<T> list;
	
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
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public InsuranceShiYanMedical getInsuranceShiYanMedical() {
		return insuranceShiYanMedical;
	}
	public void setInsuranceShiYanMedical(InsuranceShiYanMedical insuranceShiYanMedical) {
		this.insuranceShiYanMedical = insuranceShiYanMedical;
	}
	public InsuranceShiYanHtml getInsuranceShiYanHtml() {
		return insuranceShiYanHtml;
	}
	public void setInsuranceShiYanHtml(InsuranceShiYanHtml insuranceShiYanHtml) {
		this.insuranceShiYanHtml = insuranceShiYanHtml;
	}
	
}
