package app.service;

import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jining.HousingJiNingDetailAccount;
import com.microservice.dao.entity.crawler.housing.jining.HousingJiNingHtml;
import com.microservice.dao.entity.crawler.housing.jining.HousingJiNingUserInfo;
import com.microservice.dao.repository.crawler.housing.jining.HousingJiNingDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.jining.HousingJiNingHtmlRepository;
import com.microservice.dao.repository.crawler.housing.jining.HousingJiNingUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundJiNingParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import net.sf.json.JSONObject;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.jining"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.jining"})
public class HousingFundJiNingCrawlerService extends HousingBasicService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundJiNingParser housingFundJiNingParser;
	@Autowired
	private HousingJiNingUserInfoRepository housingJiNingUserInfoRepository;
	@Autowired
	private HousingJiNingHtmlRepository housingJiNingHtmlRepository;
	@Autowired
	private HousingJiNingDetailAccountRepository housingJiNingDetailAccountRepository;
	@Autowired
	private  HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	//获取用户信息
	@Async
	public void getUserInfo(TaskHousing taskHousing) {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://60.211.249.146:9982/jnwsyyt/init.summer?_PROCID=20000008"; 
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
	 		Page hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			String html = hPage.getWebResponse().getContentAsString(); 
	 			Document doc = Jsoup.parse(html);
	 			String accnum = doc.getElementById("accnum").val();  //通过该链接获取个人账号，作为完整信息获取过程中的参数
	 			url="http://60.211.249.146:9982/jnwsyyt/command.summer?uuid=1525763300868";
	 			webRequest = new WebRequest(new URL(url), HttpMethod.POST);   
	 			String requestBody="%24page=%2Fydpx%2F20000008%2F20000008_01.ydpx"
	 					+ "&_ACCNUM="+accnum.trim()+""
	 					+ "&_IS=-2210680"
	 					+ "&_PAGEID=step1"
	 					+ "&isSamePer=false"
	 					+ "&_PROCID=20000008"
	 					+ "&_BRANCHKIND=0"
	 					+ "&_ISCROP=0"
	 					+ "&_TYPE=init"
	 					+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
	 					+ "&_RW=w"
	 					+ "&_WITHKEY=0"
	 					+ "&accnum="+accnum.trim()+""
	 					+ "&certinum="
	 					+ "&indiaccstate=0&accinstcode=&bal=&basenum=&unitprop="
	 					+ "&indiprop=&gryjce=&lastdrawdate=&isloanflag=&lasttransdate=&opnaccdate=";
	 			webRequest.setRequestBody(requestBody);
	 			Page page = webClient.getPage(webRequest);
	 			if(null!=page){
	 				html = page.getWebResponse().getContentAsString(); 
	 				tracer.addTag("action.crawler.getUserInfo.html", "获取的用户基本信息源码页已经入库");
	 	 			HousingJiNingHtml housingJiNingHtml=new HousingJiNingHtml();
	 	 			housingJiNingHtml.setHtml(html);
	 	 			housingJiNingHtml.setPagenumber(1);
	 	 			housingJiNingHtml.setTaskid(taskHousing.getTaskid());
	 	 			housingJiNingHtml.setType("用户基本信息源码页");
	 	 			housingJiNingHtml.setUrl(url);
	 	 			housingJiNingHtmlRepository.save(housingJiNingHtml);
	 	 			//解析用户信息
	 	 			HousingJiNingUserInfo housingJiNingUserInfo=housingFundJiNingParser.userInfoParser(html,taskHousing);
	 	 			if(null!=housingJiNingUserInfo){
	 	 				housingJiNingUserInfoRepository.save(housingJiNingUserInfo);
	 	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
	 					tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
	 					try {
	 						getDetailAccount(taskHousing,housingJiNingUserInfo);
	 					} catch (Exception e) {
	 						tracer.addTag("action.crawler.getDetailAccount.e", taskHousing.getTaskid()+"  "+e.toString());
	 						updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid() );
	 						updateTaskHousing(taskHousing.getTaskid());
	 					}
	 	 			}
	 			}
	 		}
		} catch (Exception e) {
			//明细账信息的爬取需要依赖用户信息，故用户信息出现异常，明细账信息也无法正常爬取，故需要在异常中更新状态
			tracer.addTag("action.crawler.getUserInfo.e",e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
			updatePayStatusByTaskid("数据采集中，缴费信息已采集完成",201,taskHousing.getTaskid());
			//此处也要更新最终爬取状态
			updateTaskHousing(taskHousing.getTaskid());
		}
 		
	}
	//获取明细账信息
	private void getDetailAccount(TaskHousing taskHousing, HousingJiNingUserInfo housingJiNingUserInfo) throws Exception {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://60.211.249.146:9982/jnwsyyt/command.summer?uuid=1525766171126";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST); 
		//通过测试，发现只需要accnum即可
		String accnum = housingJiNingUserInfo.getAccnum().trim();
 		String requestBody="%24page=%2Fydpx%2F20000009%2F20000009_01.ydpx"
 				+ "&_ACCNUM="+accnum+""
 				+ "&_RW=w&_PAGEID=step1"
 				+ "&_IS=-2212363"
 				+ "&isSamePer=false"
 				+ "&_PROCID=20000009"
 				+ "&_BRANCHKIND=0"
 				+ "&_TYPE=init&_ISCROP=0"
 				+ "&_PORCNAME=%E6%98%8E%E7%BB%86%E6%9F%A5%E8%AF%A2"
 				+ "&_WITHKEY=0"
 				+ "&accnum="+accnum+""
 				+ "&begdate="+HousingFundHelperService.getTwoYearAgoDate().trim()+""
				+ "&enddate="+HousingFundHelperService.getPresentDate().trim()+"";
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			String msg = JSONObject.fromObject(html).getJSONObject("data").getString("msg");
			if(msg.contains("\u4ea4\u6613\u5904\u7406\u6210\u529f")){ //交易成功
				url="http://60.211.249.146:9982/jnwsyyt/dynamictable?uuid=1525766172682";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST); 
				requestBody="dynamicTable_id=datelist"
						+ "&dynamicTable_currentPage=0"
						+ "&dynamicTable_pageSize=50"  //只能查询两年的数据
						+ "&dynamicTable_nextPage=1"
						+ "&dynamicTable_page=%2Fydpx%2F20000009%2F20000009_01.ydpx"
						+ "&dynamicTable_paging=true"
						+ "&dynamicTable_configSqlCheck=0"
						+ "&errorFilter=1%3D1"
						+ "&accnum="+accnum.trim()+""
						+ "&begdate="+HousingFundHelperService.getTwoYearAgoDate().trim()+""
						+ "&enddate="+HousingFundHelperService.getPresentDate().trim()+""
						+ "&_APPLY=0&_CHANNEL=1"
						+ "&_PROCID=20000009"
 						+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAhkYXRlbGlzdHQAhnNlbGVjdCB1bml0YWNjbnVtMSwgdW5pdGFjY25hbWUsIHRy%0AYW5zZGF0ZSwgcmVhc29uLCBhbXQxLCBhbXQyLCBhY2NuYW1lMiwgYWNjbmFtZTEsIGZyZWV1c2Ux%0ALCBzZXFubyBmcm9tIGRwMDc3IHdoZXJlIGluc3RhbmNlPS0yMjEyMzYzeA%3D%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AAdf%0AQUNDTlVNdAAMMTIwMDA3NjE2NTAzdAADX1JXdAABd3QAC19VTklUQUNDTlVNcHQAB19QQUdFSUR0%0AAAVzdGVwMXQAA19JU3NyAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgABSgAFdmFsdWV4cgAQamF2%0AYS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHD%2F%2F%2F%2F%2F%2F9499XQADF9VTklUQUNDTkFNRXQAM%2BmdkuWy%0Am%2BiBmuaci%2BS6uuWKm%2Bi1hOa6kOaciemZkOWFrOWPuOa1juWugeWIhuWFrOWPuHQABl9MT0dJUHQA%0AETIwMTgwNTA4MTU1NTA1NTI1dAAIX0FDQ05BTUV0AAbnjovnhJV0AAlpc1NhbWVQZXJ0AAVmYWxz%0AZXQAB19QUk9DSUR0AAgyMDAwMDAwOXQAC19TRU5ET1BFUklEdAASMzcwODI5MTk5MjA3MDY0OTI3%0AdAAQX0RFUFVUWUlEQ0FSRE5VTXQAEjM3MDgyOTE5OTIwNzA2NDkyN3QACV9TRU5EVElNRXQACjIw%0AMTgtMDUtMDh0AAtfQlJBTkNIS0lORHQAATB0AAlfU0VORERBVEV0AAoyMDE4LTA1LTA4dAATQ1VS%0AUkVOVF9TWVNURU1fREFURXEAfgAidAAFX1RZUEV0AARpbml0dAAHX0lTQ1JPUHEAfgAgdAAJX1BP%0AUkNOQU1FdAAM5piO57uG5p%2Bl6K%2BidAAHX1VTQktFWXB0AAhfV0lUSEtFWXEAfgAgeHQACEBTeXNE%0AYXRldAAHQFN5c0RheXQACUBTeXNNb250aHQACEBTeXNUaW1ldAAIQFN5c1dlZWt0AAhAU3lzWWVh%0Acg%3D%3D";
 				webRequest.setRequestBody(requestBody);
				page = webClient.getPage(webRequest);
				if(null!=page){
					html=page.getWebResponse().getContentAsString();
		 			tracer.addTag("action.crawler.getDetailAccount.html", "获取的个人明细账源码页已经入库");
		 			HousingJiNingHtml housingJiNingHtml=new HousingJiNingHtml();
		 			housingJiNingHtml.setHtml(html);
		 			housingJiNingHtml.setPagenumber(1);
		 			housingJiNingHtml.setTaskid(taskHousing.getTaskid());
		 			housingJiNingHtml.setType("个人明细账源码页");
		 			housingJiNingHtml.setUrl(url);
		 			housingJiNingHtmlRepository.save(housingJiNingHtml);
		 			List<HousingJiNingDetailAccount> list = housingFundJiNingParser.detailAccountParser(html,taskHousing);
		 			if(null!=list && list.size()>0){
		 				housingJiNingDetailAccountRepository.saveAll(list);
		 				updatePayStatusByTaskid("数据采集中,个人明细账信息已采集完成", 200,taskHousing.getTaskid());
		 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中,个人明细账信息已采集完成");
		 				updateTaskHousing(taskHousing.getTaskid());
		 			}else{
		 				updatePayStatusByTaskid("数据采集中,个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
		 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中,个人明细账信息暂无可采集数据");
		 				updateTaskHousing(taskHousing.getTaskid());
		 			}
				}
			}else{
				//未查询到满足个人条件的信息
	 			updatePayStatusByTaskid("数据采集中,个人明细账信息已采集完成",201,taskHousing.getTaskid() );
				tracer.addTag("action.crawler.getDetailAccount", "数据采集中,个人明细账信息暂无可采集数据");
				updateTaskHousing(taskHousing.getTaskid());
			}
		}
	}
}
