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

import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnMedical;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\hamedical.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementsByTag = doc.getElementsByTag("tbody").get(1).getElementsByTag("td");
		System.out.println(elementsByTag);
		InsuranceHuaiAnMedical i = null;
		List<InsuranceHuaiAnMedical> list = new ArrayList<InsuranceHuaiAnMedical>();
		for (int j = 0; j < elementsByTag.size(); j=j+14) {
			i = new InsuranceHuaiAnMedical();
			Element element = elementsByTag.get(j);
			i.setCompany(element.text());
			i.setName(elementsByTag.get(j+1).text());
			i.setStatus(elementsByTag.get(j+2).text());
			i.setType(elementsByTag.get(j+3).text());
			i.setPayType(elementsByTag.get(j+4).text());
			i.setDatea(elementsByTag.get(j+5).text());
			i.setActualDate(elementsByTag.get(j+6).text());
			i.setBase(elementsByTag.get(j+7).text());
			i.setPayFlag(elementsByTag.get(j+8).text());
			i.setGetFlag(elementsByTag.get(j+9).text());
			i.setPersonalPay(elementsByTag.get(j+10).text());
			i.setCompanyPay(elementsByTag.get(j+11).text());
			i.setPersonalBill(elementsByTag.get(j+12).text());
			i.setCompanyBill(elementsByTag.get(j+13).text());
			list.add(i);
		}
		System.out.println(list);
//		Elements elementsByClass = doc.getElementsByClass("pageFormContent");
//		System.out.println(elementsByClass);
//		String nextLabelByKeyword = getNextLabelByKeyword(doc, "养老保险视同缴费月数");
//		System.out.println(nextLabelByKeyword+"--------");
		
		
		
	}
	
	
	  /**
     * @param document
     * @param keyword
     * @return
     * @Des 获取目标标签的下一个兄弟标签的内容
     */
    public static String getNextLabelByKeyword(Document document, String keyword) {
        Elements es = document.select("label:contains(" + keyword + ")");
        if (null != es && es.size() > 0) {
            Element element = es.first();
            Element nextElement = element.nextElementSibling();
            if (null != nextElement) {
                return nextElement.text();
            }
        }
        return null;
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