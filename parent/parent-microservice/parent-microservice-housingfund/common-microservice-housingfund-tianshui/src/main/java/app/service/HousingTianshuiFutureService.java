package app.service;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiAccountInfo;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiHtml;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiPayDetailed;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiPayRecord;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiUserInfo;
import com.microservice.dao.repository.crawler.housing.tianshui.HousingTianshuiAccountInfoRepository;
import com.microservice.dao.repository.crawler.housing.tianshui.HousingTianshuiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.tianshui.HousingTianshuiPayDetailedRepository;
import com.microservice.dao.repository.crawler.housing.tianshui.HousingTianshuiPayRecordRepository;
import com.microservice.dao.repository.crawler.housing.tianshui.HousingTianshuiUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.HousingTianshuiParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.tianshui")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.tianshui")
public class HousingTianshuiFutureService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private HousingTianshuiParse housingTianshuiParse;

	@Autowired
	private HousingTianshuiUserInfoRepository housingTianshuiUserInfoRepository;
	
	@Autowired
	private HousingTianshuiAccountInfoRepository housingTianshuiAccountInfoRepository;
	
	@Autowired
	private HousingTianshuiPayRecordRepository housingTianshuiPayRecordRepository;
	
	@Autowired
	private HousingTianshuiPayDetailedRepository housingTianshuiPayDetailedRepository;
	

	@Autowired
	private HousingTianshuiHtmlRepository housingTianshuiHtmlRepository;

	@Autowired
	private TracerLog tracer;

	/**
	 * 登录
	 * 
	 * @param messageLoginForHousing
	 * @param taskHousing
	 * @return
	 * @throws Exception
	 */
	public TaskHousing loginTwo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing,Integer count) {
		tracer.addTag("HousingTianshuiFutureService.login", messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		try {
			if (null != taskHousing) {
				WebClient webClient = WebCrawler.getInstance().getNewWebClient();
				String url = "http://www.tsgjj.gov.cn:7003/login.jsp";
				HtmlPage searchPage = getHtmlPage(webClient, url, null, null);
				if (null != searchPage) {
					HtmlListItem gr = searchPage.getFirstByXPath("//*[@id=\"gr\"]");
					gr.click();
					String adrOptionString = "#aType option[value='4']";
					if (StatusCodeLogin.ACCOUNT_NUM.equals(messageLoginForHousing.getLogintype())) {
						adrOptionString = "#aType option[value='3']";
					} else if (StatusCodeLogin.CO_BRANDED_CARD.equals(messageLoginForHousing.getLogintype())) {
						adrOptionString = "#aType option[value='5']";
					}
					HtmlOption htmlOption = (HtmlOption) searchPage.querySelector(adrOptionString);
					htmlOption.click();
					String verifycode = "1902";
					
					HtmlImage image = searchPage.getFirstByXPath("//img[@id='codeimg']");
					try {
						verifycode = chaoJiYingOcrService.getVerifycode(image, "1902");
					} catch (Exception e) {
						tracer.addTag("HousingTianshuiFutureService.login.code.ERROR", taskHousing.getTaskid() + "-----ERROR:" + e);
						e.printStackTrace();
					}
					HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[id='j_username']");
					if (inputUserName == null) {
						throw new Exception("username input text can not found :" + "input[id='j_username']");
					} else {
						inputUserName.reset();
						inputUserName.setText(messageLoginForHousing.getNum());
					}
					HtmlPasswordInput inputpassword = (HtmlPasswordInput) searchPage
							.querySelector("input[id='j_password']");
					if (inputpassword == null) {
						throw new Exception("password input text can not found :" + "input[id='j_password']");
					} else {
						inputpassword.reset();
						inputpassword.setText(messageLoginForHousing.getPassword());
					}
					HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("input[id='checkCode']");
					if (inputuserjym == null) {
						throw new Exception("code input text can not found :" + "input[id='checkCode']");
					} else {
						inputuserjym.reset();
						inputuserjym.setText(verifycode);
					}
					HtmlAnchor loginA = (HtmlAnchor) searchPage.querySelector("a[onclick='login();']");
					if (loginA == null) {
						throw new Exception("login button can not found : null");
					} else {
						searchPage = loginA.click();
						String login = searchPage.getWebResponse().getContentAsString();
						tracer.addTag("HousingTianshuiFutureService.login",
								messageLoginForHousing.getTask_id() + login);
						HtmlSpan htmlSpan = (HtmlSpan) searchPage.querySelector("#error");
						if (htmlSpan == null) {
							String cookieJson = CommonUnit.transcookieToJson(webClient);
							
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
							taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
							taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
							
							taskHousing.setCookies(cookieJson);
							taskHousing.setLoginMessageJson(jsonObject.toString());
							save(taskHousing);
							return taskHousing;
						} else {
							String textContent = htmlSpan.getTextContent();
							if (null != textContent && !textContent.equals("")) {
								
								//验证码输入错误
								if (textContent.contains("验证码")) {
									tracer.addTag("HousingHanzhongFutureService.login--失败次数:" + count,
											messageLoginForHousing.getTask_id());
									if (count < 4) {
										loginTwo(messageLoginForHousing, taskHousing, ++count);
									} else {
										taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
										taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
										taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
										taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
										taskHousing.setError_message(textContent);

										taskHousing.setLoginMessageJson(jsonObject.toString());
										save(taskHousing);
										return taskHousing;
									}
								}else{
									taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
									taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
									taskHousing.setDescription(textContent);
									taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
									taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());

									taskHousing.setLoginMessageJson(jsonObject.toString());
									save(taskHousing);
									return taskHousing;
								}
							} else {
								tracer.addTag("HousingTianshuiFutureService.login",
										messageLoginForHousing.getTask_id() + "登录页获取超时！");
								taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
								taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhasestatus());
								taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
								taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
								
								
								taskHousing.setLoginMessageJson(jsonObject.toString());
								// 登录失败状态存储
								save(taskHousing);
								return taskHousing;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingTianshuiFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
			taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
			
			taskHousing.setLoginMessageJson(jsonObject.toString());
			// 登录失败状态存储
			save(taskHousing);
		}
		return null;
	}

	public static HtmlPage getHtmlPage(WebClient webClient, String url, HttpMethod type, String body) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {

			return searchPage;
		}

		return null;
	}

	public WebClient addcookie(TaskHousing taskHousing) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}

	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getUserInfo(TaskHousing taskHousing) {
		tracer.addTag("HousingTianshuiFutureService.getUserInfo", taskHousing.getTaskid());
		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "http://www.tsgjj.gov.cn:7003/per/perInfoModifyAction.do";
			Page page = getPage(webClient, url, null, null, null, null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingTianshuiFutureService.getUserInfo---用户信息" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				HousingTianshuiHtml housingTianshuiHtml = new HousingTianshuiHtml(taskHousing.getTaskid(), "个人基本信息",
						"1", url, html);
				housingTianshuiHtmlRepository.save(housingTianshuiHtml);
				HousingTianshuiUserInfo housingTianshuiUserInfo = housingTianshuiParse.htmlUserInfoParser(html, taskHousing);
				housingTianshuiUserInfoRepository.save(housingTianshuiUserInfo);
				tracer.addTag("HousingTianshuiFutureService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingTianshuiFutureService.getUserInfo is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("HousingTianshuiFutureService.getUserInfo---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			e.printStackTrace();
		}
		return new AsyncResult<String>("200");
	}
	
	
	
	/**
	 * 个人缴存账户
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getAccountInfo(TaskHousing taskHousing) {
		tracer.addTag("HousingTianshuiFutureService.getAccountInfo", taskHousing.getTaskid());
		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "http://www.tsgjj.gov.cn:7003/per/perAccDetailsQueryAction!getPerAccQueryDetails.do";
			Page page = getPage(webClient, url, null, null, null, null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingTianshuiFutureService.getAccountInfo---个人缴存账户" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				HousingTianshuiHtml housingTianshuiHtml = new HousingTianshuiHtml(taskHousing.getTaskid(), "个人缴存账户",
						"1", url, html);
				housingTianshuiHtmlRepository.save(housingTianshuiHtml);
				List<HousingTianshuiAccountInfo> list = housingTianshuiParse.htmlAccountInfoParser(html, taskHousing);
				housingTianshuiAccountInfoRepository.saveAll(list);
				tracer.addTag("HousingChengduFutureService.getAccountInfo---个人缴存账户",
						"用户信息源码表入库!" + taskHousing.getTaskid());
			} else {
				tracer.addTag("HousingTianshuiFutureService.getAccountInfo is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("HousingTianshuiFutureService.getAccountInfo---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return new AsyncResult<String>("200");
	}
	
	/**
	 * 缴存记录查询
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getPayRecord(TaskHousing taskHousing,String data) {
		tracer.addTag("HousingTianshuiFutureService.getPayRecord", taskHousing.getTaskid());
		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "http://www.tsgjj.gov.cn:7003/per/perDepositQueryAction!getPerDepDetails.do";
			
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("dto['year']", data));
			paramsList.add(new NameValuePair("gridInfo['dataList_limit']", "400"));
			paramsList.add(new NameValuePair("gridInfo['dataList_start']", "0"));
			
			Page page = getPage(webClient, url, null, paramsList, null, null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingTianshuiFutureService.getPayRecord---缴存记录查询" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				HousingTianshuiHtml housingTianshuiHtml = new HousingTianshuiHtml(taskHousing.getTaskid(), "缴存记录查询",
						data, url, html);
				housingTianshuiHtmlRepository.save(housingTianshuiHtml);
				List<HousingTianshuiPayRecord> list = housingTianshuiParse.htmlPayRecordParser(html, taskHousing);
				housingTianshuiPayRecordRepository.saveAll(list);
				
				tracer.addTag("HousingTianshuiFutureService.getPayRecord---缴存记录查询",  taskHousing.getTaskid());
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingTianshuiFutureService.getPayRecord is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingTianshuiFutureService.getPayRecord---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		return new AsyncResult<String>("200");
	}
	
	
	/**
	 * 帐户明细查询
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getPayDetailed(TaskHousing taskHousing,String data) {
		tracer.addTag("HousingTianshuiFutureService.getPayDetailed", taskHousing.getTaskid());
		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "http://www.tsgjj.gov.cn:7003/per/perAccDlistQueryAction!getPerAccDlist.do";
			
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("dto['year']", data));
			paramsList.add(new NameValuePair("gridInfo['dataList_limit']", "400"));
			paramsList.add(new NameValuePair("gridInfo['dataList_start']", "0"));
			
			Page page = getPage(webClient, url, null, paramsList, null, null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingTianshuiFutureService.getPayDetailed---帐户明细查询" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				HousingTianshuiHtml housingTianshuiHtml = new HousingTianshuiHtml(taskHousing.getTaskid(), "帐户明细查询",
						data, url, html);
				housingTianshuiHtmlRepository.save(housingTianshuiHtml);
				List<HousingTianshuiPayDetailed> list = housingTianshuiParse.htmlPayDetailedParser(html, taskHousing);
				housingTianshuiPayDetailedRepository.saveAll(list);
				tracer.addTag("HousingTianshuiFutureService.getPayDetailed---帐户明细查询",  taskHousing.getTaskid());
			} else {
				tracer.addTag("HousingTianshuiFutureService.getPayDetailed is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingTianshuiFutureService.getPayDetailed---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		return new AsyncResult<String>("200");
	}
	
	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, String url, HttpMethod type, List<NameValuePair> paramsList, String code,
			String body, Map<String, String> map) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}
		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		if (null != code && !"".equals(code)) {
			webRequest.setCharset(Charset.forName(code));
		}
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			loginTwo(messageLoginForHousing, taskHousing,1);
		} catch (Exception e) {
			tracer.addTag("HousingHanzhongFutureService.login:" , messageLoginForHousing.getTask_id()+"---ERROR:"+e.toString());
			e.printStackTrace();
		}
		return taskHousing;
	}


}