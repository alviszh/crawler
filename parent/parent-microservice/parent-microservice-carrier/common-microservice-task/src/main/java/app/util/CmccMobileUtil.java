package app.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.bean.CmccBaifubao;
import app.bean.CmccTaobao;
import app.service.MobileService;

/**
 * @Description: 判断是否是不能重置密码的移动号
 * @author meidi
 * @date 2017年6月27日 上午11:31:24
 */
public class CmccMobileUtil {
	
	public static final Logger log = LoggerFactory.getLogger(CmccMobileUtil.class);
	
	public static final String[] areas = {"广东","北京","湖北","内蒙古","四川","贵州","江西","山西"};
	
	private static final String taobaoApi = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=";
	
	private static final String baifubaooApi = "https://www.baifubao.com/callback?cmd=1059&callback=phone&phone=";
	
	public static String getRequestByTaobao(String num) throws IOException{ 
		String html = Jsoup.connect(taobaoApi+num).timeout(2000).ignoreContentType(true).get().text();
		int start = html.indexOf("{");
		String cleanStr = html.substring(start); 
		log.info("淘宝提供数据------"+cleanStr); 
		return cleanStr;
	}
	
	public static String getRequestByBaifubao(String num) throws IOException{ 
		String html = Jsoup.connect(baifubaooApi+num).timeout(2000).ignoreContentType(true).get().text();  
		int start = html.indexOf("(")+1;
		int end = html.indexOf(")");
		String cleanStr = html.substring(start,end);
		log.info("百付宝提供数据------"+cleanStr); 
		return cleanStr;
	}
	
	
	public static void main(String[] args) throws IOException {
		 
		
		/*ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		CmccTaobao ctb = mapper.readValue(getRequestByTaobao("13520800817"), CmccTaobao.class); 
		ctb.getProvince();*/
		ObjectMapper mapper = new ObjectMapper();
		CmccBaifubao cb = mapper.readValue(getRequestByBaifubao("13520800817"), CmccBaifubao.class); 
		System.out.println(cb.getData().getArea());
		
 
	}
	
	
	
	
	
	
	

}
