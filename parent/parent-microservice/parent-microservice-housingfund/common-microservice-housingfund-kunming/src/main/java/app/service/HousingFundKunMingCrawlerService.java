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
import com.microservice.dao.entity.crawler.housing.kunming.HousingKunMingDetailAccount;
import com.microservice.dao.entity.crawler.housing.kunming.HousingKunMingHtml;
import com.microservice.dao.entity.crawler.housing.kunming.HousingKunMingUserInfo;
import com.microservice.dao.repository.crawler.housing.kunming.HousingKunMingDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.kunming.HousingKunMingHtmlRepository;
import com.microservice.dao.repository.crawler.housing.kunming.HousingKunMingUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundKunMingParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.kunming"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.kunming"})
public class HousingFundKunMingCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundKunMingCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundKunMingParser housingFundKunMingParser;
	@Autowired
	private HousingKunMingHtmlRepository housingKunMingHtmlRepository;
	@Autowired
	private HousingKunMingDetailAccountRepository housingKunMingDetailAccountRepository;
	@Autowired
	private HousingKunMingUserInfoRepository housingKunMingUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	@Async
	public void getUserInfo(TaskHousing taskHousing){
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://"+loginHost+"/kmnbp/init.summer?_PROCID=70000013"; 
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
	 		HtmlPage hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			String html = hPage.getWebResponse().getContentAsString(); 
	 			webClient=hPage.getWebClient();
	 			if(html.contains("账户状态")){   //作为能够响应数据的一个判断
	 				//获取公积金账号
					Document doc = Jsoup.parse(html);
					String accnum = doc.getElementById("accnum").val();
	 				url="http://"+loginHost+"/kmnbp/command.summer?uuid=1516867227259";
	 				webRequest= new WebRequest(new URL(url), HttpMethod.POST);  
	 				String requestBody=""
							+ "%24page=%2Fydpx%2F70000013%2F700013_01.ydpx"
							+ "&_ACCNUM="+accnum.trim()+""
							+ "&_PAGEID=step1"
							+ "&_PROCID=70000013"
							+ "&_TYPE=init"
							+ "&_UNITACCNAME="
							+ "&_IS=-2356775"
							+ "&isSamePer=false"
							+ "&_BRANCHKIND=0"
							+ "&_ISCROP=0"
							+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
							+ "&_RW=w"
							+ "&_WITHKEY=0"
							+ "&accnum="+accnum.trim()+""
							+ "&accname="
							+ "&certinum="
							+ "&peraccstate=0"
							+ "&balance="
							+ "&lastpaydate="
							+ "&lastdrawdate="
							+ "&basenumber="
							+ "&monpaysum="
							+ "&indiprop="
							+ "&unitprop="
							+ "&unitaccnum="
							+ "&unitaccname="
							+ "&accinstcode="
							+ "&DealSeq=1";
	 				webRequest.setRequestBody(requestBody);
					webRequest.setAdditionalHeader("Host", ""+loginHost+"");
					webRequest.setAdditionalHeader("Origin", "http://"+loginHost+"");
					webRequest.setAdditionalHeader("Referer", "http://"+loginHost+"/kmnbp/init.summer?_PROCID=70000013");
	 				Page page = webClient.getPage(webRequest);
	 				if(page!=null){
	 					html=page.getWebResponse().getContentAsString();
	 	 				HousingKunMingHtml housingKunMingHtml=new HousingKunMingHtml();
	 	 	 			housingKunMingHtml.setHtml(html);
	 	 	 			housingKunMingHtml.setPagenumber(1);
	 	 	 			housingKunMingHtml.setTaskid(taskHousing.getTaskid());
	 	 	 			housingKunMingHtml.setType("用户基本信息源码页");
	 	 	 			housingKunMingHtml.setUrl(url);
	 	 	 			housingKunMingHtmlRepository.save(housingKunMingHtml);
	 	 	 			HousingKunMingUserInfo housingKunMingUserInfo= housingFundKunMingParser.userInfoParser(html,taskHousing);
	 	 	 			if(null!=housingKunMingUserInfo){
	 	 	 				housingKunMingUserInfoRepository.save(housingKunMingUserInfo);
	 	 	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
	 	 	 				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
	 	 	 				//之后用获取的个人账号信息采集流水信息
	 	 	 				try {
	 	 	 					getDetailAccount(taskHousing,accnum);
	 	 	 				} catch (Exception e) {
	 	 	 					tracer.addTag("action.crawler.getDetailAccount.e", taskHousing.getTaskid()+"  "+e.toString());
	 	 	 					updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid());
	 	 	 				}
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
	public void getDetailAccount(TaskHousing taskHousing, String accNum) throws Exception {
		//可通过改变每页的显示数量来一次性爬取完所有的数据，（通过测试，看也可以改变区间范围，将起始日期改为2000-01-01，截止日期改为当前日期，去除年份这个参数）
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://"+loginHost+"/kmnbp/command.summer?uuid=1516933677952";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
    	String requestBody=""
    			+ "%24page=%2Fydpx%2F70000002%2F700002_01.ydpx"
    			+ "&_ACCNUM="+accNum.trim()+""
    			+ "&_RW=w"
    			+ "&_PAGEID=step1"
    			+ "&_IS=-2361238"
    			+ "&isSamePer=false"
    			+ "&_PROCID=70000002"
    			+ "&_BRANCHKIND=0"
    			+ "&_TYPE=init"
    			+ "&_ISCROP=0"
    			+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
    			+ "&_WITHKEY=0"
    			+ "&begdate=2000-01-01"
    			+ "&enddate="+HousingFundHelperService.getPresentDate().trim()+""
    			+ "&accnum="+accNum.trim()+"";
	 	webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");			
    	webRequest.setRequestBody(requestBody);
    	Page page = webClient.getPage(webRequest);
    	if(page!=null){
    		String html=page.getWebResponse().getContentAsString();
    		url="http://"+loginHost+"/kmnbp/dynamictable?uuid=1516933678761";
	    	webRequest = new WebRequest(new URL(url), HttpMethod.POST);
	    	requestBody=""
	    			+ "dynamicTable_id=datalist2"
	    			+ "&dynamicTable_currentPage=0"
	    			+ "&dynamicTable_pageSize=300"
	    			+ "&dynamicTable_nextPage=1"
	    			+ "&dynamicTable_page=%2Fydpx%2F70000002%2F700002_01.ydpx"
	    			+ "&dynamicTable_paging=true"
	    			+ "&dynamicTable_configSqlCheck=0"
	    			+ "&errorFilter=1%3D1"
	    			+ "&begdate=2000-01-01"
	    			+ "&enddate="+HousingFundHelperService.getPresentDate().trim()+""
//	    			+ "&year=2017"
	    			+ "&accnum="+accNum.trim()+""
	    			+ "&_APPLY=0"
	    			+ "&_CHANNEL=1"
	    			+ "&_PROCID=70000002"
	    			+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDJ0AJ1zZWxlY3QgaW5zdGFuY2VudW0sIHRyYW5zZGF0ZSwgdW5p%0AdGFjY251bTEsIGFtdDEsIGFtdDIsIGFtdDMsIGJlZ2luZGF0ZSwgZW5kZGF0ZSxpbnN0Y29kZSxv%0AcGVyIGZyb20gZHAwNzcgd2hlcmUgaW5zdGFuY2VudW0gPS0yMzYxMjM4IG9yZGVyIGJ5IHRyYW5z%0AZGF0ZSBkZXNjeA%3D%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AAdf%0AQUNDTlVNdAAMMTEzMTE1ODIyNzExdAADX1JXdAABd3QAC19VTklUQUNDTlVNcHQAB19QQUdFSUR0%0AAAVzdGVwMXQAA19JU3NyAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgABSgAFdmFsdWV4cgAQamF2%0AYS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHD%2F%2F%2F%2F%2F%2F9v4anQADF9VTklUQUNDTkFNRXQAAHQABl9M%0AT0dJUHQAETIwMTgwMTI2MTAyNTQ5ODU2dAAIX0FDQ05BTUV0AAnmma7oj4rnjol0AAlpc1NhbWVQ%0AZXJ0AAVmYWxzZXQAB19QUk9DSUR0AAg3MDAwMDAwMnQAC19TRU5ET1BFUklEdAASNTMyMzIzMTk5%0AMTA3MDIwNTQ0dAAQX0RFUFVUWUlEQ0FSRE5VTXEAfgAadAAJX1NFTkRUSU1FdAAKMjAxOC0wMS0y%0ANnQAC19CUkFOQ0hLSU5EdAABMHQACV9TRU5EREFURXQACjIwMTgtMDEtMjZ0ABNDVVJSRU5UX1NZ%0AU1RFTV9EQVRFcQB%2BACF0AAVfVFlQRXQABGluaXR0AAdfSVNDUk9QcQB%2BAB90AAlfUE9SQ05BTUV0%0AABjkuKrkurrmmI7nu4bkv6Hmga%2Fmn6Xor6J0AAdfVVNCS0VZcHQACF9XSVRIS0VZcQB%2BAB94dAAI%0AQFN5c0RhdGV0AAdAU3lzRGF5dAAJQFN5c01vbnRodAAIQFN5c1RpbWV0AAhAU3lzV2Vla3QACEBT%0AeXNZZWFy";
			webRequest.setRequestBody(requestBody);
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			page = webClient.getPage(webRequest);
			if(null!=page){
				html = page.getWebResponse().getContentAsString(); 
	 			HousingKunMingHtml housingKunMingHtml=new HousingKunMingHtml();
	 			housingKunMingHtml.setHtml(html);
	 			housingKunMingHtml.setPagenumber(1);
	 			housingKunMingHtml.setTaskid(taskHousing.getTaskid());
	 			housingKunMingHtml.setType("个人明细账源码页");
	 			housingKunMingHtml.setUrl(url);
	 			housingKunMingHtmlRepository.save(housingKunMingHtml);
	 			List<HousingKunMingDetailAccount> list = housingFundKunMingParser.detailAccountParser(html,taskHousing);
	 			if(null!=list && list.size()>0){
	 				housingKunMingDetailAccountRepository.saveAll(list);
	 				updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成", 200,taskHousing.getTaskid());
	 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，个人明细账信息已采集完成");
	 			}else{
	 				updatePayStatusByTaskid("数据采集中，个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
	 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，个人明细账信息暂无可采集数据");
	 			}
			}
    	}
		updateTaskHousing(taskHousing.getTaskid());
	}
}
