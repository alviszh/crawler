package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.nantong.HousingNanTongBase;
import com.microservice.dao.entity.crawler.housing.nantong.HousingNanTongHtml;
import com.microservice.dao.entity.crawler.housing.nantong.HousingNanTongPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.nantong.HousingNanTongBaseRepository;
import com.microservice.dao.repository.crawler.housing.nantong.HousingNanTongHtmlRepository;
import com.microservice.dao.repository.crawler.housing.nantong.HousingNanTongPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.nantong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.nantong")
public class HousingFundNanTongService extends AbstractChaoJiYingHandler implements ICrawlerLogin {
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundNanTongService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingNanTongHtmlRepository housingnantongHtmlRepository;
	@Autowired
	public HousingNanTongBaseRepository housingnantongBaseRepository;
	@Autowired
	public HousingNanTongPayRepository housingnantongPayRepository;
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

			 String num = messageLoginForHousing.getNum();
			 String username = messageLoginForHousing.getUsername();
			 String password = messageLoginForHousing.getPassword();
//			String num = messageLoginForHousing.getNum().trim();
//			String username = messageLoginForHousing.getUsername().trim();
//			String password = messageLoginForHousing.getPassword().trim();

			String encodeName = URLEncoder.encode(username, "utf-8");

			// 登录请求
			String loginurl3 = "http://58.221.92.98:8080/searchPersonLogon.do?spidno=" + num + "&spname=" + encodeName
					+ "&sppassword=" + password;
			WebRequest requestSettings1 = new WebRequest(new URL(loginurl3), HttpMethod.GET);
			HtmlPage pageq1 = webClient.getPage(requestSettings1);
			String contentAsString = pageq1.getWebResponse().getContentAsString();

