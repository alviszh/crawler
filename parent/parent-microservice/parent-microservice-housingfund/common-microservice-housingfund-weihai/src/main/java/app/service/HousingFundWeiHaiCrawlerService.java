package app.service;

import java.net.URL;
import java.util.List;

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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.weihai.HousingWeiHaiDetailAccount;
import com.microservice.dao.entity.crawler.housing.weihai.HousingWeiHaiHtml;
import com.microservice.dao.entity.crawler.housing.weihai.HousingWeiHaiUserInfo;
import com.microservice.dao.repository.crawler.housing.weihai.HousingWeiHaiDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.weihai.HousingWeiHaiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.weihai.HousingWeiHaiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundWeiHaiParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import net.sf.json.JSONObject;


/**
 * @description: 威海市公积金信息爬取service
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.weihai"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.weihai"})
public class HousingFundWeiHaiCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundWeiHaiCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundWeiHaiParser housingFundWeiHaiParser;
	@Autowired
	private HousingWeiHaiHtmlRepository housingWeiHaiHtmlRepository;
	@Autowired
	private HousingWeiHaiDetailAccountRepository housingWeiHaiDetailAccountRepository;
	@Autowired
	private HousingWeiHaiUserInfoRepository housingWeiHaiUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	@Async
	public void getUserInfo(TaskHousing taskHousing){
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://"+loginHost+"/whwsyyt/init.summer?_PROCID=60020007"; 
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
	 		Page hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			String html = hPage.getWebResponse().getContentAsString(); 
	 			HousingWeiHaiHtml housingWeiHaiHtml=new HousingWeiHaiHtml();
	 			housingWeiHaiHtml.setHtml(html);
	 			housingWeiHaiHtml.setPagenumber(1);
	 			housingWeiHaiHtml.setTaskid(taskHousing.getTaskid());
	 			housingWeiHaiHtml.setType("用户基本信息源码页");
	 			housingWeiHaiHtml.setUrl(url);
	 			housingWeiHaiHtmlRepository.save(housingWeiHaiHtml);
	 			//解析用户信息
	 			HousingWeiHaiUserInfo housingWeiHaiUserInfo=housingFundWeiHaiParser.userInfoParser(html,taskHousing);
	 			if(null!=housingWeiHaiUserInfo){
	 				housingWeiHaiUserInfoRepository.save(housingWeiHaiUserInfo);
	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
					try {
						getDetailAccount(taskHousing,housingWeiHaiUserInfo);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getDetailAccount.e", taskHousing.getTaskid()+"  "+e.toString());
						updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid() );
						updateTaskHousing(taskHousing.getTaskid());
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
	public void getDetailAccount(TaskHousing taskHousing, HousingWeiHaiUserInfo housingWeiHaiUserInfo) throws Exception {
		//默认只能查当前年开始往前推三年的，因为在写测试类的时候发现，哪怕传的参数是2014，返回的数据也是2015年的
		for(int i=0;i<3;i++){  //获取3年的数据
			WebClient webClient=housingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			String year = HousingFundHelperService.getYear(i);
			String accNum=housingWeiHaiUserInfo.getAccnum().trim();
			//访问这个请求之前需要访问一个带全参数的请求,这个参数中带有要请求的年份
			String url="http://"+loginHost+"/whwsyyt/command.summer?uuid=1512616537189";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);   
			String requestBody=""
	 				+ "%24page=%2Fydpx%2F60020007%2F602007_01.ydpx"
	 				+ "&_ACCNUM="+accNum+""
	 				+ "&_IS=-8017412"
	 				+ "&_PAGEID=step1"
	 				+ "&isSamePer=false"
	 				+ "&_PROCID=60020007"
	 				+ "&AccNum="+accNum+""
	 				+ "&grzh="+accNum+""
	 				+ "&_BRANCHKIND=0"
	 				+ "&_WITHKEY=0"
	 				+ "&year="+year+"";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			String html=page.getWebResponse().getContentAsString();
			String returnCode = JSONObject.fromObject(html).getString("returnCode");
			//如下内容意为：未查询到满足条件的个人明细
			if(returnCode.equals("0")){
				//把每一页显示的数据设置为了100条
				url = "http://"+loginHost+"/whwsyyt/dynamictable?uuid=1512616538628"; 
		 		webRequest = new WebRequest(new URL(url), HttpMethod.POST);  
		 		requestBody=""
 						+ "dynamicTable_id=datalist"
 						+ "&dynamicTable_currentPage=1"
 						+ "&dynamicTable_pageSize=100"
 						+ "&dynamicTable_nextPage=1"
 						+ "&dynamicTable_page=%2Fydpx%2F60020007%2F602007_01.ydpx"  //必要的参数
 						+ "&dynamicTable_paging=true"
 						+ "&dynamicTable_configSqlCheck=0"
 						+ "&errorFilter=1%3D1"
 						+ "&AccNum="+accNum.trim()+""
 						+ "&_APPLY=0"
 						+ "&_CHANNEL=1"
 						+ "&_PROCID=60020007"
 						+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAhkYXRhbGlzdHQBBnNlbGVjdCAoY2FzZSB0cmFuc2RhdGUgd2hlbiAnMTg5OS0x%0AMi0zMScgdGhlbiBudWxsIGVsc2UgdHJhbnNkYXRlIGVuZCkgYXMgdHJhbnNkYXRlLChjYXNlIGJp%0AcnRoZGF5IHdoZW4gJzE4OTktMTItMzEnIHRoZW4gbnVsbCBlbHNlIGJpcnRoZGF5IGVuZCkgYXMg%0AYmlydGhkYXksIG9wZXIsIGFtdDEsIGFtdDIsIGJhc2VudW1iZXIsIGluc3RhbmNlbnVtIGZyb20g%0AZHAwNzcgd2hlcmUgaW5zdGFuY2VudW0gPSAtODAxNzQxMiBvcmRlciBieSByZWFzb24sb3BlciB4%0A&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABR0AAdf%0AQUNDTlVNdAAIMDY3MDQxMTl0AAtfVU5JVEFDQ05VTXB0AAdfUEFHRUlEdAAFc3RlcDF0AANfSVNz%0AcgAOamF2YS5sYW5nLkxvbmc7i%2BSQzI8j3wIAAUoABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKG%0ArJUdC5TgiwIAAHhw%2F%2F%2F%2F%2F%2F%2BFqfx0AAxfVU5JVEFDQ05BTUVwdAAGX0xPR0lQdAARMjAxNzEyMDcx%0AMTA0NDU1NjR0AAhfQUNDTkFNRXQACeiSi%2BS4veWonHQACWlzU2FtZVBlcnQABWZhbHNldAAHX1BS%0AT0NJRHQACDYwMDIwMDA3dAALX1NFTkRPUEVSSUR0ABIyMzExODIxOTgzMDUxMzczMjR0ABBfREVQ%0AVVRZSURDQVJETlVNdAASMjMxMTgyMTk4MzA1MTM3MzI0dAAJX1NFTkRUSU1FdAAKMjAxNy0xMi0w%0AN3QAC19CUkFOQ0hLSU5EdAABMHQACV9TRU5EREFURXQACjIwMTctMTItMDd0ABNDVVJSRU5UX1NZ%0AU1RFTV9EQVRFcQB%2BAB90AAVfVFlQRXQABGluaXR0AAdfSVNDUk9QcQB%2BAB10AAlfUE9SQ05BTUV0%0AABXkuKrkurrmmI7nu4botKbmn6Xor6J0AAdfVVNCS0VZcHQACF9XSVRIS0VZcQB%2BAB14dAAIQFN5%0Ac0RhdGV0AAdAU3lzRGF5dAAJQFN5c01vbnRodAAIQFN5c1RpbWV0AAhAU3lzV2Vla3QACEBTeXNZ%0AZWFy";
 				webRequest.setRequestBody(requestBody);
		 		page = webClient.getPage(webRequest);  
		 		if(null!=page){
		 			html = page.getWebResponse().getContentAsString(); 
		 			HousingWeiHaiHtml housingWeiHaiHtml=new HousingWeiHaiHtml();
		 			housingWeiHaiHtml.setHtml(html);
		 			housingWeiHaiHtml.setPagenumber(1);
		 			housingWeiHaiHtml.setTaskid(taskHousing.getTaskid());
		 			housingWeiHaiHtml.setType(""+year+"个人明细账源码页");
		 			housingWeiHaiHtml.setUrl(url);
		 			housingWeiHaiHtmlRepository.save(housingWeiHaiHtml);
		 			List<HousingWeiHaiDetailAccount> list = housingFundWeiHaiParser.detailAccountParser(html,taskHousing);
		 			if(null!=list && list.size()>0){
		 				housingWeiHaiDetailAccountRepository.saveAll(list);
		 				updatePayStatusByTaskid("数据采集中，"+year+"个人明细账信息已采集完成", 200,taskHousing.getTaskid());
		 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，"+year+"年个人明细账信息已采集完成");
		 			}else{
		 				updatePayStatusByTaskid("数据采集中，"+year+"个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
		 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，"+year+"个人明细账信息暂无可采集数据");
		 			}
		 		}
	 		}else{
	 			//未查询到满足个人条件的信息
	 			updatePayStatusByTaskid("数据采集中，"+year+"个人明细账信息已采集完成",201,taskHousing.getTaskid() );
				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，"+year+"个人明细账信息暂无可采集数据");
	 		}
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
}
