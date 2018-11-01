package app.service;

import java.net.URL;
import java.net.URLEncoder;
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
import com.microservice.dao.entity.crawler.housing.hengshui.HousingHengShuiDetailAccount;
import com.microservice.dao.entity.crawler.housing.hengshui.HousingHengShuiHtml;
import com.microservice.dao.entity.crawler.housing.hengshui.HousingHengShuiUserInfo;
import com.microservice.dao.repository.crawler.housing.hengshui.HousingHengShuiDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.hengshui.HousingHengShuiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.hengshui.HousingHengShuiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundHengShuiParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import net.sf.json.JSONObject;


/**
 * @description:  衡水市公积金信息爬取service
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.hengshui"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.hengshui"})
public class HousingFundHengShuiCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundHengShuiCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundHengShuiParser housingFundHengShuiParser;
	@Autowired
	private HousingHengShuiHtmlRepository housingHengShuiHtmlRepository;
	@Autowired
	private HousingHengShuiDetailAccountRepository housingHengShuiDetailAccountRepository;
	@Autowired
	private HousingHengShuiUserInfoRepository housingHengShuiUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	@Async
	public void getUserInfo(TaskHousing taskHousing){
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.setJavaScriptTimeout(5000); //设置超时时间
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://"+loginHost+"/wsyyt/init.summer?_PROCID=60020007"; 
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
	 		HtmlPage hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			String html = hPage.getWebResponse().getContentAsString(); 
	 			webClient=hPage.getWebClient();
	 			if(html.contains("个人姓名")){   //作为能够响应数据的一个判断
	 				Document doc=Jsoup.parse(html);
	 				//通过调研，（个人信息中）只有个人账号在后来的部分请求中起到作用
	 				//获取姓名
	 				String accName= doc.getElementById("AccName").val();
	 				//获取个人账号
	 				String accNum= doc.getElementById("AccNum").val();
	 				//获取身份证号
	 				String certiNum= doc.getElementById("CertiNum").val();
	 				//获取单位名称
	 				String unitAccName= doc.getElementById("UnitAccName").val();
	 				//获取单位账号
	 				String unitAccNum= doc.getElementById("UnitAccNum").val();
	 				url="http://"+loginHost+"/wsyyt/command.summer?uuid=1512540252233";
	 				webRequest= new WebRequest(new URL(url), HttpMethod.POST);  
	 				//涉及到汉字的参数需要加密，不然返回的信息中涉及到了汉字，返回的是?代替的
	 				String encodeName=URLEncoder.encode(accName.trim(), "utf-8");
	 				String encodeUnitAccName=URLEncoder.encode(unitAccName.trim(), "utf-8");
	 				String requestBody=""
	 						+ "%24page=%2Fydpx%2F60020007%2F602007_01.ydpx"
	 						+ "&_ACCNUM="+accNum+""
	 						+ "&_UNITACCNUM="+unitAccNum+""
	 						+ "&_PAGEID=step1"
	 						+ "&_IS=-278100"
	 						+ "&_UNITACCNAME="+encodeUnitAccName+""
	 						+ "&_ACCNAME="+encodeName+""
	 						+ "&isSamePer=false"
	 						+ "&_SENDOPERID="+certiNum+""
	 						+ "&_PROCID=60020007"
	 						+ "&_DEPUTYIDCARDNUM="+certiNum+""
							+ "&_BRANCHKIND=0"
							+ "&_TYPE=init"
	 						+ "&_ISCROP=0"
	 						+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E8%B4%A6%E6%9F%A5%E8%AF%A2"
	 						+ "&_WITHKEY=0"
	 						+ "&AccName="+encodeName+""
	 						+ "&AccNum="+accNum+""
	 						+ "&CertiNum="+certiNum+""
	 						+ "&OpenDate="
	 						+ "&Balance="
	 						+ "&UnitAccNum="+unitAccNum+""
	 						+ "&UnitAccName="+encodeUnitAccName+""
	 						+ "&ajaxid=query1"
	 						+ "&accnum="+accNum+"";
	 				webRequest.setRequestBody(requestBody);
	 				//如下信息必须加上，不然返回的用汉字展示的信息会是乱码
	 				webRequest.setAdditionalHeader("Host", ""+loginHost+"");
	 				webRequest.setAdditionalHeader("Origin", "http://"+loginHost+"");
	 				webRequest.setAdditionalHeader("Referer", "http://"+loginHost+"/wsyyt/init.summer?_PROCID=60020007");
	 				webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	 				Page page = webClient.getPage(webRequest);
	 				if(page!=null){
	 					html=page.getWebResponse().getContentAsString();
	 	 				HousingHengShuiHtml housingHengShuiHtml=new HousingHengShuiHtml();
	 	 	 			housingHengShuiHtml.setHtml(html);
	 	 	 			housingHengShuiHtml.setPagenumber(1);
	 	 	 			housingHengShuiHtml.setTaskid(taskHousing.getTaskid());
	 	 	 			housingHengShuiHtml.setType("用户基本信息源码页");
	 	 	 			housingHengShuiHtml.setUrl(url);
	 	 	 			housingHengShuiHtmlRepository.save(housingHengShuiHtml);
	 	 	 			HousingHengShuiUserInfo housingHengShuiUserInfo= housingFundHengShuiParser.userInfoParser(html,taskHousing);
	 	 	 			if(null!=housingHengShuiUserInfo){
	 	 	 				housingHengShuiUserInfoRepository.save(housingHengShuiUserInfo);
	 	 	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
	 	 	 				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
	 	 	 				//之后用获取的个人账号信息采集流水信息
	 	 	 				try {
	 	 	 					getDetailAccount(taskHousing,accNum);
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
	public void getDetailAccount(TaskHousing taskHousing, String accNum) throws Exception {
		//默认只能查当前年开始往前推三年的，因为在写测试类的时候发现，哪怕传的参数是2014，返回的数据也是2015年的
		for(int i=0;i<3;i++){  //获取3年的数据
			WebClient webClient=housingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			String year = HousingFundHelperService.getYear(i);
			//访问这个请求之前需要访问一个带全参数的请求,这个参数中带有要请求的年份
			String url="http://"+loginHost+"/wsyyt/command.summer?uuid=1512528650108";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);   
			String requestBody=""
//	 				+ "%24page=%2Fydpx%2F60020007%2F602007_01.ydpx"
	 				+ "&_ACCNUM="+accNum.trim()+""
	 				+ "&_PAGEID=step1"
	 				+ "&_PROCID=60020007"
	 				+ "&AccNum="+accNum.trim()+""
	 				+ "&accnum="+accNum.trim()+""
	 				+ "&year="+year+"";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			String html=page.getWebResponse().getContentAsString();
			String returnCode = JSONObject.fromObject(html).getString("returnCode");
			//如下内容意为：未查询到满足条件的个人明细
			if(returnCode.equals("0")){
				//把每一页显示的数据设置为了100条
				url = "http://"+loginHost+"/wsyyt/dynamictable?uuid=1512528651954"; 
		 		webRequest = new WebRequest(new URL(url), HttpMethod.POST);  
		 		requestBody=""
 						+ "dynamicTable_id=list1"
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
 						+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAVsaXN0MXQAsz0ic2VsZWN0IChjYXNlIHRyYW5zZGF0ZSB3aGVuICcxODk5LTEy%0ALTMxJyB0aGVuIG51bGwgZWxzZSB0cmFuc2RhdGUgZW5kKSBhcyB0cmFuc2RhdGUsIGNlcnRpbnVt%0ALCBhbXQyLCBhbXQxLCBiYXNlbnVtLCBpbnN0YW5jZSBmcm9tIGRwMDc3IHdoZXJlIGluc3RhbmNl%0AID0gIitfSVMgKyIgb3JkZXIgYnkgc2Vxbm8ieA%3D%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABZ0AAhf%0AQnJjQ29kZXB0AAdfQUNDTlVNdAAMMTEzMDEyNzc2ODkydAALX1VOSVRBQ0NOVU10AAsyMDEwMTA1%0AMDQ0OXQAB19QQUdFSUR0AAVzdGVwMXQAA19JU3NyAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgAB%0ASgAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHD%2F%2F%2F%2F%2F%2F%2FvIXnQADF9VTklU%0AQUNDTkFNRXQANuefs%2BWutuW6hOivuuS6mumAmuS%2FoeacjeWKoeaciemZkOWFrOWPuOihoeawtOWI%0AhuWFrOWPuHQABl9MT0dJUHQADTE3Mi4xNi4xMTAuNTV0AAhfQUNDTkFNRXQACemDnembqueHlXQA%0ACWlzU2FtZVBlcnQABWZhbHNldAAJX0NFTlRFUklEcHQAC19TRU5ET1BFUklEdAASMTMxMTAyMTk5%0AMTExMjMwMjIwdAAHX1BST0NJRHQACDYwMDIwMDA3dAAQX0RFUFVUWUlEQ0FSRE5VTXQAEjEzMTEw%0AMjE5OTExMTIzMDIyMHQACV9TRU5EVElNRXQACjIwMTctMTItMDV0AAlfU0VORERBVEV0AAoyMDE3%0ALTEyLTA1dAALX0JSQU5DSEtJTkR0AAEwdAATQ1VSUkVOVF9TWVNURU1fREFURXEAfgAhdAAFX1RZ%0AUEV0AARpbml0dAAHX0lTQ1JPUHEAfgAjdAAJX1BPUkNOQU1FdAAV5Liq5Lq65piO57uG6LSm5p%2Bl%0A6K%2BidAAHX1VTQktFWXB0AAhfV0lUSEtFWXEAfgAjeHQACEBTeXNEYXRldAAHQFN5c0RheXQACUBT%0AeXNNb250aHQACEBTeXNUaW1ldAAIQFN5c1dlZWt0AAhAU3lzWWVhcg%3D%3D";
 				webRequest.setRequestBody(requestBody);
		 		page = webClient.getPage(webRequest);  
		 		if(null!=page){
		 			html = page.getWebResponse().getContentAsString(); 
		 			HousingHengShuiHtml housingHengShuiHtml=new HousingHengShuiHtml();
		 			housingHengShuiHtml.setHtml(html);
		 			housingHengShuiHtml.setPagenumber(1);
		 			housingHengShuiHtml.setTaskid(taskHousing.getTaskid());
		 			housingHengShuiHtml.setType(""+year+"个人明细账源码页");
		 			housingHengShuiHtml.setUrl(url);
		 			housingHengShuiHtmlRepository.save(housingHengShuiHtml);
		 			List<HousingHengShuiDetailAccount> list = housingFundHengShuiParser.detailAccountParser(html,taskHousing);
		 			if(null!=list && list.size()>0){
		 				housingHengShuiDetailAccountRepository.saveAll(list);
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
