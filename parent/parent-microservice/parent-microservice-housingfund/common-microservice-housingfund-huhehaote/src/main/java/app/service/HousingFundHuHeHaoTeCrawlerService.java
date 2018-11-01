package app.service;

import java.net.URL;
import java.net.URLEncoder;
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
import com.microservice.dao.entity.crawler.housing.huhehaote.HousingHuHeHaoTeDetailAccount;
import com.microservice.dao.entity.crawler.housing.huhehaote.HousingHuHeHaoTeHtml;
import com.microservice.dao.entity.crawler.housing.huhehaote.HousingHuHeHaoTeUserInfo;
import com.microservice.dao.repository.crawler.housing.huhehaote.HousingHuHeHaoTeDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.huhehaote.HousingHuHeHaoTeHtmlRepository;
import com.microservice.dao.repository.crawler.housing.huhehaote.HousingHuHeHaoTeUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundHuHeHaoTeParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import net.sf.json.JSONObject;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.huhehaote"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.huhehaote"})
public class HousingFundHuHeHaoTeCrawlerService extends HousingBasicService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundHuHeHaoTeParser housingFundHuHeHaoTeParser;
	@Autowired
	private HousingHuHeHaoTeUserInfoRepository housingHuHeHaoTeUserInfoRepository;
	@Autowired
	private HousingHuHeHaoTeHtmlRepository housingHuHeHaoTeHtmlRepository;
	@Autowired
	private HousingHuHeHaoTeDetailAccountRepository housingHuHeHaoTeDetailAccountRepository;
	@Autowired
	private  HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	//获取用户信息
	@Async
	public TaskHousing getUserInfo(TaskHousing taskHousing){
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://222.74.26.19/hhhtwsyyt/init.summer?_PROCID=60022001"; 
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
	 		Page hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			String html = hPage.getWebResponse().getContentAsString(); 
	 			HousingHuHeHaoTeHtml housingHuHeHaoTeHtml=new HousingHuHeHaoTeHtml();
	 			housingHuHeHaoTeHtml.setHtml(html);
	 			housingHuHeHaoTeHtml.setPagenumber(1);
	 			housingHuHeHaoTeHtml.setTaskid(taskHousing.getTaskid());
	 			housingHuHeHaoTeHtml.setType("用户基本信息源码页");
	 			housingHuHeHaoTeHtml.setUrl(url);
	 			housingHuHeHaoTeHtmlRepository.save(housingHuHeHaoTeHtml);
	 			tracer.addTag("action.crawler.getUserInfo.html", "用户基本信息源码页已入库");
	 			//解析用户信息
	 			HousingHuHeHaoTeUserInfo housingHuHeHaoTeUserInfo=housingFundHuHeHaoTeParser.userInfoParser(html,taskHousing);
	 			if(null!=housingHuHeHaoTeUserInfo){
	 				housingHuHeHaoTeUserInfoRepository.save(housingHuHeHaoTeUserInfo);
	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
					try {
						getDetailAccount(taskHousing,housingHuHeHaoTeUserInfo);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getDetailAccount.e",e.toString());
						updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid() );
						//此处也要更新最终爬取状态
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
		return taskHousing;
	}
	//获取明细账信息
	private void getDetailAccount(TaskHousing taskHousing, HousingHuHeHaoTeUserInfo housingHuHeHaoTeUserInfo) throws Exception {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://222.74.26.19/hhhtwsyyt/command.summer?uuid=1515047889488";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST); 
		String accnum = housingHuHeHaoTeUserInfo.getAccnum().trim();
		String encodeName=URLEncoder.encode(housingHuHeHaoTeUserInfo.getAccname().trim(), "utf-8");
		//经过测试，有些参数是可以省略掉的
 		String requestBody=""
 				+ "&_RW=w"
 				+ "&_TYPE=init"
 				+ "&_BRANCHKIND=0"
 				+ "&_SENDOPERID=150929199304182129"
 				+ "&_WITHKEY=0"
 				+ "&isSamePer=false"
 				+ "&_ACCNUM="+accnum+""
 				+ "&_PAGEID=step1"
 				+ "&_DEPUTYIDCARDNUM=150929199304182129"
 				+ "&_PROCID=60022002"
 				+ "&_IS=-158984"
 				+ "&_ISCROP=0"
 				+ "&_ACCNAME="+encodeName+""
 				+ "&accnum="+accnum+""
 				+ "&begindate=2000-01-01"    //爬取从2000年开始的数据
 				+ "&enddate="+HousingFundHelperService.getPresentDate().trim()+""
 				+ "&certinum="  //经过测试，这两个参数是必须的
 				+ "&accname=";
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			String returnCode = JSONObject.fromObject(html).getString("returnCode");
			if(returnCode.equals("0")){ //查询的年份区间有数据，上述链接的参数需要带有指定的年份区间，若是指示返回有数据，如下请求链接的参数中可以省略年份区间，直接改变每一页显示的总记录数
				url="http://222.74.26.19/hhhtwsyyt/dynamictable?uuid=1515047890557";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST); 
				requestBody="dynamicTable_id=datalist1"
						+ "&dynamicTable_currentPage=0"
						+ "&dynamicTable_pageSize=300"
						+ "&dynamicTable_nextPage=1"
						+ "&dynamicTable_page=%2Fydpx%2F611812%2Fdppage1812.ydpx"
						+ "&dynamicTable_paging=true"
						+ "&dynamicTable_configSqlCheck=0"
						+ "&errorFilter=1%3D1"
						+ "&accnum=113062621292"
//						+ "&unitaccnum=111000107037"
//						+ "&unitaccname=%E5%91%BC%E5%92%8C%E6%B5%A9%E7%89%B9%E5%B8%82%E6%98%93%E6%89%8D%E4%BA%BA%E5%8A%9B%E8%B5%84%E6%BA%90%E6%9C%89%E9%99%90%E8%B4%A3%E4%BB%BB%E5%85%AC%E5%8F%B8A"
//						+ "&begindate=2016-11-03"
//						+ "&enddate=2018-01-31"
						+ "&certinum="
						+ "&accname="
						+ "&_APPLY=0"
						+ "&_CHANNEL=1"
						+ "&_PROCID=60022002"
						+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDF0AY1zZWxlY3QgdG9fY2hhcih0cmFuc2RhdGUsJ3l5eXktbW0t%0AZGQnKSB0cmFuc2RhdGUsIHJlYXNvbiwgYW10MSwgYW10MiwgYmFzZW51bWJlciwgdW5pdGFjY251%0AbTEsIHVuaXRhY2NuYW1lLCAoY2FzZSB0b19jaGFyKGJlZ2luZGF0ZSwgJ3l5eXktbW0tZGQnKSB3%0AaGVuICcxODk5LTEyLTMxJyB0aGVuICcnIGVsc2UgdG9fY2hhcihiZWdpbmRhdGUsICd5eXl5LW1t%0ALWRkJykgZW5kKSBiZWdpbmRhdGUsIChjYXNlIHRvX2NoYXIoZW5kZGF0ZSwgJ3l5eXktbW0tZGQn%0AKSB3aGVuICcxODk5LTEyLTMxJyB0aGVuICcnIGVsc2UgdG9fY2hhcihiZWdpbmRhdGUsICd5eXl5%0ALW1tLWRkJykgZW5kKSBlbmRkYXRlLCBzZXgsIGluc3RhbmNlbnVtIGZyb20gZHAwNzcgd2hlcmUg%0AaW5zdGFuY2VudW0gPSAtMTU4OTg0eA%3D%3D"
						+ "&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AANf%0AUld0AAF3dAAFX1RZUEV0AARpbml0dAALX0JSQU5DSEtJTkR0AAEwdAATQ1VSUkVOVF9TWVNURU1f%0AREFURXQACjIwMTgtMDEtMDR0AAtfU0VORE9QRVJJRHQAEjE1MDkyOTE5OTMwNDE4MjEyOXQACF9X%0ASVRIS0VZcQB%2BAAl0AAlpc1NhbWVQZXJ0AAVmYWxzZXQAB19BQ0NOVU10AAwxMTMwNjI2MjEyOTJ0%0AAAdfUEFHRUlEdAAFc3RlcDF0ABBfREVQVVRZSURDQVJETlVNdAASMTUwOTI5MTk5MzA0MTgyMTI5%0AdAAJX1BPUkNOQU1FdAAV5Liq5Lq65piO57uG6LSm5p%2Bl6K%2BidAALX1VOSVRBQ0NOVU1wdAAGX0xP%0AR0lQdAARMjAxODAxMDQxNDM2MTE1NjV0AAdfUFJPQ0lEdAAINjAwMjIwMDJ0AAxfVU5JVEFDQ05B%0ATUVwdAADX0lTc3IADmphdmEubGFuZy5Mb25nO4vkkMyPI98CAAFKAAV2YWx1ZXhyABBqYXZhLmxh%0AbmcuTnVtYmVyhqyVHQuU4IsCAAB4cP%2F%2F%2F%2F%2F%2F%2FZL4dAAHX0lTQ1JPUHEAfgAJdAAHX1VTQktFWXB0%0AAAlfU0VORERBVEVxAH4AC3QACF9BQ0NOQU1FdAAJ6ZO25pmT5aifdAAJX1NFTkRUSU1FdAAKMjAx%0AOC0wMS0wNHh0AAhAU3lzRGF0ZXQAB0BTeXNEYXl0AAlAU3lzTW9udGh0AAhAU3lzVGltZXQACEBT%0AeXNXZWVrdAAIQFN5c1llYXI%3D";
 				webRequest.setRequestBody(requestBody);
				page = webClient.getPage(webRequest);
				if(null!=page){
					html=page.getWebResponse().getContentAsString();
		 			tracer.addTag("action.crawler.getDetailAccount.html", "获取的2000年至今个人明细账源码页已经入库");
		 			HousingHuHeHaoTeHtml housingHuHeHaoTeHtml=new HousingHuHeHaoTeHtml();
		 			housingHuHeHaoTeHtml.setHtml(html);
		 			housingHuHeHaoTeHtml.setPagenumber(1);
		 			housingHuHeHaoTeHtml.setTaskid(taskHousing.getTaskid());
		 			housingHuHeHaoTeHtml.setType("2000年至今个人明细账源码页");
		 			housingHuHeHaoTeHtml.setUrl(url);
		 			housingHuHeHaoTeHtmlRepository.save(housingHuHeHaoTeHtml);
		 			List<HousingHuHeHaoTeDetailAccount> list = housingFundHuHeHaoTeParser.detailAccountParser(html,taskHousing);
		 			if(null!=list && list.size()>0){
		 				housingHuHeHaoTeDetailAccountRepository.saveAll(list);
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
				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，2000年至今个人明细账信息暂无可采集数据");
				updateTaskHousing(taskHousing.getTaskid());
			}
		}
	}
}
