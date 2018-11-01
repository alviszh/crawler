package app.service;

import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.daqing.InsuranceDaQingHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.daqing.InsuranceDaQingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.daqing.InsuranceDaQingUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceDaQingParser;
import app.service.aop.InsuranceLogin;

/**
 * Created by root on 2017/9/19.
 */
@Component
@SuppressWarnings("all")
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
        "com.microservice.dao.entity.crawler.insurance.daqing" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
        "com.microservice.dao.repository.crawler.insurance.daqing" })
public class InsuranceDaQingService implements InsuranceLogin {
    @Autowired
    private TracerLog tracer;
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    @Autowired
    private InsuranceDaQingParser insuranceDaQingParser;
    @Autowired
    private InsuranceDaQingUserInfoRepository insuranceDaQingUserInfoRepository;
    @Autowired
    private InsuranceDaQingHtmlRepository insuranceDaQingHtmlRepository;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private InsuranceDaQingService insuranceDaQingService;
    @Autowired
    private InsuranceDaQingCrawlerService daQingCrawlerService;
    
    @Async
    @Override
    public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        try {
        	 //登陆，获取响应的页面
        	String loginType = insuranceRequestParameters.getLoginType();
        	//加密登陆所用的姓名
        	String encodeName=URLEncoder.encode(insuranceRequestParameters.getName(), "utf-8");
        	String requestBody="";
        	if(loginType.equals("IDNUM")){  //身份证号登陆
        		requestBody="code=1&pid2=1&shebaohao="+insuranceRequestParameters.getUsername().trim()+"&name="+encodeName+"";
        	}else{   //社保卡号登陆方式
        		requestBody="code=2&pid2=1&shebaohao="+insuranceRequestParameters.getUsername().trim()+"&name="+encodeName+"";
        	}
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="http://www.daqinghr.gov.cn/gerenshebao!getlist.action";   //用于验证登陆信息的连接
        	WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.POST);
        	webRequest.setRequestBody(requestBody);
			HtmlPage page = webClient.getPage(webRequest);
			if(null!=page){
				//先保存页面
				String html=page.getWebResponse().getContentAsString();
                InsuranceDaQingHtml insuranceDaQingHtml = new InsuranceDaQingHtml();
                insuranceDaQingHtml.setPagenumber(1);
                insuranceDaQingHtml.setType("登陆信息验证结果页面兼待爬取数据页面");
                insuranceDaQingHtml.setTaskid(insuranceRequestParameters.getTaskId());
                insuranceDaQingHtml.setUrl(url);
                insuranceDaQingHtml.setHtml(html);
                insuranceDaQingHtmlRepository.save(insuranceDaQingHtml);
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
	                Document doc = Jsoup.parse(html);
	                String result = doc.select("th:contains(姓名)+td").first().text();
	                tracer.addTag("登陆结果页面响应后，获取用于判断是否登陆成功标志—姓名:", result);
	                if(result.length()>0){   //如果能够获取到姓名，说明用户输入的登陆信息正确——登录成功
	                	insuranceService.changeLoginStatusSuccess(taskInsurance,page);
	                }else if(html.contains("无查询结果")){   //说明登陆信息输入错误
	                	insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
								"登陆信息输入有误，或登陆信息不存在！",taskInsurance);
	                }else{   //出现了其他类型的登陆错误
	                	tracer.addTag("出现了调研时没出现的情况", "登录失败原因详见保存的源码");
	                	insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
								"信息校验过程中出现些许小差错，请联系管理员~",taskInsurance);
	                }
				}else{  //网站暂时无法响应
					tracer.addTag("登陆页面暂时无法响应","网站可能处于维护状态");
					insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhase(),
							InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhasestatus(),
							"官网目前处于维护状态，暂时无法提供相关查询服务，抱歉~~~",taskInsurance);
				}
				
			}
		} catch (Exception e) {
			tracer.addTag("登录过程出现异常，异常信息是：", e.toString());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
					"系统繁忙，请稍后再试！",taskInsurance);
		}
       
		return taskInsurance;
    }
    /**
     * @Des 爬取总接口
     * @param insuranceRequestParameters
     * @return 
     * @throws Exception
     */
    @Async
    @Override
    public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters){
    	TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
    	//将存储的html取出进行爬取
    	InsuranceDaQingHtml htmlObj = insuranceDaQingHtmlRepository.findTopByTaskidOrderByCreatetimeDesc(insuranceRequestParameters.getTaskId());
    	if(null!=htmlObj){
    		String html = htmlObj.getHtml();
    		daQingCrawlerService.getAllInfo(taskInsurance,html);
    	}
		return taskInsurance;
    }

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
