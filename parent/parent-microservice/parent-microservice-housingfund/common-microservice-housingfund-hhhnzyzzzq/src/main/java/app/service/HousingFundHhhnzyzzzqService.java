package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.hhhnzyzzzq.HousinghhhnzyzzzqBase;
import com.microservice.dao.entity.crawler.housing.hhhnzyzzzq.HousinghhhnzyzzzqHtml;
import com.microservice.dao.entity.crawler.housing.hhhnzyzzzq.HousinghhhnzyzzzqPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.hhhnzyzzzq.HousinghhhnzyzzzqBaseRepository;
import com.microservice.dao.repository.crawler.housing.hhhnzyzzzq.HousinghhhnzyzzzqHtmlRepository;
import com.microservice.dao.repository.crawler.housing.hhhnzyzzzq.HousinghhhnzyzzzqPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.hhhnzyzzzq")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.hhhnzyzzzq")
public class HousingFundHhhnzyzzzqService extends AbstractChaoJiYingHandler implements ICrawlerLogin{
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundHhhnzyzzzqService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousinghhhnzyzzzqHtmlRepository housinghhhnzyzzzqHtmlRepository;
	@Autowired
	public HousinghhhnzyzzzqBaseRepository housinghhhnzyzzzqBaseRepository;
	@Autowired
	public HousinghhhnzyzzzqPayRepository housinghhhnzyzzzqPayRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Autowired
	public HousingBasicService housingBasicService;
	// 登录业务层
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			// 获取图片验证码
			String loginurl2 = "http://www.hhgjj.com/hhgjj/verifyCode?0.38098501518021227";
			WebRequest webRequest = new WebRequest(new URL(loginurl2), HttpMethod.GET);
			Page page00 = webClient.getPage(webRequest);
			String imagePath = getImagePath(page00);
			String code = chaoJiYingOcrService.callChaoJiYingService(imagePath, "1006");
			System.out.println("识别出来的图片验证码是---------" + code);
			// 登录请求
			String loginurl3 = "http://www.hhgjj.com/hhgjj/Login";
			String requestBody = "userName="+messageLoginForHousing.getNum().trim()+"&Password="+messageLoginForHousing.getPassword().trim()+"&v_code="+code+"&button=%E7%99%BB+%E5%BD%95";
//			String requestBody = "userName=gxr19880424&Password=gxr19880424&v_code="+code+"&button=%E7%99%BB+%E5%BD%95";
			WebRequest requestSettings1 = new WebRequest(new URL(loginurl3), HttpMethod.POST);
			requestSettings1.setRequestBody(requestBody);
			Page pageq1 = webClient.getPage(requestSettings1);
			String contentAsString2 = pageq1.getWebResponse().getContentAsString();
			System.out.println(contentAsString2);
			if (contentAsString2.contains("用户不存在!")) {
				System.out.println("登陆失败！用户不存在!");
				taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getError_code());
				taskHousingRepository.save(taskHousing);
			} else if (contentAsString2.contains("用户名或密码错误!")) {
				System.out.println("登陆失败！用户名或密码错误!");
				taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getError_code());
				taskHousingRepository.save(taskHousing);
			} else if (contentAsString2.contains("验证码错误!")) {
				System.out.println("登陆失败！验证码错误!");
				taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getError_code());
				taskHousingRepository.save(taskHousing);
			} else {
				if (contentAsString2.contains("已经登录")||contentAsString2.contains("您好，欢迎查询公积金账户情况")) {
					System.out.println("登陆成功！");
					String cookies = CommonUnit.transcookieToJson(webClient);
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
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	// 爬取数据的业务层
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		System.out.println("getCookies-----------"+taskHousing.getCookies());
		
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String jbxx1 = "http://www.hhgjj.com/hhgjj/grzhcx/grzh_info_form.jsp";
			WebRequest requestSettings = new WebRequest(new URL(jbxx1), HttpMethod.GET);
			Page page = webClient.getPage(requestSettings);

			String contentAsString = page.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(contentAsString);
			//用户名
			String uname = doc.getElementById("uname").text();
			System.out.println("用户名-----"+uname);
			//公积金账号
			String housingNumber = doc.getElementById("gjjzh").text();
			System.out.println("公积金账号-----"+housingNumber);
			//真实姓名
			String name = doc.getElementById("xm").text();
			System.out.println("真实姓名-----"+name);
			//身份证号
			String cardid = doc.getElementById("sfz").text();
			System.out.println("身份证号-----"+cardid);
			
			String jbxx2 = "http://www.hhgjj.com/hhgjj/FundQuery";
			WebRequest requestSettings2 = new WebRequest(new URL(jbxx2), HttpMethod.POST);
			Page page2 = webClient.getPage(requestSettings2);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			
			//手机号
			String[] split = contentAsString2.split("手机:");
			String[] split2 = split[1].split("</td>");
			String phone = split2[0].trim();
			System.out.println("手机号-----"+phone);
			//所属地区
			String[] split1 = contentAsString2.split("所属地区:");
			String[] split21 = split1[1].split("</td>");
			String addr = split21[0].trim();
			System.out.println("所属地区-----"+addr);
			
			HousinghhhnzyzzzqHtml housinghhhnzyzzzqHtml = new HousinghhhnzyzzzqHtml();
			housinghhhnzyzzzqHtml.setHtml(contentAsString + contentAsString2);
			housinghhhnzyzzzqHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housinghhhnzyzzzqHtml.setType("基本信息");
			housinghhhnzyzzzqHtml.setUrl(jbxx1+jbxx2);
			housinghhhnzyzzzqHtmlRepository.save(housinghhhnzyzzzqHtml);
			
			HousinghhhnzyzzzqBase housinghhhnzyzzzqBase = new HousinghhhnzyzzzqBase();
			housinghhhnzyzzzqBase.setTaskid(messageLoginForHousing.getTask_id().trim());
			housinghhhnzyzzzqBase.setAddr(addr);
			housinghhhnzyzzzqBase.setPhone(phone);
			housinghhhnzyzzzqBase.setCardid(cardid);
			housinghhhnzyzzzqBase.setName(name);
			housinghhhnzyzzzqBase.setHousingNumber(housingNumber);
			housinghhhnzyzzzqBase.setUname(uname);
			housinghhhnzyzzzqBaseRepository.save(housinghhhnzyzzzqBase);
			taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
		}

		////////////////////////////////////// 流水信息////////////////////////////////////////
		try

		{
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -0);
			String beforeMonth = df.format(c.getTime());
			String[] split = beforeMonth.split("-");
			for (int i = 0; i < split.length; i++) {
				System.out.println(split[i]);
			}
			// 获取当前的前3年
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.YEAR, -3);
			String beforeMonth1 = df1.format(c1.getTime());
			String[] split1 = beforeMonth1.split("-");
			for (int i = 0; i < split1.length; i++) {
				System.out.println(split1[i]);
			}
			
			TaskHousing findByTaskid1 = taskHousingRepository.findByTaskid(taskHousing.getTaskid());
			WebClient webClient1 = WebCrawler.getInstance().getNewWebClient();
			
			System.out.println("getCookies-----------"+findByTaskid1.getCookies());
			
			Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(findByTaskid1.getCookies());
			for (Cookie cookie : cookies1) {
				webClient1.getCookieManager().addCookie(cookie);
			}
			
			// 流水请求
			String jcmx = "http://www.hhgjj.com/hhgjj/FundQuery";
			String requestBody = "qYear="+split1[0]+"&qMonth="+split1[1]+"&zYear="+split[0]+"&zMonth="+split[1]+"&Submit=%E6%9F%A5%E8%AF%A2";
			WebRequest requestSettings1 = new WebRequest(new URL(jcmx), HttpMethod.POST);
			requestSettings1.setRequestBody(requestBody);
			Page page1 = webClient1.getPage(requestSettings1);
			String contentAsString = page1.getWebResponse().getContentAsString();
			System.out.println("流水信息----" + contentAsString);

			HousinghhhnzyzzzqHtml housinghhhnzyzzzqHtml1 = new HousinghhhnzyzzzqHtml();
			housinghhhnzyzzzqHtml1.setHtml(contentAsString);
			housinghhhnzyzzzqHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
			housinghhhnzyzzzqHtml1.setType("流水信息");
			housinghhhnzyzzzqHtml1.setUrl(jcmx);
			housinghhhnzyzzzqHtmlRepository.save(housinghhhnzyzzzqHtml1);

			Document doc = Jsoup.parse(contentAsString);
			
			System.out.println("流水的界面："+doc);
			Element tableExcel = doc.getElementById("tableExcel");
			
			System.out.println("流水的table："+tableExcel);
			
			Elements element0 = tableExcel.select("tr");
			for (int i = 5; i < element0.size(); i++) {
				Elements select = element0.get(i).select("td");
				for (int j = 1; j <=select.size(); j+=8) {
					//公积金账号
					String housingNumber = select.get(j).html();
					//日期
					String month = select.get(j+1).html();
					//摘要
					String zy = select.get(j+2).html();
					//提取
					String tq = select.get(j+3).html();
					//缴存
					String jc = select.get(j+4).html();
					//上期余额
					String sqye = select.get(j+5).html();
					//本期余额
					String bqye = select.get(j+6).html();
					System.out.println("公积金账号-----"+housingNumber);
					System.out.println("日期-----"+month);
					System.out.println("摘要-----"+zy);
					System.out.println("提取-----"+tq);
					System.out.println("缴存-----"+jc);
					System.out.println("上期余额-----"+sqye);
					System.out.println("本期余额-----"+bqye);
					HousinghhhnzyzzzqPay housinghhhnzyzzzqPay = new HousinghhhnzyzzzqPay();
					housinghhhnzyzzzqPay.setTaskid(messageLoginForHousing.getTask_id().trim());
					housinghhhnzyzzzqPay.setHousingNumber(housingNumber);
					housinghhhnzyzzzqPay.setMonth(month);
					housinghhhnzyzzzqPay.setZy(zy);
					housinghhhnzyzzzqPay.setTq(tq);
					housinghhhnzyzzzqPay.setJc(jc);
					housinghhhnzyzzzqPay.setSqye(sqye);
					housinghhhnzyzzzqPay.setBqye(bqye);
					housinghhhnzyzzzqPayRepository.save(housinghhhnzyzzzqPay);
				}
			}
			
			taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());
			
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
			path = "D://" + "/img/";
		} else {
			path = "D://" + "/img/";
//			path = System.getProperty("user.home") + "/verifyCodeImage/";
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