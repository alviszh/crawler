package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.anqing.HousingAnQingPay;
import com.microservice.dao.entity.crawler.housing.anqing.HousingAnQingUserinfo;

import app.bean.WebParamHousing;

public class HousingAnQingParse {
	static String rgex = "共(.*?)页 ";  
	public static WebParamHousing<HousingAnQingUserinfo> userinfo_parse(String html) {
		WebParamHousing<HousingAnQingUserinfo> webParamHousing = new WebParamHousing<>();
		if(html.indexOf("出错了，请重试")!=-1){
			webParamHousing.setErrormessage("webParamHousing");
			return webParamHousing;
		}
		HousingAnQingUserinfo userinfo = new HousingAnQingUserinfo();
		List<HousingAnQingUserinfo> listresult = new ArrayList<>();

		Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(html), "utf-8");

		String name = doc.select("td:contains(" + "个人姓名" + ")+td").text().trim();
		String companyname = doc.select("td:contains(" + "单位名称" + ")+td").text().trim();
		String companynum = doc.select("td:contains(" + "单位代码" + ")+td").text().trim();
		String personnum = doc.select("td:contains(" + "个人代码" + ")+td").text().trim();
		String accountbank = doc.select("td:contains(" + "单位银行账号" + ")+td").text().trim();
		String personalbank = doc.select("td:contains(" + "个人银行账号" + ")+td").text().trim();
		String bankname = doc.select("td:contains(" + "归集银行名称" + ")+td").text().trim();
		String enddate = doc.select("td:contains(" + "缴至日期" + ")+td").text().trim();
		String ownness = doc.select("td:contains(" + "个人状态" + ")+td").text().trim();
		String depositratio = doc.select("td:contains(" + "缴存比例" + ")+td").text().trim();
		String monthpay = doc.select("td:contains(" + "月应缴额" + ")+td").text().trim();
		String balance = doc.select("td:contains(" + "余额" + ")+td").text().trim();
		

		userinfo.setName(name);
		userinfo.setCompanyname(companyname);
		userinfo.setCompanynum(companynum);
		userinfo.setPersonnum(personnum);
		userinfo.setAccountbank(accountbank);
		userinfo.setPersonalbank(personalbank);
		userinfo.setBankname(bankname);
		userinfo.setEnddate(enddate);
		userinfo.setOwnness(ownness);
		userinfo.setDepositratio(depositratio);
		userinfo.setMonthpay(monthpay);
		userinfo.setBalance(balance);
		userinfo.setEnddate(enddate);
		listresult.add(userinfo);
		webParamHousing.setList(listresult);
		return webParamHousing;
	}

	public static WebParamHousing<HousingAnQingPay> pay_parse(String html) {

		WebParamHousing<HousingAnQingPay> webParamHousing = new WebParamHousing<>();
		
		List<HousingAnQingPay> listresult = new ArrayList<>();
		
		Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(html), "utf-8");

		Elements treles = doc.select("table[bordercolor=#ccc]").select("tbody").select("tr");
		String pagnum = getSubUtilSimple(doc.select("div[style=padding:5px]").first().ownText(),rgex);
		int i=0;
		for(Element trele : treles){
			i++;
			if(i==1){
				continue;
			}
			String xuhao = trele.select("td").get(0).text().trim();
			String abstracttxt = trele.select("td").get(1).text().trim();
			String startdate = trele.select("td").get(2).text().trim();
			String debitenum = trele.select("td").get(3).text().trim();
			String creditnum = trele.select("td").get(4).text().trim();
			String balance = trele.select("td").get(5).text().trim();

			HousingAnQingPay pay = new HousingAnQingPay();
			pay.setXuhao(xuhao);
			pay.setAbstracttxt(abstracttxt);
			pay.setStartdate(startdate);
			pay.setDebitenum(debitenum);
			pay.setCreditnum(creditnum);
			pay.setBalance(balance);
			listresult.add(pay);
		}
		webParamHousing.setList(listresult);
		webParamHousing.setPagnum(pagnum);
		return webParamHousing;
	}
	
	 /** 
     * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样 
     * @param soap 
     * @param rgex 
     * @return 
     */  
    public static String getSubUtilSimple(String soap,String rgex){  
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式  
        Matcher m = pattern.matcher(soap);  
        while(m.find()){  
            return m.group(1);  
        }  
        return "";  
    }  

}
