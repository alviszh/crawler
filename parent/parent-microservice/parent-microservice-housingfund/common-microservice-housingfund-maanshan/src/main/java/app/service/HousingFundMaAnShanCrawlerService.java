package app.service;

import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.microservice.dao.entity.crawler.housing.maanshan.HousingMaAnShanDetailAccount;
import com.microservice.dao.entity.crawler.housing.maanshan.HousingMaAnShanHtml;
import com.microservice.dao.entity.crawler.housing.maanshan.HousingMaAnShanUserInfo;
import com.microservice.dao.repository.crawler.housing.maanshan.HousingMaAnShanDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.maanshan.HousingMaAnShanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.maanshan.HousingMaAnShanUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundMaAnShanParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;


/**
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.maanshan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.maanshan"})
public class HousingFundMaAnShanCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundMaAnShanCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundMaAnShanParser housingFundMaAnShanParser;
	@Autowired
	private HousingMaAnShanHtmlRepository housingMaAnShanHtmlRepository;
	@Autowired
	private HousingMaAnShanDetailAccountRepository housingMaAnShanDetailAccountRepository;
	@Autowired
	private HousingMaAnShanUserInfoRepository housingMaAnShanUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	@Async
	public void getUserInfo(TaskHousing taskHousing) {
		//获取爬取数据需要的cookie
		WebClient webClient = housingFundHelperService.addcookie(taskHousing);
		String url = "http://"+loginHost+"/maswt/init.summer?_PROCID=70000013"; 
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
	 		webClient.getOptions().setJavaScriptEnabled(false);
	 		HtmlPage hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			String html = hPage.getWebResponse().getContentAsString(); 
	 			if(html.contains("个人账号")){
	 				Document doc=Jsoup.parse(html);
	 				//获取个人账号
	 				String accNum= doc.getElementById("grzh").val();
	 				//获取最终的信息
	 				String  urlSecond="http://"+loginHost+"/maswt/command.summer?uuid=1524621572218";
	 				webRequest= new WebRequest(new URL(urlSecond), HttpMethod.POST);  
	 				String requestBody="$page=/ydpx/70000013/700013_01.ydpx&_RW=w&_BRANCHKIND=0"
	 						+ "&_ACCNUM="+accNum+""
	 						+ "&_PAGEID=step1&_PORCNAME=%E4%B8%AA%E4%BA%BA%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
	 						+ "&_PROCID=70000013&_IS=-121855&_ISCROP=0&_TYPE=init&_WITHKEY=0&isSamePer=false"
	 						+ "&grzh="+accNum+""
	 						+ "&xingming=&zjhm=&grzhzt=&grzhye=&khrq=&unitprop=&indiprop=&grjcjs=&yjce=&dwyjce="
	 						+ "&gryjce=&jzny=&dwzh=&unitaccname=&isloanflag=&sjhm=&jkhtbh=&useflag=&cardno="
	 						+ "&frzflag=&flag=&smrzbs=&DealSeq=1";
	 				webRequest.setRequestBody(requestBody);
	 				Page page = webClient.getPage(webRequest);
	 				if(page!=null){
	 					html=page.getWebResponse().getContentAsString();
	 	 				HousingMaAnShanHtml housingMaAnShanHtml=new HousingMaAnShanHtml();
	 	 	 			housingMaAnShanHtml.setHtml(html);
	 	 	 			housingMaAnShanHtml.setPagenumber(1);
	 	 	 			housingMaAnShanHtml.setTaskid(taskHousing.getTaskid());
	 	 	 			housingMaAnShanHtml.setType("用户基本信息源码页");
	 	 	 			housingMaAnShanHtml.setUrl(url);
	 	 	 			housingMaAnShanHtmlRepository.save(housingMaAnShanHtml);
	 	 	 			HousingMaAnShanUserInfo housingMaAnShanUserInfo= housingFundMaAnShanParser.userInfoParser(html,taskHousing);
	 	 	 			if(null!=housingMaAnShanUserInfo){
	 	 	 				housingMaAnShanUserInfoRepository.save(housingMaAnShanUserInfo);
	 	 	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
	 	 	 				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
	 	 	 				//用户信息采集完成之后，将所有返回的数据作为缴费信息的请求参数
	 	 	 				try {
	 	 	 					getDetailAccount(taskHousing,housingMaAnShanUserInfo);
							} catch (Exception e) {
								tracer.addTag("action.crawler.getDetailAccount.e", e.toString());
								updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid() );
								updateTaskHousing(taskHousing.getTaskid());
							}
	 	 	 			}
	 				}
	 			}
	 		}
		} catch (Exception e) {
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
			updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid());
			updateTaskHousing(taskHousing.getTaskid());
		}
	}
	public void getDetailAccount(TaskHousing taskHousing, HousingMaAnShanUserInfo housingMaAnShanUserInfo) throws Exception {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String year = HousingFundHelperService.getYear(0);  //参数中year是当前年份
		String accNum=housingMaAnShanUserInfo.getAccnum().trim();
		//访问这个请求之前需要访问一个带全参数的请求,这个参数中带有要请求的年份
		String url="http://"+loginHost+"/maswt/command.summer?uuid=1524563142385";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);   
		String requestBody="%24page=%2Fydpx%2F70000002%2F700002_01.ydpx"
				+ "&_RW=w"
				+ "&_TYPE=init"
				+ "&_BRANCHKIND=0"
//				+ "&CURRENT_SYSTEM_DATE=2018-04-25"
//				+ "&_SENDOPERID=34050319890511002X"
				+ "&_WITHKEY=0"
				+ "&isSamePer=false"
				+ "&_ACCNUM="+accNum.trim()+""
				+ "&_PAGEID=step1"
//				+ "&_DEPUTYIDCARDNUM=34050319890511002X"
				+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
//				+ "&_LOGIP=20180425100321531"
				+ "&_PROCID=70000002"
//				+ "&_UNITACCNAME=unitaccname"
				+ "&_IS=-122162"
				+ "&_ISCROP=0"
//				+ "&_SENDDATE=2018-04-25"
//				+ "&_ACCNAME=%E9%BB%84%E5%86%B0"
//				+ "&_SENDTIME=2018-04-25"
				+ "&begdate=2000-01-01"
				+ "&enddate="+HousingFundHelperService.getPresentDate().trim()+""
				+ "&year="+year+""
				+ "&dynamicTable_flag=0"
				+ "&instancenum=-122162"
				+ "&accnum="+accNum.trim()+"";
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		url = "http://"+loginHost+"/maswt/dynamictable?uuid=1524624946916"; 
 		webRequest = new WebRequest(new URL(url), HttpMethod.POST);  
 		requestBody="dynamicTable_id=datalist2"
 				+ "&dynamicTable_currentPage=1"
 				+ "&dynamicTable_pageSize=300"
 				+ "&dynamicTable_nextPage=1"
 				+ "&dynamicTable_page=%2Fydpx%2F70000002%2F700002_01.ydpx"
 				+ "&dynamicTable_paging=true"
 				+ "&dynamicTable_configSqlCheck=0"
 				+ "&errorFilter=1%3D1"
 				+ "&begdate=2000-01-01"
 				+ "&enddate="+HousingFundHelperService.getPresentDate().trim()+""
 				+ "&year="+year+""
 				+ "&accnum="+accNum.trim()+""
 				+ "&_APPLY=0"
 				+ "&_CHANNEL=1"
 				+ "&_PROCID=70000002"
 				+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDJ0ALNzZWxlY3QgaW5zdGFuY2VudW0sdHJhbnNkYXRlLCBhY2Nu%0AdW0xLCBhY2NuYW1lMSwgYWNjbmFtZTIsIGFtdDEsIGFtdDIsIGJlZ2luZGF0ZSwgZW5kZGF0ZSwg%0AcmVhc29uLCBmcmVldXNlMiwgZnJlZXVzZTEgZnJvbSBkcDA3NyB3aGVyZSBpbnN0YW5jZW51bSA9%0ALTEyMjE2MiBvcmRlciBieSB0cmFuc2RhdGUgZGVzY3g%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AANf%0AUld0AAF3dAAFX1RZUEV0AARpbml0dAALX0JSQU5DSEtJTkR0AAEwdAATQ1VSUkVOVF9TWVNURU1f%0AREFURXQACjIwMTgtMDQtMjV0AAtfU0VORE9QRVJJRHQAEjM0MDUwMzE5ODkwNTExMDAyWHQACF9X%0ASVRIS0VZcQB%2BAAl0AAlpc1NhbWVQZXJ0AAVmYWxzZXQAB19BQ0NOVU10AAwxMDAxMDkwMDA2MDJ0%0AAAdfUEFHRUlEdAAFc3RlcDF0ABBfREVQVVRZSURDQVJETlVNcQB%2BAA10AAlfUE9SQ05BTUV0ABjk%0AuKrkurrmmI7nu4bkv6Hmga%2Fmn6Xor6J0AAtfVU5JVEFDQ05VTXB0AAZfTE9HSVB0ABEyMDE4MDQy%0ANTEwMDMyMTUzMXQAB19QUk9DSUR0AAg3MDAwMDAwMnQADF9VTklUQUNDTkFNRXQAC3VuaXRhY2Nu%0AYW1ldAADX0lTc3IADmphdmEubGFuZy5Mb25nO4vkkMyPI98CAAFKAAV2YWx1ZXhyABBqYXZhLmxh%0AbmcuTnVtYmVyhqyVHQuU4IsCAAB4cP%2F%2F%2F%2F%2F%2F%2FiLOdAAHX0lTQ1JPUHEAfgAJdAAHX1VTQktFWXB0%0AAAlfU0VORERBVEVxAH4AC3QACF9BQ0NOQU1FdAAG6buE5YawdAAJX1NFTkRUSU1FdAAKMjAxOC0w%0ANC0yNXh0AAhAU3lzRGF0ZXQAB0BTeXNEYXl0AAlAU3lzTW9udGh0AAhAU3lzVGltZXQACEBTeXNX%0AZWVrdAAIQFN5c1llYXI%3D";
		webRequest.setRequestBody(requestBody);
 		page = webClient.getPage(webRequest);  
 		if(null!=page){
 			html = page.getWebResponse().getContentAsString(); 
 			HousingMaAnShanHtml housingMaAnShanHtml=new HousingMaAnShanHtml();
 			housingMaAnShanHtml.setHtml(html);
 			housingMaAnShanHtml.setPagenumber(1);
 			housingMaAnShanHtml.setTaskid(taskHousing.getTaskid());
 			housingMaAnShanHtml.setType("个人明细账源码页");
 			housingMaAnShanHtml.setUrl(url);
 			housingMaAnShanHtmlRepository.save(housingMaAnShanHtml);
 			List<HousingMaAnShanDetailAccount> list = housingFundMaAnShanParser.detailAccountParser(html,taskHousing);
 			if(null!=list && list.size()>0){
 				housingMaAnShanDetailAccountRepository.saveAll(list);
 				updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成", 200,taskHousing.getTaskid());
 				tracer.addTag("action.crawler.getDetailAccount", "数据采集，个人明细账信息已采集完成");
 			}else{
 				updatePayStatusByTaskid("数据采集中，个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，个人明细账信息暂无可采集数据");
 			}
 		}
		updateTaskHousing(taskHousing.getTaskid());
	}
}
