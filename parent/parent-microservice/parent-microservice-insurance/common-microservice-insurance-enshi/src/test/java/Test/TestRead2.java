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

import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiMedical;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiUserInfo;

import app.service.InsuranceEnShiCommonService;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\yl.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document parse = Jsoup.parse(json);
		InsuranceEnShiMedical in = null;
		List<InsuranceEnShiMedical> list = new ArrayList<InsuranceEnShiMedical>();
		Elements elementById = parse.getElementById("ctl00_ContentPlaceHolder1_GridView4").getElementsByTag("tbody").get(0).getElementsByTag("tr");
		for (int i = 1; i < elementById.size(); i++) {
//			System.out.println(elementById.get(i));	
			in = new InsuranceEnShiMedical();
			in.setPersonalNum(elementById.get(i).getElementsByTag("td").get(0).text());
			in.setInDate(elementById.get(i).getElementsByTag("td").get(1).text());
			in.setXz(elementById.get(i).getElementsByTag("td").get(2).text());
			in.setMoneyType(elementById.get(i).getElementsByTag("td").get(3).text());
			in.setPayType(elementById.get(i).getElementsByTag("td").get(4).text());
			in.setBase(elementById.get(i).getElementsByTag("td").get(5).text());
			in.setPayNow(elementById.get(i).getElementsByTag("td").get(6).text());
			in.setPersonalMoney(elementById.get(i).getElementsByTag("td").get(7).text());
			in.setGetFlag(elementById.get(i).getElementsByTag("td").get(8).text());
			in.setGetDate(elementById.get(i).getElementsByTag("td").get(9).text());
			in.setSendFlag(elementById.get(i).getElementsByTag("td").get(10).text());
			in.setTaskid("");
			list.add(in);
		}
		System.out.println(list);
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
     * @param elementById
     * @param keyword
     * @return
     * @Des 获取目标标签的下一个兄弟标签的内容
     */
    public static String getNextLabelByKeyword(Element elementById, String keyword) {
        Elements es = elementById.select("td:contains(" + keyword + ")");
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