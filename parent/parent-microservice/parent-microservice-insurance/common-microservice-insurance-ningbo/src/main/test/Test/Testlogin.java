package Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboEndowment;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboUserInfo;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Testlogin {

	public static void main(String[] args) throws Exception {
		String url="https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/index.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		HtmlTextInput loginid = (HtmlTextInput)page.getFirstByXPath("//input[@id='loginid']");
		loginid.reset();
		loginid.setText("330222197703136914");
		
		
		
		HtmlPasswordInput pwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='pwd']");
		pwd.reset();
		pwd.setText("63207764");
		
		HtmlImage img = page.getFirstByXPath("//img[@id='yzmJsp']");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput yzm = (HtmlTextInput)page.getFirstByXPath("//input[@id='yzm']");
		yzm.reset();
		yzm.setText(inputValue);
		
		HtmlElement button = page.getFirstByXPath("//input[@id='btnLogin']");
		HtmlPage page2 = button.click();
//		String string22 = page2.getWebResponse().getContentAsString();
//		System.out.println(string22);
		WebClient webClient2 = page2.getWebClient();
		
		String imgurl="https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/comm/checkYzm.jsp?yzm="+inputValue+"&client=NBHRSS_WEB";
		Page page4 = webClient2.getPage(imgurl);
		
		String loginurl="https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/rzxt/nbhrssLogin.parser?id=330222197703136914&password=63207764&phone=&client=NBHRSS_WEB";
		Page page5 = webClient2.getPage(loginurl);
		
        String url2="https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/query/query-grxx.jsp";
        HtmlPage page3 = webClient2.getPage(url2);
        System.out.println(page5.getWebResponse().getContentAsString());
        Set<Cookie> cookies = webClient2.getCookieManager().getCookies();
        String string = null;
        for (Cookie cookie : cookies) {
		if(cookie.getName().equals("__rz__k"))
		{
			string = cookie.getValue();
		}
	    }
        
        String url3="https://app.nbhrss.gov.cn/nbykt/rest/commapi?callback=jQuery112407393386748973498_1508834036437&access_token="+string+"&api=10S005&bustype=01&refresh=true&param=%7B%7D&client=NBHRSS_WEB&_="+System.currentTimeMillis();
        Page page6 = webClient2.getPage(url3);
        //System.out.println(page6.getWebResponse().getContentAsString());
        String string2 = page6.getWebResponse().getContentAsString();
 //       System.out.println(string2);
        int i = string2.indexOf("(");
        String string3 = string2.substring(i+1,string2.length()-2);
 //       System.out.println(string3);
        String replaceAll = string3.replaceAll("\\\\", "");
        String substring = replaceAll.substring(11,replaceAll.length()-12);
        //System.out.println(substring);
        
        JSONObject fromObject2 = JSONObject.fromObject(substring);
        //System.out.println(fromObject2);
        InsuranceNingboUserInfo insuranceNingboInfo = new InsuranceNingboUserInfo();
        insuranceNingboInfo.setInsuranceNum(fromObject2.getString("AAZ500"));
        insuranceNingboInfo.setCardMoney(fromObject2.getString("AAE010"));
        insuranceNingboInfo.setNational(fromObject2.getString("AZA103"));
        insuranceNingboInfo.setCardDate(fromObject2.getString("AAZ503"));
        String sex =null;
        if(fromObject2.getString("AAC004")=="1")
        {
        	 sex = "男";
        }
        else{
        	 sex ="女";
        }
        insuranceNingboInfo.setSex(sex);
        insuranceNingboInfo.setPostalcode(fromObject2.getString("AAZ220"));
        insuranceNingboInfo.setPhoneNum(fromObject2.getString("AAE004"));
        String status=null;
        if(fromObject2.getString("AAZ502")=="1")
        {
        	status="正常有卡状态";
        }
        else if(fromObject2.getString("AAZ502")=="4")
        {
        	status="临时挂失状态";
        }
        else if(fromObject2.getString("AAZ502")=="2")
        {
        	status="正式挂失状态";
        }
        insuranceNingboInfo.setCardStatus(status);
        insuranceNingboInfo.setAddr(fromObject2.getString("AAE006"));
        insuranceNingboInfo.setLandlineNum(fromObject2.getString("AAE005"));
        System.out.println(insuranceNingboInfo);
        
        String ylurl="https://app.nbhrss.gov.cn/nbykt/rest/commapi?callback=jQuery112402946609875493056_1508899402245&access_token="+string+"&api=91S012&bustype=01&refresh=true&param=%7B%22AAB301%22%3A%22330282%22%2C%22PAGENO%22%3A1%2C%22PAGESIZE%22%3A100%7D&client=NBHRSS_WEB&_="+System.currentTimeMillis();
        Page page7 = webClient2.getPage(ylurl);
        System.out.println(page7.getWebResponse().getContentAsString());
        String ylstring = page7.getWebResponse().getContentAsString();
        int j = ylstring.indexOf("(");
        String substring2 = ylstring.substring(j+1, ylstring.length()-2);
        JSONObject ylfromObject = JSONObject.fromObject(substring2);
        String ylstring4 = ylfromObject.getString("result");
        JSONObject fromObject = JSONObject.fromObject(ylstring4);
        String string4 = fromObject.getString("COSTLIST");
        JSONObject fromObject3 = JSONObject.fromObject(string4);
        System.out.println(fromObject3);
        String string5 = fromObject3.getString("COST");
        JSONArray fromObject4 = JSONArray.fromObject(string5);
        System.out.println(fromObject4);
        InsuranceNingboEndowment insuranceNingboEndowment = null;
        List list = new ArrayList();
        for (int k = 0; k < fromObject4.size(); k++) {
        	insuranceNingboEndowment = new InsuranceNingboEndowment();
			JSONObject fromObject5 = JSONObject.fromObject(fromObject4.get(k));
			insuranceNingboEndowment.setPayDate(fromObject5.getString("AAE002"));
			insuranceNingboEndowment.setInsuranceBase(fromObject5.getString("AAE180"));
			insuranceNingboEndowment.setPersonMoney(fromObject5.getString("AAE022"));
			insuranceNingboEndowment.setGetStatus(fromObject5.getString("AAE078"));
			list.add(insuranceNingboEndowment);
		}
        System.out.println(list);
        
        
//        String[] split2 = split[1].split(")");
//        System.out.println(split2[0]);
//		Document doc = Jsoup.parse(string);
//		//Elements elementById = doc.getElementById("table33").getElementById("table36").getElementsByTag("strong");
//		Elements elementById1 = doc.getElementById("table33").getElementById("table36").getElementsByTag("font");
//
//		HousingFundHaErBinUserInfo haErBinUserInfo = new HousingFundHaErBinUserInfo();
//		haErBinUserInfo.setName(elementById1.get(0).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setCompany(elementById1.get(1).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setPersonalNum(elementById1.get(2).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setFundNum(elementById1.get(3).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setIdCard(elementById1.get(4).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setOpenDate(elementById1.get(5).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setPersonalBase(elementById1.get(6).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setPersonalRatio(elementById1.get(7).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setMonthSave(elementById1.get(8).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setCompanyRatio(elementById1.get(9).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setYearSave(elementById1.get(10).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setLastDate(elementById1.get(11).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setYearGet(elementById1.get(12).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setStatus(elementById1.get(13).text().replaceAll("&nbsp;", ""));
//		haErBinUserInfo.setFee(elementById1.get(14).text().replaceAll("&nbsp;", ""));
//		System.out.println("================"+page2.getWebResponse().getContentAsString());
	}
}
