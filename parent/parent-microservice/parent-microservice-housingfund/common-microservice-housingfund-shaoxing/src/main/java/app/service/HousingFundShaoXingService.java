package app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.shaoxing.HousingShaoXingBase;
import com.microservice.dao.entity.crawler.housing.shaoxing.HousingShaoXingHtml;
import com.microservice.dao.entity.crawler.housing.shaoxing.HousingShaoXingPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.shaoxing.HousingShaoXingBaseRepository;
import com.microservice.dao.repository.crawler.housing.shaoxing.HousingShaoXingHtmlRepository;
import com.microservice.dao.repository.crawler.housing.shaoxing.HousingShaoXingPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;
import com.module.ocr.utils.ChaoJiYingUtils;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.shaoxing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.shaoxing")
public class HousingFundShaoXingService extends AbstractChaoJiYingHandler implements ICrawlerLogin {
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundShaoXingService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingShaoXingHtmlRepository housingShaoXingHtmlRepository;
	@Autowired
	public HousingShaoXingBaseRepository housingShaoXingBaseRepository;
	@Autowired
	public HousingShaoXingPayRepository housingShaoXingPayRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Autowired
	public HousingBasicService housingBasicService;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static final String OCR_FILE_PATH = "D:\\img";
	private String uuid = UUID.randomUUID().toString();
	@Value("${ak}")
	String ak;
	@Value("${sk}")
	String sk;

