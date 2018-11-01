package app.service;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

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
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhaoqing.HousingZhaoQingPay;
import com.microservice.dao.entity.crawler.housing.zhaoqing.HousingZhaoQingUserinfo;
import com.microservice.dao.repository.crawler.housing.zhaoqing.HousingZhaoQingPayRepository;
import com.microservice.dao.repository.crawler.housing.zhaoqing.HousingZhaoQingUserinfoRepository;

import app.crawler.htmlparse.HousingZQParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zhaoqing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zhaoqing")
public class HousingZhaoQingService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingZhaoQingService.class);
	
	@Autowired
	private HousingZhaoQingUserinfoRepository housingZhaoQingUserinfoRepository;
	@Autowired
	private HousingZhaoQingPayRepository housingZhaoQingPayRepository;
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		String urlInfo = "http://121.10.236.70:9080/wsyyt/init.summer?_PROCID=60020009";
//		String url = "http://121.10.236.70:9080/wsyyt/dynamictable?uuid=1521430346368&dynamicTable_id=datalist&dynamicTable_currentPage=0&dynamicTable_pageSize=1000&dynamicTable_nextPage=1&dynamicTable_page=%2Fydpx%2F60020007%2Fdppage4003.ydpx&dynamicTable_paging=true&dynamicTable_configSqlCheck=0&errorFilter=1%3D1&accnum=20227906153&accname=%E5%AE%8B%E8%8C%82%E6%A1%82&certitype=01&certinum=441202199004170013&begdate=1999-3-1&enddate=2018-3-19&_APPLY=0&_CHANNEL=1&_PROCID=60020007&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAhkYXRhbGlzdHQAz3NlbGVjdCB0cmFuc2RhdGUsIHVuaXRhY2NudW0xLCB1bml0%0AYWNjbmFtZSwgYWNjbnVtMSwgYWNjbmFtZTEsIGZ1bmRzb3VmbGFnLGFtdDEsIGFtdDIsIGFjY25h%0AbWUyLCBmcmVldXNlNCwgYmVnZGF0ZSwgZW5kZGF0ZSwgaW5zdGFuY2UgZnJvbSBkcDA3NyB3aGVy%0AZSBpbnN0YW5jZT0tMTA2NDcwNSBvcmRlciBieSB0cmFuc2RhdGUgZGVzYywgZnJlZXVzZTQgZGVz%0AY3g%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAMHcIAAAAQAAAACx0AAdf%0AQUNDTlVNdAALMjAyMjc5MDYxNTN0AAtfVU5JVEFDQ05VTXB0AAdfT1BFUklEcQB%2BAAV0AANfSVNz%0AcgAOamF2YS5sYW5nLkxvbmc7i%2BSQzI8j3wIAAUoABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKG%0ArJUdC5TgiwIAAHhw%2F%2F%2F%2F%2F%2F%2FvwP90AAdfUEFHRUlEdAAFc3RlcDF0AAZfTE9HSVB0ABEyMDE4MDMx%0AOTE0MjUyODQ0MXQACWlzU2FtZVBlcnQABWZhbHNldAAPdGVtcF8uaXRlbWlkWzdddAACMTN0AAtf%0AU0VORE9QRVJJRHEAfgAFdAAHX1BST0NJRHQACDYwMDIwMDA3dAAPdGVtcF8uaXRlbWlkWzVddAAC%0AMDZ0AA90ZW1wXy5pdGVtaWRbM110AAIwNHQACl9UUkFDRURBVEV0AAoyMDE4LTAzLTE5dAALX0JS%0AQU5DSEtJTkR0AAEwdAAJX1NFTkREQVRFcQB%2BABx0AAx0ZW1wXy5feGhbNl10AAE2dAAPdGVtcF8u%0AaXRlbWlkWzFddAACMDF0ABB0ZW1wXy5pdGVtdmFsWzZddAAJ6K2m5a6Y6K%2BBdAAMdGVtcF8uX3ho%0AWzRddAABNHQAE0NVUlJFTlRfU1lTVEVNX0RBVEVxAH4AHHQAEHRlbXBfLml0ZW12YWxbNF10AAnl%0Ao6vlrpjor4F0AAdfSVNDUk9QcQB%2BAB50AAVfVFlQRXQABGluaXR0AAx0ZW1wXy5feGhbMl10AAEy%0AdAAJX1BPUkNOQU1FdAAV5Liq5Lq65piO57uG6LSm5p%2Bl6K%2BidAAQdGVtcF8uaXRlbXZhbFsyXXQA%0ABuaKpOeFp3QADHRlbXBfX3Jvd251bXQAATd0AAxfVU5JVEFDQ05BTUVwdAAIX0FDQ05BTUV0AAnl%0ArovojILmoYJ0AA90ZW1wXy5pdGVtaWRbNl10AAIwOXQAEF9ERVBVVFlJRENBUkROVU10ABI0NDEy%0AMDIxOTkwMDQxNzAwMTN0AA90ZW1wXy5pdGVtaWRbNF10AAIwNXQACV9TRU5EVElNRXQAEzIwMTgt%0AMDMtMTkgMTQ6MjU6MzJ0AAx0ZW1wXy5feGhbN110AAE3dAAPdGVtcF8uaXRlbWlkWzJddAACMDN0%0AABB0ZW1wXy5pdGVtdmFsWzdddAAN5YW25a6D6K%2BB5Lu2IHQADHRlbXBfLl94aFs1XXQAATV0ABB0%0AZW1wXy5pdGVtdmFsWzVddAAS5riv5r6z5Y%2Bw6YCa6KGM6K%2BBdAAMdGVtcF8uX3hoWzNddAABM3QA%0AEHRlbXBfLml0ZW12YWxbM110AAnlhpvlrpjor4F0AAhfV0lUSEtFWXEAfgAedAAHX1VTQktFWXB0%0AAAx0ZW1wXy5feGhbMV10AAExdAAQdGVtcF8uaXRlbXZhbFsxXXQACei6q%2BS7veivgXh0AAhAU3lz%0ARGF0ZXQAB0BTeXNEYXl0AAlAU3lzTW9udGh0AAhAU3lzVGltZXQACEBTeXNXZWVrdAAIQFN5c1ll%0AYXI%3D";
//		String urlPay = "http://www.gxwzgjj.com/dc_zg_jctqmx.asp";
		
		Page page = null;
		//基本信息
		try {
			WebRequest webRequest = new WebRequest(new URL(urlInfo), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("个人信息html", html);
		//System.out.println(html);
		HousingZhaoQingUserinfo userinfo = HousingZQParse.userinfo_parse(html);
		System.out.println(userinfo);
		if(userinfo==null){
			//System.out.println("111111");
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
			//System.out.println("222222");
			userinfo.setTaskid(taskHousing.getTaskid());
			housingZhaoQingUserinfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		
		//个人对账明细
		Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		int beginNian = endNian-20;
		int yue = now.get(Calendar.MONTH) +1;
		int ri = now.get(Calendar.DAY_OF_MONTH);
		String endTime = endNian+"-"+yue+"-"+ri;
		String beginTime = beginNian+"-"+yue+"-"+ri;
		String name = userinfo.getName();
		
		try {
			String url = "http://121.10.236.70:9080/wsyyt/dynamictable?uuid=1521430346368&dynamicTable_id=datalist&"
					+"dynamicTable_currentPage=0&dynamicTable_pageSize=1000&dynamicTable_nextPage=1&"
					+"dynamicTable_page=%2Fydpx%2F60020007%2Fdppage4003.ydpx&dynamicTable_paging=true&"
					+"dynamicTable_configSqlCheck=0&errorFilter=1%3D1&accnum="+userinfo.getAccountNum()+"&"
					+"accname="+URLDecoder.decode(name, "UTF-8")+"&certitype=01&certinum="+userinfo.getIdNum()+"&"
					+"begdate="+beginTime+"&enddate="+endTime+"&_APPLY=0&_CHANNEL=1&_PROCID=60020007&"
					+"DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAhkYXRhbGlzdHQAz3NlbGVjdCB0cmFuc2RhdGUsIHVuaXRhY2NudW0xLCB1bml0%0AYWNjbmFtZSwgYWNjbnVtMSwgYWNjbmFtZTEsIGZ1bmRzb3VmbGFnLGFtdDEsIGFtdDIsIGFjY25h%0AbWUyLCBmcmVldXNlNCwgYmVnZGF0ZSwgZW5kZGF0ZSwgaW5zdGFuY2UgZnJvbSBkcDA3NyB3aGVy%0AZSBpbnN0YW5jZT0tMTA2NDcwNSBvcmRlciBieSB0cmFuc2RhdGUgZGVzYywgZnJlZXVzZTQgZGVz%0AY3g%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAMHcIAAAAQAAAACx0AAdf%0AQUNDTlVNdAALMjAyMjc5MDYxNTN0AAtfVU5JVEFDQ05VTXB0AAdfT1BFUklEcQB%2BAAV0AANfSVNz%0AcgAOamF2YS5sYW5nLkxvbmc7i%2BSQzI8j3wIAAUoABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKG%0ArJUdC5TgiwIAAHhw%2F%2F%2F%2F%2F%2F%2FvwP90AAdfUEFHRUlEdAAFc3RlcDF0AAZfTE9HSVB0ABEyMDE4MDMx%0AOTE0MjUyODQ0MXQACWlzU2FtZVBlcnQABWZhbHNldAAPdGVtcF8uaXRlbWlkWzdddAACMTN0AAtf%0AU0VORE9QRVJJRHEAfgAFdAAHX1BST0NJRHQACDYwMDIwMDA3dAAPdGVtcF8uaXRlbWlkWzVddAAC%0AMDZ0AA90ZW1wXy5pdGVtaWRbM110AAIwNHQACl9UUkFDRURBVEV0AAoyMDE4LTAzLTE5dAALX0JS%0AQU5DSEtJTkR0AAEwdAAJX1NFTkREQVRFcQB%2BABx0AAx0ZW1wXy5feGhbNl10AAE2dAAPdGVtcF8u%0AaXRlbWlkWzFddAACMDF0ABB0ZW1wXy5pdGVtdmFsWzZddAAJ6K2m5a6Y6K%2BBdAAMdGVtcF8uX3ho%0AWzRddAABNHQAE0NVUlJFTlRfU1lTVEVNX0RBVEVxAH4AHHQAEHRlbXBfLml0ZW12YWxbNF10AAnl%0Ao6vlrpjor4F0AAdfSVNDUk9QcQB%2BAB50AAVfVFlQRXQABGluaXR0AAx0ZW1wXy5feGhbMl10AAEy%0AdAAJX1BPUkNOQU1FdAAV5Liq5Lq65piO57uG6LSm5p%2Bl6K%2BidAAQdGVtcF8uaXRlbXZhbFsyXXQA%0ABuaKpOeFp3QADHRlbXBfX3Jvd251bXQAATd0AAxfVU5JVEFDQ05BTUVwdAAIX0FDQ05BTUV0AAnl%0ArovojILmoYJ0AA90ZW1wXy5pdGVtaWRbNl10AAIwOXQAEF9ERVBVVFlJRENBUkROVU10ABI0NDEy%0AMDIxOTkwMDQxNzAwMTN0AA90ZW1wXy5pdGVtaWRbNF10AAIwNXQACV9TRU5EVElNRXQAEzIwMTgt%0AMDMtMTkgMTQ6MjU6MzJ0AAx0ZW1wXy5feGhbN110AAE3dAAPdGVtcF8uaXRlbWlkWzJddAACMDN0%0AABB0ZW1wXy5pdGVtdmFsWzdddAAN5YW25a6D6K%2BB5Lu2IHQADHRlbXBfLl94aFs1XXQAATV0ABB0%0AZW1wXy5pdGVtdmFsWzVddAAS5riv5r6z5Y%2Bw6YCa6KGM6K%2BBdAAMdGVtcF8uX3hoWzNddAABM3QA%0AEHRlbXBfLml0ZW12YWxbM110AAnlhpvlrpjor4F0AAhfV0lUSEtFWXEAfgAedAAHX1VTQktFWXB0%0AAAx0ZW1wXy5feGhbMV10AAExdAAQdGVtcF8uaXRlbXZhbFsxXXQACei6q%2BS7veivgXh0AAhAU3lz%0ARGF0ZXQAB0BTeXNEYXl0AAlAU3lzTW9udGh0AAhAU3lzVGltZXQACEBTeXNXZWVrdAAIQFN5c1ll%0AYXI%3D";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlPay, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html1 = page.getWebResponse().getContentAsString();
		tracer.addTag("缴费信息html", html1);
		System.out.println(html1);
		List<HousingZhaoQingPay> listresul = HousingZQParse.paydetails_parse(html1,taskHousing);
		if(listresul==null){
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
		}else{
			
			housingZhaoQingPayRepository.saveAll(listresul);
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
		}
			
			
		
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}
}
