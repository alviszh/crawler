package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.Page;
import com.microservice.dao.entity.crawler.insurance.nanyang.InsuranceNanYangUserInfo;
import com.microservice.dao.entity.crawler.insurance.pingdingshan.InsurancePingDingShanEndowment;

import app.common.WebParam;


public class TestRead{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\NanYang.txt"); 
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
		
		Elements elementsByClass = doc.getElementsByClass("content1");
		Element element2 = elementsByClass.get(1);
		Elements elementsByTag = element2.getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1).getElementsByTag("td");
		Elements elementsByTag1 = element2.getElementsByTag("tbody").get(0).getElementsByTag("tr").get(2).getElementsByTag("td");
		InsurancePingDingShanEndowment j = null;
		List<InsurancePingDingShanEndowment> list = new ArrayList<InsurancePingDingShanEndowment>();
		for (int i = 0; i < elementsByTag.size()/2; i++) {
			System.out.println(elementsByTag.get(i).text().length());
			System.out.println(elementsByTag1.get(i).text().length());
			j = new InsurancePingDingShanEndowment();
			if(elementsByTag.get(i).text().length()>4)
			{
				if(elementsByTag.get(i).text().contains("年"))
				{
					j.setYear(elementsByTag.get(i).text().substring(0, 4));
					
				}
				if(elementsByTag1.get(i).text().length()>5)
				{
					j.setBase(elementsByTag1.get(i).text().substring(0, 4));
				}
			}
			
			list.add(j);
		}
		
		System.out.println(list+"-----------------list--------------------------------------------------------------------------------------------");
		
		WebParam<InsuranceNanYangUserInfo> webParam = new WebParam<InsuranceNanYangUserInfo>();
		if(json.contains("个人信息"))
		{
			InsuranceNanYangUserInfo i = new InsuranceNanYangUserInfo();
//			Element elementsByClass = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(1).getElementsByTag("tbody").get(0).getElementsByTag("td").get(1);
//			<td width="180" align="left"> 用户名：410182199311021448 </td>
//			System.out.println(elementsByClass);
			Element elementsByClass1 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(1);
			if(elementsByClass1.getElementsByTag("td").get(0).text().contains("单位编号"))
			{
				i.setCompanyNum(elementsByClass1.getElementsByTag("td").get(0).text().substring(5));
			}
			else
			{
				i.setCompanyNum(elementsByClass1.getElementsByTag("td").get(0).text().substring(9));
			}
			i.setCompany(elementsByClass1.getElementsByTag("td").get(1).text().substring(5));
//			<td height="41" colspan="2" bgcolor="#ffffff" align="left" class="left5"> 单位编号:410499003579 </td> 
//			<td height="41" colspan="4" bgcolor="#ffffff" align="left" class="left5"> 单位名称:河南成就人力资源有限公司 </td>
			
			Element element = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2);
			System.out.println(element);
			
			
			Elements elementsByClass2 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0).getElementsByTag("td");
			i.setName(elementsByClass2.get(3).text());
			i.setSex(elementsByClass2.get(5).text());
//			
			Elements elementsByClass3 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1).getElementsByTag("td");
			i.setCardStatus(elementsByClass3.get(1).text());
//			i.setPersonalNum();
			i.setIdNum(elementsByClass3.get(3).text());
			i.setNational(elementsByClass3.get(5).text());
			Elements elementsByClass4 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(2).getElementsByTag("td");
			i.setBirthday(elementsByClass4.get(1).text());
			i.setJoinDate(elementsByClass4.get(3).text());
			i.setPersonalDate(elementsByClass4.get(5).text());
			Elements elementsByClass5 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(3).getElementsByTag("td");
			i.setSetDate(elementsByClass5.get(1).text());
			i.setStatus(elementsByClass5.get(3).text());
			i.setPayStatus(elementsByClass5.get(5).text());
			Elements elementsByClass6 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(4).getElementsByTag("td");
			i.setBaseMoney(elementsByClass6.get(1).text());
		}
	}
	public static String txt2String(File file) { 
		StringBuilder result = new StringBuilder(); 
		try { 
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); 
		String s = null; 
		while ((s = br.readLine()) != null) { 
		result.append(System.lineSeparator() + s); 
		} 
		br.close(); 
		} catch (Exception e) { 
		e.printStackTrace(); 
		} 
		return result.toString(); 
		}

		
}