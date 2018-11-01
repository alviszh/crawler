package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanMedical;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\yn.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementById = doc.getElementById("personInfo").getElementsByTag("tbody").get(0).getElementsByTag("td");
		System.out.println(elementById);
		InsuranceSZYunNanMedical  i = null;
		List<InsuranceSZYunNanMedical> list = new ArrayList<InsuranceSZYunNanMedical>();
		for (int j = 0; j < elementById.size(); j=j+7) {
			i = new InsuranceSZYunNanMedical();
			i.setDatea(elementById.get(j).text());
			i.setDateaIn(elementById.get(j+1).text());
			i.setBase(elementById.get(j+2).text());
			i.setPersonal(elementById.get(j+3).text());
			i.setCompany(elementById.get(j+4).text());
			i.setSum(elementById.get(j+5).text());
			i.setLastDatea(elementById.get(j+6).text());
			i.setTaskid("");
			list.add(i);
		}
		System.out.println(list);
		
//		InsuranceSZYunNanUserInfo i = new InsuranceSZYunNanUserInfo();
//		String ybkhTxt = doc.getElementsByClass("heade").get(0).getElementById("ybkhTxt").val();
//		i.setPersonalNum(ybkhTxt);
//		String CblxText = doc.getElementsByClass("heade").get(0).getElementById("CblxText").val();
//		i.setType(CblxText);
//		System.out.println(ybkhTxt+CblxText);
//		String xmTxt = doc.getElementsByClass("content").get(0).getElementById("xmTxt").val();
//		i.setName(xmTxt);
//		String xbTxt = doc.getElementsByClass("content").get(0).getElementById("xbTxt").val();
//		i.setSex(xbTxt);
//		String csnyTxt = doc.getElementsByClass("content").get(0).getElementById("csnyTxt").val();
//		i.setBirthday(csnyTxt);
//		String sfgwyTxt = doc.getElementsByClass("content").get(0).getElementById("sfgwyTxt").val();
//		i.setYn(sfgwyTxt);
//		String sfylzgryTxt = doc.getElementsByClass("content").get(0).getElementById("sfyyzgryTxt").val();
//		i.setMedical(sfylzgryTxt);
//		String sznlTxt = doc.getElementsByClass("content").get(0).getElementById("sznlTxt").val();
//		i.setYears(sznlTxt);
//		
//		String dwmcTxt = doc.getElementsByClass("content").get(0).getElementById("dwmcTxt").val();
//		i.setCompany(dwmcTxt);
//		String cbztTxt = doc.getElementsByClass("content").get(0).getElementById("cbztText").val();
//		i.setStatus(cbztTxt);
//		String zhyeTxt = doc.getElementsByClass("content").get(0).getElementById("zhyeTxt").val();
//		i.setFee(zhyeTxt);
//		
//		String czzsrTxt = doc.getElementsByClass("content").get(0).getElementById("zhzsrTxt").val();
//		i.setSum(czzsrTxt);
//		String zhzzcTxt = doc.getElementsByClass("content").get(0).getElementById("zhzzcTxt").val();
//		i.setSumPay(zhzzcTxt);
		
		
		
		
		
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