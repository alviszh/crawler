package app.service;

import java.net.URL;

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
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yantai.HousingYanTaiBase;
import com.microservice.dao.entity.crawler.housing.yantai.HousingYanTaiHtml;
import com.microservice.dao.entity.crawler.housing.yantai.HousingYanTaiPay;
import com.microservice.dao.repository.crawler.housing.yantai.HousingYanTaiBaseRepository;
import com.microservice.dao.repository.crawler.housing.yantai.HousingYanTaiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yantai.HousingYanTaiPayRepository;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yantai")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yantai")
public class HousingFundYanTaiService extends HousingBasicService implements ICrawler {
	/** 烟台公积金登录的URL */
	public static final String LoginPage = "https://cx.zfgjj.cn/dzyw-grwt/index.do";

	public static final Logger log = LoggerFactory.getLogger(HousingFundYanTaiService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingYanTaiHtmlRepository housingYanTaiHtmlRepository;
	@Autowired
	public HousingYanTaiBaseRepository housingYanTaiBaseRepository;
	@Autowired
	public HousingYanTaiPayRepository housingYanTaiPayRepository;
	@Autowired
	public HousingBasicService housingBasicService;

	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		tracer.addTag("service.crawler.taskid", messageLoginForHousing.getTask_id().trim());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://www.ytgjj.com/module/search/gjjsearch.jsp";
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage html = webClient.getPage(webRequest);

			String htmlasXml = html.asXml();
			System.out.println(htmlasXml);
			
			// 身份证号
			HtmlTextInput IDcard = (HtmlTextInput) html.getFirstByXPath("//input[@id='IDcard']");
			// 姓名
			HtmlTextInput name = (HtmlTextInput) html.getFirstByXPath("//input[@id='acount']");
			// 查询按钮
			HtmlElement button = (HtmlElement) html.getFirstByXPath("//input[@id='imageField']");
			// 验证码输入框
			HtmlTextInput trand = (HtmlTextInput) html.getFirstByXPath("//input[@id='trand']");
			// 图片
			HtmlImage randImage = (HtmlImage) html.getFirstByXPath("//img[@id='randImage']");
			randImage.click();
			String verifycode = chaoJiYingOcrService.getVerifycode(randImage, "1004");

			String num = messageLoginForHousing.getNum();
			String username = messageLoginForHousing.getUsername();
			IDcard.setText(num);
			name.setText(username);
			trand.setText(verifycode);

			HtmlPage htmlpage = button.click();

			Thread.sleep(2000);

			String alertMsg = WebCrawler.getAlertMsg();
			String asXml = htmlpage.asXml();

			// 查询账户明细
			HtmlAnchor button2 = (HtmlAnchor) htmlpage.getFirstByXPath("//a[@class='link03']");
			HtmlPage click = button2.click();
			String clickasxml = click.asXml();
			Document docq = Jsoup.parse(clickasxml);
			Elements select1 = docq.select("table");
			if (select1.size() > 25) {
				System.out.println("获取数据成功！");
				Element element = select1.get(25);
				Elements tr = element.select("tr");
				for (int i = 1; i < tr.size(); i++) {
					Elements select = tr.get(i).select("td");
					for (int j = 0; j < select.size(); j += 6) {
						// 个人账号
						String person_number = select.get(j).html();
						// 记账时间
						String date = select.get(j + 1).html();
						// 收支类型
						String type = select.get(j + 2).html();
						// 月份
						String month = select.get(j + 3).html();
						// 发生额
						String open_money = select.get(j + 4).html();
						// 余额
						String yue = select.get(j + 5).html();
						System.out.println(person_number);
						System.out.println(date);
						System.out.println(type);
						System.out.println(month);
						System.out.println(open_money);
						System.out.println(yue);
						HousingYanTaiPay housingYanTaiPay = new HousingYanTaiPay();
						housingYanTaiPay.setTaskid(messageLoginForHousing.getTask_id().trim());
						housingYanTaiPay.setPerson_number(person_number);
						housingYanTaiPay.setDate(date);
						housingYanTaiPay.setType(type);
						housingYanTaiPay.setMonth(month);
						housingYanTaiPay.setOpen_money(open_money);
						housingYanTaiPay.setYue(yue);
						housingYanTaiPayRepository.save(housingYanTaiPay);
					}

				}

				TaskHousing findByTaskid = taskHousingRepository
						.findByTaskid(messageLoginForHousing.getTask_id().trim());
				findByTaskid.setCity("烟台市");
				findByTaskid.setDescription("缴费信息数据采集成功！");
				findByTaskid.setLogintype(messageLoginForHousing.getLogintype());
				findByTaskid.setPhase("CRAWLER");
				findByTaskid.setPhase_status("SUCCESS");
				findByTaskid.setError_code(0);
				findByTaskid.setPaymentStatus(200);
				taskHousingRepository.save(findByTaskid);

			} else {
				System.out.println("获取数据失败！");
				TaskHousing findByTaskid = taskHousingRepository
						.findByTaskid(messageLoginForHousing.getTask_id().trim());
				findByTaskid.setCity("烟台市");
				findByTaskid.setDescription("缴费信息数据采集失败！");
				findByTaskid.setLogintype(messageLoginForHousing.getLogintype());
				findByTaskid.setPhase("CRAWLER");
				findByTaskid.setPhase_status("FAIL");
				findByTaskid.setError_code(2);
				findByTaskid.setPaymentStatus(404);
				taskHousingRepository.save(findByTaskid);
			}

			if (alertMsg.contains("验证码有误，点击数字刷新！")) {
				System.out.println("查询失败！验证码有误！");

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR5.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR5.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR5.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR5.getError_code());

				save(taskHousing);
			} else {

				if (asXml.contains("没有查询到相关数据，请确认个人信息！")) {
					System.out.println("查询失败！身份证号码输入有误！");

					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getError_code());

					save(taskHousing);

				} else {
					Document doc = Jsoup.parse(asXml);
					Element table = doc.getElementById("Content");
					if (null != table) {
						Document doc2 = Jsoup.parse(table + "");
						Elements doc22 = doc2.getElementsByTag("div");
						if (null != doc22) {
							System.out.println("获取数据成功！");

							HousingYanTaiHtml housingYanTaiHtml = new HousingYanTaiHtml();
							housingYanTaiHtml.setHtml(asXml);
							housingYanTaiHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
							housingYanTaiHtml.setType("基本信息");
							housingYanTaiHtml.setUrl(url);
							housingYanTaiHtmlRepository.save(housingYanTaiHtml);

							Elements tables = doc2.getElementsByTag("table");
							Element element = tables.get(1);
							Elements elementsByTag = element.getElementsByTag("td");
							// 单位名称
							String company = elementsByTag.get(1).text();
							System.out.println("单位名称-----"+company);
							// 单位账号
							String company_number = elementsByTag.get(3).text();
							System.out.println("单位账号-----"+company_number);
							// 职工姓名
							String name2 = elementsByTag.get(5).text();
							System.out.println("职工姓名-----"+name2);
							// 获取账户状态
							String status = elementsByTag.get(7).text();
							System.out.println("获取账户状态-----"+status);
							// 证件类型
							String certificate_type = elementsByTag.get(9).text();
							System.out.println("证件类型-----"+certificate_type);
							// 身份证号
							String card = elementsByTag.get(11).text();
							System.out.println("身份证号-----"+card);
							// 缴存基数
							String pay_cardinal = elementsByTag.get(13).text();
							System.out.println("缴存基数-----"+pay_cardinal);
							// 账户余额
							String yue = elementsByTag.get(15).text();
							System.out.println("账户余额-----"+yue);
							// 单位缴存比例
							String company_proportion = elementsByTag.get(17).text();
							System.out.println("单位缴存比例-----"+company_proportion);
							// 个人缴存比例
							String person_proportion = elementsByTag.get(19).text();
							System.out.println("个人缴存比例-----"+person_proportion);

							HousingYanTaiBase housingYanTaiBase = new HousingYanTaiBase();
							housingYanTaiBase.setTaskid(messageLoginForHousing.getTask_id().trim());
							housingYanTaiBase.setPerson_proportion(person_proportion);
							housingYanTaiBase.setYue(yue);
							housingYanTaiBase.setCard(card);
							housingYanTaiBase.setCompany_number(company_number);
							housingYanTaiBase.setCompany_proportion(company_proportion);
							housingYanTaiBase.setPay_cardinal(pay_cardinal);
							housingYanTaiBase.setCertificate_type(certificate_type);
							housingYanTaiBase.setName(name2);
							housingYanTaiBase.setCompany(company);
							housingYanTaiBase.setStatus(status);

							housingYanTaiBaseRepository.save(housingYanTaiBase);

							TaskHousing findByTaskid = taskHousingRepository
									.findByTaskid(messageLoginForHousing.getTask_id().trim());
							findByTaskid.setCity("烟台市");
							findByTaskid.setDescription("基本信息数据采集成功！");
							findByTaskid.setLogintype(messageLoginForHousing.getLogintype());
							findByTaskid.setPhase("CRAWLER");
							findByTaskid.setPhase_status("SUCCESS");
							findByTaskid.setError_code(0);
							findByTaskid.setUserinfoStatus(200);
							taskHousingRepository.save(findByTaskid);

						} else {
							System.out.println("获取数据失败！");

							TaskHousing findByTaskid = taskHousingRepository
									.findByTaskid(messageLoginForHousing.getTask_id().trim());
							findByTaskid.setCity("烟台市");
							findByTaskid.setDescription("基本信息数据采集失败！");
							findByTaskid.setLogintype(messageLoginForHousing.getLogintype());
							findByTaskid.setPhase("CRAWLER");
							findByTaskid.setPhase_status("FAIL");
							findByTaskid.setError_code(2);
							findByTaskid.setUserinfoStatus(404);
							taskHousingRepository.save(findByTaskid);

						}
					} else {
						System.out.println("获取数据失败！");

						TaskHousing findByTaskid = taskHousingRepository
								.findByTaskid(messageLoginForHousing.getTask_id().trim());
						findByTaskid.setCity("烟台市");
						findByTaskid.setDescription("基本信息数据采集失败！");
						findByTaskid.setLogintype(messageLoginForHousing.getLogintype());
						findByTaskid.setPhase("CRAWLER");
						findByTaskid.setPhase_status("FAIL");
						findByTaskid.setError_code(2);
						findByTaskid.setUserinfoStatus(404);
						taskHousingRepository.save(findByTaskid);

					}

				}

			}
			// 更新最后的状态
			taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getError_code());
			save(taskHousing);
		}
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}