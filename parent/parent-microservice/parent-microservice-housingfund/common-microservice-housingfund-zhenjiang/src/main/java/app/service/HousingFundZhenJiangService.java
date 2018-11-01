package app.service;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.ExUtils;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import app.service.common.aop.ICrawlerLogin;

/**
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.zhenjiang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.zhenjiang"})
public class HousingFundZhenJiangService extends HousingBasicService  implements ICrawlerLogin{
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Autowired
	private HousingFundZhenJiangCrawlerService housingFundZhenJiangCrawlerService;
	private static Integer imageErrorCount=0;   //验证码识别错误次数计数器
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		housingFundZhenJiangCrawlerService.getUserInfo(taskHousing);
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			String url="http://www.zjzfjj.com.cn/searchPersonLogon.do";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				int status = hPage.getWebResponse().getStatusCode();
				if(200 == status){
					HtmlImage image = hPage.getFirstByXPath("//img[@src='jcaptcha?onlynum=true']"); 
					String code = chaoJiYingOcrService.getVerifycode(image, "1902");
					String password = messageLoginForHousing.getPassword().trim();
					String encryptPassword = housingFundHelperService.encrypt(password);
					//select=1是职工账号方式登录，select=2是身份证号的方式登录
					String requestBody="";
					if(messageLoginForHousing.getLogintype().equals("IDNUM")){  //身份证号登录
						String idnum = messageLoginForHousing.getNum().trim().trim();  //身份证号
						String encryptIdnum = housingFundHelperService.encrypt(idnum);
						requestBody="select=2&spcode="+encryptIdnum+"&sppassword="+encryptPassword+"&rand="+code.trim()+"";
					}else{  
						String countNumber = messageLoginForHousing.getCountNumber().trim(); //职工账号
						String encryptCountNumber = housingFundHelperService.encrypt(countNumber);
						requestBody="select=1&spcode="+encryptCountNumber+"&sppassword="+encryptPassword+"&rand="+code.trim()+"";
					}
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					webRequest.setRequestBody(requestBody);
					webClient.getOptions().setJavaScriptEnabled(false);
					Page logonPage=webClient.getPage(webRequest);
					if(null!=logonPage){
						String html = logonPage.getWebResponse().getContentAsString();
						if(html.contains("职工查询")){
							changeLoginStatusSuccess(taskHousing, webClient);
						}else{
							if(html.contains("您输入的验证有误")){
								imageErrorCount++;
								if(imageErrorCount>3){
									 tracer.addTag("操作失败:进行身份校验时出错:您输入的验证码与图片不符"+imageErrorCount, taskHousing.getTaskid());
									 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
									 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhasestatus());
									 taskHousing.setDescription("登录失败，图片验证码识别错误！");
									 save(taskHousing);
									 imageErrorCount=0;
								}else{
									login(messageLoginForHousing);
								}
							}else if(html.contains("请认真核实您输入的职工帐号或身份证号码及密码")){
								tracer.addTag("登录失败，请认真核实您输入的职工帐号或身份证号码及密码！", taskHousing.getTaskid());
								taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
								taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
								taskHousing.setDescription("登录失败，请认真核实您输入的职工帐号或身份证号码及密码！");
								save(taskHousing);
							}else{
								tracer.addTag("登录出现了其他错误，此处日志记录，详见响应的源码："+taskHousing.getTaskid(), html);
								taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
								taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
								taskHousing.setDescription("系统繁忙，请稍后再试！");
								save(taskHousing);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录失败，程序出现异常：",ExUtils.getEDetail(e));
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("系统繁忙，请稍后再试！");
			save(taskHousing);
		}
		return taskHousing;
	}
}
