package app.service;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;
import app.commontracerlog.TracerLog;

@Component
public class YgbxRetryService {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

	/**
	 * 模拟点击校验信息（加重试机制）
	 * @param carInsuranceRequestBean
	 * @return
	 */
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	public String retry(CarInsuranceRequestBean carInsuranceRequestBean) {
		String url = "http://wecare.sinosig.com/common/new_customerservice/html/baodanfuwu/dzbd_index.html";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		HtmlPage page = null;
		try {
			page = (HtmlPage) getHtml(url,webClient);
		} catch (Exception e) {
			throw new RuntimeException("获取验证界面出错！"+e.getMessage());
		}
//		System.out.println(page.getWebResponse().getContentAsString());
		//保单号
		HtmlTextInput insuredNo = page.getFirstByXPath("//input[@id='insuredNo']");
		tracer.addTag("保单号输入框:", insuredNo.asXml());
		//身份证号
		HtmlTextInput insuredCardNo = page.getFirstByXPath("//input[@id='insuredCardNo']");
		tracer.addTag("身份证号输入框:", insuredCardNo.asXml());
		//验证码
		HtmlTextInput verifycode = page.getFirstByXPath("//input[@id='verifycode']");
		tracer.addTag("验证码输入框:", verifycode.asXml());
		//登录按钮
		HtmlInput land = (HtmlInput)page.getFirstByXPath("//input[@id='land']");
		tracer.addTag("登录按钮:", land.asXml());
		//验证码
		HtmlImage image = page.getFirstByXPath("//img[@id='cheNum']");
		tracer.addTag("验证码:", image.asXml());
		
		String code = null;
		try {
			code = chaoJiYingOcrService.getVerifycode(image, "1902");
		} catch (Exception e) {
			throw new RuntimeException("超级鹰解析验证码失败！"+e.getMessage());
		}		
		tracer.addTag("超级鹰解析验证码后结果：", code);
		
		insuredNo.setText(carInsuranceRequestBean.getPolicyNum());
		insuredCardNo.setText(carInsuranceRequestBean.getIdnum());
		verifycode.setText(code);
		
		DomNode loginPage = null;
		try {
			loginPage = land.click();
		} catch (IOException e) {
			throw new RuntimeException("获取验证后页面失败！"+e.getMessage());
		}
		Document doc = Jsoup.parse(loginPage.asXml());
		Element element = doc.getElementById("iframe");
		if(null == element){
			throw new RuntimeException("获取验证后页面失败！"+"iframe = null");
		}
		String src = element.attr("src");			
		if(StringUtils.isNoneBlank(src)){
			return src;
		}else{
			throw new RuntimeException("未获取到正确的请求参数！"+"src = null");
		}
	}
	
	
	public Page getHtml(String url,WebClient webClient) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}

}
