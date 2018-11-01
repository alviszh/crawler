package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanEndowment;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\sg.txt"); 
		String json = txt2String(file);
		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Element elementsByClass = doc.getElementsByClass("tableblue").get(0).getElementsByTag("tbody").get(0);
		Element element = elementsByClass.getElementsByTag("tr").get(0).getElementsByTag("td").get(1);
//		System.out.println(elementsByClass);
		InsuranceShaoGuanUserInfo i = new InsuranceShaoGuanUserInfo();
		i.setCompany(elementsByClass.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text());
		i.setIDNum(elementsByClass.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text());
		i.setName(elementsByClass.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text());
		i.setStatus(elementsByClass.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text());
		i.setMoneyType(elementsByClass.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text());
		i.setJoinDate(elementsByClass.getElementsByTag("tr").get(5).getElementsByTag("td").get(1).text());
		i.setYongGong(elementsByClass.getElementsByTag("tr").get(6).getElementsByTag("td").get(1).text());
		i.setPersonal(elementsByClass.getElementsByTag("tr").get(7).getElementsByTag("td").get(1).text());
		i.setLastThree(elementsByClass.getElementsByTag("tr").get(8).getElementsByTag("td").get(1).text());
		i.setSf(elementsByClass.getElementsByTag("tr").get(9).getElementsByTag("td").get(1).text());
		i.setNetPoint(elementsByClass.getElementsByTag("tr").get(10).getElementsByTag("td").get(1).text());
		i.setPhone(elementsByClass.getElementsByTag("tr").get(11).getElementsByTag("td").get(1).text());
		i.setAddr(elementsByClass.getElementsByTag("tr").get(12).getElementsByTag("td").get(1).text());
		i.setTaskid("");
//		System.out.println(i);
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

	  /**
     * @param document
     * @param keyword
     * @return
     * @Des 获取目标标签的下一个兄弟标签的内容
     */
    public static String getNextLabelByKeyword(Document document, String keyword) {
        Elements es = document.select("td:contains(" + keyword + ")");
        if (null != es && es.size() > 0) {
            Element element = es.first();
            Element nextElement = element.nextElementSibling();
            if (null != nextElement) {
                return nextElement.text();
            }
        }
        return null;
    }
		
}