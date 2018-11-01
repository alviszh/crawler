package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhoushan.HousingZhouShanBase;
import com.microservice.dao.entity.crawler.housing.zhoushan.HousingZhouShanHtml;
import com.microservice.dao.entity.crawler.housing.zhoushan.HousingZhouShanPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.zhoushan.HousingZhouShanBaseRepository;
import com.microservice.dao.repository.crawler.housing.zhoushan.HousingZhouShanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.zhoushan.HousingZhouShanPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zhoushan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zhoushan")
public class HousingFundZhouShanService extends AbstractChaoJiYingHandler implements ICrawlerLogin {
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundZhouShanService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingZhouShanHtmlRepository housingZhouShanHtmlRepository;
	@Autowired
	public HousingZhouShanBaseRepository housingZhouShanBaseRepository;
	@Autowired
	public HousingZhouShanPayRepository housingZhouShanPayRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Autowired
	public HousingBasicService housingBasicService;

	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {

			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://115.231.121.147/";
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage html = webClient.getPage(webRequest);

			HtmlRadioButtonInput rbl = (HtmlRadioButtonInput) html.getFirstByXPath("//*[@id=\"rbl_2\"]");
			rbl.click();
			// 身份证号
			HtmlTextInput name = (HtmlTextInput) html.getFirstByXPath("//input[@id='name']");
			// 密码
			HtmlPasswordInput password = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='password']");
			// 查询按钮
			HtmlElement btn_ok = (HtmlElement) html.getFirstByXPath("//input[@id='btn_ok']");
			// 验证码输入框
			HtmlTextInput yzm2 = (HtmlTextInput) html.getFirstByXPath("//input[@id='yzm2']");
			// 图片
			HtmlButtonInput login_check_code = (HtmlButtonInput) html
					.getFirstByXPath("//input[@id='login_check_code']");

			String base = login_check_code + "";
			String[] split = base.split("value=");
			String[] split2 = split[1].split("\"");
			String[] split3 = split2[1].split("=");
			String[] split4 = split3[0].split("\\+");
			String left = split4[0].trim();
			String right = split4[1].trim();
			int intleft = Integer.parseInt(left);
			int intright = Integer.parseInt(right);
			int yzm = intleft + intright;

			name.setText(messageLoginForHousing.getNum());
			password.setText(messageLoginForHousing.getPassword());
			yzm2.setText(yzm + "");

			btn_ok.click();

