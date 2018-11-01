package app.service;

import java.net.URL;
import java.util.List;

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
import com.microservice.dao.entity.crawler.housing.jilin.HousingJiLinDetailAccount;
import com.microservice.dao.entity.crawler.housing.jilin.HousingJiLinHtml;
import com.microservice.dao.entity.crawler.housing.jilin.HousingJiLinUserInfo;
import com.microservice.dao.repository.crawler.housing.jilin.HousingJiLinDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.jilin.HousingJiLinHtmlRepository;
import com.microservice.dao.repository.crawler.housing.jilin.HousingJiLinUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundJiLinParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import net.sf.json.JSONObject;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.jilin"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.jilin"})
public class HousingFundJiLinCrawlerService extends HousingBasicService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundJiLinParser housingFundJiLinParser;
	@Autowired
	private HousingJiLinUserInfoRepository housingJiLinUserInfoRepository;
	@Autowired
	private HousingJiLinHtmlRepository housingJiLinHtmlRepository;
	@Autowired
	private HousingJiLinDetailAccountRepository housingJiLinDetailAccountRepository;
	@Autowired
	private  HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	//获取用户信息
	@Async
	public void getUserInfo(TaskHousing taskHousing, String accnum, String certinum) throws Exception {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://old.jlgjj.gov.cn/website/website/trans/gjjquery.do?className=TRB00005"; 
		String requestBody="time=1524815910978&accnum="+accnum.trim()+"";
 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);   
 		webRequest.setRequestBody(requestBody);
 		Page hPage = webClient.getPage(webRequest);  
 		if(null!=hPage){
 			String html = hPage.getWebResponse().getContentAsString(); 
 			HousingJiLinHtml housingJiLinHtml=new HousingJiLinHtml();
 			housingJiLinHtml.setHtml(html);
 			housingJiLinHtml.setPagenumber(1);
 			housingJiLinHtml.setTaskid(taskHousing.getTaskid());
 			housingJiLinHtml.setType("用户基本信息源码页");
 			housingJiLinHtml.setUrl(url);
 			housingJiLinHtmlRepository.save(housingJiLinHtml);
 			//解析用户信息
 			HousingJiLinUserInfo housingJiLinUserInfo=housingFundJiLinParser.userInfoParser(html,taskHousing,certinum);
 			if(null!=housingJiLinUserInfo){
 				housingJiLinUserInfoRepository.save(housingJiLinUserInfo);
 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
 				updatePayStatusByTaskid("数据采集中，缴费信息已采集完成",201,taskHousing.getTaskid());
				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
 			}
 		}
	}
	//获取明细账信息(改版后无此项可供爬取)
	@Async
	public void detailAccount(TaskHousing taskHousing, HousingJiLinUserInfo housingJiLinUserInfo, WebClient webClient) throws Exception {
		String url0="https://"+loginHost+"/jlwsyyt/command.summer?uuid=1512010202033";
		//此初步请求的响应：
//		{"data":{"url":"\/ydpx\/60010009\/601009_01.ydpx"},"returnCode":"0"}   //开发的账号两年均有数据，故此处暂认为returnCode为0就是有数据
		WebRequest webRequest0 = new WebRequest(new URL(url0), HttpMethod.POST); 
		for(int i=0;i<2;i++){   //只能查当年和去年的数据
			String qryYear=HousingFundHelperService.getYear(i);
			webRequest0.setAdditionalHeader("Host", ""+loginHost+"");
	 		webRequest0.setAdditionalHeader("Origin", "https://"+loginHost+"");
	 		webRequest0.setAdditionalHeader("Referer", "https://"+loginHost+"/jlwsyyt/init.summer?_PROCID=60010009");
	 		webRequest0.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			String requestBody0="$page=/ydpx/60010009/601009_01.ydpx"
	 				+ "&_ACCNUM="+housingJiLinUserInfo.getAccnum().trim()+""
	 				+ "&_RW=w"
	 				+ "&_PAGEID=step1&_IS=-1228708"
//	 				+ "&_LOGIP=20171130103611489"
	 				+ "&_ACCNAME="+housingJiLinUserInfo.getAccname().trim()+""
	 				+ "&isSamePer=false"
	 				+ "&_PROCID=60010009"
	 				+ "&_SENDOPERID="+housingJiLinUserInfo.getCertinum().trim()+""
	 				+ "&_DEPUTYIDCARDNUM="+housingJiLinUserInfo.getCertinum().trim()+""
	 				+ "&_SENDTIME="+HousingFundHelperService.getPresentDate().trim()+""
	 				+ "&_BRANCHKIND=0"
	 				+ "&_SENDDATE="+HousingFundHelperService.getPresentDate().trim()+""
	 				+ "&CURRENT_SYSTEM_DATE="+HousingFundHelperService.getPresentDate().trim()+""
	 				+ "&_TYPE=init&_ISCROP=0&_PORCNAME=个人明细账查询&_WITHKEY=0"
	 				+ "&Year="+qryYear+""
	 				+ "&accnum="+housingJiLinUserInfo.getAccnum().trim()+"";
	 		webRequest0.setRequestBody(requestBody0);
	 		Page hPage0 = webClient.getPage(webRequest0);
	 		if(null!=hPage0){
	 			//如下获取明细信息的请求，requestBody需要urlencoded,不然会报数据总线读取出错的错误
	 			String html0=hPage0.getWebResponse().getContentAsString();
	 			String returnCode = JSONObject.fromObject(html0).getString("returnCode");
	 			if(returnCode.equals("0")) {//查询年有数据
	 				String url = "https://"+loginHost+"/jlwsyyt/dynamictable?uuid=1512010204024";
	 				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST); 
 					String requestBody="dynamicTable_id=datalist2"
 		 				+ "&dynamicTable_currentPage=1"
 		 				+ "&dynamicTable_pageSize=50"   //获取一年的记录，此处写个较大的数值
 		 				+ "&dynamicTable_nextPage=1"
 		 				+ "&dynamicTable_page=%2Fydpx%2F60010009%2F601009_01.ydpx"
 		 				+ "&dynamicTable_paging=true&dynamicTable_configSqlCheck=0"
 		 				+ "&errorFilter=1%3D1"
 		 				+ "&Year="+qryYear+""
 		 				+ "&accnum="+housingJiLinUserInfo.getAccnum().trim()+"&_APPLY=0"
 		 				+ "&_CHANNEL=1&_PROCID=60010009"
 		 				//如下两个参数必须encode
 		 				+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDJ0AUNzZWxlY3QgcGVvcGxlbnVtLCByZXBsYWNlIChjaGFyKHRy%0AYW5zZGF0ZSksICcxODk5LTEyLTMxJywnJykgYXMgdHJhbnNkYXRlLHJlcGxhY2UgKGNoYXIoYmVn%0AaW5kYXRlKSwgJzE4OTktMTItMzEnLCcnKSBhcyBiZWdpbmRhdGUsIHJlcGxhY2UgKGNoYXIoZW5k%0AZGF0ZSksICcxODk5LTEyLTMxJywnJykgYXMgZW5kZGF0ZSwgdXNlZGF0ZWMsIGFtdDEsIGFtdDIs%0AIGJhc2VudW1iZXIsIHNleCwgaW5zdGNvZGUsIG9wZXIsIGluc3RhbmNlbnVtIGZyb20gZHAwNzcg%0Ad2hlcmUgaW5zdGFuY2VudW09LTEyMjg3MDggb3JkZXIgYnkgdHJhbnNkYXRlIGRlc2MsIHBlb3Bs%0AZW51bXg%3D"
						+ "&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AAdf%0AQUNDTlVNdAAMNzc3MDg1MTY3MjE2dAADX1JXdAABd3QAC19VTklUQUNDTlVNcHQAB19QQUdFSUR0%0AAAVzdGVwMXQAA19JU3NyAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgABSgAFdmFsdWV4cgAQamF2%0AYS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHD%2F%2F%2F%2F%2F%2F%2B1AXHQADF9VTklUQUNDTkFNRXB0AAZfTE9H%0ASVB0ABEyMDE3MTEzMDEwMzYxMTQ4OXQACF9BQ0NOQU1FdAAG5ZSQ5biGdAAJaXNTYW1lUGVydAAF%0AZmFsc2V0AAdfUFJPQ0lEdAAINjAwMTAwMDl0AAtfU0VORE9QRVJJRHQAEjIyMjQwNDE5ODcwNzA2%0AMzAyOXQAEF9ERVBVVFlJRENBUkROVU10ABIyMjI0MDQxOTg3MDcwNjMwMjl0AAlfU0VORFRJTUV0%0AAAoyMDE3LTExLTMwdAALX0JSQU5DSEtJTkR0AAEwdAAJX1NFTkREQVRFdAAKMjAxNy0xMS0zMHQA%0AE0NVUlJFTlRfU1lTVEVNX0RBVEVxAH4AIXQABV9UWVBFdAAEaW5pdHQAB19JU0NST1BxAH4AH3QA%0ACV9QT1JDTkFNRXQAFeS4quS6uuaYjue7hui0puafpeivonQAB19VU0JLRVlwdAAIX1dJVEhLRVlx%0AAH4AH3h0AAhAU3lzRGF0ZXQAB0BTeXNEYXl0AAlAU3lzTW9udGh0AAhAU3lzVGltZXQACEBTeXNX%0AZWVrdAAIQFN5c1llYXI%3D";
 					webRequest.setAdditionalHeader("Host", ""+loginHost+"");
 			 		webRequest.setAdditionalHeader("Origin", "https://"+loginHost+"");
 			 		webRequest.setAdditionalHeader("Referer", "https://"+loginHost+"/jlwsyyt/init.summer?_PROCID=60010009");
 			 		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	 		 		webRequest.setRequestBody(requestBody);
					Page hPage = webClient.getPage(webRequest); 
	 		 		if(null!=hPage){
	 		 			String html=hPage.getWebResponse().getContentAsString();
	 		 			tracer.addTag("action.crawler.detailAccount.html", "获取的"+qryYear+"年度个人明细账html====>"+html);
	 		 			HousingJiLinHtml housingJiLinHtml=new HousingJiLinHtml();
	 		 			housingJiLinHtml.setHtml(html);
	 		 			housingJiLinHtml.setPagenumber(1);
	 		 			housingJiLinHtml.setTaskid(taskHousing.getTaskid());
	 		 			housingJiLinHtml.setType(qryYear+"年度个人明细账源码页");
	 		 			housingJiLinHtml.setUrl(url);
	 		 			housingJiLinHtmlRepository.save(housingJiLinHtml);
	 		 			List<HousingJiLinDetailAccount> list = housingFundJiLinParser.detailAccountParser(html,taskHousing);
			 			if(null!=list && list.size()>0){
			 				housingJiLinDetailAccountRepository.saveAll(list);
			 				updatePayStatusByTaskid("数据采集中"+qryYear+"年度个人明细账信息已采集完成", 200,taskHousing.getTaskid());
			 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中"+qryYear+"年度个人明细账信息已采集完成");
			 			}else{
			 				updatePayStatusByTaskid("数据采集中"+qryYear+"年度个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
			 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中"+qryYear+"年度个人明细账信息暂无可采集数据");
			 			}
	 		 		}
	 			}else{
	 				//未查询到满足个人条件的信息
		 			updatePayStatusByTaskid("数据采集中"+qryYear+"年度个人明细账信息已采集完成",201,taskHousing.getTaskid() );
					tracer.addTag("action.crawler.getDetailAccount", "数据采集中"+qryYear+"年度个人明细账信息暂无可采集数据");
	 			}
	 		}
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
}
