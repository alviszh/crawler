package app.service;


import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jilin.HousingJiLinParams;
import com.microservice.dao.repository.crawler.housing.jilin.HousingJiLinParamsRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.ExUtils;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;
/**
 * 吉林公积金改版之后，用之前调研过程中发现的另一个吉林公积金网站（只能爬取个人信息，没有具体的流水信息）
 * @author sln
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.jilin"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.jilin"})
public class HousingFundJiLinRevisionService extends HousingBasicService  implements ICrawlerLogin{
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundJiLinCrawlerService housingFundJiLinCrawlerService;
	@Autowired
	private HousingJiLinParamsRepository paramsRepository;
	private static int imageErrorCount;
	
	public String splitData(String str, String strStart, String strEnd) {  
		int i = str.indexOf(strStart); 
		int j = str.indexOf(strEnd, i); 
		String tempStr=str.substring(i+strStart.length(), j); 
        return tempStr;  
	}
	//爬取所有的数据，实际上就是个人基本信息数据
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		HousingJiLinParams params = paramsRepository.findTopByTaskidOrderByCreatetimeDesc(messageLoginForHousing.getTask_id());		
		String accnum=null;
		String certinum=null;
		if(null!=params){
			accnum=params.getAccnum().trim();
			certinum=params.getCertinum().trim();
		}
		//改版后只能爬取个人信息，无流水明细可供爬取
		try {
			housingFundJiLinCrawlerService.getUserInfo(taskHousing, accnum, certinum);
		} catch (Exception e) {
			//明细账信息的爬取需要依赖用户信息，故用户信息出现异常，明细账信息也无法正常爬取，故需要在异常中更新状态
			tracer.addTag("action.crawler.getUserInfo.e",e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
			updatePayStatusByTaskid("数据采集中，缴费信息已采集完成",201,taskHousing.getTaskid());
			//此处也要更新最终爬取状态
			updateTaskHousing(taskHousing.getTaskid());
		}
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
			String url="http://old.jlgjj.gov.cn/website/website/trans/newperlogin.html";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				HtmlImage image = hPage.getFirstByXPath("//img[@id='image']");
				String code = chaoJiYingOcrService.getVerifycode(image, "2004");
				//验证登陆信息的准确性
				url="http://old.jlgjj.gov.cn/website/website/trans/gjjquery.do?className=TRC020001"; 
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				code=URLEncoder.encode(code, "utf-8");
				String requestBody=""
						+ "time=1533793008849"
						+ "&accnum="
						+ "&certinum="+messageLoginForHousing.getNum().trim()+""
						+ "&password="+messageLoginForHousing.getPassword().trim()+""
						+ "&mark=2"
						+ "&txt=1"
						+ "&verify="+code+"";
				webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest.setAdditionalHeader("Connection", "keep-alive");
				webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				webRequest.setAdditionalHeader("Host", "old.jlgjj.gov.cn");
				webRequest.setAdditionalHeader("Origin", "http://old.jlgjj.gov.cn");
				webRequest.setAdditionalHeader("Referer", "http://old.jlgjj.gov.cn/website/website/trans/newperlogin.html");
				webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
				webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				webRequest.setRequestBody(requestBody);
				Page page = webClient.getPage(webRequest);
				if(null!=page){
					String logonHtml=page.getWebResponse().getContentAsString(); 
					tracer.addTag("校验登信息返回的页面是为：",logonHtml);
					if(logonHtml.contains("success")){ 
						//登录成功
						webClient = hPage.getWebClient();
				 		String cookieString = CommonUnit.transcookieToJson(webClient);  //存储cookie
				 		changeLoginStatusSuccess(taskHousing, webClient);
						//登录成功从cookie中获取公积金账号
						String accnum = splitData(cookieString,"gjjaccnum\",\"value\":\"","\"}");
						//获取身份证号
						String certinum = messageLoginForHousing.getNum().trim();
						HousingJiLinParams params=new HousingJiLinParams();
						params.setAccnum(accnum);
						params.setCertinum(certinum);
						params.setTaskid(messageLoginForHousing.getTask_id());
						paramsRepository.save(params);
						tracer.addTag("登陆成功之后获取的爬取参数已经入库","分别是公积金账号:"+accnum+"和身份账号:"+certinum+"");
					}else if(logonHtml.contains("msg")){
						String errMsg = JSONObject.fromObject(logonHtml).getString("msg");
						if(errMsg.contains("身份证转换错误")){
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							 taskHousing.setDescription(errMsg);
							 save(taskHousing);
						}else if(errMsg.contains("用户密码不匹配")){
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
							 taskHousing.setDescription(errMsg);
							 save(taskHousing);
						}else if(errMsg.contains("请输入六位有效的数字密码")){
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
							 taskHousing.setDescription(errMsg);
							 save(taskHousing);
						}else if(errMsg.contains("验证码不正确")){
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
						}else{
							tracer.addTag("登录出现了其他错误，此处日志记录：", errMsg);
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getPhasestatus());
							taskHousing.setDescription(errMsg);
							save(taskHousing);
						}
					}else{
						tracer.addTag("登录出现了其他错误", "详见登陆页面源码");
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getPhasestatus());
						taskHousing.setDescription("登录失败，系统繁忙，请稍后再试！");
						save(taskHousing);
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录失败，程序出现异常：", ExUtils.getEDetail(e));
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录失败，系统繁忙，请稍后再试！");
			save(taskHousing);
			imageErrorCount=0;
		}
		return taskHousing;
	}
}
