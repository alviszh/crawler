package app.parser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.pingdingshan.InsurancePingDingShanEndowment;
import com.microservice.dao.entity.crawler.insurance.pingdingshan.InsurancePingDingShanUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;

@Component
public class InsurancePingDingShanParser {

	public WebParam<InsurancePingDingShanEndowment> getEndowment(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance
   ) throws Exception {
		String url="http://218.28.196.73:33002/siq/web/staff_queryYpwilist.action";
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		Page page = webClient.getPage(url);
		String json = page.getWebResponse().getContentAsString();
		if(json.contains("年"))
		{
			String substring = json.substring(4, json.length()-1);
			StringBuilder sb = new StringBuilder(); 
			sb.append("<table>"); 
			sb.append(substring); 
			sb.append("</table>");
			Document doc = Jsoup.parse(sb.toString());
			//基数部分
			Elements elementsByClass1 = doc.getElementsByClass("table-number");
			System.out.println(elementsByClass1);
			
			InsurancePingDingShanEndowment j = null;
			List<InsurancePingDingShanEndowment> list = new ArrayList<InsurancePingDingShanEndowment>();
			WebParam<InsurancePingDingShanEndowment> webParam = new WebParam<InsurancePingDingShanEndowment>();
			
			Elements elementsByTag = doc.getElementsByTag("td");
		    System.out.println(elementsByTag.text());
			
			for (int i = 0; i < elementsByTag.size()/2; i++) {
				System.out.println(elementsByTag.get(i).text());
				j = new InsurancePingDingShanEndowment();
				if(elementsByTag.get(i).text().length()>6)
				{
					if(elementsByTag.get(i).text().contains("年"))
					{
						j.setYear(elementsByTag.get(i).text().substring(0, 4));
						
					}
					if(elementsByClass1.get(i).text().length()>6)
					{
						j.setBase(elementsByClass1.get(i).text().substring(0, 4));
					}
					j.setTaskid(taskInsurance.getTaskid());
					list.add(j);
				}
			}
			System.out.println(list);
			webParam.setList(list);
			webParam.setHtml(json);
			webParam.setUrl(url);
			return webParam;
		}
		return null;
	}

	//个人信息
	public WebParam<InsurancePingDingShanUserInfo> getUserInfo(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) throws Exception {
		String url="http://218.28.196.73:33002/siq/web/staff_disStaffInfo.action";
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		Page page = webClient.getPage(url);
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		WebParam<InsurancePingDingShanUserInfo> webParam = new WebParam<InsurancePingDingShanUserInfo>();
		if(page.getWebResponse().getContentAsString().contains("个人信息"))
		{
			InsurancePingDingShanUserInfo i = new InsurancePingDingShanUserInfo();
//			Element elementsByClass = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(1).getElementsByTag("tbody").get(0).getElementsByTag("td").get(1);
//			<td width="180" align="left"> 用户名：410182199311021448 </td>
//			System.out.println(elementsByClass);
			Element elementsByClass1 = doc.getElementsByClass("white1000").get(1).getElementsByTag("tr").get(3);
			i.setCompanyNum(elementsByClass1.getElementsByTag("td").get(0).text().substring(5));
			i.setCompany(elementsByClass1.getElementsByTag("td").get(1).text().substring(5));
			
//			<td height="41" colspan="2" bgcolor="#ffffff" align="left" class="left5"> 单位编号:410499003579 </td> 
//			<td height="41" colspan="4" bgcolor="#ffffff" align="left" class="left5"> 单位名称:河南成就人力资源有限公司 </td> 
			Elements elementsByClass2 = doc.getElementsByClass("white1000").get(1).getElementsByTag("tr").get(4).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0).getElementsByTag("td");
			i.setName(elementsByClass2.get(1).text());
			i.setSex(elementsByClass2.get(3).text());
			i.setNational(elementsByClass2.get(5).text());
			i.setBirthday(elementsByClass2.get(7).text());
			Elements elementsByClass3 = doc.getElementsByClass("white1000").get(1).getElementsByTag("tr").get(4).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1).getElementsByTag("td");
			i.setPersonalNum(elementsByClass3.get(1).text());
			i.setIdNum(elementsByClass3.get(3).text());
			Elements elementsByClass4 = doc.getElementsByClass("white1000").get(1).getElementsByTag("tr").get(4).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(2).getElementsByTag("td");
			i.setJoinDate(elementsByClass4.get(1).text());
			i.setPersonalDate(elementsByClass4.get(3).text());
			i.setSetDate(elementsByClass4.get(5).text());
			Elements elementsByClass5 = doc.getElementsByClass("white1000").get(1).getElementsByTag("tr").get(4).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(3).getElementsByTag("td");
			i.setStatus(elementsByClass5.get(1).text());
			i.setMonth(elementsByClass5.get(3).text());
			i.setSum(elementsByClass5.get(5).text());
			Elements elementsByClass6 = doc.getElementsByClass("white1000").get(1).getElementsByTag("tr").get(4).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(4).getElementsByTag("td");
			i.setCompanyMoney(elementsByClass6.get(1).text());
			i.setPersonalMoney(elementsByClass6.get(3).text());
			i.setMoneyMonth(elementsByClass6.get(5).text());
			Elements elementsByClass7 = doc.getElementsByClass("white1000").get(1).getElementsByTag("tr").get(4).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(5).getElementsByTag("td");
			i.setMoneySum(elementsByClass7.get(1).text());
			i.setTaskid(taskInsurance.getTaskid());
			System.out.println(i);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setInsurancePingDingShanUserInfo(i);
			webParam.setUrl(url);
			return webParam;
		}
		return null;
		
	}
	
	
	public WebClient addcookie(WebClient webclient, TaskInsurance taskInsurance) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}
		return webclient;
	}
	
}
