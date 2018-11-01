package app.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.hulunbeier.HousingHuLunBeiErHtml;
import com.microservice.dao.entity.crawler.housing.hulunbeier.HousingHuLunBeiErPay;
import com.microservice.dao.entity.crawler.housing.hulunbeier.HousingHuLunBeiErUserInfo;
import com.microservice.dao.repository.crawler.housing.hulunbeier.HousingHuLunBeiErHtmlRepository;
import com.microservice.dao.repository.crawler.housing.hulunbeier.HousingHuLunBeiErPayRepository;
import com.microservice.dao.repository.crawler.housing.hulunbeier.HousingHuLunBeiErUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingfundHuLunBeiErParser;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.hulunbeier"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.hulunbeier"})
public class HousingfundHuLunBeiErService extends HousingBasicService implements ICrawler,ICrawlerLogin{

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingfundHuLunBeiErParser housingfundHuLunBeiErParser;
	@Autowired
	private HousingHuLunBeiErUserInfoRepository housingHuLunBeiErUserInfoRepository;
	@Autowired
	private HousingHuLunBeiErPayRepository housingHuLunBeiErPayRepository;
	@Autowired
	private HousingHuLunBeiErHtmlRepository housingHuLunBeiErHtmlRepository;

	static String num;
	/**
	 *   登录
	 * @param messageLoginForHousing
	 * @param taskHousing
	 */
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("parser.login.taskid", messageLoginForHousing.getTask_id());
		HousingHuLunBeiErHtml housingHuLunBeiErHtml = new HousingHuLunBeiErHtml();
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.zfgjj.com.cn/perlogin.jhtml";
		try {
			HtmlPage page = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
			HtmlTextInput cardno = (HtmlTextInput) page.getElementById("cardno");//身份证
			HtmlPasswordInput perpwd = (HtmlPasswordInput) page.getElementById("perpwd");//密码
			HtmlTextInput verify = (HtmlTextInput)page.getFirstByXPath("//input[@name='verify']");//图片验证码
			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='guestbookCaptcha']");//图片
			String verifycode = chaoJiYingOcrService.getVerifycode(image, "1902");

