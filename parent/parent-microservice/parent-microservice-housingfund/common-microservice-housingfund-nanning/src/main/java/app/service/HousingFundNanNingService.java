package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
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
import com.microservice.dao.entity.crawler.housing.nanning.HousingNanNingBase;
import com.microservice.dao.entity.crawler.housing.nanning.HousingNanNingHtml;
import com.microservice.dao.entity.crawler.housing.nanning.HousingNanNingPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.nanning.HousingNanNingBaseRepository;
import com.microservice.dao.repository.crawler.housing.nanning.HousingNanNingHtmlRepository;
import com.microservice.dao.repository.crawler.housing.nanning.HousingNanNingPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.service.common.aop.ISms;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.nanning")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.nanning")
public class HousingFundNanNingService extends AbstractChaoJiYingHandler implements ICrawlerLogin , ISms  {
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundNanNingService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingNanNingHtmlRepository housingnanningHtmlRepository;
	@Autowired
	public HousingNanNingBaseRepository housingnanningBaseRepository;
	@Autowired
	public HousingNanNingPayRepository housingnanningPayRepository;
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
			String loginurl2 = "http://www.nngjj.com/gryw/frame/captcha.jpg";
			WebRequest webRequest = new WebRequest(new URL(loginurl2), HttpMethod.GET);
			Page page00 = webClient.getPage(webRequest);
			String imagePath = getImagePath(page00);
			String code = chaoJiYingOcrService.callChaoJiYingService(imagePath, "1007");
			System.out.println("识别出来的图片验证码是---------" + code);

