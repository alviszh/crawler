package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wuhu.HousingWuHuHtml;
import com.microservice.dao.entity.crawler.housing.wuhu.HousingWuHuPaydetails;
import com.microservice.dao.entity.crawler.housing.wuhu.HousingWuHuUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.wuhu.HousingWuHuHtmlRepository;
import com.microservice.dao.repository.crawler.housing.wuhu.HousingWuHuPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.wuhu.HousingWuHuUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuhu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuhu")
public class HousingFundWuHuService extends  HousingBasicService implements ICrawler {
	public static final Logger log = LoggerFactory.getLogger(HousingFundWuHuService.class);
	@Autowired
	public HousingWuHuHtmlRepository housingWuHuHtmlRepository;
	@Autowired
	public HousingWuHuPaydetailsRepository housingWuHuPaydetailsRepository;
	@Autowired
	public HousingWuHuUserInfoRepository housingWuHuUserInfoRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	// 登录业务层
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("HousingFundWuHuService.", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			JSONObject loginObject = JSONObject.fromObject(messageLoginForHousing);
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url="https://sso.ahzwfw.gov.cn/uccp-server/login?appCode=4fc73e5bcd794b08889f39ad2b89acde&service=https://uc.ewoho.com/provinceCas/login?whService=aHR0cDovL3d3dy5ld29oby5jb20vcGVyc29uYWxjZW50ZXIvbG9hZGhvdXNlZnVuZC5kbw";
			// 调用下面的getHtml方法
			WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest1);
			String asXml = page.asXml();
			Document doc = Jsoup.parse(asXml);
			Element elementById = doc.getElementById("legpsdFm");
			String lt = elementById.getElementsByAttributeValue("name", "lt").val();
			
			String execution = elementById.getElementsByAttributeValue("name", "execution").val();
			
			String _eventId = elementById.getElementsByAttributeValue("name", "_eventId").val();
			
			String platform = elementById.getElementsByAttributeValue("name", "platform").val();
			