			if (contentAsString.contains("<title>职工查询</title>")) {
				System.out.println("登陆成功！下一步输入手机号---");

				// 手机号码
				HtmlTextInput name = (HtmlTextInput) pageq1.getFirstByXPath("//input[@id='tel']");
				// 确定按钮
				HtmlElement button = (HtmlElement) pageq1.getFirstByXPath("//input[@id='button']");
				name.setText("11111111111");
				Page click = button.click();
				String contentAsString2 = click.getWebResponse().getContentAsString();

				if (contentAsString2.contains("<title>南通职工查询系统</title>")) {
					System.out.println("登陆成功！");
					String cookies = CommonUnit.transcookieToJson(webClient);
					taskHousing.setCookies(cookies);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getError_code());
					taskHousingRepository.save(taskHousing);
				} else {
					System.out.println("输入手机号过程失败！登陆失败！");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getError_code());
					taskHousingRepository.save(taskHousing);
				}

			} else {
				System.out.println("登陆失败！异常错误！");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getError_code());
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
		String jbxx = "http://58.221.92.98:8080/searchGrye.do?logon=2018-03-02T14:41:30.700+0800";
		try {
			WebRequest requestSettings = new WebRequest(new URL(jbxx), HttpMethod.GET);
			Page page = webClient.getPage(requestSettings);

			String contentAsString = page.getWebResponse().getContentAsString();
			HousingNanTongHtml housingnantongHtml = new HousingNanTongHtml();
			housingnantongHtml.setHtml(contentAsString + "");
			housingnantongHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingnantongHtml.setType("基本信息");
			housingnantongHtml.setUrl(jbxx);
			housingnantongHtmlRepository.save(housingnantongHtml);

			// 解析基本信息
			Document doc = Jsoup.parse(contentAsString);
			Elements tables = doc.getElementsByClass("shuju1");
			Elements tds = tables.get(0).getElementsByTag("td");
			// 单位名称
			String dwmc = tds.get(0).text().trim();
			System.out.println("单位名称---" + dwmc);
			// 单位账户
			String dwzh = tds.get(1).text().trim();
			System.out.println("单位账户---" + dwzh);
			// 姓名
			String xm = tds.get(2).text().trim();
			System.out.println("姓名---" + xm);
			// 身份证号码
			String sfzhm = tds.get(3).text().trim();
			System.out.println("身份证号码---" + sfzhm);
			// 个人账户
			String grzh = tds.get(4).text().trim();
			System.out.println("个人账户---" + grzh);
			// 缴存基数
			String jcjs = tds.get(5).text().trim();
			System.out.println("缴存基数---" + jcjs);
			// 月汇缴额
			String yhje = tds.get(6).text().trim();
			System.out.println("月汇缴额---" + yhje);
			// 公积金余额
			String gjjye = tds.get(7).text().trim();
			System.out.println("公积金余额---" + gjjye);
			// 补贴月缴存额
			String btyjce = tds.get(8).text().trim();
			System.out.println("补贴月缴存额---" + btyjce);
			// 补贴余额
			String btye = tds.get(9).text().trim();
			System.out.println("补贴余额---" + btye);
			// 最新汇缴年月
			String zxhjny = tds.get(10).text().trim();
			System.out.println("最新汇缴年月---" + zxhjny);
			// 账户状态
			String zhzt = tds.get(11).text().trim();
			System.out.println("账户状态---" + zhzt);

			//////////////////////////////////////////////////////// 个人信息//////////////////////////////////
			HousingNanTongBase housingnantongBase = new HousingNanTongBase();
			housingnantongBase.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingnantongBase.setDwmc(dwmc);
			housingnantongBase.setDwzh(dwzh);
			housingnantongBase.setXm(xm);
			housingnantongBase.setSfzhm(sfzhm);
			housingnantongBase.setGrzh(grzh);
			housingnantongBase.setJcjs(jcjs);
			housingnantongBase.setYhje(yhje);
			housingnantongBase.setGjjye(gjjye);
			housingnantongBase.setBtyjce(btyjce);
			housingnantongBase.setBtye(btye);
			housingnantongBase.setZxhjny(zxhjny);
			housingnantongBase.setZhzt(zhzt);
			housingnantongBaseRepository.save(housingnantongBase);

			taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
		}

		////////////////////////////////////// 流水信息////////////////////////////////////////
		try

		{

			SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyy");
			Calendar calendar22 = Calendar.getInstance();
			calendar22.setTime(new Date());
			List<String> list22 = new ArrayList<String>();
			for (int j = 0; j <= 3; j++) {
				list22.add(dateFormat22.format(calendar22.getTime()));
				calendar22.set(Calendar.YEAR, calendar22.get(Calendar.YEAR) - 1);
			}
			for (int k = 0; k < list22.size(); k++) {
				String monthdate = list22.get(k);
				String year = Integer.parseInt(monthdate) + "";
				System.out.println(year);
				// 流水请求
				String jcmx = "http://58.221.92.98:8080/searchGrmx.do?year=" + year;
				WebRequest requestSettings1 = new WebRequest(new URL(jcmx), HttpMethod.GET);
				Page page1 = webClient.getPage(requestSettings1);
				String contentAsString = page1.getWebResponse().getContentAsString();

				HousingNanTongHtml housingnantongHtml1 = new HousingNanTongHtml();
				housingnantongHtml1.setHtml(contentAsString);
				housingnantongHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
				housingnantongHtml1.setType("流水信息");
				housingnantongHtml1.setUrl(jcmx);
				housingnantongHtmlRepository.save(housingnantongHtml1);

				Document doc = Jsoup.parse(contentAsString);
				Elements tables = doc.getElementsByClass("shuju");
				Element element = tables.get(0);
				Elements trs = element.getElementsByTag("tr");
				for (int i = 1; i < trs.size(); i++) {
					Elements tds = trs.get(i).getElementsByTag("td");
					// 业务日期
					String ywrq = tds.get(1).text().trim();
					System.out.println("业务日期---" + ywrq);
					// 汇缴年月
					String hjny = tds.get(2).text().trim();
					System.out.println("汇缴年月---" + hjny);
					// 摘要
					String zy = tds.get(3).text().trim();
					System.out.println("摘要---" + zy);
					// 收入
					String sr = tds.get(4).text().trim();
					System.out.println("收入---" + sr);
					// 支出
					String zc = tds.get(5).text().trim();
					System.out.println("支出---" + zc);
					// 余额
					String ye = tds.get(6).text().trim();
					System.out.println("余额---" + ye);
					// 补贴收入
					String btsr = tds.get(7).text().trim();
					System.out.println("补贴收入---" + btsr);
					// 补贴支出
					String btzc = tds.get(8).text().trim();
					System.out.println("补贴支出---" + btzc);
					// 补贴余额
					String btye = tds.get(9).text().trim();
					System.out.println("补贴余额---" + btye);
					HousingNanTongPay housingnantongPay = new HousingNanTongPay();
					housingnantongPay.setTaskid(messageLoginForHousing.getTask_id().trim());
					housingnantongPay.setYwrq(ywrq);
					housingnantongPay.setHjny(hjny);
					housingnantongPay.setZy(zy);
					housingnantongPay.setSr(btsr);
					housingnantongPay.setZc(btzc);
					housingnantongPay.setYe(btye);
					housingnantongPay.setBtsr(btsr);
					housingnantongPay.setBtzc(btzc);
					housingnantongPay.setBtye(btye);
					housingnantongPayRepository.save(housingnantongPay);
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