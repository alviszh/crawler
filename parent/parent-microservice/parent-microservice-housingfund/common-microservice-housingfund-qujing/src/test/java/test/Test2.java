package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.qujing.HousingQujingPayDetails;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class Test2 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\2222.txt"), "UTF-8");
		//System.out.println(html);
		JSONObject jsonObject = JSONObject.fromObject(html);
		String result= jsonObject.getString("result");
	    System.out.println(result);
		JSONArray listArray = JSONArray.fromObject(result);
		for (int i = 0; i < listArray.size(); i++) {
			System.out.println("============"+i);
			System.out.println(listArray.get(i));
			
			JSONArray listArrayList = JSONArray.fromObject(listArray.get(i));
			 String dealDate=JSONObject.fromObject(listArrayList.get(0)).getString("info");
			 String summary=JSONObject.fromObject(listArrayList.get(1)).getString("info");
			 String increaseAmount=JSONObject.fromObject(listArrayList.get(2)).getString("info");
			 String reduceAmount=JSONObject.fromObject(listArrayList.get(3)).getString("info");
			 String balance=JSONObject.fromObject(listArrayList.get(4)).getString("info");
			 System.out.println(dealDate);
				HousingQujingPayDetails HousingQujingPayDetail = new HousingQujingPayDetails();
				HousingQujingPayDetail.setDealDate(dealDate);
				HousingQujingPayDetail.setSummary(summary);
				HousingQujingPayDetail.setIncreaseAmount(increaseAmount);
				HousingQujingPayDetail.setReduceAmount(reduceAmount);
				HousingQujingPayDetail.setBalance(balance);
				HousingQujingPayDetail.setTaskid("1111");
		}
	}
	
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
