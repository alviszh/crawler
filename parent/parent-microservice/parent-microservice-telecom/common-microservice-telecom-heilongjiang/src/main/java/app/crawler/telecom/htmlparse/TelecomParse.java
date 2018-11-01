package app.crawler.telecom.htmlparse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomCallThemResult;
import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomCustomerThemResult;
import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomPayMsgThemResult;

@Component
@Service
public class TelecomParse {
	public static List<TelecomCallThemResult> callParse(String html)  {
//		没有查找到相关数据！
		
		if(html.indexOf("没有查找到相关数据")!=-1){
			return null;
		}
		Document doc = Jsoup.parse(html);
		Elements elestrs = doc.select("table#tb1").select("tbody").select("tr");
		
//		System.out.println("elestrs::::::;"+elestrs);
		
		List<TelecomCallThemResult> list = new ArrayList<>();
		for (Element ele : elestrs) {
			
//			System.out.println("ele::::::;"+ele);
			TelecomCallThemResult teleesult = new TelecomCallThemResult();
			
			try {

				try{
					teleesult.setDate(ele.select("td").get(1).text());

				}catch(Exception e){
					System.out.println("date error");
					e.printStackTrace();
				}
				
				try{
					teleesult.setGuishudi(ele.select("td").get(2).text());

				}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
					teleesult.setCalltype(ele.select("td").get(3).text());

				}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
					teleesult.setOthernum(ele.select("td").get(4).text());
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					teleesult.setTime(ele.select("td").get(5).text());
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					teleesult.setCalltype(ele.select("td").get(6).text());
				}catch(Exception e){
					e.printStackTrace();

				}
				try{
					teleesult.setMoney(ele.select("td").get(7).text());
				}catch(Exception e){
					e.printStackTrace();

				}
//				try{
//					teleesult.setOthermoney(ele.select("td").get(8).text());
//
//				}catch(Exception e){
//					e.printStackTrace();
//
//				}
//				try {
//					teleesult.setCalltype(ele.select("td").get(9).text());
//				} catch (Exception e) {
//
//					e.printStackTrace();
//				}
//				
//				try {
//					teleesult.setGuishudi(ele.select("td").get(10).text());
//				} catch (Exception e) {
//
//					e.printStackTrace();
//				}
				System.out.println("teleesult:::::::::;"+teleesult.toString());
				list.add(teleesult);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
		return list;
	}
	