	// 登录业务层
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			// 获取图片验证码
			String loginurl2 = "https://115.239.233.150:9090/wt/captcha?0.2800135684138121";
			WebClient loginwebClient2 = WebCrawler.getInstance().getWebClient();
			Page loginsearchPage2 = null;
			try {
				loginsearchPage2 = loginwebClient2.getPage(loginurl2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			InputStream contentAsStream = null;
			String code = null;
			try {
				contentAsStream = loginsearchPage2.getWebResponse().getContentAsStream();
				saveFile(contentAsStream, OCR_FILE_PATH + "\\" + uuid + ".png");

				code = callChaoJiYingService(OCR_FILE_PATH + "\\" + uuid + ".png", "3005");
				System.out.println(code);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			String num = messageLoginForHousing.getNum();
			String password = messageLoginForHousing.getPassword();
//			String num = messageLoginForHousing.getNum().trim();
//			String password = messageLoginForHousing.getPassword().trim();
			String endpassword = encryptedPhone(password);
			System.out.println("身份证号码："+num);
			System.out.println("密码："+password);
			System.out.println("处理之后的密码："+endpassword);
			
			String url = "https://115.239.233.150:9090/wt/grlogin";
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("force_and_dxyz", "1"));
			requestSettings.getRequestParameters().add(new NameValuePair("grloginDxyz", "0"));
			requestSettings.getRequestParameters().add(new NameValuePair("username", num));
			requestSettings.getRequestParameters()
					.add(new NameValuePair("password", endpassword));
			requestSettings.getRequestParameters().add(new NameValuePair("force", ""));
			requestSettings.getRequestParameters().add(new NameValuePair("captcha", code));
			requestSettings.getRequestParameters().add(new NameValuePair("sliderCaptcha", ""));
			HtmlPage loginedPage = loginwebClient2.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			//结果五:系统版本与数据库版本两者不一致！
			System.out.println("登陆结果-------" + contentAsString); 

			if (contentAsString.contains("123.126.87.169")) {
				System.out.println("进入强制登录....");
				// 查询按钮
				HtmlElement button2 = (HtmlElement) loginedPage.getFirstByXPath("//button[@id='qzdl']");
				// 验证码输入框
				HtmlTextInput trand2 = (HtmlTextInput) loginedPage.getFirstByXPath("//input[@id='force_captcha']");
				// 图片
				HtmlImage randImage2 = (HtmlImage) loginedPage.getFirstByXPath("//*[@id='captcha_div']/div[1]/img");

				String verifycode2 = chaoJiYingOcrService.getVerifycode(randImage2, "3005");
				trand2.setText(verifycode2);
				Page click = button2.dblClick();

				Thread.sleep(10000);

				String contentAsString2 = click.getWebResponse().getContentAsString();
				System.out.println("强制登录的结果-----" + contentAsString2);

				if (contentAsString2.contains("123.126.87.169")) {
					System.out.println("再次进入强制登录....");
					// 查询按钮
					HtmlElement button21 = (HtmlElement) loginedPage.getFirstByXPath("//button[@id='qzdl']");
					// 验证码输入框
					HtmlTextInput trand21 = (HtmlTextInput) loginedPage.getFirstByXPath("//input[@id='force_captcha']");
					// 图片
					HtmlImage randImage21 = (HtmlImage) loginedPage
							.getFirstByXPath("//*[@id='captcha_div']/div[1]/img");

					String verifycode21 = chaoJiYingOcrService.getVerifycode(randImage21, "3005");
					trand21.setText(verifycode21);
					Page click1 = button21.dblClick();

					Thread.sleep(10000);

					String contentAsString21 = click1.getWebResponse().getContentAsString();
					System.out.println("第二次强制登录的结果-----" + contentAsString21);
				}

			} else if (contentAsString.contains("<title>绍兴市住房公积金网上服务大厅</title>")) {
				System.out.println("登陆成功！");
				String cookies = CommonUnit.transcookieToJson(loginwebClient2);
				taskHousing.setCookies(cookies);
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
				taskHousingRepository.save(taskHousing);

			} else {
				System.out.println("登陆失败！异常错误！");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getError_code());
				taskHousingRepository.save(taskHousing);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	// 爬取数据的业务层
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		
		 // 基本信息请求
		 String jbxx =
		 "https://115.239.233.150:9090/wt/jcr/jcrkhxxcx_mh.service";
		 try {
		 WebRequest requestSettings = new WebRequest(new URL(jbxx),
		 HttpMethod.POST);
		 requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		 requestSettings.getRequestParameters().add(new NameValuePair("ffbm",
		 "01"));
		 requestSettings.getRequestParameters().add(new NameValuePair("ywfl",
		 "01"));
		 requestSettings.getRequestParameters().add(new NameValuePair("ywlb",
		 "99"));
		 requestSettings.getRequestParameters().add(new NameValuePair("cxlx",
		 "01"));
		 requestSettings.getRequestParameters().add(new NameValuePair("grxx",
		 "grbh"));
		 Page page = webClient.getPage(requestSettings);
		
		 String contentAsString = page.getWebResponse().getContentAsString();
		
		 HousingShaoXingHtml housingShaoXingHtml = new HousingShaoXingHtml();
		 housingShaoXingHtml.setHtml(contentAsString + "");
		 housingShaoXingHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
		 housingShaoXingHtml.setType("基本信息");
		 housingShaoXingHtml.setUrl(jbxx);
		 housingShaoXingHtmlRepository.save(housingShaoXingHtml);
		
		 ////////////////////////////////////////////////////////个人信息//////////////////////////////////
		
		 if (contentAsString.contains("\"success\":true")) {
		 System.out.println("获取基本信息1成功！下面开始解析......");
		 JSONObject json = JSONObject.fromObject(contentAsString);
		 String results = json.getString("results").trim();
		 JSONArray jsonArray = JSONArray.fromObject(results);
		 for (int i = 0; i < jsonArray.size(); i++) {
		 String string = jsonArray.get(i).toString();
		 JSONObject json2 = JSONObject.fromObject(string);
		 HousingShaoXingBase housingShaoXingBase = new HousingShaoXingBase();
		 housingShaoXingBase.setTaskid(messageLoginForHousing.getTask_id().trim());
		 // 姓名
		 String xingming = json2.getString("xingming").trim();
		 System.out.println("姓名------" + xingming);
		 housingShaoXingBase.setXingming(xingming);
		 // 证件类型
		 String zjlx = json2.getString("zjlx").trim();
		 System.out.println("证件类型------" + zjlx);
		 housingShaoXingBase.setZjlx(zjlx);
		 // 固定电话号码
		 String gddhhm = json2.getString("gddhhm").trim();
		 System.out.println("固定电话号码------" + gddhhm);
		 housingShaoXingBase.setGddhhm(gddhhm);
		 // 家庭住址
		 String jtzz = json2.getString("jtzz").trim();
		 System.out.println("家庭住址------" + jtzz);
		 housingShaoXingBase.setJtzz(jtzz);
		 // 出生年月
		 String csny = json2.getString("csny").trim();
		 System.out.println("出生年月------" + csny);
		 housingShaoXingBase.setCsny(csny);
		 // 证件号码
		 String zjhm = json2.getString("zjhm").trim();
		 System.out.println("证件号码------" + zjhm);
		 housingShaoXingBase.setZjhm(zjhm);
		 // 邮政编码
		 String yzbm = json2.getString("yzbm").trim();
		 System.out.println("邮政编码------" + yzbm);
		 housingShaoXingBase.setYzbm(yzbm);
		 // 婚姻状况
		 String hyzk = json2.getString("hyzk").trim();
		 System.out.println("婚姻状况------" + hyzk);
		 housingShaoXingBase.setHyzk(hyzk);
		 // 性别
		 String xingbie = json2.getString("xingbie").trim();
		 System.out.println("性别------" + xingbie);
		 housingShaoXingBase.setXingbie(xingbie);
		 // 手机号码
		 String sjhm = json2.getString("sjhm").trim();
		 System.out.println("手机号码------" + sjhm);
		 housingShaoXingBase.setSjhm(sjhm);
		 // 家庭月收入
		 String jtysr = json2.getString("jtysr").trim();
		 System.out.println("家庭月收入------" + jtysr);
		 housingShaoXingBase.setJtysr(jtysr);
		 // 开户日期
		 String djrq = json2.getString("djrq").trim();
		 System.out.println("开户日期------" + djrq);
		 housingShaoXingBase.setDjrq(djrq);
		 // 个人缴存基数
		 String grjcjs = json2.getString("grjcjs").trim();
		 System.out.println("个人缴存基数------" + grjcjs);
		 housingShaoXingBase.setGrjcjs(grjcjs);
		 // 个人存款账户号码
		 String grckzhhm = json2.getString("grckzhhm").trim();
		 System.out.println("个人存款账户号码------" + grckzhhm);
		 housingShaoXingBase.setGrckzhhm(grckzhhm);
		 // 单位名称
		 String dwmc = json2.getString("dwmc").trim();
		 System.out.println("单位名称------" + dwmc);
		 housingShaoXingBase.setDwmc(dwmc);
		 // 月缴存额
		 String yjce = json2.getString("gryjce").trim() +
		 json2.getString("dwyjce").trim();
		 System.out.println("月缴存额------" + yjce);
		 housingShaoXingBase.setYjce(yjce);
		 // 缴存比例
		 String jcbl = json2.getString("jcbl").trim();
		 System.out.println("缴存比例------" + jcbl);
		 housingShaoXingBase.setJcbl(jcbl);
		 // 个人存款账户开户银行名称
		 String grckzhkhyhmc = json2.getString("grckzhkhyhmc").trim();
		 System.out.println("个人存款账户开户银行名称------" + grckzhkhyhmc);
		 housingShaoXingBase.setGrckzhkhyhmc(grckzhkhyhmc);
		 // 月补贴金额
		 String ybtje = json2.getString("ybtje").trim();
		 System.out.println("月补贴金额------" + ybtje);
		 housingShaoXingBase.setYbtje(ybtje);
		 // 补贴比例
		 String btbl = json2.getString("zfbtbl").trim();
		 System.out.println("补贴比例------" + btbl);
		 housingShaoXingBase.setBtbl(btbl);
		
		 housingShaoXingBaseRepository.save(housingShaoXingBase);
		 }
		 }
		
		 taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",
		 200, taskHousing.getTaskid());
		
		 } catch (Exception e) {
		 e.printStackTrace();
		 }

		////////////////////////////////////// 流水信息////////////////////////////////////////
		try {

			// 数据的总条数请求
			String jcmx1 = "https://115.239.233.150:9090/wt/jcr/jcrxxcxzhmxcx_.service";
			WebRequest requestSettings1 = new WebRequest(new URL(jcmx1), HttpMethod.POST);
			requestSettings1.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings1.getRequestParameters().add(new NameValuePair("ffbm", "01"));
			requestSettings1.getRequestParameters().add(new NameValuePair("ywfl", "01"));
			requestSettings1.getRequestParameters().add(new NameValuePair("ywlb", "99"));
			requestSettings1.getRequestParameters().add(new NameValuePair("blqd", "wt_02"));
			requestSettings1.getRequestParameters().add(new NameValuePair("ksrq", "2014-01-01"));
			requestSettings1.getRequestParameters().add(new NameValuePair("jsrq", "2018-03-06"));
			requestSettings1.getRequestParameters().add(new NameValuePair("grxx", "0100000267272"));
			requestSettings1.getRequestParameters().add(new NameValuePair("fontSize", "13px"));
			requestSettings1.getRequestParameters().add(new NameValuePair("pageNum", "1"));
			requestSettings1.getRequestParameters().add(new NameValuePair("pageSize", "10"));
			Page page1 = webClient.getPage(requestSettings1);
			String contentAsString = page1.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			JSONObject object = JSONObject.fromObject(contentAsString);
			String totalcount = object.getString("totalcount").trim();
			System.out.println("总条数：" + totalcount);
			// 计算出识别图片的张数
			int total = Integer.parseInt(totalcount) / 10;
			int total2 = Integer.parseInt(totalcount) % 10;
			String pagesize = "";
			if (total2 == 0) {
				pagesize = total + "";
			} else {
				pagesize = total + 1 + "";
			}
			System.out.println("总共多少页数据：" + pagesize + "页");
			// 图片的相对路径
			String imagePath = "";
			for (int i = 1; i < total + 1; i++) {
				String Stringi = i + "";
				// 流水请求
				String jcmx = "https://115.239.233.150:9090/wt/jcr/jcrxxcxzhmxcx_.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2011-01-01&jsrq=2018-03-06&grxx=0100000267272&fontSize=13px&pageNum="
						+ Stringi + "&pageSize=10&totalcount=" + totalcount + "&pages=" + pagesize
						+ "&random=0.11132938976460993";
				WebRequest requestSettings = new WebRequest(new URL(jcmx), HttpMethod.GET);
				Page page = webClient.getPage(requestSettings);

				imagePath = getImagePath(page);

				System.out.println("图片路径：" + imagePath);

				HousingShaoXingHtml housingShaoXingHtml1 = new HousingShaoXingHtml();
				housingShaoXingHtml1.setHtml("图片流");
				housingShaoXingHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
				housingShaoXingHtml1.setType("流水信息");
				housingShaoXingHtml1.setUrl(jcmx);
				housingShaoXingHtmlRepository.save(housingShaoXingHtml1);

				// 解析本地图片调用百度api

				String token = getAuth();
				System.out.println(token);
				if (token == null) {
					System.out.println("token获取失败!请检查是否网络问题或者失效了!");
				} else {
					System.out.println("token获取成功!");
					// 通用识别url
					String otherHost = "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise";
					try {

						byte[] imgData = FileUtil.readFileByBytes(imagePath);
						String imgStr = Base64Util.encode(imgData);
						String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8")
								+ "&" + URLEncoder.encode("templateSign", "UTF-8") + "="
								+ URLEncoder.encode("44c9bfe5409bd01c6d4a14afdf0a393c", "UTF-8");
						/**
						 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
						 */
						String result = HttpUtil.post(otherHost, token, params);
						System.out.println("解析图片的结果：" + result);

						JSONObject jsonobject = JSONObject.fromObject(result);
						if (result.contains("error_code")) {
							String error_code = jsonobject.getString("error_code").trim();
							if ("0".equals(error_code)) {
								System.out.println("图片解析成功！进行数据解析！");

								JSONObject object8 = JSONObject.fromObject(result);
								String data = object8.getString("data").toString().trim();
								JSONObject object2 = JSONObject.fromObject(data);
								String ret = object2.getString("ret").toString().trim();
								JSONArray array = JSONArray.fromObject(ret);

								for (int j = 0; j < 70; j += 7) {

									String string = array.get(j + 0).toString();
									JSONObject object3 = JSONObject.fromObject(string);
									String jzrq = object3.getString("word").toString().trim();
									System.out.println("记账日期：" + jzrq);

									String string2 = array.get(j + 1).toString();
									JSONObject object32 = JSONObject.fromObject(string2);
									String gjhtqywlx = object32.getString("word").toString().trim();
									System.out.println("归集和提取业务类型：" + gjhtqywlx);

									String string3 = array.get(j + 2).toString();
									JSONObject object33 = JSONObject.fromObject(string3);
									String fse = object33.getString("word").toString().trim();
									System.out.println("发生额：" + fse);

									String string4 = array.get(j + 3).toString();
									JSONObject object34 = JSONObject.fromObject(string4);
									String fslxe = object34.getString("word").toString().trim();
									System.out.println("发生利息额：" + fslxe);

									String string5 = array.get(j + 4).toString();
									JSONObject object35 = JSONObject.fromObject(string5);
									String tqyy = object35.getString("word").toString().trim();
									System.out.println("提取原因：" + tqyy);

									String string6 = array.get(j + 5).toString();
									JSONObject object36 = JSONObject.fromObject(string6);
									String tqfs = object36.getString("word").toString().trim();
									System.out.println("提取方式：" + tqfs);

									String string7 = array.get(j + 6).toString();
									JSONObject object37 = JSONObject.fromObject(string7);
									String zhye = object37.getString("word").toString().trim();
									System.out.println("账户余额：" + zhye);
									System.out.println("/////////////////////////////////////////////////////");
									HousingShaoXingPay HousingShaoXingPay = new HousingShaoXingPay();
									HousingShaoXingPay.setTaskid(messageLoginForHousing.getTask_id().trim());
									HousingShaoXingPay.setZhye(zhye);
									HousingShaoXingPay.setTqfs(tqfs);
									HousingShaoXingPay.setTqyy(tqyy);
									HousingShaoXingPay.setFslxe(fslxe);
									HousingShaoXingPay.setFse(fse);
									HousingShaoXingPay.setGjhtqywlx(gjhtqywlx);
									HousingShaoXingPay.setJzrq(jzrq);
									housingShaoXingPayRepository.save(HousingShaoXingPay);
								}

							} else if ("17".equals(error_code)) {
								System.out.println("每天请求量超限额");
							} else {
								System.out.println("其他异常错误！");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			 taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成",
			 200, taskHousing.getTaskid());
			
			 taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());


		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	// 利用IO流保存验证码成功后，返回验证码图片保存路径
	public static String getImagePath(Page page) throws Exception {
		File imageFile = getImageCustomPath();
		String imgagePath = imageFile.getAbsolutePath();
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath)));
		if (inputStream != null && outputStream != null) {
			int temp = 0;
			while ((temp = inputStream.read()) != -1) { // 开始拷贝
				outputStream.write(temp); // 边读边写
			}
			outputStream.close();
			inputStream.close(); // 关闭输入输出流
		}
		return imgagePath;
	}

	// 创建验证码图片保存路径
	public static File getImageCustomPath() {
		String path = "";
		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			path = System.getProperty("user.dir") + "/verifyCodeImage/";
		} else {
			path = System.getProperty("user.home") + "/verifyCodeImage/";
		}
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}

	public static void saveFile(InputStream inputStream, String filePath) throws Exception {

		OutputStream outputStream = new FileOutputStream(filePath);

		int byteCount = 0;

		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, byteCount);

		}
		inputStream.close();
		outputStream.close();
	}

	public String callChaoJiYingService(String imgPath, String codeType) {
		Gson gson = new GsonBuilder().create();
		String chaoJiYingResult = super.getVerifycodeByChaoJiYing(codeType, LEN_MIN, TIME_ADD, STR_DEBUG, imgPath);

		String errNo = ChaoJiYingUtils.getErrNo(chaoJiYingResult);
		if (!ChaoJiYingUtils.RESULT_SUCCESS.equals(errNo)) {
			return ChaoJiYingUtils.getErrMsg(errNo);
		}

		return (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");

	}

	public static void save(InputStream inputStream, String filePath) throws Exception {

		OutputStream outputStream = new FileOutputStream(filePath);

		int byteCount = 0;

		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, byteCount);
		}
		inputStream.close();
		outputStream.close();
	}

	/**
	 * 获取权限token
	 */
	public String getAuth() {
		// API Key
		String clientId = ak;
		// Secret Key
		String clientSecret = sk;
		return getAuth(clientId, clientSecret);
	}

	public String getAuth(String ak, String sk) {
		// 获取token地址
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		String getAccessTokenUrl = authHost
				// grant_type为固定参数
				+ "grant_type=client_credentials"
				// API Key
				+ "&client_id=" + ak
				// Secret Key
				+ "&client_secret=" + sk;
		try {
			URL realUrl = new URL(getAccessTokenUrl);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.err.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			/**
			 * 返回结果示例
			 */
			System.err.println("result:" + result);
			JSONObject jsonObject = JSONObject.fromObject(result);
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		}
		return null;
	}
	public static String encryptedPhone(String phonenum) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("zaozhuang.js", Charsets.UTF_8);
		engine.eval(path);
		final Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", phonenum);
		return data.toString();
	}
	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}