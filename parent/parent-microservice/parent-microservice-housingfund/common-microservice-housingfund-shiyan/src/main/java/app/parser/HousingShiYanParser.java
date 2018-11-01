package app.parser;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.shiyan.HousingFundShiYanUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingShiYanParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	public WebParam crawler(MessageLoginForHousing messageLoginForHousing) throws Exception{
		tracer.addTag("crawler.HousingShiYanParser.crawler.taskid", messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String loginUrl = "https://grcx.sygjj.gov.cn/";
		webParam.setUrl(loginUrl);
		tracer.addTag("crawler.HousingShiYanParser.crawler.loginUrl", loginUrl);
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		webParam.setPage(loginPage);
		tracer.addTag("crawler.HousingShiYanParser.crawler.loginPage", "<xmp>"+loginPage.asXml()+"</xmp>");
		HtmlImage img = (HtmlImage) loginPage.getElementById("authcode_img");
		if(null != img){
			String loginUrl2 = "https://grcx.sygjj.gov.cn/servlet/query.do";
			webRequest = new WebRequest(new URL(loginUrl2), HttpMethod.POST);
			webRequest.setAdditionalHeader(":authority", "grcx.sygjj.gov.cn");
			webRequest.setAdditionalHeader(":method", "POST");
			webRequest.setAdditionalHeader(":path", "/servlet/query.do");
			webRequest.setAdditionalHeader(":scheme", "https");
			webRequest.setAdditionalHeader("accept", "application/json, text/javascript, */*; q=0.01");
			webRequest.setAdditionalHeader("accept-encoding", "gzip, deflate, br");
			webRequest.setAdditionalHeader("accept-language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest.setAdditionalHeader("origin", "https://grcx.sygjj.gov.cn");
			webRequest.setAdditionalHeader("referer", "https://grcx.sygjj.gov.cn/");
			webRequest.setAdditionalHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			webRequest.setAdditionalHeader("x-requested-with", "XMLHttpRequest");
			String requestBody = "cardno="+messageLoginForHousing.getNum()+"&xm="+getURLEncoderString(messageLoginForHousing.getUsername())+"&authcode="+chaoJiYingOcrService.getVerifycode(img, "1004");
			webRequest.setRequestBody(requestBody);
			webParam.setUrl(loginUrl2+requestBody);
			tracer.addTag("crawler.HousingShiYanParser.crawler.loginUrl2", loginUrl2+"?"+requestBody);
			Page page = webClient.getPage(webRequest);
			tracer.addTag("crawler.HousingShiYanParser.crawler.loginedPage", page.getWebResponse().getContentAsString());
			webParam.setPage(page);
			if(page.getWebResponse().getContentAsString().contains("massage")){
				JsonParser parser = new JsonParser();
				JsonObject obj = (JsonObject) parser.parse(page.getWebResponse().getContentAsString());
				int code = obj.get("code").getAsInt();
				if(code == 1){
					String name = obj.get("name").getAsString();
					String accout = obj.get("accout").getAsString();
					String balance = obj.get("balance").getAsString();
					String status = obj.get("status").getAsString();
					
					List<HousingFundShiYanUserInfo> userInfos = new ArrayList<HousingFundShiYanUserInfo>();
					HousingFundShiYanUserInfo userInfo = new HousingFundShiYanUserInfo();
					userInfo.setTaskid(messageLoginForHousing.getTask_id());
					userInfo.setName(name);
					userInfo.setAccout(accout);
					userInfo.setBalance(balance);
					userInfo.setStatus(status);
					userInfos.add(userInfo);
					webParam.setList(userInfos);
				}else{
					String massage = obj.get("massage").getAsString();
					webParam.setHtml(massage);
					tracer.addTag("crawler.HousingShiYanParser.crawler.error", massage);
				}
			}else{
				tracer.addTag("crawler.HousingShiYanParser.crawler.error", page.getWebResponse().getContentAsString());
				webParam.setHtml("网络异常，请您稍后重试！");
			}
		}else{
			tracer.addTag("crawler.HousingShiYanParser.crawler.error2", "登录页面异常，无法获取到验证码图片");
			webParam.setHtml("网络异常，请您稍后重试！");
		}
		
		return webParam;
	}
	
	public String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


}
