package app.crawler.htmlparse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.insurance.lianyungang.InsuranceLianYunGangPay;
import com.microservice.dao.entity.crawler.insurance.lianyungang.InsuranceLianYunGangUser;

@Component
@Service
public class InsuranceLianYunGangParseService  {

	private Gson gs = new Gson();

	public InsuranceLianYunGangUser userParse(String html) throws Exception {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<InsuranceLianYunGangUser>() {
		}.getType();
		InsuranceLianYunGangUser jsonObject = gs.fromJson(html, type);

		return jsonObject;
	}


	public List<InsuranceLianYunGangPay> payParse(String html,String taskid) {

		// File input = new File("C:/Users/Administrator/Desktop/111.html");
		// Document doc = Jsoup.parse(input, "UTF-8");

		Document doc = Jsoup.parse(html);

		Elements divEles = doc.select("div.ui-grid-canvas").select("div.ng-isolate-scope");

		List<InsuranceLianYunGangPay>  list = new ArrayList<>();
		
		for (Element divEle : divEles) {

			InsuranceLianYunGangPay insuranceLianYunGangPay = new InsuranceLianYunGangPay();
			
			Elements divTxtEles = null;
			
			try{
				divTxtEles = divEle.select("div.ng-binding");
			}catch(Exception e){
				continue;
			}
			

			try {
				String company = divTxtEles.get(0).text();
				insuranceLianYunGangPay.setCompany(company);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				String type = divTxtEles.get(1).text();
				insuranceLianYunGangPay.setType(type);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				String date = divTxtEles.get(2).text();
				insuranceLianYunGangPay.setDate(date);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				String basepay = divTxtEles.get(3).text();
				insuranceLianYunGangPay.setBasepay(basepay);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				String companyPay = divTxtEles.get(4).text();
				insuranceLianYunGangPay.setCompanyPay(companyPay);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				String manPay = divTxtEles.get(5).text();
				insuranceLianYunGangPay.setManPay(manPay);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				String overduefine = divTxtEles.get(6).text();
				insuranceLianYunGangPay.setOverduefine(overduefine);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				String total = divTxtEles.get(7).text();
				insuranceLianYunGangPay.setTotal(total);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				String payStatue = divTxtEles.get(8).text();
				insuranceLianYunGangPay.setPayStatue(payStatue);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// System.out.println("company = "+company);
			// System.out.println("type = "+type);
			// System.out.println("date = "+date);
			// System.out.println("basepay = "+basepay);
			// System.out.println("companyPay = "+companyPay);
			// System.out.println("manPay = "+manPay);
			// System.out.println("overduefine = "+overduefine);
			// System.out.println("total = "+total);
			// System.out.println("payStatue = "+payStatue);
			insuranceLianYunGangPay.setTaskid(taskid);
			list.add(insuranceLianYunGangPay);
		}
		
		return list;
	}

}
