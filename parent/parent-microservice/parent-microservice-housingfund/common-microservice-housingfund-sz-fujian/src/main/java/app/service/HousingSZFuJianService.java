package app.service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.sz.fujian.HousingSZFuJianPay;
import com.microservice.dao.entity.crawler.housing.sz.fujian.HousingSZFuJianUserInfo;
import com.microservice.dao.repository.crawler.housing.sz.fujian.HousingSZFuJianPayRepository;
import com.microservice.dao.repository.crawler.housing.sz.fujian.HousingSZFuJianUserInfoRepository;

import app.crawler.htmlparse.HousingSZFJParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.sz.fujian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.sz.fujian")
public class HousingSZFuJianService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingSZFuJianService.class);
	@Autowired
	public HousingSZFuJianUserInfoRepository housingSZFuJianUserInfoRepository;
	@Autowired
	public HousingSZFuJianPayRepository housingSZFuJianPayRepository;
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient, String html) throws Exception {
		//基本信息
		tracer.addTag("个人信息html", html);
		HousingSZFuJianUserInfo userinfo = HousingSZFJParse.userinfo_parse(html);
		if(userinfo==null){
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
			userinfo.setTaskid(taskHousing.getTaskid());
			housingSZFuJianUserInfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		int beginNian = endNian-15;
		int yue = now.get(Calendar.MONTH) +1;
		int ri = now.get(Calendar.DAY_OF_MONTH);
		String yu = null;
		String r = null;
		if (yue>9){
			yu = String.valueOf(yue);
		}else{
			yu = "0"+yue;
		}
		if (ri>9){
			r = String.valueOf(ri);
		}else{
			r = "0"+ri;
		}
		String endTime = String.valueOf(endNian)+"-"+ yu +"-" + r;
		String beginTime = beginNian +"-01-01";
		Base64.Encoder encoder = Base64.getEncoder();
		String text = beginTime;
		String text1 = endTime;
	    byte[] textByte = text.getBytes("UTF-8");
	    byte[] textByte1 = text1.getBytes("UTF-8");
		//编码
		String encodedText = encoder.encodeToString(textByte);
		String encodedText1 = encoder.encodeToString(textByte1);
		String url = "http://www.fjszgjj.com/gjj/accountDetial/view?&page=1&&startTime="+encodedText+"&&endTime="+encodedText1+"&type=ZHo=&peracct=MTQwMjAyMzM0MDAyMTI4OTMyMw==";
		Page page = null;
		
		//缴存明细
		try {
			page = loginAndGetCommon.getHtml(url, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html1 = page.getWebResponse().getContentAsString();
//		System.out.println(html1);
		Document doc = Jsoup.parse(html1);
		Elements ele = doc.select("div.pages-jump > p > span");
		String index = null;
		int page1 = 0;
		if(ele.size()>0){
			index = ele.get(1).text().trim();
			page1 = Integer.parseInt(index);
		}else {
			page1 = 1;
		}
		for (int i = 1;i <=page1;i++){
			String url1 = "http://www.fjszgjj.com/gjj/accountDetial/view?&page="+i+"&&startTime="+encodedText+"&&endTime="+encodedText1+"&type=ZHo=&peracct=MTQwMjAyMzM0MDAyMTI4OTMyMw==";
			try {
				page = loginAndGetCommon.getHtml(url1, webClient);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String html2 = page.getWebResponse().getContentAsString();
			tracer.addTag("缴费信息html"+i+"", html2);
			List<HousingSZFuJianPay> listresult = HousingSZFJParse.paydetails_parse(html2,taskHousing);
			if(listresult==null){
				taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
				break;
			}else{
				
				housingSZFuJianPayRepository.saveAll(listresult);
				taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
			}
				
			
			
		}
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}
}
