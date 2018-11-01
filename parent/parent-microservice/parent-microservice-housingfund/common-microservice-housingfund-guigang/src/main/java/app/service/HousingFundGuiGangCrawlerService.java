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
import com.microservice.dao.entity.crawler.housing.guigang.HousingGuiGangDetailAccount;
import com.microservice.dao.entity.crawler.housing.guigang.HousingGuiGangHtml;
import com.microservice.dao.entity.crawler.housing.guigang.HousingGuiGangUserInfo;
import com.microservice.dao.repository.crawler.housing.guigang.HousingGuiGangDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.guigang.HousingGuiGangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.guigang.HousingGuiGangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundGuiGangParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.guigang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.guigang"})
public class HousingFundGuiGangCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundGuiGangCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundGuiGangParser housingFundGuiGangParser;
	@Autowired
	private HousingGuiGangHtmlRepository housingGuiGangHtmlRepository;
	@Autowired
	private HousingGuiGangDetailAccountRepository housingGuiGangDetailAccountRepository;
	@Autowired
	private HousingGuiGangUserInfoRepository housingGuiGangUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	@Async
	public void getUserInfo(TaskHousing taskHousing){
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://"+loginHost+"/ggwt/init.summer?_PROCID=70000013"; 
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
	 		HtmlPage hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			String html = hPage.getWebResponse().getContentAsString(); 
	 			if(html.contains("个人账号")){   //作为能够响应数据的一个判断
	 				//获取公积金账号
					Document doc = Jsoup.parse(html);
					String accnum = doc.getElementById("grzh").val();
	 				url="http://"+loginHost+"/ggwt/command.summer?uuid=1517210014284";
	 				webRequest= new WebRequest(new URL(url), HttpMethod.POST);  
	 				String requestBody=""
	 						+ "%24page=%2Fydpx%2F70000013%2F700013_01.ydpx"
	 						+ "&_ACCNUM="+accnum.trim()+""
	 						+ "&_IS=-440328"
	 						+ "&_PAGEID=step1"
//	 						+ "&_LOGIP=20180129150016017"
	 						+ "&isSamePer=false"
//	 						+ "&temp_.itemid%5B7%5D=99"
//	 						+ "&_SENDOPERID=450702198112235113"
	 						+ "&_PROCID=70000013"
//	 						+ "&temp_.itemid%5B5%5D=05"
//	 						+ "&temp_.itemid%5B3%5D=03"
//	 						+ "&_SENDDATE=2018-01-29"
	 						+ "&_BRANCHKIND=0"
//	 						+ "&temp_._xh%5B6%5D=6"
//	 						+ "&temp_.itemid%5B1%5D=01"
//	 						+ "&temp_.itemval%5B6%5D=%E5%86%BB%E7%BB%93"
//	 						+ "&temp_._xh%5B4%5D=4"
//	 						+ "&CURRENT_SYSTEM_DATE=2018-01-29"
//	 						+ "&temp_.itemval%5B4%5D=%E5%A4%96%E9%83%A8%E8%BD%AC%E5%87%BA%E9%94%80%E6%88%B7"
	 						+ "&_ISCROP=0"
	 						+ "&_TYPE=init"
//	 						+ "&temp_._xh%5B2%5D=2"
	 						+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
//	 						+ "&temp_.itemval%5B2%5D=%E5%B0%81%E5%AD%98"
//	 						+ "&temp__rownum=7"
	 						+ "&_RW=w"
//	 						+ "&_UNITACCNAME="
//	 						+ "&_ACCNAME=%E7%AC%A6%E9%B9%8F"
//	 						+ "&temp_.itemid%5B6%5D=06"
//	 						+ "&_DEPUTYIDCARDNUM=450702198112235113"
//	 						+ "&temp_.itemid%5B4%5D=04"
//	 						+ "&_SENDTIME=2018-01-29"
//	 						+ "&temp_._xh%5B7%5D=7"
//	 						+ "&temp_.itemid%5B2%5D=02"
//	 						+ "&temp_.itemval%5B7%5D=%E5%85%B6%E4%BB%96"
//	 						+ "&temp_._xh%5B5%5D=5"
//	 						+ "&temp_.itemval%5B5%5D=%E6%8F%90%E5%8F%96%E9%94%80%E6%88%B7"
//	 						+ "&temp_._xh%5B3%5D=3"
//	 						+ "&temp_.itemval%5B3%5D=%E5%90%88%E5%B9%B6%E9%94%80%E6%88%B7"
	 						+ "&_WITHKEY=0"
//	 						+ "&temp_._xh%5B1%5D=1"
//	 						+ "&temp_.itemval%5B1%5D=%E6%AD%A3%E5%B8%B8"
	 						+ "&grzh="+accnum.trim()+""
	 						+ "&xingming="
	 						+ "&zjhm="
	 						+ "&grzhzt="
	 						+ "&grzhye="
	 						+ "&khrq="
	 						+ "&unitprop="
	 						+ "&indiprop="
	 						+ "&grjcjs="
	 						+ "&indipaysum="
	 						+ "&dwyjce="
	 						+ "&gryjce="
	 						+ "&jzny="
	 						+ "&dwzh="
	 						+ "&unitaccname="
	 						+ "&isloanflag="
	 						+ "&flag="
	 						+ "&useflag="
	 						+ "&cardno="
	 						+ "&jkhtbh="
	 						+ "&sjhm="
	 						+ "&frzflag="
	 						+ "&smrzbs="
	 						+ "&DealSeq=1";
	 				webRequest.setRequestBody(requestBody);
//					webRequest.setAdditionalHeader("Host", ""+loginHost+"");
//					webRequest.setAdditionalHeader("Origin", "http://"+loginHost+"");
//					webRequest.setAdditionalHeader("Referer", "http://"+loginHost+"/kmnbp/init.summer?_PROCID=70000013");
	 				Page page = webClient.getPage(webRequest);
	 				if(page!=null){
	 					html=page.getWebResponse().getContentAsString();
	 	 				HousingGuiGangHtml housingGuiGangHtml=new HousingGuiGangHtml();
	 	 	 			housingGuiGangHtml.setHtml(html);
	 	 	 			housingGuiGangHtml.setPagenumber(1);
	 	 	 			housingGuiGangHtml.setTaskid(taskHousing.getTaskid());
	 	 	 			housingGuiGangHtml.setType("用户基本信息源码页");
	 	 	 			housingGuiGangHtml.setUrl(url);
	 	 	 			housingGuiGangHtmlRepository.save(housingGuiGangHtml);
	 	 	 			HousingGuiGangUserInfo housingGuiGangUserInfo= housingFundGuiGangParser.userInfoParser(html,taskHousing);
	 	 	 			if(null!=housingGuiGangUserInfo){
	 	 	 				housingGuiGangUserInfoRepository.save(housingGuiGangUserInfo);
	 	 	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
	 	 	 				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
	 	 	 				//之后用获取的个人账号信息采集流水信息
	 	 	 				try {
	 	 	 					getDetailAccount(taskHousing,accnum);
	 	 	 				} catch (Exception e) {
	 	 	 					tracer.addTag("action.crawler.getDetailAccount.e", taskHousing.getTaskid()+"  "+e.toString());
	 	 	 					updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid());
	 	 	 					updateTaskHousing(taskHousing.getTaskid());
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
	@Async
	public void getDetailAccount(TaskHousing taskHousing, String accNum) throws Exception {
		//可通过改变每页的显示数量来一次性爬取完所有的数据，（通过测试，看也可以改变区间范围，将起始日期改为2000-01-01，截止日期改为当前日期，去除年份这个参数）
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://"+loginHost+"/ggwt/command.summer?uuid=1517210384970";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
    	String requestBody=""
    			+ "%24page=%2Fydpx%2F70000002%2F700002_01.ydpx"
    			+ "&_ACCNUM="+accNum.trim()+""
    			+ "&_RW=w"
    			+ "&_PAGEID=step1"
    			+ "&_IS=-440349"
//    			+ "&_UNITACCNAME="
//    			+ "&_LOGIP=20180129150016017"
//    			+ "&_ACCNAME=%E7%AC%A6%E9%B9%8F"
    			+ "&isSamePer=false"
    			+ "&_PROCID=70000002"
//    			+ "&_SENDOPERID=450702198112235113"
//    			+ "&_DEPUTYIDCARDNUM=450702198112235113"
//    			+ "&_SENDTIME=2018-01-29"
    			+ "&_BRANCHKIND=0"
//    			+ "&_SENDDATE=2018-01-29"
//    			+ "&CURRENT_SYSTEM_DATE=2018-01-29"
    			+ "&_TYPE=init"
    			+ "&_ISCROP=0"
    			+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
    			+ "&_WITHKEY=0"
    			+ "&begdate=2000-01-01"
    			+ "&enddate="+HousingFundHelperService.getPresentDate().trim()+""
//    			+ "&year=2018"
    			+ "&accnum="+accNum.trim()+"";
	 	webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");			
    	webRequest.setRequestBody(requestBody);
    	Page page = webClient.getPage(webRequest);
    	if(page!=null){
    		String html=page.getWebResponse().getContentAsString();
    		url="http://"+loginHost+"/ggwt/dynamictable?uuid=1517210386249";
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
//	    			+ "&year=2018"
	    			+ "&accnum="+accNum.trim()+""
	    			+ "&_APPLY=0"
	    			+ "&_CHANNEL=1"
	    			+ "&_PROCID=70000002"
	    			+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDJ0AK1zZWxlY3QgaW5zdGFuY2VudW0sIHRyYW5zZGF0ZSwgZ3J6%0AaCwgeGluZ21pbmcsIHhpbmdtaW5nMiwgYW10MSwgYW10MiwgYmVneW0sIGVuZHltLCByZWFzb24s%0AIHBheXZvdW51bSwgZnJlZXVzZTEgZnJvbSBkcDA3NyB3aGVyZSBpbnN0YW5jZW51bSA9LTQ0MDM0%0AOSBvcmRlciBieSB0cmFuc2RhdGUgZGVzY3g%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AAdf%0AQUNDTlVNdAAIODAxODE2NzV0AANfUld0AAF3dAALX1VOSVRBQ0NOVU1wdAAHX1BBR0VJRHQABXN0%0AZXAxdAADX0lTc3IADmphdmEubGFuZy5Mb25nO4vkkMyPI98CAAFKAAV2YWx1ZXhyABBqYXZhLmxh%0AbmcuTnVtYmVyhqyVHQuU4IsCAAB4cP%2F%2F%2F%2F%2F%2F%2BUfjdAAMX1VOSVRBQ0NOQU1FdAAAdAAGX0xPR0lQ%0AdAARMjAxODAxMjkxNTAwMTYwMTd0AAhfQUNDTkFNRXQABuespum5j3QACWlzU2FtZVBlcnQABWZh%0AbHNldAAHX1BST0NJRHQACDcwMDAwMDAydAALX1NFTkRPUEVSSUR0ABI0NTA3MDIxOTgxMTIyMzUx%0AMTN0ABBfREVQVVRZSURDQVJETlVNcQB%2BABp0AAlfU0VORFRJTUV0AAoyMDE4LTAxLTI5dAALX0JS%0AQU5DSEtJTkR0AAEwdAAJX1NFTkREQVRFdAAKMjAxOC0wMS0yOXQAE0NVUlJFTlRfU1lTVEVNX0RB%0AVEVxAH4AIXQABV9UWVBFdAAEaW5pdHQAB19JU0NST1BxAH4AH3QACV9QT1JDTkFNRXQAGOS4quS6%0AuuaYjue7huS%2FoeaBr%2BafpeivonQAB19VU0JLRVlwdAAIX1dJVEhLRVlxAH4AH3h0AAhAU3lzRGF0%0AZXQAB0BTeXNEYXl0AAlAU3lzTW9udGh0AAhAU3lzVGltZXQACEBTeXNXZWVrdAAIQFN5c1llYXI%3D";
			webRequest.setRequestBody(requestBody);
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			page = webClient.getPage(webRequest);
			if(null!=page){
				html = page.getWebResponse().getContentAsString(); 
	 			HousingGuiGangHtml housingGuiGangHtml=new HousingGuiGangHtml();
	 			housingGuiGangHtml.setHtml(html);
	 			housingGuiGangHtml.setPagenumber(1);
	 			housingGuiGangHtml.setTaskid(taskHousing.getTaskid());
	 			housingGuiGangHtml.setType("个人明细账源码页");
	 			housingGuiGangHtml.setUrl(url);
	 			housingGuiGangHtmlRepository.save(housingGuiGangHtml);
	 			List<HousingGuiGangDetailAccount> list = housingFundGuiGangParser.detailAccountParser(html,taskHousing);
	 			if(null!=list && list.size()>0){
	 				housingGuiGangDetailAccountRepository.saveAll(list);
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
