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
import com.microservice.dao.entity.crawler.housing.jinan.HousingJiNanDetailAccount;
import com.microservice.dao.entity.crawler.housing.jinan.HousingJiNanHtml;
import com.microservice.dao.entity.crawler.housing.jinan.HousingJiNanUserInfo;
import com.microservice.dao.repository.crawler.housing.jinan.HousingJiNanDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.jinan.HousingJiNanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.jinan.HousingJiNanUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundJiNanParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import net.sf.json.JSONObject;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.jinan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.jinan"})
public class HousingFundJiNanCrawlerService extends HousingBasicService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundJiNanParser housingFundJiNanParser;
	@Autowired
	private HousingJiNanUserInfoRepository housingJiNanUserInfoRepository;
	@Autowired
	private HousingJiNanHtmlRepository housingJiNanHtmlRepository;
	@Autowired
	private HousingJiNanDetailAccountRepository housingJiNanDetailAccountRepository;
	@Autowired
	private  HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	//获取用户信息
	@Async
	public void getUserInfo(TaskHousing taskHousing) {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://"+loginHost+"/jnwt/init.summer?_PROCID=60020009"; 
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
	 		Page hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			String html = hPage.getWebResponse().getContentAsString(); 
	 			HousingJiNanHtml housingJiNanHtml=new HousingJiNanHtml();
	 			housingJiNanHtml.setHtml(html);
	 			housingJiNanHtml.setPagenumber(1);
	 			housingJiNanHtml.setTaskid(taskHousing.getTaskid());
	 			housingJiNanHtml.setType("用户基本信息源码页");
	 			housingJiNanHtml.setUrl(url);
	 			housingJiNanHtmlRepository.save(housingJiNanHtml);
	 			//解析用户信息
	 			HousingJiNanUserInfo housingJiNanUserInfo=housingFundJiNanParser.userInfoParser(html,taskHousing);
	 			if(null!=housingJiNanUserInfo){
	 				housingJiNanUserInfoRepository.save(housingJiNanUserInfo);
	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
					try {
						getDetailAccount(taskHousing,housingJiNanUserInfo);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getDetailAccount.e", e.toString());
						updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid() );
					}
					updateTaskHousing(taskHousing.getTaskid());
	 			}
	 		}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
			updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid() );
			updateTaskHousing(taskHousing.getTaskid());
		}
	}
	//获取明细账信息
	private void getDetailAccount(TaskHousing taskHousing, HousingJiNanUserInfo housingJiNanUserInfo) throws Exception {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url0="http://"+loginHost+"/jnwt/command.summer?uuid=1512444743373";
		WebRequest webRequest0 = new WebRequest(new URL(url0), HttpMethod.POST); 
		webRequest0.setAdditionalHeader("Host", ""+loginHost+"");
		webRequest0.setAdditionalHeader("Origin", "http://"+loginHost+"");
		webRequest0.setAdditionalHeader("Referer", "http://"+loginHost+"/jnwt/init.summer?_PROCID=60020007");
		String presentDate = HousingFundHelperService.getPresentDate();
		String requestBody0="$page:/ydpx/60020007/602007_01.ydpx"
				+ "&_ACCNUM="+housingJiNanUserInfo.getAccnum().trim()+""
				+ "&_PAGEID=step1"
				+ "&_IS=-8480096"
				+ "&_UNITACCNAME="+housingJiNanUserInfo.getUnitaccname().trim()+""
				+ "&_ACCNAME="+housingJiNanUserInfo.getAccname().trim()+""
				+ "&isSamePer=false"
				+ "&_PROCID=60020007"
				+ "&_SENDOPERID="+housingJiNanUserInfo.getAccnum().trim()+""
				+ "&_DEPUTYIDCARDNUM="+housingJiNanUserInfo.getCertinum().trim()+""
				+ "&_SENDTIME="+presentDate+""
				+ "&_BRANCHKIND=0"
				+ "&_SENDDATE="+presentDate+""
				+ "&CURRENT_SYSTEM_DATE="+presentDate+""
				+ "&_TYPE=init&_ISCROP=1"
				+ "&_PORCNAME=当年个人明细账查询"
				+ "&_WITHKEY=0"
				+ "&BegDate=2014-01-01"
				+ "&EndDate="+presentDate+""
				+ "&dynamicTable_flag=0"
				+ "&instancenum=-8480096";
		webRequest0.setRequestBody(requestBody0);
		Page page0 = webClient.getPage(webRequest0);
		Thread.sleep(1000);
		if(null!=page0){
			String html0=page0.getWebResponse().getContentAsString();
			tracer.addTag("确定查询日期范围后，返回数据有无提示的页面是：", html0);
			System.out.println("确定查询日期范围后，返回数据有无提示的页面是："+html0);
			String returnCode = JSONObject.fromObject(html0).getString("returnCode");
			if(returnCode.equals("0")){ //查询年有数据
				String url="http://"+loginHost+"/jnwt/dynamictable?uuid=1512457480281";
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST); 
				String requestBody="&dynamicTable_id=datalist"
						+ "&dynamicTable_currentPage=1"
						+ "&dynamicTable_pageSize=300"  //设置为显示300条数据
						+ "&dynamicTable_nextPage=1"
						+ "&dynamicTable_page=%2Fydpx%2F60020007%2F602007_01.ydpx"
						+ "&dynamicTable_paging=true&dynamicTable_configSqlCheck=0"
						+ "&errorFilter=1%3D1"
						+ "&BegDate=2014-01-01"    // 只能查询2014-01-01之后的数据
						+ "&EndDate="+presentDate+""
						+ "&_APPLY=0&_CHANNEL=1"
						+ "&_PROCID=60020007"
						+ "&_LoginType=1"
						+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAhkYXRhbGlzdHQA%2BHNlbGVjdCB0cmFuc2RhdGUsIG9wZXIsIHNleCwgYW10MSwg%0AYW10MiwoY2FzZSBiZWdpbmRhdGVjIHdoZW4gJzE4OTkxMicgdGhlbiAnJyBlbHNlIGJlZ2luZGF0%0AZWMgZW5kKSBiZWdpbmRhdGVjLCAoY2FzZSBlbmRkYXRlYyB3aGVuICcxODk5MTInIHRoZW4gJycg%0AZWxzZSBlbmRkYXRlYyBlbmQpIGVuZGRhdGVjLCBpbnN0YW5jZW51bSBmcm9tIGRwMDc3IHdoZXJl%0AIGluc3RhbmNlbnVtID0gLTg0ODAwOTYgb3JkZXIgYnkgdHJhbnNkYXRleA%3D%3D"
						+ "&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABR0AAdf%0AQUNDTlVNdAAMMjAyMTU1MTI0Nzc5dAALX1VOSVRBQ0NOVU1wdAAHX1BBR0VJRHQABXN0ZXAxdAAD%0AX0lTc3IADmphdmEubGFuZy5Mb25nO4vkkMyPI98CAAFKAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVt%0AYmVyhqyVHQuU4IsCAAB4cP%2F%2F%2F%2F%2F%2FfpqgdAAMX1VOSVRBQ0NOQU1FcHQABl9MT0dJUHQAETIwMTcx%0AMjA1MTQwMzUyMjUwdAAIX0FDQ05BTUV0AAnljY7ojo7ojo50AAlpc1NhbWVQZXJ0AAVmYWxzZXQA%0AB19QUk9DSUR0AAg2MDAyMDAwN3QAC19TRU5ET1BFUklEdAAMMjAyMTU1MTI0Nzc5dAAQX0RFUFVU%0AWUlEQ0FSRE5VTXQAEjM3MDgyNjE5OTIwMzAxMDA0MXQACV9TRU5EVElNRXQACjIwMTctMTItMDV0%0AAAtfQlJBTkNIS0lORHQAATB0AAlfU0VORERBVEV0AAoyMDE3LTEyLTA1dAATQ1VSUkVOVF9TWVNU%0ARU1fREFURXEAfgAfdAAFX1RZUEV0AARpbml0dAAHX0lTQ1JPUHQAATF0AAlfUE9SQ05BTUV0ABvl%0AvZPlubTkuKrkurrmmI7nu4botKbmn6Xor6J0AAdfVVNCS0VZcHQACF9XSVRIS0VZcQB%2BAB14dAAI%0AQFN5c0RhdGV0AAdAU3lzRGF5dAAJQFN5c01vbnRodAAIQFN5c1RpbWV0AAhAU3lzV2Vla3QACEBT%0AeXNZZWFy";
				webRequest.setAdditionalHeader("Host", ""+loginHost+"");
				webRequest.setAdditionalHeader("Origin", "http://"+loginHost+"");
				webRequest.setAdditionalHeader("Referer", "http://"+loginHost+"/jnwt/init.summer?_PROCID=60020007");
				webRequest.setRequestBody(requestBody);
				Page page = webClient.getPage(webRequest);
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
		 			HousingJiNanHtml housingJiNanHtml=new HousingJiNanHtml();
		 			housingJiNanHtml.setHtml(html);
		 			housingJiNanHtml.setPagenumber(1);
		 			housingJiNanHtml.setTaskid(taskHousing.getTaskid());
		 			housingJiNanHtml.setType("2014年至今个人明细账源码页");
		 			housingJiNanHtml.setUrl(url);
		 			housingJiNanHtmlRepository.save(housingJiNanHtml);
		 			List<HousingJiNanDetailAccount> list = housingFundJiNanParser.detailAccountParser(html,taskHousing);
		 			if(null!=list && list.size()>0){
		 				housingJiNanDetailAccountRepository.saveAll(list);
		 				updatePayStatusByTaskid("数据采集中，2014年至今个人明细账信息已采集完成", 200,taskHousing.getTaskid());
		 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，2014年至今个人明细账信息已采集完成");
		 			}else{
		 				updatePayStatusByTaskid("数据采集中，2014年至今个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
		 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，2014年至今个人明细账信息暂无可采集数据");
		 			}
				}
			}else{
				//未查询到满足个人条件的信息
	 			updatePayStatusByTaskid("数据采集中，2014年至今个人明细账信息已采集完成",201,taskHousing.getTaskid() );
				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，2014年至今个人明细账信息暂无可采集数据");
			}
		}
	}
}
