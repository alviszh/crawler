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
import com.microservice.dao.entity.crawler.housing.xingtai.HousingXingTaiDetailAccount;
import com.microservice.dao.entity.crawler.housing.xingtai.HousingXingTaiHtml;
import com.microservice.dao.entity.crawler.housing.xingtai.HousingXingTaiUserInfo;
import com.microservice.dao.repository.crawler.housing.xingtai.HousingXingTaiDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.xingtai.HousingXingTaiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.xingtai.HousingXingTaiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundXingTaiParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import net.sf.json.JSONObject;


/**
 * @description:  邢台市公积金信息爬取service
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.xingtai"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.xingtai"})
public class HousingFundXingTaiCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundXingTaiCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundXingTaiParser housingFundXingTaiParser;
	@Autowired
	private HousingXingTaiHtmlRepository housingXingTaiHtmlRepository;
	@Autowired
	private HousingXingTaiDetailAccountRepository housingXingTaiDetailAccountRepository;
	@Autowired
	private HousingXingTaiUserInfoRepository housingXingTaiUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	@Async
	public void getUserInfo(TaskHousing taskHousing) {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.setJavaScriptTimeout(5000); //设置超时时间
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://"+loginHost+"/init.summer?_PROCID=60020007"; 
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
	 				url="http://"+loginHost+"/command.summer?uuid=1512613053175";
	 				webRequest= new WebRequest(new URL(url), HttpMethod.POST);  
	 				//涉及到汉字的参数需要加密，不然返回的信息中涉及到了汉字，返回的是?代替的
	 				String encodeName=URLEncoder.encode(accName.trim(), "utf-8");
	 				String encodeUnitAccName=URLEncoder.encode(unitAccName.trim(), "utf-8");
	 				String requestBody=""
	 						+ "%24page=%2Fydpx%2F60020007%2F602007_01.ydpx"
	 						+ "&_ACCNUM="+accNum+""
	 						+ "&_UNITACCNUM="+unitAccNum+""
	 						+ "&_PAGEID=step1"
	 						+ "&_IS=-1385289"
	 						+ "&_UNITACCNAME="+encodeUnitAccName+""
	 						+ "&_ACCNAME="+encodeName+""
	 						+ "&isSamePer=true"
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
	 	 				HousingXingTaiHtml housingXingTaiHtml=new HousingXingTaiHtml();
	 	 	 			housingXingTaiHtml.setHtml(html);
	 	 	 			housingXingTaiHtml.setPagenumber(1);
	 	 	 			housingXingTaiHtml.setTaskid(taskHousing.getTaskid());
	 	 	 			housingXingTaiHtml.setType("用户基本信息源码页");
	 	 	 			housingXingTaiHtml.setUrl(url);
	 	 	 			housingXingTaiHtmlRepository.save(housingXingTaiHtml);
	 	 	 			tracer.addTag("action.crawler.getUserInfo.html", "用户信息源码页已经入库");
	 	 	 			HousingXingTaiUserInfo housingXingTaiUserInfo= housingFundXingTaiParser.userInfoParser(html,taskHousing);
	 	 	 			if(null!=housingXingTaiUserInfo){
	 	 	 				housingXingTaiUserInfoRepository.save(housingXingTaiUserInfo);
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
			String url="http://"+loginHost+"/command.summer?uuid=1512614118958";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);   
			String requestBody=""
					+ "%24page=%2Fydpx%2F60020007%2F602007_01.ydpx"
					+ "&_ACCNUM="+accNum+""
					+ "&_UNITACCNUM=700006"
					+ "&_PAGEID=step1"
					+ "&_IS=-2049449"
//					+ "&_UNITACCNAME=%E7%9F%B3%E5%AE%B6%E5%BA%84%E8%AF%BA%E4%BA%9A%E9%80%9A%E4%BF%A1%E6%9C%8D%E5%8A%A1%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E9%82%A2%E5%8F%B0%E5%88%86%E5%85%AC%E5%8F%B8"
//					+ "&_LOGIP=200.200.210.249"
//					+ "&_ACCNAME=%E7%8E%8B%E6%A0%A1%E6%A2%85"
					+ "&isSamePer=true"
//					+ "&_SENDOPERID=130521198901110528"
					+ "&_PROCID=60020007"
//					+ "&_DEPUTYIDCARDNUM=130521198901110528"
//					+ "&_SENDTIME=2018-08-07"
//					+ "&_SENDDATE=2018-08-07"
					+ "&_BRANCHKIND=0"
//					+ "&CURRENT_SYSTEM_DATE=2018-08-07"
					+ "&_TYPE=init"
					+ "&_ISCROP=0"
					+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E8%B4%A6%E6%9F%A5%E8%AF%A2"
					+ "&_WITHKEY=0"
//					+ "&AccName=%E7%8E%8B%E6%A0%A1%E6%A2%85"
					+ "&AccNum="+accNum+""
//					+ "&CertiNum=130521198901110528"
//					+ "&OpenDate=2015-07-23"
//					+ "&Balance=10440.98"
					+ "&UnitAccNum=700006"
//					+ "&UnitAccName=%E7%9F%B3%E5%AE%B6%E5%BA%84%E8%AF%BA%E4%BA%9A%E9%80%9A%E4%BF%A1%E6%9C%8D%E5%8A%A1%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E9%82%A2%E5%8F%B0%E5%88%86%E5%85%AC%E5%8F%B8"
					+ "&dynamicTable_flag=0"
					+ "&instance=-2049449"
					+ "&trancode=110015"
					+ "&accnum="+accNum+""
					+ "&unitaccnum=700006"
					+ "&year="+year+"";

			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			String html=page.getWebResponse().getContentAsString();
			String returnCode = JSONObject.fromObject(html).getString("returnCode");
			//如下内容意为：未查询到满足条件的个人明细
			if(returnCode.equals("0")){
				//把每一页显示的数据设置为了100条
				url = "http://"+loginHost+"/dynamictable?uuid=1512614119771"; 
		 		webRequest = new WebRequest(new URL(url), HttpMethod.POST);  
		 		requestBody="dynamicTable_id=list1"
		 				+ "&dynamicTable_currentPage=1"
		 				+ "&dynamicTable_pageSize=100"
		 				+ "&dynamicTable_nextPage=1"
		 				+ "&dynamicTable_page=%2Fydpx%2F60020007%2F602007_01.ydpx"
		 				+ "&dynamicTable_paging=true"
		 				+ "&dynamicTable_configSqlCheck=0"
		 				+ "&errorFilter=1%3D1"
//		 				+ "&AccName=%E7%8E%8B%E6%A0%A1%E6%A2%85"
		 				+ "&AccNum="+accNum+""
//		 				+ "&CertiNum=130521198901110528"
//		 				+ "&OpenDate=2015-07-23"
//		 				+ "&Balance=10440.98"
		 				+ "&UnitAccNum=700006"
//		 				+ "&UnitAccName=%E7%9F%B3%E5%AE%B6%E5%BA%84%E8%AF%BA%E4%BA%9A%E9%80%9A%E4%BF%A1%E6%9C%8D%E5%8A%A1%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E9%82%A2%E5%8F%B0%E5%88%86%E5%85%AC%E5%8F%B8"
		 				+ "&_APPLY=0"
		 				+ "&_CHANNEL=1"
		 				+ "&_PROCID=60020007"
		 				+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAVsaXN0MXQAsz0ic2VsZWN0IChjYXNlIHRyYW5zZGF0ZSB3aGVuICcxODk5LTEy%0ALTMxJyB0aGVuIG51bGwgZWxzZSB0cmFuc2RhdGUgZW5kKSBhcyB0cmFuc2RhdGUsIGNlcnRpbnVt%0ALCBhbXQyLCBhbXQxLCBiYXNlbnVtLCBpbnN0YW5jZSBmcm9tIGRwMDc3IHdoZXJlIGluc3RhbmNl%0AID0gIitfSVMgKyIgb3JkZXIgYnkgc2Vxbm8ieA%3D%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABZ0AAhf%0AQnJjQ29kZXB0AAdfQUNDTlVNdAAMMTEzMTExNjkxNzM3dAALX1VOSVRBQ0NOVU10AAY3MDAwMDZ0%0AAAdfUEFHRUlEdAAFc3RlcDF0AANfSVNzcgAOamF2YS5sYW5nLkxvbmc7i%2BSQzI8j3wIAAUoABXZh%0AbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhw%2F%2F%2F%2F%2F%2F%2Fguld0AAxfVU5JVEFDQ05B%0ATUV0ADbnn7PlrrbluoTor7rkuprpgJrkv6HmnI3liqHmnInpmZDlhazlj7jpgqLlj7DliIblhazl%0Aj7h0AAZfTE9HSVB0AA8yMDAuMjAwLjIxMC4yNDl0AAhfQUNDTkFNRXQACeeOi%2BagoeaihXQACWlz%0AU2FtZVBlcnQABHRydWV0AAlfQ0VOVEVSSURwdAALX1NFTkRPUEVSSUR0ABIxMzA1MjExOTg5MDEx%0AMTA1Mjh0AAdfUFJPQ0lEdAAINjAwMjAwMDd0ABBfREVQVVRZSURDQVJETlVNcQB%2BABl0AAlfU0VO%0ARFRJTUV0AAoyMDE4LTA4LTA3dAAJX1NFTkREQVRFdAAKMjAxOC0wOC0wN3QAC19CUkFOQ0hLSU5E%0AdAABMHQAE0NVUlJFTlRfU1lTVEVNX0RBVEVxAH4AIHQABV9UWVBFdAAEaW5pdHQAB19JU0NST1Bx%0AAH4AInQACV9QT1JDTkFNRXQAFeS4quS6uuaYjue7hui0puafpeivonQAB19VU0JLRVlwdAAIX1dJ%0AVEhLRVlxAH4AInh0AAhAU3lzRGF0ZXQAB0BTeXNEYXl0AAlAU3lzTW9udGh0AAhAU3lzVGltZXQA%0ACEBTeXNXZWVrdAAIQFN5c1llYXI%3D";
 				webRequest.setRequestBody(requestBody);
		 		page = webClient.getPage(webRequest);  
		 		if(null!=page){
		 			html = page.getWebResponse().getContentAsString(); 
		 			HousingXingTaiHtml housingXingTaiHtml=new HousingXingTaiHtml();
		 			housingXingTaiHtml.setHtml(html);
		 			housingXingTaiHtml.setPagenumber(1);
		 			housingXingTaiHtml.setTaskid(taskHousing.getTaskid());
		 			housingXingTaiHtml.setType(""+year+"个人明细账源码页");
		 			housingXingTaiHtml.setUrl(url);
		 			housingXingTaiHtmlRepository.save(housingXingTaiHtml);
		 			tracer.addTag("action.crawler.getDetailAccount.html："+year, "个人明细账源码页已经入库");

		 			List<HousingXingTaiDetailAccount> list = housingFundXingTaiParser.detailAccountParser(html,taskHousing);
		 			if(null!=list && list.size()>0){
		 				housingXingTaiDetailAccountRepository.saveAll(list);
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
