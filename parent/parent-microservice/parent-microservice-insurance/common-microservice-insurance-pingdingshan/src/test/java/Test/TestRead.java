package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.pingdingshan.InsurancePingDingShanUserInfo;

public class TestRead {

	public static void main(String[] args) {
		
			File file = new File("C:\\Users\\Administrator\\Desktop\\1.txt"); 
			String json = txt2String(file);
			//System.out.println(json);
			Document doc = Jsoup.parse(json);
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
			System.out.println(i);
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