	public static List<TelecomCallThemResult>  smsParse(String html)  {
//		没有查找到相关数据！
		
		if(html.indexOf("没有查找到相关数据")!=-1){
			return null;
		}
		Document doc = Jsoup.parse(html);
		Elements elestrs = doc.select("table#tb1").select("tbody").select("tr");
		
//		System.out.println("elestrs::::::;"+elestrs);
		
		List<TelecomCallThemResult> list = new ArrayList<>();
		Elements scripteles =  doc.select("[name=CqdQueryForm]").select("script");
		int i = 0;
		for (Element ele : elestrs) {
			
//			System.out.println("ele::::::;"+ele);
			TelecomCallThemResult teleesult = new TelecomCallThemResult();
			
			try {

				try{
					teleesult.setUsernumber(ele.select("td").get(1).text());

				}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
					teleesult.setOthernum(ele.select("td").get(2).text());

				}catch(Exception e){
					e.printStackTrace();
				}
				
//				try{
//					teleesult.setDate(ele.select("td").get(3).text());
//
//				}catch(Exception e){
//					e.printStackTrace();
//				}
				try {
					Element scriptele = scripteles.get(i);

					String texts =scriptele.toString().split("var")[1];

					String txt = texts.substring(texts.indexOf("(")+1, texts.indexOf(")"));
					teleesult.setDate(txt);
				} catch (Exception e) {

					e.printStackTrace();
					//System.out.println(ele);
				}
				
				try{
					teleesult.setTime(ele.select("td").get(4).text());
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					teleesult.setMoney(ele.select("td").get(5).text());
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					teleesult.setCalltype(ele.select("td").get(6).text());
				}catch(Exception e){
					e.printStackTrace();

				}
				try{
					teleesult.setOthermoney(ele.select("td").get(7).text());
				}catch(Exception e){
					e.printStackTrace();

				}
//				try{
//					teleesult.setOthermoney(ele.select("td").get(8).text());
//
//				}catch(Exception e){
//					e.printStackTrace();
//
//				}
//				try {
//					teleesult.setCalltype(ele.select("td").get(9).text());
//				} catch (Exception e) {
//
//					e.printStackTrace();
//				}
//				
//				try {
//					teleesult.setGuishudi(ele.select("td").get(10).text());
//				} catch (Exception e) {
//
//					e.printStackTrace();
//				}
				i++;
				
//				System.out.println("==========="+ele.select("td").get(3));
				System.out.println("teleesult:::::::::;"+teleesult.toString());
				list.add(teleesult);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
		return list;
	}
	
	
	public static Integer getPagecall(String html)  {

		if(html.indexOf("没有查找到相关数据")!=-1){
			return 1;
		}
		Document doc = Jsoup.parse(html);
		
//		System.out.println("====="+doc.select("body > div.step_pagewidth2.step_pagebody1 > table > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr > td > table > tbody > tr > td > table:nth-child(1) > tbody > tr:nth-child(1) > td > table > tbody > tr:nth-child(2) > td > form > table:nth-child(1) > tbody > tr > td:nth-child(2)").toString());
		
		String totalnumString = doc.select("body > div.step_pagewidth2.step_pagebody1 > table > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr > td > table > tbody > tr > td > table:nth-child(1) > tbody > tr:nth-child(1) > td > table > tbody > tr:nth-child(2) > td > form > table:nth-child(1) > tbody > tr > td:nth-child(2)").text().trim();
		Integer totalnum = 0;
		if(totalnumString != null && totalnumString.length()>0){
			 totalnum  = Integer.parseInt(totalnumString) ;

		}
		return totalnum;
		
	}
	public static List<TelecomPayMsgThemResult> payMsg_parse(String html) {

		Document doc = Jsoup.parse(html);
		List<TelecomPayMsgThemResult> list = new ArrayList<>();
		Elements tableeles = doc.select("div.fe-yu-ku").select("table").select("tr");

		int i = 0;
		for (Element trEle : tableeles) {
			if (i > 0) {
				Elements tdEles = trEle.select("td");
				if (tdEles.size() > 0) {
					TelecomPayMsgThemResult result = new TelecomPayMsgThemResult();
					result.setTelenumid(tdEles.get(0).text());
					result.setType(tdEles.get(1).text());
					result.setPaydate(tdEles.get(2).text());

					result.setPaynum(tdEles.get(3).text());
					list.add(result);
				}

			}

			i++;
		}
		return list;

	}

	public static TelecomCustomerThemResult custom_parse(String html) {
		TelecomCustomerThemResult result = new TelecomCustomerThemResult();
		Document doc = Jsoup.parse(html);
		try {
			String name = doc.select("td:contains(客户名称)").first().text().split(":")[1];
			result.setName(name);
		} catch (Exception e) {

		}
		
		try {
			String postcode = doc.select("td:contains(邮编)").first().toString().split("<br>")[0].split(":")[1].replaceAll("\"", "");
			result.setPostcode(postcode);
		} catch (Exception e) {

		}
		
		try {
			String address = doc.select("td:contains(地址)").first().toString().split("<br>")[1].split(":")[1].replaceAll("\"", "");
			result.setAddress(address);
		} catch (Exception e) {

		}
		
		try {
			String paydate = doc.select("td:contains(计费账期)").first().text().split(":")[1];
			result.setPaydate(paydate);
		} catch (Exception e) {

		}
		try {
			String paynum = doc.select("table:contains(本期费用合计)").first().select("td:contains(本期费用合计)").first()
					.select("tr:contains(本期费用合计)").first().toString();
			result.setPaynum(paynum);
		} catch (Exception e) {

		}
		try {
			String bussoptionalpackage = doc.select("table:contains(手机上网月功能费业务可选包)").first()
					.select("td:contains(手机上网月功能费业务可选包)").first().select("tr:contains(手机上网月功能费业务可选包)").select("td")
					.get(1).text();
			result.setBussoptional(bussoptionalpackage);
		} catch (Exception e) {

		}
		try {
			String bussrent = doc.select("table:contains(基本月租费)").first().select("td:contains(基本月租费)").first()
					.select("tr:contains(基本月租费)").select("td").get(1).text();
			result.setBussrent(bussrent);
		} catch (Exception e) {

		}
		try {
			String internetpay = doc.select("table:contains(上网及数据通信费)").first().select("td:contains(上网及数据通信费)").first()
					.select("tr:contains(上网及数据通信费)").select("td").get(1).text();
			result.setInternetpay(internetpay);

		} catch (Exception e) {

		}
		try {
			String internetchinapay = doc.select("table:contains(手机国内上网费)").first().select("td:contains(手机国内上网费)")
					.first().select("tr:contains(手机国内上网费)").select("td").get(1).text();
			result.setInternetchinapay(internetchinapay);

		} catch (Exception e) {

		}
		try {
			Element integraEle_tr = doc.select("table:contains(本期末可用积分)").first().select("td:contains(本期末可用积分)").first()
					.select("tr:contains(本期末可用积分)+tr").first();
			try {
				String currentIntegra = integraEle_tr.select("td").get(0).text();// 本期末可用积分
				result.setCurrentIntegra(currentIntegra);

			} catch (Exception e) {

			}
			try {
				String lastIntegra = integraEle_tr.select("td").get(2).text();// 上期末可用积分
				result.setLastIntegra(lastIntegra);

			} catch (Exception e) {

			}
			try {
				String usingIntegra = integraEle_tr.select("td").get(4).text();// 当前使用积分
				result.setUsingIntegra(usingIntegra);

			} catch (Exception e) {

			}
			try {
				String riseIntegra = integraEle_tr.select("td").get(6).text();// 本期新增积分
				result.setRiseIntegra(riseIntegra);

			} catch (Exception e) {

			}
		} catch (Exception e) {

		}

		return result;

	}

	public static void main(String[] args) throws IOException {
//		callThe_parse(null);
		
		File input = new File("C:/Users/Administrator/Desktop/111.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
		
		String html = doc.toString();
//		callParse(html);
		smsParse(html);
//		System.out.println(getPagecall(html));
	}

}
