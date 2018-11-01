import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;

public class test {

	public static void main(String[] args){
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String name = '魏丹丹';
//		String encode = URLEncoder.encode(name, 'UTF-8');
//		String url = 'http://xyzfgjj.xys.gov.cn/chaxun.asp?RealName='+encode+'&UserIDCard=610404198907225046&GetType=0&button=++%B2%E9%D1%AF++';
//		HtmlPage htmlpage = null;
//		try {
//			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//			webClient.getOptions().setJavaScriptEnabled(false);
//			webClient.setJavaScriptTimeout(50000); 
//			webClient.getOptions().setTimeout(50000); // 15->60 
//			htmlpage = webClient.getPage(webRequest);
//			System.out.println(htmlpage.asXml());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String html = "{'controls':[],'custom':'{\"dkxx\":\"[]\",\"grxx\":\"[{\\\"DWDM\\\":\\\"0000040137\\\",\\\"DWNAME\\\":\\\"河南成就人力资源有限公司\\\",\\\"GRDM\\\":\\\"1162\\\",\\\"GRJZNY\\\":\\\"201801\\\",\\\"GRZT\\\":\\\"正常\\\",\\\"GZJS\\\":2412,\\\"SFZID\\\":\\\"410423198812061597\\\",\\\"UP_DATE\\\":\\\"2018-2-22\\\",\\\"XM\\\":\\\"陈磊\\\",\\\"YE\\\":4303.14,\\\"YJE\\\":482.4}]\"}','status':{'code':'200','text':'','url':''}}";
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		String accountCard = object.get("custom").toString().replaceAll("\\\\", "");
		accountCard = accountCard.substring(accountCard.lastIndexOf("[")+1,accountCard.lastIndexOf("]"));
		JsonObject object1 = (JsonObject) parser.parse(accountCard); // 创建JsonObject对象
		String years = object1.get("DWDM").toString().replaceAll("\"", "");
		System.out.println(years);
	}

}
