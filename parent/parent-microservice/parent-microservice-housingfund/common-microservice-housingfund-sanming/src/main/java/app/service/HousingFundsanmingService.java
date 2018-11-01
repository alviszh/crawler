package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.sanming.HousingsanmingBase;
import com.microservice.dao.entity.crawler.housing.sanming.HousingsanmingHtml;
import com.microservice.dao.entity.crawler.housing.sanming.HousingsanmingPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.sanming.HousingsanmingBaseRepository;
import com.microservice.dao.repository.crawler.housing.sanming.HousingsanmingHtmlRepository;
import com.microservice.dao.repository.crawler.housing.sanming.HousingsanmingPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.sanming")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.sanming")
public class HousingFundsanmingService extends AbstractChaoJiYingHandler implements ICrawlerLogin {
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundsanmingService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingsanmingHtmlRepository housingsanmingHtmlRepository;
	@Autowired
	public HousingsanmingBaseRepository housingsanmingBaseRepository;
	@Autowired
	public HousingsanmingPayRepository housingsanmingPayRepository;
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

			//获取登录请求的入参
			String loginUrl0 = "http://www.smgjj.com/MemberLogin.aspx";
			WebRequest webRequest0 = new WebRequest(new URL(loginUrl0), HttpMethod.GET);
			Page page0 = webClient.getPage(webRequest0);
			String contentAsString0 = page0.getWebResponse().getContentAsString();
            Document doc = Jsoup.parse(contentAsString0);
            System.out.println(contentAsString0);
            String form1 = doc.getElementsByAttributeValue("name", "form1").val();
            System.out.println("form1:"+form1);
            String __EVENTTARGET = doc.getElementById("__EVENTTARGET").val();
            System.out.println("__EVENTTARGET:"+__EVENTTARGET);
            String __EVENTARGUMENT = doc.getElementById("__EVENTARGUMENT").val();
            System.out.println("__EVENTARGUMENT:"+__EVENTARGUMENT);
            String __VIEWSTATE = doc.getElementById("__VIEWSTATE").val();
            System.out.println("__VIEWSTATE:"+__VIEWSTATE);
            String __VIEWSTATEENCRYPTED = doc.getElementById("__VIEWSTATEENCRYPTED").val();
            System.out.println("__VIEWSTATEENCRYPTED:"+__VIEWSTATEENCRYPTED);
            
            // 图片请求
            String loginurl3 = "http://www.smgjj.com/CheckCode.aspx";
            WebRequest requestSettings1 = new WebRequest(new URL(loginurl3), HttpMethod.GET);
            HtmlPage html = webClient.getPage(requestSettings1);
            // 图片
            HtmlImage randImage = (HtmlImage) html.getFirstByXPath("//*[@id=\"form1\"]/img");
            String code = chaoJiYingOcrService.getVerifycode(randImage, "1006");
//            String imageName = "111.jpg";
//            File file = new File("D:\\img\\" + imageName);
//            randImage.saveAs(file);
//            // 验证登录信息的链接：
//            String code = JOptionPane.showInputDialog("请输入验证码……");
            
