package app.service.common;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.exceptiondetail.ExUtils;

/**
 * @description: 登录模板，石家庄、南京、济南    等公积金登录的公共service
 * 		之前用selenium的方式写，是因为登录页面有时候在加载的时候，图片验证码为空
 * 		或者是空白的图片，所以driver.get(loginUrl);写了两次
 * @author: sln 
 * @date: 2017年12月4日 下午2:25:30 
 * 
 *=========================分界线=====================================
 * 
 * 	爬取泰安社保的时候将page所代表的图片用流的 方式保存到本地，故决定用这个方式再来尝试以石家庄为模板的登录
 * 	经测试，可行，故决定放弃用selenium的方式，采用HTMLunit的方式登录，避免了selenium打包时间
 * 	长，测试需要查看登录页面的麻烦 							
 *  
 *  @date: 2017年12月27日 下午6:33
 */

public class HousingShiJiaZhuangTemplateCommonService extends HousingBasicService{
	private static Integer captchaErrorCount=0;   //验证码识别错误次数计数器
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,String loginUrl,String vericodeUrl, String fileSavePath){
		try {
			WebRequest  webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setTimeout(60 * 1000);  //联调加的
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				//此处请求图片验证码链接
				webRequest = new WebRequest(new URL(vericodeUrl), HttpMethod.GET);
				Page page = webClient.getPage(webRequest);
				//利用io流保存图片验证码
				String imgagePath=getImagePath(page,fileSavePath);
				String code = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1902");   
				HtmlTextInput loginName = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='certinum']"); 
				if(loginUrl.contains("maswt")){ //马鞍山公积金
					HtmlPasswordInput loginPassword = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@id='pwd']"); 
					loginPassword.setText(messageLoginForHousing.getPassword().trim()); 
				}else{
					HtmlPasswordInput loginPassword = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@name='perpwd']"); 
					loginPassword.setText(messageLoginForHousing.getPassword().trim()); 	
				}
				HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='vericode']"); 
				HtmlElement submitbt = (HtmlElement)hPage.getFirstByXPath("//button[@type='submit']"); 
				loginName.setText(messageLoginForHousing.getNum().trim()); 
				validateCode.setText(code); 	
				HtmlPage logonPage= submitbt.click(); 
				if(null!=logonPage){
					String html=logonPage.asXml();
					if(html.contains("欢迎您")){
						changeLoginStatusSuccess(taskHousing, webClient);
				    	Thread.sleep(1000);
					}else{
						Document doc = Jsoup.parse(html);
						String errorMsg= doc.getElementsByClass("WTLoginError").get(0).text();  //获取页面中可能出现的错误信息
						//页面信息提示中可能存在返回这个字段，截取掉
//						String str="操作失败:进行身份校验时出错:密码错误,请检查! 返回";
						String[] split = errorMsg.split(" ");
						errorMsg=split[0];
						tracer.addTag("登录失败时，跳转页面后，页面上的提示信息是："+errorMsg, taskHousing.getTaskid());
						//通过调研，发现图片验证码输入错误，这几个公积金网站的提示都是一样的
						if(errorMsg.contains("您输入的验证码与图片不符")){
							captchaErrorCount++;
							 tracer.addTag("action.login.auth.imageErrorCount", "这是第"+captchaErrorCount+"次因图片验证码识别错误重新调用登录方法");
							 //图片验证码识别错误，重试三次登录
							 if(captchaErrorCount>3){
								 tracer.addTag("操作失败:进行身份校验时出错:您输入的验证码与图片不符"+captchaErrorCount, taskHousing.getTaskid());
								 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
								 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhasestatus());
								 taskHousing.setDescription("进行身份校验时出错:您输入的验证码与图片不符");
								 save(taskHousing);
							 }else{
								 login(messageLoginForHousing,taskHousing,loginUrl,vericodeUrl,fileSavePath);
							 }
						//身份证号输入有误的各网站提示
						}else if(errorMsg.contains("此身份证号无对应账户信息") || errorMsg.contains("个人证件号码") || 
								errorMsg.contains("该身份证号在系统中不存在或该账号已销户") || errorMsg.contains("身份证转换错误")
								|| errorMsg.contains("身份证号不正确") || errorMsg.contains("身份证号或公积金帐号不正确")
								|| errorMsg.contains("进行身份校验时出错:满足条件的注册用户数据 不存在")){
							 tracer.addTag("操作失败:进行身份校验时出错:账号输入有误！", taskHousing.getTaskid());
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							 taskHousing.setDescription(errorMsg);
							 save(taskHousing);
						//密码输入有误的各网站提示
						}else if(errorMsg.contains("您输入的密码有误") || errorMsg.contains("个人登录密码不正确") 
								|| errorMsg.contains("密码错误") || errorMsg.contains("用户密码不匹配")
								|| errorMsg.contains("密码错误请重试")){
							 tracer.addTag("进行身份校验时出错:您输入的密码有误，请重新输入！", taskHousing.getTaskid());
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
							 taskHousing.setDescription(errorMsg);
							 save(taskHousing);
						//其他错误
						}else if(errorMsg.contains("操作失败:系统错误") || errorMsg.contains("系统日终尚未结束") 
								|| errorMsg.contains("一台机器只能登陆一个用户")){
					    	 tracer.addTag(taskHousing.getTaskid()+"操作失败:出现了用户名或密码输入有误以外的错误",errorMsg);
					    	 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							 taskHousing.setDescription(errorMsg);
							 save(taskHousing);
						}else{
							tracer.addTag(taskHousing.getTaskid()+"登录失败，出现了调研时没有遇到的错误：",errorMsg);
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription(errorMsg);
							save(taskHousing);
						} 
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.login.taskid===>e",ExUtils.getEDetail(e));
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录失败，公积金网站系统繁忙，请稍后再试！");
			save(taskHousing);
		}
	}
}
