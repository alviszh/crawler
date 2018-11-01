package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.yibin.InsuranceYibinPersion;
import com.microservice.dao.entity.crawler.insurance.yibin.InsuranceYinbinMedical;
import com.microservice.dao.entity.crawler.insurance.yibin.InsuranceYinbinUserInfo;

public class WebParam {

	public HtmlPage page;
	public Integer code;	
	public String alertMsg;//存储弹框信息
	public InsuranceYinbinUserInfo  userInfo;	
	public String url;//存储请求页面的url
	public String html;//存储请求页面的html代码
	public List<InsuranceYinbinMedical>  medicalList;
	public List<InsuranceYibinPersion>  persionList;
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
	public InsuranceYinbinUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(InsuranceYinbinUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public List<InsuranceYinbinMedical> getMedicalList() {
		return medicalList;
	}
	public void setMedicalList(List<InsuranceYinbinMedical> medicalList) {
		this.medicalList = medicalList;
	}
	public List<InsuranceYibinPersion> getPersionList() {
		return persionList;
	}
	public void setPersionList(List<InsuranceYibinPersion> persionList) {
		this.persionList = persionList;
	}
	public String getAlertMsg() {
		return alertMsg;
	}
	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}
	
}
