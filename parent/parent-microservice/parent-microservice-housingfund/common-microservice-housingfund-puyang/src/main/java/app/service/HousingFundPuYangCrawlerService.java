package app.service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.puyang.HousingPuYangFlowInfo;
import com.microservice.dao.entity.crawler.housing.puyang.HousingPuYangHtml;
import com.microservice.dao.entity.crawler.housing.puyang.HousingPuYangUserInfo;
import com.microservice.dao.repository.crawler.housing.puyang.HousingPuYangFlowInfoRepository;
import com.microservice.dao.repository.crawler.housing.puyang.HousingPuYangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.puyang.HousingPuYangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundPuYangParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.puyang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.puyang"})
public class HousingFundPuYangCrawlerService extends HousingBasicService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundPuYangParser housingFundPuYangParser;
	@Autowired
	private  HousingFundHelperService housingFundHelperService;
	@Autowired
	private HousingPuYangHtmlRepository housingPuYangHtmlRepository;
	@Autowired
	private HousingPuYangUserInfoRepository housingPuYangUserInfoRepository;
	@Autowired
	private HousingPuYangFlowInfoRepository housingPuYangFlowInfoRepository;
	
	@Value("${loginhost}") 
	public String loginHost;
	@Value("${filesavepath}") 
	public String fileSavePath;
	
	/**
	 * 用户基本信息是PDF
	 * @param taskHousing
	 * @return
	 */
	@Async
	public Future<String> getUserInfo(TaskHousing taskHousing) {
		try {
			WebClient webClient=housingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			//将个人基本信息表的pdf保存到服务器的指定文件夹中
			String url="http://"+loginHost+"/wt-web/grxxpdf/grxx";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Content-Type", "application/json;charset=UTF-8");
			Page page= webClient.getPage(webRequest);
			if(page!=null){
				//==========================================================
				InputStream is = page.getWebResponse().getContentAsStream();
				String path = HousingFundHelperService.getFileSavePath(fileSavePath);
				tracer.addTag("返回的用于保存个人基本信息pdf的路径是:", path);
				HousingFundHelperService.savePdf(is, path);
				//将请求成功后保存的pdf转化为txt文本文件，在同样的路径下
				String strRreadFdfToTxt = HousingFundHelperService.readFdfToTxt(path);
				//用IO流读取转化成功的文本文件
				File file = new File(strRreadFdfToTxt);
				String readTxt = HousingFundHelperService.readTxtFile(file);
				//==========================================================
				tracer.addTag("action.crawler.getUserInfo.txt", "获取的用户基本信息的txt====>"+"\r\n"+readTxt);
	 			HousingPuYangHtml housingPuYangHtml=new HousingPuYangHtml();
	 			housingPuYangHtml.setHtml(readTxt);
	 			housingPuYangHtml.setPagenumber(1);
	 			housingPuYangHtml.setTaskid(taskHousing.getTaskid());
	 			housingPuYangHtml.setType("用户基本信息源码页");
	 			housingPuYangHtml.setUrl(url);
	 			housingPuYangHtmlRepository.save(housingPuYangHtml);
	 			//解析用户信息(解析txt的时候用字符串截取的方式)
	 			HousingPuYangUserInfo housingPuYangUserInfo=housingFundPuYangParser.userInfoParser(readTxt,taskHousing);
	 			if(null!=housingPuYangUserInfo){
	 				housingPuYangUserInfoRepository.save(housingPuYangUserInfo);
	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
	 			}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskHousing.getTaskid()+"  "+e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
		}
		return new AsyncResult<String>("200");	
	}
	
	@Async
	public Future<String> getFlowInfo(TaskHousing taskHousing) {
		try {
			WebClient webClient=housingFundHelperService.addcookie(taskHousing);
//			webClient.getOptions().setJavaScriptEnabled(false);
			String url="http://"+loginHost+"/wt-web/personal/jcmxlist";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			/*String requestBody=""
					+ "beginDate=2000-"+HousingFundHelperService.getPresentDate().trim()+""
					+ "&userId=1"
					+ "&pageNum=1"
					+ "&pageSize=300";   //先写死，每页显示的数据
*/			String requestBody="beginDate=dqnd"
					+ "&userId=1"
					+ "&pageNum=1"
					+ "&pageSize=10";
			webRequest.setRequestBody(requestBody);
			Page page= webClient.getPage(webRequest);
			if(page!=null){
				String html=page.getWebResponse().getContentAsString();
				tracer.addTag("action.crawler.getFlowInfo.html", "获取的流水信息html====>"+html);
	 			HousingPuYangHtml housingPuYangHtml=new HousingPuYangHtml();
	 			housingPuYangHtml.setHtml(html);
	 			housingPuYangHtml.setPagenumber(1);
	 			housingPuYangHtml.setTaskid(taskHousing.getTaskid());
	 			housingPuYangHtml.setType("流水信息源码页");
	 			housingPuYangHtml.setUrl(url);
	 			housingPuYangHtmlRepository.save(housingPuYangHtml);
	 			List<HousingPuYangFlowInfo> list = housingFundPuYangParser.flowInfoParser(html,taskHousing);
	 			if(null!=list && list.size()>0){
	 				housingPuYangFlowInfoRepository.saveAll(list);
	 				updatePayStatusByTaskid("数据采集中，流水信息已采集完成",200,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getFlowInfo", "数据采集中，流水信息已采集完成");
	 			}else{
	 				updatePayStatusByTaskid("数据采集中，流水信息已采集完成",200,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getFlowInfo", "数据采集中，流水信息无数据可供采集");
	 			}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getFlowInfo.e", taskHousing.getTaskid()+"  "+e.toString());
			updatePayStatusByTaskid("数据采集中，流水信息已采集完成",200,taskHousing.getTaskid());
		}
		return new AsyncResult<String>("200");	
		
	}
}