			// 登录请求
			String loginurl3 = "http://www.nngjj.com/gryw/login.html";
			String requestBody = "username=" + messageLoginForHousing.getNum().trim() + "&password="
					+ messageLoginForHousing.getPassword().trim() + "&captcha="+code;
			WebRequest requestSettings1 = new WebRequest(new URL(loginurl3), HttpMethod.POST);
			requestSettings1.setRequestBody(requestBody);
			Page pageq1 = webClient.getPage(requestSettings1);
			String contentAsString2 = pageq1.getWebResponse().getContentAsString();
			System.out.println("登陆结果"+contentAsString2);
			if (contentAsString2.contains("验证码错误！")) {
				System.out.println("登陆失败！验证码错误！");
				taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getError_code());
				taskHousingRepository.save(taskHousing);
			} else if (contentAsString2.contains("您输入用户代码或密码错误！ ")) {
				System.out.println("登陆失败！您输入用户代码或密码错误！ ");
				taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getError_code());
				taskHousingRepository.save(taskHousing);
			}else {
				if (contentAsString2.contains("欢迎您登录本系统")) {
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
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		// 基本信息请求
		String jbxx = "http://www.nngjj.com/gryw/gjjgr/grzm.html?_t=601951&_winid=w6622";
		try {
			WebRequest requestSettings = new WebRequest(new URL(jbxx), HttpMethod.GET);
			Page page = webClient.getPage(requestSettings);

			String contentAsString = page.getWebResponse().getContentAsString();
			HousingNanNingHtml housingnanningHtml = new HousingNanNingHtml();
			housingnanningHtml.setHtml(contentAsString + "");
			housingnanningHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingnanningHtml.setType("基本信息");
			housingnanningHtml.setUrl(jbxx);
			housingnanningHtmlRepository.save(housingnanningHtml);

			//解析基本信息
			Document doc = Jsoup.parse(contentAsString);
			Elements tables = doc.getElementsByTag("table");
			
			HousingNanNingBase housingnanningBase = new HousingNanNingBase();
			
            ////////////////////////////////////////////////////////个人信息
			Element element = tables.get(0);
			Elements tdrs = element.getElementsByClass("tdr");
			//姓名
			String name = tdrs.get(0).text().trim();
			System.out.println("姓名："+name);
			//身份证号
			String cardid = tdrs.get(1).text().trim();
			System.out.println("身份证号："+cardid);
			//开户日期
			String khrq = tdrs.get(2).text().trim();
			System.out.println("开户日期："+khrq);
			//单位名称
			String dwmc = tdrs.get(3).text().trim();
			System.out.println("单位名称："+dwmc);
			//单位账户
			String dwzh = tdrs.get(4).text().trim();
			System.out.println("单位账号："+dwzh);
			//个人编号
			String grbh = tdrs.get(5).text().trim();
			System.out.println("个人编号："+grbh);
			//联系电话
			String lxdh = tdrs.get(6).text().trim();
			System.out.println("联系电话："+lxdh);
			//银行账号
			String yhzh = tdrs.get(7).text().trim();
			System.out.println("银行账号："+yhzh);
			//银行名称
			String yhmc = tdrs.get(8).text().trim();
			System.out.println("银行名称："+yhmc);
			housingnanningBase.setName(name);
			housingnanningBase.setCardid(cardid);
			housingnanningBase.setDwmc(dwmc);
			housingnanningBase.setDwzh(dwzh);
			housingnanningBase.setKhrq(khrq);
			housingnanningBase.setGrbh(grbh);
			housingnanningBase.setLxdh(lxdh);
			housingnanningBase.setYhzh(yhzh);
			housingnanningBase.setYhmc(yhmc);
			
			
			////////////////////////////////////////////////////////账户信息
			Element element2 = tables.get(1);
			Elements tdrs2 = element2.getElementsByClass("tdr");
			//缴存基数
			String jcjs = tdrs2.get(0).text().trim();
			System.out.println("缴费基数："+jcjs);
			//缴存比例
			String jcbl = tdrs2.get(1).text().trim();
			System.out.println("缴存比例："+jcbl);
			//当前缴费年月
			String dqjfny = tdrs2.get(2).text().trim();
			System.out.println("当前缴费年月："+dqjfny);
			//个人缴存额
			String grjce = tdrs2.get(3).text().trim();
			System.out.println("个人缴费额："+grjce);
			//单位缴存额
			String dwjce = tdrs2.get(4).text().trim();
			System.out.println("单位缴存额："+dwjce);
			//月缴存合计
			String yjchj = tdrs2.get(5).text().trim();
			System.out.println("月缴存合计："+yjchj);
			//余额
			String yue = tdrs2.get(6).text().trim();
			System.out.println("余额："+yue);
			//缴存状态
			String jczt = tdrs2.get(7).text().trim();
			System.out.println("缴存状态："+jczt);
			//账户状态
			String zhzt = tdrs2.get(8).text().trim();
			System.out.println("账户状态："+zhzt);
			
			
			housingnanningBase.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingnanningBase.setZhzt(zhzt);
			housingnanningBase.setJczt(jczt);
			housingnanningBase.setYue(yue);
			housingnanningBase.setYjchj(yjchj);
			housingnanningBase.setDwjce(dwjce);
			housingnanningBase.setGrjce(grjce);
			housingnanningBase.setDqjcny(dqjfny);
			housingnanningBase.setJcbl(jcbl);
			housingnanningBase.setJcjs(jcjs);
			housingnanningBaseRepository.save(housingnanningBase);

			taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
		}

		////////////////////////////////////// 流水信息////////////////////////////////////////
		try

		{
			// 流水请求
			String jcmx = "http://www.nngjj.com/gryw/gjjgr/grmxQuery.html?type=2";
			String requestBody = "pageIndex=0&pageSize=1000";
			WebRequest requestSettings1 = new WebRequest(new URL(jcmx), HttpMethod.POST);
			requestSettings1.setRequestBody(requestBody);
			Page page1 = webClient.getPage(requestSettings1);
			String contentAsString = page1.getWebResponse().getContentAsString();

			HousingNanNingHtml housingnanningHtml1 = new HousingNanNingHtml();
			housingnanningHtml1.setHtml(contentAsString);
			housingnanningHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingnanningHtml1.setType("流水信息");
			housingnanningHtml1.setUrl(jcmx);
			housingnanningHtmlRepository.save(housingnanningHtml1);

			System.out.println("流水信息----" + contentAsString);
			
			JSONObject jsonobject = JSONObject.fromObject(contentAsString);
			String data = jsonobject.getString("data").toString();
            JSONArray jsonarray = JSONArray.fromObject(data);
            for (int i = 0; i < jsonarray.size(); i++) {
            	String string = jsonarray.get(i).toString();
            	JSONObject jsonstring = JSONObject.fromObject(string);
            	//身份证号码
            	String cardid = jsonstring.getString("grid").trim();
            	System.out.println("身份证号码:"+cardid);
            	//记账日期
            	String jzrq = jsonstring.getString("jzrq").trim();
            	System.out.println("记账日期:"+jzrq);
            	//业务类型
            	String ywlx = jsonstring.getString("bzxx").trim();
            	System.out.println("业务类型:"+ywlx);
            	//汇交年月
            	String hjny = jsonstring.getString("hjny").trim();
            	System.out.println("汇交年月:"+hjny);
            	//增加额
            	String zje = jsonstring.getString("zje").trim();
            	System.out.println("增加额:"+zje);
            	//减少额
            	String jse = jsonstring.getString("jse").trim();
            	System.out.println("减少额:"+jse);
            	//发生利息
            	String fslx = jsonstring.getString("fslx").trim();
            	System.out.println("发生利息:"+fslx);
            	//单位账号
            	String dwzh = jsonstring.getString("dwzh").trim();
            	System.out.println("单位账号:"+dwzh);
            	//流水号
            	String lsh = jsonstring.getString("lsh").trim();
            	System.out.println("流水号:"+lsh);
            	//提取原因
            	String tqyy = jsonstring.getString("bdyy").trim();
            	System.out.println("提取原因:"+tqyy);
            	HousingNanNingPay housingnanningPay = new HousingNanNingPay();
            	housingnanningPay.setTaskid(messageLoginForHousing.getTask_id().trim());
            	housingnanningPay.setTqyy(tqyy);
            	housingnanningPay.setLsh(lsh);
            	housingnanningPay.setDwzh(dwzh);
            	housingnanningPay.setFslx(fslx);
            	housingnanningPay.setJse(jse);
            	housingnanningPay.setZje(zje);
            	housingnanningPay.setHjny(hjny);
            	housingnanningPay.setYwlx(ywlx);
            	housingnanningPay.setJzrq(jzrq);
            	housingnanningPay.setCardid(cardid);
            	housingnanningPayRepository.save(housingnanningPay);
			}

			taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());

			taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());

			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return taskHousing;
	}

	@Async
	public TaskHousing sendSms(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
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

	@Override
	public TaskHousing verifySms(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}


}