			String u = "http://115.231.121.147/Index.aspx";
			WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);
			HtmlPage html1 = webClient.getPage(webRequest1);
			String contentAsString = html1.getWebResponse().getContentAsString();
			System.out.println(contentAsString);

			Thread.sleep(2000);
			
			if (contentAsString.contains("退出系统")) {
				System.out.println("登陆成功！");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setCookies(cookies);
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
				taskHousingRepository.save(taskHousing);
			} else {
				System.out.println("登陆失败！请从新登陆！");
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

	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		// 基本信息请求
		String jbxx = "http://115.231.121.147/Controller/GR/gjcx/gjjzlcx.ashx?dt=1520473911647";
		try {
			WebRequest requestSettings = new WebRequest(new URL(jbxx), HttpMethod.GET);
			Page page = webClient.getPage(requestSettings);

			String contentAsString = page.getWebResponse().getContentAsString();
			HousingZhouShanHtml housingZhouShanHtml = new HousingZhouShanHtml();
			housingZhouShanHtml.setHtml(contentAsString + "");
			housingZhouShanHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingZhouShanHtml.setType("基本信息");
			housingZhouShanHtml.setUrl(jbxx);
			housingZhouShanHtmlRepository.save(housingZhouShanHtml);

			//////////////////////////////////////////////////////// 个人信息//////////////////////////////////

			// 解析基本信息
			JSONObject object = JSONObject.fromObject(contentAsString);
			HousingZhouShanBase housingZhouShanBase = new HousingZhouShanBase();
			housingZhouShanBase.setTaskid(messageLoginForHousing.getTask_id().trim());
			// 职工账号
			String zgzh = object.getString("khh").trim();
			System.out.println("职工账号-----" + zgzh);
			housingZhouShanBase.setZgzh(zgzh);
			// 证件号
			String zjh = object.getString("sfz").trim();
			System.out.println("证件号-----" + zjh);
			housingZhouShanBase.setZjh(zjh);
			// 月工资额
			String ygze = object.getString("gze").trim();
			System.out.println("月工资额-----" + ygze);
			housingZhouShanBase.setYgze(ygze);
			// 个人月汇缴额
			String gryhje = object.getString("gryhjje").trim();
			System.out.println("个人月汇缴额-----" + gryhje);
			housingZhouShanBase.setGryhje(gryhje);
			// 姓名
			String xm = object.getString("hm").trim();
			System.out.println("姓名-----" + xm);
			housingZhouShanBase.setXm(xm);
			// 手机号码
			String sjhm = object.getString("sjhm").trim();
			System.out.println("手机号码-----" + sjhm);
			housingZhouShanBase.setSjhm(sjhm);
			// 单位比例
			String dwbl = object.getString("dwjcbl").trim();
			System.out.println("单位比例-----" + dwbl);
			housingZhouShanBase.setDwbl(dwbl);
			// 单位月汇缴额
			String dwyhje = object.getString("dwyhjje").trim();
			System.out.println("单位月汇缴额-----" + dwyhje);
			housingZhouShanBase.setDwyhje(dwyhje);
			// 账户状态
			String zhzt = object.getString("zt").trim();
			System.out.println("账户状态-----" + zhzt);
			housingZhouShanBase.setZhzt(zhzt);
			// 开户日期
			String khrq = object.getString("khrq").trim();
			System.out.println("开户日期-----" + khrq);
			housingZhouShanBase.setKhrq(khrq);
			// 个人比例
			String grbl = object.getString("grjcbl").trim();
			System.out.println("个人比例-----" + grbl);
			housingZhouShanBase.setGrbl(grbl);
			// 账户余额
			String zhye = object.getString("zhye").trim();
			System.out.println("账户余额-----" + zhye);
			housingZhouShanBase.setZhye(zhye);
			housingZhouShanBaseRepository.save(housingZhouShanBase);

			taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
		}

		////////////////////////////////////// 流水信息////////////////////////////////////////
		try

		{
			SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar22 = Calendar.getInstance();
			calendar22.setTime(new Date());
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH));
			System.out.println("当前年月---" + dateFormat22.format(calendar22.getTime()));

			SimpleDateFormat dateFormat222 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar222 = Calendar.getInstance();
			calendar222.setTime(new Date());
			calendar222.set(Calendar.MONTH, calendar222.get(Calendar.MONTH) - 36);
			System.out.println("三年前---" + dateFormat222.format(calendar222.getTime()));
			// 流水请求
			String jcmx = "http://115.231.121.147/Controller/GR/gjcx/gjcx.ashx";
			WebRequest requestSettings1 = new WebRequest(new URL(jcmx), HttpMethod.POST);
			requestSettings1.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings1.getRequestParameters().add(new NameValuePair("dt", "1520474056231"));
			requestSettings1.getRequestParameters().add(new NameValuePair("m", "grjcmx"));
			requestSettings1.getRequestParameters()
					.add(new NameValuePair("start", dateFormat222.format(calendar222.getTime())));
			requestSettings1.getRequestParameters()
					.add(new NameValuePair("end", dateFormat22.format(calendar22.getTime())));
			requestSettings1.getRequestParameters().add(new NameValuePair("page", "1"));
			requestSettings1.getRequestParameters().add(new NameValuePair("rows", "500"));
			requestSettings1.getRequestParameters().add(new NameValuePair("sort", "csrq"));
			requestSettings1.getRequestParameters().add(new NameValuePair("order", "desc"));
			Page page1 = webClient.getPage(requestSettings1);
			String contentAsString = page1.getWebResponse().getContentAsString();

			HousingZhouShanHtml housingZhouShanHtml1 = new HousingZhouShanHtml();
			housingZhouShanHtml1.setHtml(contentAsString);
			housingZhouShanHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingZhouShanHtml1.setType("流水信息");
			housingZhouShanHtml1.setUrl(jcmx);
			housingZhouShanHtmlRepository.save(housingZhouShanHtml1);

			JSONObject object = JSONObject.fromObject(contentAsString);
			String rows = object.getString("rows").trim();
			JSONArray array = JSONArray.fromObject(rows);
			for (int i = 0; i < array.size(); i++) {
				String string = array.get(i).toString();
				JSONObject object2 = JSONObject.fromObject(string);
				HousingZhouShanPay housingZhouShanPay = new HousingZhouShanPay();
				housingZhouShanPay.setTaskid(messageLoginForHousing.getTask_id().trim());
				// 产生日期
				String csrq = object2.getString("csrq").trim();
				System.out.println("产生日期-----" + csrq);
				housingZhouShanPay.setCsrq(csrq);
				// 所属年月
				String ssny = object2.getString("ssny").trim();
				System.out.println("所属年月-----" + ssny);
				housingZhouShanPay.setSsny(ssny);
				// 单位金额
				String dwje = object2.getString("dwje").trim();
				System.out.println("单位金额-----" + dwje);
				housingZhouShanPay.setDwje(dwje);
				// 个人金额
				String grje = object2.getString("grje").trim();
				System.out.println("个人金额-----" + grje);
				housingZhouShanPay.setGrje(grje);
				// 缴交单位
				String jjdw = object2.getString("hm").trim();
				System.out.println("缴交单位-----" + jjdw);
				housingZhouShanPay.setJjdw(jjdw);
				// 缴交原因
				String jjyy = object2.getString("jjyyname").trim();
				System.out.println("缴交原因-----" + jjyy);
				housingZhouShanPay.setJjyy(jjyy);
				// 单据状态
				String djzt = object2.getString("ztname").trim();
				System.out.println("单据状态-----" + djzt);
				housingZhouShanPay.setDjzt(djzt);
				// 结算方式
				String jsfs = object2.getString("jslxname").trim();
				System.out.println("结算方式-----" + jsfs);
				housingZhouShanPay.setJsfs(jsfs);

				housingZhouShanPayRepository.save(housingZhouShanPay);

			}

			taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());

			// 更新最后的状态
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
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}