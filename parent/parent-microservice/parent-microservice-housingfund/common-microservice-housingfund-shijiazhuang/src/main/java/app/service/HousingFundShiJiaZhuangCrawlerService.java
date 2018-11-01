package app.service;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
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
import com.microservice.dao.entity.crawler.housing.shijiazhuang.HousingShiJiaZhuangDetailAccount;
import com.microservice.dao.entity.crawler.housing.shijiazhuang.HousingShiJiaZhuangHtml;
import com.microservice.dao.entity.crawler.housing.shijiazhuang.HousingShiJiaZhuangUserInfo;
import com.microservice.dao.repository.crawler.housing.shijiazhuang.HousingShiJiaZhuangDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.shijiazhuang.HousingShiJiaZhuangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.shijiazhuang.HousingShiJiaZhuangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundShiJiaZhuangParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;


/**
 * @description:  石家庄公积金信息爬取service
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.shijiazhuang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.shijiazhuang"})
public class HousingFundShiJiaZhuangCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundShiJiaZhuangCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundShiJiaZhuangParser housingFundShiJiaZhuangParser;
	@Autowired
	private HousingShiJiaZhuangHtmlRepository housingShiJiaZhuangHtmlRepository;
	@Autowired
	private HousingShiJiaZhuangDetailAccountRepository housingShiJiaZhuangDetailAccountRepository;
	@Autowired
	private HousingShiJiaZhuangUserInfoRepository housingShiJiaZhuangUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Async
	public void getUserInfo(TaskHousing taskHousing) throws Exception {
		//获取爬取数据需要的cookie
		WebClient webClient = housingFundHelperService.addcookie(taskHousing);
		String url = "http://www.sjzgjj.cn/wsyyt/init.summer?_PROCID=60020007"; 
 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
 		HtmlPage hPage = webClient.getPage(webRequest);  
 		if(null!=hPage){
 			String html = hPage.getWebResponse().getContentAsString(); 
 			webClient=hPage.getWebClient();
 			tracer.addTag("action.crawler.getUserInfo.html===>1", "获取的用户基本信息的html第一部分(解析部分信息作为完整用户信息的请求参数)====>"+html);
 			System.out.println("获取的第一部分的用户信息是："+html);
 			if(html.contains("个人姓名")){
 				Document doc=Jsoup.parse(html);
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
 				String presentDate = HousingFundHelperService.getPresentDate();
 				//获取最终的数据，用如上参数最为最终的返回结果：
 				String  urlSecond="http://www.sjzgjj.cn/wsyyt/command.summer?uuid=1508729272715";
 				webRequest= new WebRequest(new URL(urlSecond), HttpMethod.POST);  
 				webRequest.setAdditionalHeader("Host", "www.sjzgjj.cn");
 				webRequest.setAdditionalHeader("Referer", "http://www.sjzgjj.cn/wsyyt/init.summer?_PROCID=60020007");
 				webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
				String requestBody="&_ACCNUM="+accNum+"&_UNITACCNUM="+unitAccNum+"&_IS=-2458970&_PAGEID=step1"
						+ "&_LOGIP=123.126.87.169"
						+ "&isSamePer=true&_PROCID=60020007&_SENDOPERID="+certiNum+""
								+ "&temp_.itemid[3]=2&_BRANCHKIND=0&_SENDDATE="+presentDate+""
										+ "&temp_.itemid[1]=0&temp_._xh[4]=4&CURRENT_SYSTEM_DATE="+presentDate+""
												+ "&temp_.itemval[4]=销户"
												+ "&_ISCROP=0&_TYPE=init&temp_._xh[2]=2&_PORCNAME=个人明细账查询"
												+ "&temp_.itemval[2]=封存&temp__rownum=4&_UNITACCNAME="+unitAccName+""
														+ "&_ACCNAME="+accName+"&_DEPUTYIDCARDNUM="+certiNum+""
														+ "&$page=/ydpx/60020007/602007_01.ydpx&temp_.itemid[4]=9"
														+ "&_SENDTIME="+presentDate+"&temp_.itemid[2]=1&temp_._xh[3]=3"
														+ "&temp_.itemval[3]=空账&_WITHKEY=0&temp_._xh[1]=1"
														+ "&temp_.itemval[1]=正常&AccName="+accName+"&AccNum="+accNum+""
														+ "&CertiNum="+certiNum+"&OpenDate=&Balance=&UnitAccNum="+unitAccNum+""
//														+ "&UnitAccName="+unitAccName+"&indiaccstate=0"
														+ "&UnitAccName="+unitAccName+""
																+ "&prin=&intamt=&date1=&trancode=118898"
																+ "&accname2=query1&accnum="+accNum+"";
 				webRequest.setRequestBody(requestBody);
 				webRequest.setCharset(Charset.forName("gbk")); 
 				Page page2 = webClient.getPage(webRequest);
 				if(page2!=null){
 					html=page2.getWebResponse().getContentAsString();
 					tracer.addTag("action.crawler.getUserInfo.html===>2", "获取的用户基本信息的html(完整信息)====>"+html);
 	 				System.out.println("获取的第二个页面是："+html);
 	 				HousingShiJiaZhuangHtml housingShiJiaZhuangHtml=new HousingShiJiaZhuangHtml();
 	 	 			housingShiJiaZhuangHtml.setHtml(html);
 	 	 			housingShiJiaZhuangHtml.setPagenumber(1);
 	 	 			housingShiJiaZhuangHtml.setTaskid(taskHousing.getTaskid());
 	 	 			housingShiJiaZhuangHtml.setType("用户基本信息源码页");
 	 	 			housingShiJiaZhuangHtml.setUrl(url);
 	 	 			housingShiJiaZhuangHtmlRepository.save(housingShiJiaZhuangHtml);
 	 	 			//由于返回的json数据中，中文响应的都是?,故此处直接将第一个页面返回的姓名和单位名称作为参数
 	 	 			HousingShiJiaZhuangUserInfo housingShiJiaZhuangUserInfo= housingFundShiJiaZhuangParser.userInfoParser(html,accName,unitAccName,taskHousing);
 	 	 			if(null!=housingShiJiaZhuangUserInfo){
 	 	 				housingShiJiaZhuangUserInfoRepository.save(housingShiJiaZhuangUserInfo);
 	 	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
 	 	 				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
 	 	 				updateTaskHousing(taskHousing.getTaskid());
 	 	 				try {
 	 	 					//用户信息采集完成之后，将所有返回的数据作为缴费信息的请求参数
 	 	 	 				getDetailAccount(housingShiJiaZhuangUserInfo,taskHousing);
						} catch (Exception e) {
							tracer.addTag("action.crawler.getDetailAccount.e", taskHousing.getTaskid()+"  "+e.toString());
							updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid());
							updateTaskHousing(taskHousing.getTaskid());
						}
 	 	 			}else{
 	 	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息暂无可采集数据",201,taskHousing.getTaskid());
 	 	 				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
 	 	 				updateTaskHousing(taskHousing.getTaskid());
 	 	 			}
 				}
 			}
 		}
	}
//	@Async
	public void getDetailAccount(HousingShiJiaZhuangUserInfo housingShiJiaZhuangUserInfo,TaskHousing taskHousing) throws Exception {
		for(int i=0;i<3;i++){
			WebClient webClient = housingFundHelperService.addcookie(taskHousing);
			String year = HousingFundHelperService.getYear(i);
			//访问这个请求之前需要访问一个带全参数的请求,这个参数中带有要请求的年份
			String url0="http://www.sjzgjj.cn/wsyyt/command.summer?";
			WebRequest webRequest = new WebRequest(new URL(url0), HttpMethod.POST);   
	 		webRequest.setAdditionalHeader("Host", "www.sjzgjj.cn");
	 		webRequest.setAdditionalHeader("Referer", "http://www.sjzgjj.cn/wsyyt/init.summer?_PROCID=60020007");
			String requestBody="_ACCNUM="+housingShiJiaZhuangUserInfo.getAccnum().trim()+""
					+ "&_UNITACCNUM="+housingShiJiaZhuangUserInfo.getUnitaccnum().trim()+""
					+ "&_IS=-2483351"    //这个参数还有下边那个instance参数必须写成和第二个请求中返回值中一样的，不然不管传的参是那一年，都返回的是2016年的数据
					+ "&_PAGEID=step1&_LOGIP=123.126.87.166"
					+ "&isSamePer=true&_PROCID=60020007"
					+ "&_SENDOPERID="+housingShiJiaZhuangUserInfo.getCertinum().trim()+""
					+ "&temp_.itemid[3]=2&_BRANCHKIND=0"
					+ "&_SENDDATE="+HousingFundHelperService.getPresentDate().trim()+"&temp_.itemid[1]=0"
					+ "&temp_._xh[4]=4&CURRENT_SYSTEM_DATE="+HousingFundHelperService.getPresentDate().trim()+""
					+ "&temp_.itemval[4]=销户&_ISCROP=0&_TYPE=init&temp_._xh[2]=2"
					+ "&_PORCNAME=个人明细账查询&temp_.itemval[2]=封存"
					+ "&temp__rownum=4&_UNITACCNAME="+housingShiJiaZhuangUserInfo.getUnitaccname().trim()+""
					+ "&_ACCNAME="+housingShiJiaZhuangUserInfo.getAccname().trim()+""
							+ "&_DEPUTYIDCARDNUM="+housingShiJiaZhuangUserInfo.getCertinum().trim()+""
					+ "&$page=/ydpx/60020007/602007_01.ydpx&temp_.itemid[4]=9"
					+ "&_SENDTIME="+HousingFundHelperService.getPresentDate()+""
					+"&temp_.itemid[2]=1&temp_._xh[3]=3"
					+ "&temp_.itemval[3]=空账&_WITHKEY=0&temp_._xh[1]=1"
					+ "&temp_.itemval[1]=正常&AccName="+housingShiJiaZhuangUserInfo.getAccname().trim()+""
					+ "&AccNum="+housingShiJiaZhuangUserInfo.getAccnum().trim()+"&CertiNum="+housingShiJiaZhuangUserInfo.getCertinum()+""
					+ "&OpenDate="+housingShiJiaZhuangUserInfo.getOpenaccountdate().trim()+"&Balance="+housingShiJiaZhuangUserInfo.getBalance().trim()+""
					+ "&UnitAccNum="+housingShiJiaZhuangUserInfo.getUnitaccnum().trim()+"&UnitAccName="+housingShiJiaZhuangUserInfo.getUnitaccname().trim()+""
					+ "&indiaccstate="+housingShiJiaZhuangUserInfo.getStatuscode().trim()+"&prin="+housingShiJiaZhuangUserInfo.getChargebasenum().trim()+"&intamt="+housingShiJiaZhuangUserInfo.getChargemonth().trim()+""
					+ "&date1="+housingShiJiaZhuangUserInfo.getChargetodate().trim()+"&trancode=110015&accnum="+housingShiJiaZhuangUserInfo.getAccnum().trim()+""
					+ "&unitaccnum="+housingShiJiaZhuangUserInfo.getUnitaccnum().trim()+""
					+ "&year="+year+""
					+ "&instance=-2483351";
			webRequest.setRequestBody(requestBody);
			Page page0 = webClient.getPage(webRequest);
			String html0=page0.getWebResponse().getContentAsString();
			tracer.addTag("点击要查询的年份"+year+"后，返回的页面是：", html0);
			System.out.println("获取的个人明细信息的第一部分是："+html0);
			//如下内容意为：未查询到满足条件的个人明细
			if(!html0.contains("\u672a\u67e5\u8be2\u5230\u6ee1\u8db3\u6761\u4ef6\u7684\u4e2a\u4eba\u660e\u7ec6\uff01")){
				//把每一页显示的数据设置为了30条
				String url = "http://www.sjzgjj.cn/wsyyt/dynamictable?"; 
		 		webRequest = new WebRequest(new URL(url), HttpMethod.POST);  
		 		webRequest.setAdditionalHeader("Host", "www.sjzgjj.cn");
		 		webRequest.setAdditionalHeader("Origin", "http://www.sjzgjj.cn");
		 		webRequest.setAdditionalHeader("Referer", "http://www.sjzgjj.cn/wsyyt/init.summer?_PROCID=60020007");
		 		String encodeName=URLEncoder.encode(housingShiJiaZhuangUserInfo.getAccname().trim(), "utf-8");
		 		String encodeCompName=URLEncoder.encode(housingShiJiaZhuangUserInfo.getUnitaccname().trim(), "utf-8");
		 		requestBody="dynamicTable_id=list1&dynamicTable_currentPage=1&dynamicTable_pageSize=40&dynamicTable_nextPage=1&dynamicTable_page=%2Fydpx%2F60020007%2F602007_01.ydpx&dynamicTable_paging=true&dynamicTable_configSqlCheck=0&errorFilter=1%3D1"
		 				+ "&AccName="+encodeName+"&AccNum="+housingShiJiaZhuangUserInfo.getAccnum().trim()+""
		 						+ "&CertiNum="+housingShiJiaZhuangUserInfo.getCertinum().trim()+""
		 				+ "&OpenDate="+housingShiJiaZhuangUserInfo.getOpenaccountdate().trim()+""
		 						+ "&Balance="+housingShiJiaZhuangUserInfo.getBalance().trim()+""
		 				+ "&UnitAccNum="+housingShiJiaZhuangUserInfo.getUnitaccnum().trim()+""
		 						+ "&UnitAccName="+encodeCompName+""
		 				+ "&indiaccstate="+housingShiJiaZhuangUserInfo.getStatuscode().trim()+"&prin="+housingShiJiaZhuangUserInfo.getChargebasenum().trim()+""
		 						+ "&intamt="+housingShiJiaZhuangUserInfo.getChargemonth().trim()+""
		 								+ "&date1="+housingShiJiaZhuangUserInfo.getChargetodate().trim()+""
		 				+ "&_APPLY=0&_CHANNEL=1&_PROCID=60020007"
					+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAVsaXN0MXQAsz0ic2VsZWN0IChjYXNlIHRyYW5zZGF0ZSB3aGVuICcxODk5LTEy%0ALTMxJyB0aGVuIG51bGwgZWxzZSB0cmFuc2RhdGUgZW5kKSBhcyB0cmFuc2RhdGUsIGNlcnRpbnVt%0ALCBhbXQyLCBhbXQxLCBiYXNlbnVtLCBpbnN0YW5jZSBmcm9tIGRwMDc3IHdoZXJlIGluc3RhbmNl%0AID0gIitfSVMgKyIgb3JkZXIgYnkgc2Vxbm8ieA%3D%3D"
						+ "&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAMHcIAAAAQAAAACR0AAhf%0AQnJjQ29kZXB0AAdfQUNDTlVNdAAMMTEzMTYzMzYxNjc5dAALX1VOSVRBQ0NOVU10AAoyMDE2MDMz%0AMjA2dAADX0lTc3IADmphdmEubGFuZy5Mb25nO4vkkMyPI98CAAFKAAV2YWx1ZXhyABBqYXZhLmxh%0AbmcuTnVtYmVyhqyVHQuU4IsCAAB4cP%2F%2F%2F%2F%2F%2F2htpdAAHX1BBR0VJRHQABXN0ZXAxdAAGX0xPR0lQ%0AdAAOMTIzLjEyNi44Ny4xNjl0AAlpc1NhbWVQZXJ0AAR0cnVldAAJX0NFTlRFUklEcHQAB19QUk9D%0ASUR0AAg2MDAyMDAwN3QAC19TRU5ET1BFUklEdAASMTMwMTgxMTk5MDA0MDM0ODI3dAAPdGVtcF8u%0AaXRlbWlkWzNddAABMnQAC19CUkFOQ0hLSU5EdAABMHQACV9TRU5EREFURXQACjIwMTctMTAtMjR0%0AAA90ZW1wXy5pdGVtaWRbMV10AAEwdAAMdGVtcF8uX3hoWzRddAABNHQAE0NVUlJFTlRfU1lTVEVN%0AX0RBVEVxAH4AHXQAEHRlbXBfLml0ZW12YWxbNF10AAbplIDmiLd0AAdfSVNDUk9QcQB%2BABt0AAVf%0AVFlQRXQABGluaXR0AAx0ZW1wXy5feGhbMl10AAEydAAJX1BPUkNOQU1FdAAV5Liq5Lq65piO57uG%0A6LSm5p%2Bl6K%2BidAAQdGVtcF8uaXRlbXZhbFsyXXQABuWwgeWtmHQADHRlbXBfX3Jvd251bXQAATR0%0AAAxfVU5JVEFDQ05BTUV0ADDmsrPljJfmmbrogZTmmJPmiY3kurrlipvotYTmupDpob7pl67mnInp%0AmZDlhazlj7h0AAhfQUNDTkFNRXQABui1teiPsnQAEF9ERVBVVFlJRENBUkROVU10ABIxMzAxODEx%0AOTkwMDQwMzQ4Mjd0AAUkcGFnZXQAHS95ZHB4LzYwMDIwMDA3LzYwMjAwN18wMS55ZHB4dAAPdGVt%0AcF8uaXRlbWlkWzRddAABOXQACV9TRU5EVElNRXQACjIwMTctMTAtMjR0AA90ZW1wXy5pdGVtaWRb%0AMl10AAExdAAMdGVtcF8uX3hoWzNddAABM3QAEHRlbXBfLml0ZW12YWxbM110AAbnqbrotKZ0AAhf%0AV0lUSEtFWXEAfgAbdAAHX1VTQktFWXB0AAx0ZW1wXy5feGhbMV10AAExdAAQdGVtcF8uaXRlbXZh%0AbFsxXXQABuato%2BW4uHh0AAhAU3lzRGF0ZXQAB0BTeXNEYXl0AAlAU3lzTW9udGh0AAhAU3lzVGlt%0AZXQACEBTeXNXZWVrdAAIQFN5c1llYXI%3D"; 		
		 		webRequest.setRequestBody(requestBody);
		 		Page page = webClient.getPage(webRequest);  
		 		if(null!=page){
		 			String html = page.getWebResponse().getContentAsString(); 
		 			tracer.addTag("action.crawler.getDetailAccount.html"+year, "获取的爬取个人明细账的html ===="+html);
		 			System.out.println("获取的个人明细信息的html是："+html);
		 			HousingShiJiaZhuangHtml housingShiJiaZhuangHtml=new HousingShiJiaZhuangHtml();
		 			housingShiJiaZhuangHtml.setHtml(html);
		 			housingShiJiaZhuangHtml.setPagenumber(1);
		 			housingShiJiaZhuangHtml.setTaskid(taskHousing.getTaskid());
		 			housingShiJiaZhuangHtml.setType(""+year+"个人明细账源码页");
		 			housingShiJiaZhuangHtml.setUrl(url);
		 			housingShiJiaZhuangHtmlRepository.save(housingShiJiaZhuangHtml);
		 			List<HousingShiJiaZhuangDetailAccount> list = housingFundShiJiaZhuangParser.detailAccountParser(html,taskHousing);
		 			if(null!=list && list.size()>0){
		 				housingShiJiaZhuangDetailAccountRepository.saveAll(list);
		 				updatePayStatusByTaskid("数据采集中，"+year+"个人明细账信息已采集完成", 200,taskHousing.getTaskid());
		 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，"+year+"年个人明细账信息已采集完成");
		 			}else{
		 				updatePayStatusByTaskid("数据采集中，"+year+"个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
		 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，"+year+"个人明细账信息暂无可采集数据");
		 			}
		 		}
	 		}else{
	 			//未查询到满足个人条件的信息
	 			updatePayStatusByTaskid("数据采集中，"+year+"个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，"+year+"个人明细账信息已采集完成");
	 		}
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
}
