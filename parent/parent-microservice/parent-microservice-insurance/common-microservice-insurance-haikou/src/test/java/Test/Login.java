package Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouMedical;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.InsuranceHaiKouCommonService;
import app.unit.CommonUnit;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Login {

	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url="http://202.100.251.116:8880/uaa/api/person/idandmobile?client_id=acme&redirect_uri=http://202.100.251.116:8880/ehrss/si/person/ui/&response_type=code";
		HtmlPage page = webClient.getPage(url);		
		Thread.sleep(1000);
//		System.out.println(page.getWebResponse().getContentAsString());
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies) {
//			System.out.println("登录Page获取到的cookie是：" + cookie.toString());
			if(cookie.toString().contains("XSRF"))
			{
				String substring = cookie.toString().substring(11,47);
				System.out.println(substring);
				String url2="http://202.100.251.116:8880/uaa/api/person/idandmobile/login?idnumber=460102199110021529&mobilenumber=13698922625&password=123456&_csrf="+substring;
				Page page2 = webClient.getPage(url2);
				System.out.println(page2.getWebResponse().getContentAsString());
				String url4="http://202.100.251.116:8880/ehrss-si-person/api/rights/payment/4000006004928672?insurance=110&year=2011";
				Page page3 = webClient.getPage(url4);
				System.out.println(page3.getWebResponse().getContentAsString());
				if(page3.getWebResponse().getContentAsString().contains("unitname"))
				{
					WebParam<InsuranceHaiKouMedical> webParam = new WebParam<InsuranceHaiKouMedical>();
					List<InsuranceHaiKouMedical> list = new ArrayList<InsuranceHaiKouMedical>();
					InsuranceHaiKouMedical in =null;
					JSONArray fromObject = JSONArray.fromObject(page3.getWebResponse().getContentAsString());
					for (int i = 0; i < fromObject.size(); i++) {
						in = new InsuranceHaiKouMedical();
						JSONObject fromObject2 = JSONObject.fromObject(fromObject.get(i));
						in.setCompany(fromObject2.getString("unitname"));
						in.setStatus(fromObject2.getString("insurance"));
						in.setDatea(fromObject2.getString("costperiod"));
						in.setBase(fromObject2.getString("paymentbase"));
						in.setCompanyPay(fromObject2.getString("unitacttransfersum"));
						in.setPersonalPay(fromObject2.getString("personalacttranssum"));
						in.setOtherPay(fromObject2.getString("otheracttransfersum"));
						in.setSum(fromObject2.getString("acttransfersum"));
						in.setTaskid("");
						list.add(in);
					}
					System.out.println(list);
				}
//				String url3="http://202.100.251.116:8880/ehrss-si-person/api/rights/persons/4000006004928672";
//				Page page3 = webClient.getPage(url3);
//				System.out.println(page3.getWebResponse().getContentAsString());
//				InsuranceHaiKouUserInfo i= new InsuranceHaiKouUserInfo();
//				JSONObject fromObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
//				
//				i.setIDNum(fromObject.getString("certificateType"));
//				i.setName(fromObject.getString("name"));
//				if(fromObject.getString("sex").contains("1"))
//				{
//					i.setSex("男");
//				}
//				else
//				{
//					i.setSex("女");
//				}
//				if(fromObject.getString("nation").contains("01"))
//				{
//					i.setNational("汉族");
//				}
//				else
//				{
//					i.setNational("非汉族");
//				}
//				i.setBirth(fromObject.getString("birthday"));
//				i.setJoinDate(fromObject.getString("workdate"));
//				i.setHomeLand(fromObject.getString("accountLocation"));
//				i.setAddr(fromObject.getString("householdType"));
//				i.setSchool(fromObject.getString("educationalBackground"));
//				i.setCode(fromObject.getString("administrativePost"));
//				i.setMarry(fromObject.getString("maritalStatus"));
//				i.setTaskid("");
//				System.out.println(i);
			}
		}
//		String a ="276decbb-5851-46ab-89fa-7ea186410f82";
		
	}
}
