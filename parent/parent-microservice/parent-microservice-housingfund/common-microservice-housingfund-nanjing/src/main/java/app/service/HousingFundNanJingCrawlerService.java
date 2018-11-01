package app.service;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.nanjing.HousingNanJingDetailAccount;
import com.microservice.dao.entity.crawler.housing.nanjing.HousingNanJingHtml;
import com.microservice.dao.entity.crawler.housing.nanjing.HousingNanJingUserInfo;
import com.microservice.dao.repository.crawler.housing.nanjing.HousingNanJingDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.nanjing.HousingNanJingHtmlRepository;
import com.microservice.dao.repository.crawler.housing.nanjing.HousingNanJingUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundNanJingParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月26日 下午4:10:37 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.nanjing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.nanjing"})
public class HousingFundNanJingCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundNanJingCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundNanJingParser housingFundNanJingParser;
	@Autowired
	private HousingNanJingHtmlRepository housingNanJingHtmlRepository;
	@Autowired
	private HousingNanJingDetailAccountRepository housingNanJingDetailAccountRepository;
	@Autowired
	private HousingNanJingUserInfoRepository housingNanJingUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Async
	public void getUserInfo(TaskHousing taskHousing){
		try {
			//获取爬取数据需要的cookie
			WebClient webClient = housingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			String url = "http://www.njgjj.com/init.summer?_PROCID=80000003"; 
	 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
	 		HtmlPage hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			String html = hPage.getWebResponse().getContentAsString(); 
	 			HousingNanJingHtml housingNanJingHtml=new HousingNanJingHtml();
	 			housingNanJingHtml.setHtml(html);
	 			housingNanJingHtml.setPagenumber(1);
	 			housingNanJingHtml.setTaskid(taskHousing.getTaskid());
	 			housingNanJingHtml.setType("用户基本信息所需参数源码页");
	 			housingNanJingHtml.setUrl(url);
	 			housingNanJingHtmlRepository.save(housingNanJingHtml);
				tracer.addTag("action.crawler.getUserInfo.params.html", "获取用户基本信息所需参数源码页已经入库");
	 			if(html.contains("个人姓名")){
	 				Document doc=Jsoup.parse(html);
	 				//获取姓名
	 				String accName= doc.getElementById("xingming").val();
	 				//获取个人账号
	 				String accNum= doc.getElementById("grzh").val();
	 				//获取身份证号
	 				String certiNum= doc.getElementById("zjhm").val();
	 				//获取最终的数据，用如上参数最为最终的返回结果：
	 				String  urlSecond="http://www.njgjj.com/command.summer?uuid=1509009664900";
	 				webRequest= new WebRequest(new URL(urlSecond), HttpMethod.POST);  
	 				String requestBody="xingming="+accName+"&"
	 						+ "grzh="+accNum+""
	 						+ "&prodcode=1&certinum="+certiNum+""
	 						+ "&unitaccname=&unitaccnum1=&cardnocsp=&smsflag="
	 						+ "&linkphone=&opnaccdate=&indiaccstate="
	 						+ "&monamt3=&bal=&lpaym=&indiprop="
	 						+ "&unitprop=&unitopnaccdate="
	 						+ "&unitaccstate1=&opnaccnet="
	 						+ "&position="
	 						+ "&_PROCID=80000003"  //必要参数
	 						+ "&_PAGEID=step1"
	 						+"&_TYPE=init";
	 				webRequest.setRequestBody(requestBody);
	 				Page page2 = webClient.getPage(webRequest);
	 				if(page2!=null){
	 					html=page2.getWebResponse().getContentAsString();
	 	 				housingNanJingHtml=new HousingNanJingHtml();
	 	 	 			housingNanJingHtml.setHtml(html);
	 	 	 			housingNanJingHtml.setPagenumber(1);
	 	 	 			housingNanJingHtml.setTaskid(taskHousing.getTaskid());
	 	 	 			housingNanJingHtml.setType("用户基本信息源码页");
	 	 	 			housingNanJingHtml.setUrl(url);
	 	 	 			housingNanJingHtmlRepository.save(housingNanJingHtml);
	 					tracer.addTag("action.crawler.getUserInfo.html", "获取的用户基本信息(完整信息)已经入库");
	 	 	 			HousingNanJingUserInfo housingNanJingUserInfo= housingFundNanJingParser.userInfoParser(html,accName,taskHousing);
	 	 	 			if(null!=housingNanJingUserInfo){
	 	 	 				housingNanJingUserInfoRepository.save(housingNanJingUserInfo);
	 	 	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
	 	 	 				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
	 	 	 				//用户信息采集完成之后，将所有返回的数据作为缴费信息的请求参数
	 	 	 				try {
	 	 	 					getDetailAccount(housingNanJingUserInfo,webClient,taskHousing);
	 						} catch (Exception e) {
	 							tracer.addTag("action.crawler.getDetailAccount.e", taskHousing.getTaskid()+"  "+e.toString());
	 							updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid() );
	 							updateTaskHousing(taskHousing.getTaskid());
	 						}
	 	 	 			}else{
	 	 	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
	 	 	 				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
	 	 	 			}
	 				}
	 			}
	 		}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e",e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
			updatePayStatusByTaskid("数据采集中，缴费信息已采集完成",201,taskHousing.getTaskid());
			//此处也要更新最终爬取状态
			updateTaskHousing(taskHousing.getTaskid());
		}
	}
	public void getDetailAccount(HousingNanJingUserInfo housingNanJingUserInfo, WebClient webClient, TaskHousing taskHousing) throws Exception {
		String url0="http://www.njgjj.com/command.summer?";
		WebRequest webRequest0 = new WebRequest(new URL(url0), HttpMethod.POST); 
		webRequest0.setAdditionalHeader("Host", "www.njgjj.com");
		webRequest0.setAdditionalHeader("Origin", "http://www.njgjj.com");
		webRequest0.setAdditionalHeader("Referer", "http://www.njgjj.com/init.summer?_PROCID=70000002");
		String presentDate = HousingFundHelperService.getPresentDate();
		String requestBody0="$page=/ydpx/70000002/700002_01.ydpx"
				+ "&_ACCNUM="+housingNanJingUserInfo.getAccnum().trim()+""
				+ "&_RW=w&_PAGEID=step1"
				+ "&_IS=-24167674"
				+ "&_UNITACCNAME="+housingNanJingUserInfo.getUnitaccname().trim()+""
//				+ "&_LOGIP=20171027173924017"  
				+ "&_ACCNAME="+housingNanJingUserInfo.getAccname().trim()+""
				+ "&isSamePer=false"
				+ "&_PROCID=70000002"
				+ "&_SENDOPERID="+housingNanJingUserInfo.getCertinum().trim()+""
				+ "&_DEPUTYIDCARDNUM="+housingNanJingUserInfo.getCertinum().trim()+""
				+ "&_SENDTIME="+presentDate+""
				+ "&_BRANCHKIND=0"
				+ "&_SENDDATE="+presentDate+""
				+ "&CURRENT_SYSTEM_DATE="+presentDate+""
				+ "&_TYPE=init&_ISCROP=0&_PORCNAME=个人明细信息查询"
				+ "&_WITHKEY=0"
				+ "&begdate=2000-01-01"
				+ "&enddate="+presentDate+""
				+ "&grzh="+housingNanJingUserInfo.getAccnum().trim()+""
				+ "&xingming="+housingNanJingUserInfo.getAccname().trim()+"";
		webRequest0.setRequestBody(requestBody0);
		Page page0 = webClient.getPage(webRequest0);
		Thread.sleep(1000);
		if(null!=page0){
			String html0=page0.getWebResponse().getContentAsString();
			if(!html0.contains("\u51fa\u9519:\u65e0\u67e5\u8be2\u7ed3\u679c")){ //无查询结果
		 		String encodeName=URLEncoder.encode(housingNanJingUserInfo.getAccname().trim(), "utf-8");
				String url="http://www.njgjj.com/dynamictable?uuid=1509073184230"
						+ "&dynamicTable_id=datalist2"
						+ "&dynamicTable_currentPage=1"
						+ "&dynamicTable_pageSize=300"  //设置为显示300条数据
						+ "&dynamicTable_nextPage=1"
						+ "&dynamicTable_page=%2Fydpx%2F70000002%2F700002_01.ydpx"
						+ "&dynamicTable_paging=true&dynamicTable_configSqlCheck=0"
						+ "&errorFilter=1%3D1"
						+ "&begdate=2000-01-01"
						+ "&enddate="+presentDate+""
						+ "&grzh="+housingNanJingUserInfo.getAccnum().trim()+""
						+ "&xingming="+encodeName+""
						+ "&_APPLY=0&_CHANNEL=1"
						+ "&_PROCID=70000002"
						+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDJ0AL5zZWxlY3QgaW5zdGFuY2UsIHVuaXRhY2NudW0xLCB1bml0%0AYWNjbmFtZSwgYWNjbnVtMSwgYWNjbmFtZTEsIGNlcnRpbnVtLCB0cmFuc2RhdGUsIHJlYXNvbiAs%0AIGRwYnVzaXR5cGUsIGJhc2VudW0sIHBheXZvdWFtdCwgc2Vxbm8gZnJvbSBkcDA3NyB3aGVyZSBp%0AbnN0YW5jZSA9LTI0MTY3Njc0IG9yZGVyIGJ5IHRyYW5zZGF0ZSBkZXNjeA%3D%3D"
						+ "&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AAdf%0AQUNDTlVNdAAQMzIwMTAwMDM5NTA1NDY1MnQAA19SV3QAAXd0AAtfVU5JVEFDQ05VTXB0AAdfUEFH%0ARUlEdAAFc3RlcDF0AANfSVNzcgAOamF2YS5sYW5nLkxvbmc7i%2BSQzI8j3wIAAUoABXZhbHVleHIA%0AEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhw%2F%2F%2F%2F%2F%2F6POwZ0AAxfVU5JVEFDQ05BTUV0ACTl%0AjZfkuqzmmJPmiY3kurrlipvotYTmupDmnInpmZDlhazlj7h0AAZfTE9HSVB0ABEyMDE3MTAyNzE4%0ANDgzODM5NHQACF9BQ0NOQU1FdAAJ5b2t6YGT55C0dAAJaXNTYW1lUGVydAAFZmFsc2V0AAdfUFJP%0AQ0lEdAAINzAwMDAwMDJ0AAtfU0VORE9QRVJJRHQAEjMyMDEyMzE5ODYwMzA3NDQyMnQAEF9ERVBV%0AVFlJRENBUkROVU10ABIzMjAxMjMxOTg2MDMwNzQ0MjJ0AAlfU0VORFRJTUV0AAoyMDE3LTEwLTI3%0AdAALX0JSQU5DSEtJTkR0AAEwdAAJX1NFTkREQVRFdAAKMjAxNy0xMC0yN3QAE0NVUlJFTlRfU1lT%0AVEVNX0RBVEVxAH4AInQABV9UWVBFdAAEaW5pdHQAB19JU0NST1BxAH4AIHQACV9QT1JDTkFNRXQA%0AGOS4quS6uuaYjue7huS%2FoeaBr%2BafpeivonQAB19VU0JLRVlwdAAIX1dJVEhLRVlxAH4AIHh0AAhA%0AU3lzRGF0ZXQAB0BTeXNEYXl0AAlAU3lzTW9udGh0AAhAU3lzVGltZXQACEBTeXNXZWVrdAAIQFN5%0Ac1llYXI%3D";
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST); 
				webRequest.setAdditionalHeader("Host", "www.njgjj.com");
				webRequest.setAdditionalHeader("Origin", "http://www.njgjj.com");
				webRequest.setAdditionalHeader("Referer", "http://www.njgjj.com/init.summer?_PROCID=70000002");
				Page page = webClient.getPage(webRequest);
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
		 			tracer.addTag("action.crawler.getDetailAccount.html", "获取的2000年至今个人明细账的html ===="+html);
		 			HousingNanJingHtml housingNanJingHtml=new HousingNanJingHtml();
		 			housingNanJingHtml.setHtml(html);
		 			housingNanJingHtml.setPagenumber(1);
		 			housingNanJingHtml.setTaskid(taskHousing.getTaskid());
		 			housingNanJingHtml.setType("2000年至今个人明细账源码页");
		 			housingNanJingHtml.setUrl(url);
		 			housingNanJingHtmlRepository.save(housingNanJingHtml);
		 			List<HousingNanJingDetailAccount> list = housingFundNanJingParser.detailAccountParser(html,taskHousing);
		 			if(null!=list && list.size()>0){
		 				housingNanJingDetailAccountRepository.saveAll(list);
		 				updatePayStatusByTaskid("数据采集中，2000年至今个人明细账信息已采集完成", 200,taskHousing.getTaskid());
		 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，2000年至今个人明细账信息已采集完成");
		 				updateTaskHousing(taskHousing.getTaskid());
		 			}else{
		 				updatePayStatusByTaskid("数据采集中，2000年至今个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
		 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，2000年至今个人明细账信息暂无可采集数据");
		 				updateTaskHousing(taskHousing.getTaskid());
		 			}
				}
			}else{
				//未查询到满足个人条件的信息
	 			updatePayStatusByTaskid("数据采集中，2000年至今个人明细账信息已采集完成",201,taskHousing.getTaskid() );
				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，2000年至今个人明细账信息已采集完成");
				updateTaskHousing(taskHousing.getTaskid());
			}
		}
	}
}