			cardno.setText(messageLoginForHousing.getNum());
			perpwd.setText(messageLoginForHousing.getPassword());
			verify.setText(verifycode);
			HtmlButton sub = (HtmlButton) page.getElementById("sub");//登录
			Page click = sub.click();
			String url7 = "http://www.zfgjj.com.cn/GJJQuery?"
					+ "tranCode=112805"
					+ "&task="
					+ "&certinum="+messageLoginForHousing.getNum()
					+ "&flag=0";
			Page page3 = LoginAndGetCommon.gethtmlPost(webClient, null, url7);
			String string = page3.getWebResponse().getContentAsString();
			num = JSONArray.fromObject(string).getJSONObject(0).getString("accnum");
			String html = click.getWebResponse().getContentAsString();
			housingHuLunBeiErHtml.setHtml(page.getWebResponse().getContentAsString());
			housingHuLunBeiErHtml.setUrl(url);
			housingHuLunBeiErHtml.setPagenumber(1);
			housingHuLunBeiErHtml.setType("登录页面");
			housingHuLunBeiErHtml.setTaskid(messageLoginForHousing.getTask_id());
			housingHuLunBeiErHtmlRepository.save(housingHuLunBeiErHtml);
			if(html.indexOf("个人账户基本信息查询-呼伦贝尔市住房公积金管理中心")!=-1){
				System.out.println("登录成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setCookies(cookies);
				taskHousing.setPassword(messageLoginForHousing.getPassword());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				save(taskHousing);
				return taskHousing;
			}else{
				System.out.println("登录失败");
				String alertMsg = WebCrawler.getAlertMsg();
				if(alertMsg.length()>0){
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
					taskHousing.setPassword(messageLoginForHousing.getPassword());
					taskHousing.setDescription(alertMsg);
					save(taskHousing);
					return taskHousing;
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
					taskHousing.setPassword(messageLoginForHousing.getPassword());
					taskHousing.setDescription("查询密码输入有误，必须为6位数字");
					save(taskHousing);
					return taskHousing;
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.login.taskid", e.getMessage());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword());
			taskHousing.setError_message("登录页面出问题");
			save(taskHousing);
			return taskHousing;
		}
	}

	/**
	 * 爬取个人信息
	 * @param taskHousing
	 * @param webClient
	 * @throws Exception
	 */
	public void getuserinfo(TaskHousing taskHousing, WebClient webClient){
		try {
			tracer.addTag("parser.getuser.gettaskid", taskHousing.getTaskid());
			String url = "http://www.zfgjj.com.cn/GJJQuery?"
					+ "tranCode=112813"
					+ "&task="
					+ "&accnum="+num;
			HousingHuLunBeiErHtml housingHuLunBeiErHtml = new HousingHuLunBeiErHtml();
			Page page = LoginAndGetCommon.gethtmlPost(webClient, null, url);
			String html = page.getWebResponse().getContentAsString();
			housingHuLunBeiErHtml.setHtml(html);
			housingHuLunBeiErHtml.setUrl(url);
			housingHuLunBeiErHtml.setPagenumber(1);
			housingHuLunBeiErHtml.setType("个人信息页面");
			housingHuLunBeiErHtml.setTaskid(taskHousing.getTaskid());
			housingHuLunBeiErHtmlRepository.save(housingHuLunBeiErHtml);
			if(html!=null){
				HousingHuLunBeiErUserInfo user = housingfundHuLunBeiErParser.getuserinfo(html);
				if(user!=null){
					user.setTaskid(taskHousing.getTaskid());
					housingHuLunBeiErUserInfoRepository.save(user);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(200);
					save(taskHousing);
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setError_message("个人信息解析有误！");
					taskHousing.setUserinfoStatus(201);
					save(taskHousing);
				}
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setError_message("个人信息网页有问题");
				taskHousing.setUserinfoStatus(500);
				save(taskHousing);
			}
			updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			tracer.addTag("parser.getuser", "个人信息网页"+e.getMessage());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskHousing.setError_message("个人信息网页无法显示");
			taskHousing.setUserinfoStatus(404);
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
		}
	}
	/**
	 * 流水
	 * @param taskHousing
	 * @param webClient
	 */
	public void getpay(TaskHousing taskHousing, WebClient webClient) {
		tracer.addTag("parser.getpay.gettaskid", taskHousing.getTaskid());
		//两年之前
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();	
		calendar.setTime(new Date());
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-2);
		String format = dateFormat.format(calendar.getTime());
		//当前日期
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(d);
		try {
			String url = "http://www.zfgjj.com.cn/GJJQuery?"
					+ "tranCode=112814"
					+ "&task=ftp"
					+ "&accnum="+num
					+ "&begdate="+format
					+ "&enddate="+dateNowStr;
			Page page3 = LoginAndGetCommon.gethtmlPost(webClient, null, url);
			String html = page3.getWebResponse().getContentAsString();
			HousingHuLunBeiErHtml housingHuLunBeiErHtml = new HousingHuLunBeiErHtml();
			housingHuLunBeiErHtml.setHtml(html);
			housingHuLunBeiErHtml.setUrl(url);
			housingHuLunBeiErHtml.setPagenumber(1);
			housingHuLunBeiErHtml.setType("流水信息页面");
			housingHuLunBeiErHtml.setTaskid(taskHousing.getTaskid());
			housingHuLunBeiErHtmlRepository.save(housingHuLunBeiErHtml);
			if(html.indexOf("accnum")!=-1){
				List<HousingHuLunBeiErPay> getpay = housingfundHuLunBeiErParser.getpay(html,taskHousing.getTaskid());
				if(getpay!=null){
					housingHuLunBeiErPayRepository.saveAll(getpay);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(200);
					save(taskHousing);
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setError_message("流水信息无数据");
					taskHousing.setPaymentStatus(201);
					save(taskHousing);
				}
			}else if(html.indexOf("msg")!=-1){
				String error = JSONObject.fromObject(html).getString("msg");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setError_message(error);
				taskHousing.setPaymentStatus(404);
				save(taskHousing);
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setError_message("流水信息有问题");
				taskHousing.setPaymentStatus(500);
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("parser.getpay", "个人信息网页"+e.getMessage());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			taskHousing.setError_message("流水信息网页无法显示");
			taskHousing.setPaymentStatus(404);
			save(taskHousing);
		}
		
	}
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		getuserinfo(taskHousing,webClient);
		getpay(taskHousing,webClient);
		TaskHousing updateTaskHousing = updateTaskHousing(taskHousing.getTaskid());
		return updateTaskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		return null; 
	}
}
