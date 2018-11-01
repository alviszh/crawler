package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiGeneral;
import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiHtml;
import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiUserInfo;

public class WebParam {
	
	public HtmlPage page;
	public Integer code;
	
	public InsuranceShanghaiUserInfo InsuranceShanghaiUserInfo;
	
	public List<InsuranceShanghaiGeneral> insuranceShanghaiGeneral;
	
	public List<InsuranceShanghaiHtml> insuranceShanghaiHtml;
	
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
	public InsuranceShanghaiUserInfo getInsuranceShanghaiUserInfo() {
		return InsuranceShanghaiUserInfo;
	}
	public void setInsuranceShanghaiUserInfo(InsuranceShanghaiUserInfo insuranceShanghaiUserInfo) {
		InsuranceShanghaiUserInfo = insuranceShanghaiUserInfo;
	}
	public List<InsuranceShanghaiGeneral> getInsuranceShanghaiGeneral() {
		return insuranceShanghaiGeneral;
	}
	public void setInsuranceShanghaiGeneral(List<InsuranceShanghaiGeneral> insuranceShanghaiGeneral) {
		this.insuranceShanghaiGeneral = insuranceShanghaiGeneral;
	}
	
	public List<InsuranceShanghaiHtml> getInsuranceShanghaiHtml() {
		return insuranceShanghaiHtml;
	}
	public void setInsuranceShanghaiHtml(List<InsuranceShanghaiHtml> insuranceShanghaiHtml) {
		this.insuranceShanghaiHtml = insuranceShanghaiHtml;
	}
	
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", code=" + code + ", InsuranceShanghaiUserInfo=" + InsuranceShanghaiUserInfo
				+ ", insuranceShanghaiGeneral=" + insuranceShanghaiGeneral + ", insuranceShanghaiHtml="
				+ insuranceShanghaiHtml + "]";
	}
	

}