			// 登录请求
			String loginUrl = "http://www.smgjj.com/MemberLogin.aspx";
			WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			webRequest.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest.getRequestParameters().add(new NameValuePair("form1", form1));
			webRequest.getRequestParameters().add(new NameValuePair("__EVENTTARGET", __EVENTTARGET));
			webRequest.getRequestParameters().add(new NameValuePair("__EVENTARGUMENT", __EVENTARGUMENT));
			webRequest.getRequestParameters().add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
			webRequest.getRequestParameters().add(new NameValuePair("__VIEWSTATEENCRYPTED", __VIEWSTATEENCRYPTED));
			webRequest.getRequestParameters().add(new NameValuePair("txtUUKey", messageLoginForHousing.getNum()));
			webRequest.getRequestParameters().add(new NameValuePair("txtMMKey", messageLoginForHousing.getPassword()));
			webRequest.getRequestParameters().add(new NameValuePair("keypwd", ""));
			webRequest.getRequestParameters().add(new NameValuePair("txtValidate", code));
			webRequest.getRequestParameters().add(new NameValuePair("btnSummit", "登 录"));
			webRequest.getRequestParameters().add(new NameValuePair("Serial_ID", ""));
			webRequest.getRequestParameters().add(new NameValuePair("Digest", ""));
			webRequest.getRequestParameters().add(new NameValuePair("random1", ""));
			webRequest.getRequestParameters().add(new NameValuePair("HomePageBottomInfo1$dlsthzdw", ""));
			webRequest.getRequestParameters().add(new NameValuePair("HomePageBottomInfo1$dlstzfbm", ""));
			webRequest.getRequestParameters().add(new NameValuePair("HomePageBottomInfo1$dlstqtgjj", ""));
			webRequest.getRequestParameters().add(new NameValuePair("HomePageBottomInfo1$dlstqtwz", ""));
			Page page = webClient.getPage(webRequest);
			String contentAsString = page.getWebResponse().getContentAsString();
            System.out.println(contentAsString);
			if (contentAsString.contains("三明市住房公积金后台管理系统")) {
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
		try {
			// 基本信息
			String jbxx = "http://www.smgjj.com/MemberFunction/PersonFunction/PersonBasicInfo.aspx?PageType=Add&SiteTicket=";
			WebRequest requestSettings = new WebRequest(new URL(jbxx), HttpMethod.GET);
			Page page = webClient.getPage(requestSettings);

			String contentAsString = page.getWebResponse().getContentAsString();
			System.out.println("基本信息----" + contentAsString);
			HousingsanmingHtml housingsanmingHtml = new HousingsanmingHtml();
			housingsanmingHtml.setHtml(contentAsString + "");
			housingsanmingHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingsanmingHtml.setType("基本信息");
			housingsanmingHtml.setUrl(jbxx);
			housingsanmingHtmlRepository.save(housingsanmingHtml);

			if (contentAsString.contains("id=\"txtRealName\"")) {
				System.out.println("基本信息获取成功！");
				Document doc = Jsoup.parse(contentAsString);
				//用户名
				String yhm = doc.getElementById("txtUserName").val();
				System.out.println("用户名---" + yhm);
				//姓名
				String xm = doc.getElementById("txtRealName").val();
				System.out.println("姓名---" + xm);
				//性别   1：男  2：女
				String xb = doc.getElementById("ddlSex").getElementsByAttribute("selected").get(0).text();
				System.out.println("姓别---" + xb);
				//身份证号
				String sfzh = doc.getElementById("txtCardId").val();
				System.out.println("身份证号---" + sfzh);
				//出生日期
				String csrq = doc.getElementById("txtBirthday").val();
				System.out.println("出生日期---" + csrq);
				//所属行业
				String sshy = doc.getElementById("drpTRADE").getElementsByAttribute("selected").get(0).text();
				System.out.println("所属行业---" + sshy);
				//单位名称
				String dwmc = doc.getElementById("txtUnitName").val();
				System.out.println("单位名称---" + dwmc);
				//民族
				String mz = doc.getElementById("drpNation").getElementsByAttribute("selected").get(0).text();
				System.out.println("民族---" + mz);
				HousingsanmingBase housingsanmingBase = new HousingsanmingBase();
				housingsanmingBase.setTaskid(messageLoginForHousing.getTask_id().trim());
				housingsanmingBase.setMz(mz);
				housingsanmingBase.setDwmc(dwmc);
				housingsanmingBase.setSshy(sshy);
				housingsanmingBase.setCsrq(csrq);
				housingsanmingBase.setSfzh(sfzh);
				housingsanmingBase.setXb(xb);
				housingsanmingBase.setXm(xm);
				housingsanmingBase.setYhm(yhm);
				housingsanmingBaseRepository.save(housingsanmingBase);

				taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());
			} else {
				System.out.println("基本信息获取失败！");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		////////////////////////////////////// 流水信息////////////////////////////////////////
		try

		{
//			// 获取当前的年
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar c = Calendar.getInstance();
//			c.add(Calendar.YEAR, -0);
//			String beforeMonth = df.format(c.getTime());
//			// 获取当前的前3年
//			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar c1 = Calendar.getInstance();
//			c1.add(Calendar.YEAR, -3);
//			String beforeMonth1 = df1.format(c1.getTime());
			// 流水入参的请求
			String jcmx = "http://www.smgjj.com/MemberFunction/PersonFunction/PersonAcfSerach.aspx?PageType=Add&SiteTicket=";
			WebRequest requestSettings1 = new WebRequest(new URL(jcmx), HttpMethod.GET);
			Page page1 = webClient.getPage(requestSettings1);
			String contentAsString = page1.getWebResponse().getContentAsString();
			
			Document doc = Jsoup.parse(contentAsString);
			String form1 = doc.getElementsByAttributeValue("name", "form1").get(0).val().trim();
//			String __EVENTTARGET = doc.getElementById("__EVENTTARGET").val();
			String __EVENTARGUMENT = doc.getElementById("__EVENTARGUMENT").val();
			String __VIEWSTATE = doc.getElementById("__VIEWSTATE").val();
			String __VIEWSTATEENCRYPTED = doc.getElementById("__VIEWSTATEENCRYPTED").val();
			String __EVENTVALIDATION = doc.getElementById("__EVENTVALIDATION").val();
			
			// 流水1入参的请求
			String jcmx1 = "http://www.smgjj.com/MemberFunction/PersonFunction/PersonAcfSerach.aspx?PageType=Add&SiteTicket=";
			WebRequest requestSettings11 = new WebRequest(new URL(jcmx1), HttpMethod.POST);
			requestSettings11.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings11.getRequestParameters().add(new NameValuePair("form1", form1));
			requestSettings11.getRequestParameters().add(new NameValuePair("__EVENTTARGET", "dlZhangHao$ctl01$lblmore"));
			requestSettings11.getRequestParameters().add(new NameValuePair("__EVENTARGUMENT", __EVENTARGUMENT));
			requestSettings11.getRequestParameters().add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
			requestSettings11.getRequestParameters().add(new NameValuePair("__VIEWSTATEENCRYPTED", __VIEWSTATEENCRYPTED));
			requestSettings11.getRequestParameters().add(new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
			Page page11 = webClient.getPage(requestSettings11);
			String contentAsString1 = page11.getWebResponse().getContentAsString();
            System.out.println(contentAsString1);
			
			HousingsanmingHtml housingsanmingHtml1 = new HousingsanmingHtml();
			housingsanmingHtml1.setHtml(contentAsString1);
			housingsanmingHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingsanmingHtml1.setType("流水信息");
			housingsanmingHtml1.setUrl(jcmx1);
			housingsanmingHtmlRepository.save(housingsanmingHtml1);

			System.out.println("流水信息----" + contentAsString1);
			if (contentAsString1.contains("id=\"gridView\"")) {
				System.out.println("流水信息获取成功！");
				
				Document doc2 = Jsoup.parse(contentAsString1);
				Element table = doc2.getElementById("gridView");
				Elements trs = table.getElementsByTag("tr");
				for (int i = 1; i < trs.size(); i++) {
					Elements tds = trs.get(i).getElementsByTag("td");
					for (int j = 0; j < tds.size(); j+=7) {
						//账号
						String zh = tds.get(j).text();
						System.out.println("账号："+zh);
						//日期
						String rq = tds.get(j+1).text();
						System.out.println("日期："+rq);
						//摘要
						String zy = tds.get(j+2).text();
						System.out.println("摘要："+zy);
						//支取金额
						String zqje = tds.get(j+3).text();
						System.out.println("支取金额："+zqje);
						//缴存金额
						String jcje = tds.get(j+4).text();
						System.out.println("缴存金额："+jcje);
						//金额
						String je = tds.get(j+5).text();
						System.out.println("金额："+je);
						//账别
						String zb = tds.get(j+6).text();
						System.out.println("账别："+zb);
						System.out.println("=====================");
						HousingsanmingPay housingsanmingPay = new HousingsanmingPay();
						housingsanmingPay.setTaskid(messageLoginForHousing.getTask_id().trim());
						housingsanmingPay.setZb(zb);
						housingsanmingPay.setJe(je);
						housingsanmingPay.setJcje(jcje);
						housingsanmingPay.setZqje(zqje);
						housingsanmingPay.setZy(zy);
						housingsanmingPay.setRq(rq);
						housingsanmingPay.setZh(zh);
						housingsanmingPayRepository.save(housingsanmingPay);
					}
					
				}
				taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());
			} else {
				System.out.println("流水信息获取失败！");
			}
			// 流水2入参的请求
			String jcmx12 = "http://www.smgjj.com/MemberFunction/PersonFunction/PersonAcfSerach.aspx?PageType=Add&SiteTicket=";
			WebRequest requestSettings112 = new WebRequest(new URL(jcmx12), HttpMethod.POST);
			requestSettings112.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings112.getRequestParameters().add(new NameValuePair("form1", form1));
			requestSettings112.getRequestParameters().add(new NameValuePair("__EVENTTARGET", "dlZhangHao$ctl02$lblmore"));
			requestSettings112.getRequestParameters().add(new NameValuePair("__EVENTARGUMENT", __EVENTARGUMENT));
			requestSettings112.getRequestParameters().add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
			requestSettings112.getRequestParameters().add(new NameValuePair("__VIEWSTATEENCRYPTED", __VIEWSTATEENCRYPTED));
			requestSettings112.getRequestParameters().add(new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
			Page page112 = webClient.getPage(requestSettings112);
			String contentAsString12 = page112.getWebResponse().getContentAsString();
			
			
			HousingsanmingHtml housingsanmingHtml12 = new HousingsanmingHtml();
			housingsanmingHtml12.setHtml(contentAsString12);
			housingsanmingHtml12.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingsanmingHtml12.setType("流水信息");
			housingsanmingHtml12.setUrl(jcmx12);
			housingsanmingHtmlRepository.save(housingsanmingHtml12);
			
			System.out.println("流水信息----" + contentAsString12);
			if (contentAsString12.contains("id=\"gridView\"")) {
				System.out.println("流水信息获取成功！");
				Document doc2 = Jsoup.parse(contentAsString12);
				Element table = doc2.getElementById("gridView");
				Elements trs = table.getElementsByTag("tr");
				for (int i = 1; i < trs.size(); i++) {
					Elements tds = trs.get(i).getElementsByTag("td");
					for (int j = 0; j < tds.size(); j+=7) {
						//账号
						String zh = tds.get(j).text();
						System.out.println("账号："+zh);
						//日期
						String rq = tds.get(j+1).text();
						System.out.println("日期："+rq);
						//摘要
						String zy = tds.get(j+2).text();
						System.out.println("摘要："+zy);
						//支取金额
						String zqje = tds.get(j+3).text();
						System.out.println("支取金额："+zqje);
						//缴存金额
						String jcje = tds.get(j+4).text();
						System.out.println("缴存金额："+jcje);
						//金额
						String je = tds.get(j+5).text();
						System.out.println("金额："+je);
						//账别
						String zb = tds.get(j+6).text();
						System.out.println("账别："+zb);
						System.out.println("=====================");
						HousingsanmingPay housingsanmingPay = new HousingsanmingPay();
						housingsanmingPay.setTaskid(messageLoginForHousing.getTask_id().trim());
						housingsanmingPay.setZb(zb);
						housingsanmingPay.setJe(je);
						housingsanmingPay.setJcje(jcje);
						housingsanmingPay.setZqje(zqje);
						housingsanmingPay.setZy(zy);
						housingsanmingPay.setRq(rq);
						housingsanmingPay.setZh(zh);
						housingsanmingPayRepository.save(housingsanmingPay);
					}
					
				}
				
				taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());
				
			} else {
				System.out.println("流水信息获取失败！");
			}
			// 流水3入参的请求
			String jcmx123 = "http://www.smgjj.com/MemberFunction/PersonFunction/PersonAcfSerach.aspx?PageType=Add&SiteTicket=";
			WebRequest requestSettings1123 = new WebRequest(new URL(jcmx123), HttpMethod.POST);
			requestSettings1123.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings1123.getRequestParameters().add(new NameValuePair("form1", form1));
			requestSettings1123.getRequestParameters().add(new NameValuePair("__EVENTTARGET", "dlZhangHao$ctl03$lblmore"));
			requestSettings1123.getRequestParameters().add(new NameValuePair("__EVENTARGUMENT", __EVENTARGUMENT));
			requestSettings1123.getRequestParameters().add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
			requestSettings1123.getRequestParameters().add(new NameValuePair("__VIEWSTATEENCRYPTED", __VIEWSTATEENCRYPTED));
			requestSettings1123.getRequestParameters().add(new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
			Page page1123 = webClient.getPage(requestSettings1123);
			String contentAsString123 = page1123.getWebResponse().getContentAsString();
			
			
			HousingsanmingHtml housingsanmingHtml123 = new HousingsanmingHtml();
			housingsanmingHtml123.setHtml(contentAsString123);
			housingsanmingHtml123.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingsanmingHtml123.setType("流水信息");
			housingsanmingHtml123.setUrl(jcmx123);
			housingsanmingHtmlRepository.save(housingsanmingHtml123);
			
			System.out.println("流水信息----" + contentAsString123);
			if (contentAsString123.contains("id=\"gridView\"")) {
				System.out.println("流水信息获取成功！");
				Document doc2 = Jsoup.parse(contentAsString123);
				Element table = doc2.getElementById("gridView");
				Elements trs = table.getElementsByTag("tr");
				for (int i = 1; i < trs.size(); i++) {
					Elements tds = trs.get(i).getElementsByTag("td");
					for (int j = 0; j < tds.size(); j+=7) {
						//账号
						String zh = tds.get(j).text();
						System.out.println("账号："+zh);
						//日期
						String rq = tds.get(j+1).text();
						System.out.println("日期："+rq);
						//摘要
						String zy = tds.get(j+2).text();
						System.out.println("摘要："+zy);
						//支取金额
						String zqje = tds.get(j+3).text();
						System.out.println("支取金额："+zqje);
						//缴存金额
						String jcje = tds.get(j+4).text();
						System.out.println("缴存金额："+jcje);
						//金额
						String je = tds.get(j+5).text();
						System.out.println("金额："+je);
						//账别
						String zb = tds.get(j+6).text();
						System.out.println("账别："+zb);
						System.out.println("=====================");
						HousingsanmingPay housingsanmingPay = new HousingsanmingPay();
						housingsanmingPay.setTaskid(messageLoginForHousing.getTask_id().trim());
						housingsanmingPay.setZb(zb);
						housingsanmingPay.setJe(je);
						housingsanmingPay.setJcje(jcje);
						housingsanmingPay.setZqje(zqje);
						housingsanmingPay.setZy(zy);
						housingsanmingPay.setRq(rq);
						housingsanmingPay.setZh(zh);
						housingsanmingPayRepository.save(housingsanmingPay);
					}
				}
				taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());
				
			} else {
				System.out.println("流水信息获取失败！");
			}
			taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}