			String loginType = elementById.getElementsByAttributeValue("name", "loginType").val();
			
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest.getRequestParameters().add(new NameValuePair("lt", lt));
			webRequest.getRequestParameters().add(new NameValuePair("execution", execution));
			webRequest.getRequestParameters().add(new NameValuePair("_eventId", _eventId));
			webRequest.getRequestParameters().add(new NameValuePair("platform", platform));
			webRequest.getRequestParameters().add(new NameValuePair("loginType", loginType));
			webRequest.getRequestParameters().add(new NameValuePair("credentialType", "PASSWORD"));
			webRequest.getRequestParameters().add(new NameValuePair("userType", "0"));
			webRequest.getRequestParameters().add(new NameValuePair("username", messageLoginForHousing.getNum()));
			webRequest.getRequestParameters().add(new NameValuePair("password", messageLoginForHousing.getPassword()));
			webRequest.getRequestParameters().add(new NameValuePair("random", ""));
			Page loginPage = webClient.getPage(webRequest);
			Thread.sleep(1500);
			String loginHtml = loginPage.getWebResponse().getContentAsString();
			if (loginHtml.contains("mainDiv")) {
				tracer.addTag("parser.housing.login.sucess", "登陆成功");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
				taskHousing.setLoginMessageJson(loginObject.toString());
				taskHousingRepository.save(taskHousing);
				List<String> datalist = new ArrayList<>();				
				Document docLogin = Jsoup.parse(loginHtml);
				Elements option = docLogin.getElementsByTag("option");
				for (Element element : option) {
					String text = element.val();
					datalist.add(text);
				}						
				for (String data : datalist) {
					// 公积金账户信息
					String accountUrl = "http://www.ewoho.com/personalcenter/getBaseInfoByUnit.do?perAccount=" + data;
					WebRequest requestSettings = new WebRequest(new URL(accountUrl), HttpMethod.GET);
					Page page1 = webClient.getPage(requestSettings);
					String contentAsString = page1.getWebResponse().getContentAsString();
				    HousingWuHuHtml housingWuHuHtml = new HousingWuHuHtml();
				    housingWuHuHtml.setHtml(contentAsString);
				    housingWuHuHtml.setPageCount(1);
				    housingWuHuHtml.setTaskid(messageLoginForHousing.getTask_id());
				    housingWuHuHtml.setType("对应的公积金账号为"+data);
				    housingWuHuHtml.setUrl(accountUrl);
					housingWuHuHtmlRepository.save(housingWuHuHtml); 	
					taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
					if (contentAsString.contains("houseFund")) {
						JSONObject jsonObject = JSONObject.fromObject(contentAsString);				
						String userAccount = jsonObject.getString("houseFund");
						JSONObject accountObject = JSONObject.fromObject(userAccount);	
						String username=accountObject.getString("NAME");//姓名
						String gender=accountObject.getString("GENDER");//性别
						String idnum=accountObject.getString("IDCARD");// 身份证
						String state=accountObject.getString("ACCOUNTSTATE");//账户状态
						String unitaccount=accountObject.getString("UNITACCOUNT");// 单位公积金账号
						String personaccount=accountObject.getString("PERSONACCOUNT");// 个人公积金账号
						String monthpay=accountObject.getString("MONTHPAY");// 月缴额
						String lastaccountdate=accountObject.getString("LASTACCOUNTDATE");// 最近时间		
						String  creditramount=accountObject.getString("CREDITORAMOUNT");// 最新到账金额
						String  company=accountObject.getString("UNITNAME");//  公司名称
						String acountamount=accountObject.getString("ACCOUNT_BALANCE");	//公积金账户总金额	
						String  foundloan=accountObject.getString("PERSONBANKACCOUNT");//  公积金贷款情况
						
						String userAccount2 = jsonObject.getString("userFoundAccount");
						JSONObject accountObject2 = JSONObject.fromObject(userAccount2);				
						String accountbalance=accountObject2.getString("ACCOUNT_BALANCE");
						
						HousingWuHuUserInfo userInfo=new HousingWuHuUserInfo(username, gender, idnum, state, unitaccount,
								unitaccount+personaccount, monthpay, lastaccountdate, creditramount, accountbalance,
								foundloan, company, acountamount, taskHousing.getTaskid());
						if (null !=userInfo) {
							housingWuHuUserInfoRepository.save(userInfo);						
							taskHousing.setUserinfoStatus(200);
							taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
							taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
							taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
							taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getError_code());
							save(taskHousing);
						}			
					}
					if (contentAsString.contains("allLoanList")) {
						List<HousingWuHuPaydetails>  housingWuHuPaydetails=new ArrayList<HousingWuHuPaydetails>();
						JSONObject jsonObject = JSONObject.fromObject(contentAsString);				
						String detailsList = jsonObject.getString("allLoanList");
						JSONArray listArray = JSONArray.fromObject(detailsList);
						for (int i = 0; i < listArray.size(); i++) {
							 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
							 HousingWuHuPaydetails housingWuHuPaydetail=new HousingWuHuPaydetails();
							 String personaccount=data;//	个人公积金账号
							 String accountdate=listArrayObjs.getString("accounting_date");//	收支日期
							 String type=listArrayObjs.getString("summary");//	收支类型
							 String creditoramount=listArrayObjs.getString("creditor_amount");//	金额
							 String accountbalance=listArrayObjs.getString("accumulate_balance");//	账户余额
							 housingWuHuPaydetail.setPersonaccount(personaccount);
							 housingWuHuPaydetail.setAccountdate(accountdate);
							 housingWuHuPaydetail.setType(type);
							 housingWuHuPaydetail.setCreditoramount(creditoramount);
							 housingWuHuPaydetail.setAccountbalance(accountbalance);
							 housingWuHuPaydetail.setTaskid(taskHousing.getTaskid());
							 housingWuHuPaydetails.add(housingWuHuPaydetail);
						}
						if (null !=housingWuHuPaydetails && !housingWuHuPaydetails.isEmpty()) {
							housingWuHuPaydetailsRepository.saveAll(housingWuHuPaydetails);
							taskHousing.setPaymentStatus(200);
							taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
							taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
							taskHousing.setDescription("数据采集中，【流水信息】采集成功");
							taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getError_code());
							save(taskHousing);
						}else{
							taskHousing.setPaymentStatus(201);
							taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
							taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhasestatus());
							taskHousing.setDescription("数据采集中，【流水信息】采集成功");
							taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getError_code());
							save(taskHousing);
						}
					}
				}			
				updateTaskHousing(taskHousing.getTaskid());	
      		 } else if(loginHtml.contains("用户名或者密码错误")){
				tracer.addTag("parser.housing.login.fail", "用户名或者密码错误");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription("用户名或者密码错误");
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
				taskHousing.setLoginMessageJson(loginObject.toString());
				taskHousingRepository.save(taskHousing);
			}else {
				tracer.addTag("parser.housing.login.fail", "登陆失败！异常错误！");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getError_code());
				taskHousing.setLoginMessageJson(loginObject.toString());
				taskHousingRepository.save(taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
	
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}