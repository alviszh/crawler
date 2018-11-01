package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import com.microservice.dao.entity.crawler.housing.panzhihua.HousingPanzhihuaBase;
import com.microservice.dao.entity.crawler.housing.panzhihua.HousingPanzhihuaHtml;
import com.microservice.dao.entity.crawler.housing.panzhihua.HousingPanzhihuaPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.panzhihua.HousingPanzhihuaBaseRepository;
import com.microservice.dao.repository.crawler.housing.panzhihua.HousingPanzhihuaHtmlRepository;
import com.microservice.dao.repository.crawler.housing.panzhihua.HousingPanzhihuaPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.panzhihua")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.panzhihua")
public class HousingFundPanZhiHuaService extends AbstractChaoJiYingHandler implements ICrawlerLogin {
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundPanZhiHuaService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingPanzhihuaHtmlRepository housingPanZhiHuaHtmlRepository;
	@Autowired
	public HousingPanzhihuaBaseRepository housingPanZhiHuaBaseRepository;
	@Autowired
	public HousingPanzhihuaPayRepository housingPanZhiHuaPayRepository;
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

			// 登录请求
			String loginurl3 = "http://www.pzhgjj.com/pzhnt/login.do?r=0.6364636803285223";
			String requestBody = "username="+messageLoginForHousing.getNum()+"&password="+messageLoginForHousing.getPassword()+"&loginType=4&vertcode=&bsr=&vertype=1";
//			String requestBody = "username="+messageLoginForHousing.getNum().trim()+"&password="+messageLoginForHousing.getPassword().trim()+"&loginType=4&vertcode=&bsr=&vertype=1";
			WebRequest requestSettings1 = new WebRequest(new URL(loginurl3), HttpMethod.POST);
			requestSettings1.setRequestBody(requestBody);
			Page pageq1 = webClient.getPage(requestSettings1);
			String contentAsString2 = pageq1.getWebResponse().getContentAsString();
			System.out.println(contentAsString2);
			if (contentAsString2.contains("无效的身份证号码")) {
				System.out.println("登陆失败！无效的身份证号码！");
				taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getError_code());
				taskHousingRepository.save(taskHousing);

			} else if (contentAsString2.contains("用户名或密码错误")) {
				System.out.println("登陆失败！用户名错误！");
				taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getError_code());
				taskHousingRepository.save(taskHousing);
			} else if (contentAsString2.contains("用户名/密码错误")) {
				System.out.println("登陆失败！密码错误！");
				taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_PWD_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_PWD_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_PWD_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeEnum.HOUSING_LOGIN_PWD_ERROR.getError_code());
				taskHousingRepository.save(taskHousing);
			} else {
				if (contentAsString2.contains("网上业务大厅")) {
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
		// 登录请求
		String jbxx = "http://www.pzhgjj.com/pzhnt/per/queryPerInfo.do";
		try {
			WebRequest requestSettings = new WebRequest(new URL(jbxx), HttpMethod.GET);
			Page page = webClient.getPage(requestSettings);

			String contentAsString = page.getWebResponse().getContentAsString();
			System.out.println("基本信息----" + contentAsString);
			HousingPanzhihuaHtml housingPanZhiHuaHtml = new HousingPanzhihuaHtml();
			housingPanZhiHuaHtml.setHtml(contentAsString + "");
			housingPanZhiHuaHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingPanZhiHuaHtml.setType("基本信息");
			housingPanZhiHuaHtml.setUrl(jbxx);
			housingPanZhiHuaHtmlRepository.save(housingPanZhiHuaHtml);

			if (contentAsString.contains("id=\"user_info_table\"")) {
				System.out.println("基本信息获取成功！");
				Document doc = Jsoup.parse(contentAsString);
				// 单位名称
				String corpcode2 = doc.getElementById("corpcode2").text();
				System.out.println("单位名称---" + corpcode2);
				// 单位账号
				String corpcode = doc.getElementById("corpcode").text();
				System.out.println("单位账号---" + corpcode);
				// 缴存管理部
				String depname = doc.getElementById("depname").text();
				System.out.println("缴存管理部---" + depname);
				// 缴存银行
				String bkname = doc.getElementById("bkname").text();
				System.out.println("缴存银行---" + bkname);
				// 缴存基数
				String bmny = doc.getElementById("bmny").text();
				System.out.println("缴存基数---" + bmny);
				// 缴存比例
				String jcbl = "个人：" + doc.getElementById("perperscale").text() + "%  单位："
						+ doc.getElementById("percorpscale").text();
				System.out.println("缴存比例---" + jcbl);
				// 工资基数
				String gzjs = "";
				String[] split = contentAsString.split("工资基数");
				String[] split2 = split[1].split("<td id=\"bkname\">");
				String[] split3 = split2[1].split("</td>");
				gzjs = split3[0].trim();
				System.out.println("工资基数---" + gzjs);
				// 月汇缴额
				String yhje = "个人：" + doc.getElementById("perdepmny").text() + "元    单位："
						+ doc.getElementById("corpdepmny").text() + "元    合计：" + doc.getElementById("depmny").text();
				System.out.println("月汇缴额---" + yhje);
				// 缴存余额
				String accbal = doc.getElementById("accbal").text();
				System.out.println("缴存余额---" + accbal);
				// 账户状态
				String accstate = doc.getElementById("accstate").text();
				System.out.println("账户状态---" + accstate);
				// 开户日期
				String regtime = doc.getElementById("regtime").text();
				System.out.println("开户日期---" + regtime);
				// 缴至年月
				String payendmnh = doc.getElementById("payendmnh").text();
				System.out.println("缴至年月---" + payendmnh);
				// 绑定银行
				String bkcardname = doc.getElementById("bkcardname").text();
				System.out.println("绑定银行---" + bkcardname);
				// 绑定银行卡号
				String bkcard = doc.getElementById("bkcard").text();
				System.out.println("绑定银行卡号---" + bkcard);

				HousingPanzhihuaBase housingPanZhiHuaBase = new HousingPanzhihuaBase();
				housingPanZhiHuaBase.setTaskid(messageLoginForHousing.getTask_id().trim());
				housingPanZhiHuaBase.setDwmc(corpcode2);
				housingPanZhiHuaBase.setDwzh(corpcode);
				housingPanZhiHuaBase.setJcglb(depname);
				housingPanZhiHuaBase.setJcyh(bkname);
				housingPanZhiHuaBase.setJcjs(bmny);
				housingPanZhiHuaBase.setJcbl(jcbl);
				housingPanZhiHuaBase.setGzjs(gzjs);
				housingPanZhiHuaBase.setYhje(yhje);
				housingPanZhiHuaBase.setJcye(accbal);
				housingPanZhiHuaBase.setZhzt(accstate);
				housingPanZhiHuaBase.setKhrq(regtime);
				housingPanZhiHuaBase.setJzny(payendmnh);
				housingPanZhiHuaBase.setBdyh(bkcardname);
				housingPanZhiHuaBase.setBdyhkh(bkcard);
				housingPanZhiHuaBaseRepository.save(housingPanZhiHuaBase);

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
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -0);
			String beforeMonth = df.format(c.getTime());
			// 获取当前的前3年
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.YEAR, -3);
			String beforeMonth1 = df1.format(c1.getTime());
			// 流水请求
			String jcmx = "http://www.pzhgjj.com/pzhnt/per/perPayRecord!getPerAccDetails.do";
			String requestBody = "dto%5B'smnh'%5D=" + beforeMonth1 + "&dto%5B'emnh'%5D=" + beforeMonth + "&&";
			WebRequest requestSettings1 = new WebRequest(new URL(jcmx), HttpMethod.POST);
			requestSettings1.setRequestBody(requestBody);
			Page page1 = webClient.getPage(requestSettings1);
			String contentAsString = page1.getWebResponse().getContentAsString();

			HousingPanzhihuaHtml housingPanZhiHuaHtml1 = new HousingPanzhihuaHtml();
			housingPanZhiHuaHtml1.setHtml(contentAsString);
			housingPanZhiHuaHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingPanZhiHuaHtml1.setType("流水信息");
			housingPanZhiHuaHtml1.setUrl(jcmx);
			housingPanZhiHuaHtmlRepository.save(housingPanZhiHuaHtml1);

			System.out.println("流水信息----" + contentAsString);
			if (contentAsString.contains("\"success\":true")) {
				System.out.println("流水信息获取成功！");
				JSONObject json = JSONObject.fromObject(contentAsString.trim());
				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists.trim());
				String datalist = json2.getString("datalist").trim();
				JSONObject json3 = JSONObject.fromObject(datalist.trim());
				String list = json3.getString("list").trim();
				JSONArray jsonarray = JSONArray.fromObject(list);
				for (int i = 0; i < jsonarray.size(); i++) {
					String base = jsonarray.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(base.trim());
					// 年月
					String month = json4.getString("paybmnh").trim();
					System.out.println("年月-----" + month);
					// 入账时间
					String rzsj = json4.getString("acctime").trim();
					System.out.println("入账时间-----" + rzsj);
					// 单位名称
					String dwmc = json4.getString("corpname").trim();
					System.out.println("单位名称-----" + dwmc);
					// 业务类型
					String ywlx = "";
					String yewuleixing = json4.getString("bustype").trim();
					if("1".equals(yewuleixing)){
						ywlx = "汇缴";
					}
					System.out.println("业务类型-----" + ywlx);
					// 个人月缴存额
					String gryjce = json4.getString("perdepmny").trim();
					System.out.println("个人月缴存额-----" + gryjce);
					// 单位月缴存额
					String dwyjce = json4.getString("corpdepmny").trim();
					System.out.println("单位月缴存额-----" + dwyjce);
					// 合计月缴存额
					String hjyjce = json4.getString("depmny").trim();
					System.out.println("合计月缴存额-----" + hjyjce);
					// 备注
					String bz = json4.getString("remark").trim();
					System.out.println("备注-----" + bz);
					HousingPanzhihuaPay housingPanZhiHuaPay = new HousingPanzhihuaPay();
					housingPanZhiHuaPay.setTaskid(messageLoginForHousing.getTask_id().trim());
					housingPanZhiHuaPay.setMonth(month);
					housingPanZhiHuaPay.setRzsj(rzsj);
					housingPanZhiHuaPay.setDwmc(dwmc);
					housingPanZhiHuaPay.setYwlx(ywlx);
					housingPanZhiHuaPay.setGryjce(gryjce);
					housingPanZhiHuaPay.setDwyjce(dwyjce);
					housingPanZhiHuaPay.setHjyjce(hjyjce);
					housingPanZhiHuaPay.setBz(bz);
					housingPanZhiHuaPayRepository.save(housingPanZhiHuaPay);
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