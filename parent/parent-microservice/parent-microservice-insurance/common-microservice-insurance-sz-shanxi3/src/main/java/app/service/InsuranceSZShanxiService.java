package app.service;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.module.htmlunit.WebCrawler;

import app.bean.CityCode;
import app.bean.ResponseBean;
import app.commontracerlog.TracerLog;

@Component
public class InsuranceSZShanxiService {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	
	/**
	 * 统一登录方法
	 * @param url
	 * @param insuranceRequestParameters
	 * @return
	 */
	public ResponseBean uniteLogin(String url,InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance){
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		ResponseBean responseBean = new ResponseBean();
		try {
			HtmlPage searchPage = (HtmlPage) getHtml(url,webClient);
			tracer.addTag("登录页面：", "<xmp>"+searchPage.asXml()+"</xmp>");
			//获取身份证号标签
			HtmlTextInput idnum = searchPage.getFirstByXPath("//input[@name='uname']");
			tracer.addTag("获取身份证号标签：", "<xmp>"+idnum.asXml()+"</xmp>");
			//获取姓名标签
			HtmlTextInput aac003 = searchPage.getFirstByXPath("//input[@name='aac003']");
			tracer.addTag("获取姓名标签：", "<xmp>"+aac003.asXml()+"</xmp>");
			//获取输入验证码框的标签
			HtmlTextInput psinput = searchPage.getFirstByXPath("//input[@maxlength='4']");
			tracer.addTag("获取输入验证码框的标签：", "<xmp>"+psinput.asXml()+"</xmp>");
			//验证码
			HtmlTextInput checkCode = searchPage.getFirstByXPath("//input[@id='checkCode']");
			tracer.addTag("验证码所在标签：", "<xmp>"+checkCode.asXml()+"</xmp>");
			String code = checkCode.getValueAttribute();
			tracer.addTag("验证码：", code);
			//获取点击登录按钮
			HtmlInput Icon2 = (HtmlInput)searchPage.getFirstByXPath("//input[@id='Icon2']");
			tracer.addTag("获取点击登录按钮：", "<xmp>"+Icon2.asXml()+"</xmp>");
			if(StringUtils.isNotBlank(insuranceRequestParameters.getCity())){
				//选择城市的select标签
				HtmlSelect select = searchPage.getFirstByXPath("//select[@name='ylslFlag']");
				if(null != select){
					if(insuranceRequestParameters.getCity().equals(CityCode.CITY_BAOJI.getCity())){
						select.getOption(CityCode.CITY_BAOJI.getCode()).setSelected(true);  					
					}
					if(insuranceRequestParameters.getCity().equals(CityCode.CITY_YANAN.getCity())){
						select.getOption(CityCode.CITY_YANAN.getCode()).setSelected(true);  					
					}
					if(insuranceRequestParameters.getCity().equals(CityCode.CITY_YULIN.getCity())){
						select.getOption(CityCode.CITY_YULIN.getCode()).setSelected(true);  					
					}
					
				}
				
			}
			
			idnum.setText(insuranceRequestParameters.getUsername());
			aac003.setText(insuranceRequestParameters.getName());
			psinput.setText(code);
			
			HtmlPage loginPage = Icon2.click();
			tracer.addTag("登录后的页面：", "<xmp>"+loginPage.asXml()+"</xmp>");
			if(loginPage.asXml().contains("身份证号码或姓名不正确，需重新输入")){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
						"身份证号码或姓名不正确，需重新输入",taskInsurance);
				responseBean.setCode(500);
				return responseBean;
			}else{				
				responseBean.setTaskInsurance(taskInsurance);
				responseBean.setWebClient(loginPage.getWebClient());
				return responseBean;
			}
			
		} catch (Exception e) {
			tracer.addTag("模拟登录出错：", e.getMessage());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhase(),
					"TIME_OUT",
					InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getDescription(),taskInsurance);
			return null;
		}
		
	}
	
	public Page getHtml(String url,WebClient webClient){
		Page searchPage = null;
		try {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
			searchPage = webClient.getPage(webRequest);
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return searchPage;
		
	}

}
