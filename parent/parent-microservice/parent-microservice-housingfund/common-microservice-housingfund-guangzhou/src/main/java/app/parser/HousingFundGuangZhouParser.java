package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.guangzhou.HousingGuangzhouDetailAccount;
import com.microservice.dao.entity.crawler.housing.guangzhou.HousingGuangzhouUserInfo;


/**
 * @description:广州公积金信息解析parser
 * @author: sln 
 * @date: 2017年9月29日 上午9:52:13 
 */
@Component
public class HousingFundGuangZhouParser {
	public List<HousingGuangzhouDetailAccount> detailAccountParser(String html, TaskHousing taskHousing, String presentDate) {
		List<HousingGuangzhouDetailAccount> list=null;
		HousingGuangzhouDetailAccount housingGuangzhouDetailAccount=null;
		if(html.contains("gridarea_tr1")){  //说明有数据可以提供采集
			list=new ArrayList<HousingGuangzhouDetailAccount>();
			Document doc = Jsoup.parse(html);
			Elements elements = doc.getElementsByClass("gridarea").get(0).getElementsByTag("table").get(0).getElementsByClass("gridarea_tr1");
			int size = elements.size();
			for(int i=0;i<size;i++){
				housingGuangzhouDetailAccount =new HousingGuangzhouDetailAccount();
				housingGuangzhouDetailAccount.setTaskid(taskHousing.getTaskid());
				housingGuangzhouDetailAccount.setSortnum(elements.get(i).getElementsByTag("td").get(0).text());
				housingGuangzhouDetailAccount.setAccountdate(elements.get(i).getElementsByTag("td").get(1).text());
				housingGuangzhouDetailAccount.setBusinesstype(elements.get(i).getElementsByTag("td").get(2).text());
				housingGuangzhouDetailAccount.setPayearmonth(elements.get(i).getElementsByTag("td").get(3).text());
				housingGuangzhouDetailAccount.setAmount(elements.get(i).getElementsByTag("td").get(4).text());	
				housingGuangzhouDetailAccount.setBalance(elements.get(i).getElementsByTag("td").get(5).text());
				housingGuangzhouDetailAccount.setReachaccountdate(elements.get(i).getElementsByTag("td").get(6).text());
				housingGuangzhouDetailAccount.setStartdate("20000101");
				housingGuangzhouDetailAccount.setEndate(presentDate);
				list.add(housingGuangzhouDetailAccount);
			}
		}else{
			list=null;
		}
		return list;
	}
	public HousingGuangzhouUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		HousingGuangzhouUserInfo housingGuangzhouUserInfo=null;
		if(html.contains("姓名")){
			Document doc=Jsoup.parse(html);
			housingGuangzhouUserInfo=new HousingGuangzhouUserInfo();
			housingGuangzhouUserInfo.setAccountnumstatus(doc.select("td:contains(账户状态)+td").first().text());
			housingGuangzhouUserInfo.setChargebasenum(doc.select("td:contains(缴存基数)+td").first().text());
			housingGuangzhouUserInfo.setCompchargescale(doc.select("td:contains(单位缴存比例)+td").first().text());
			housingGuangzhouUserInfo.setCompname(doc.select("td:contains(单位名称)+td").first().text());
			housingGuangzhouUserInfo.setCompnum(doc.select("td:contains(单位登记号)+td").first().text());
			housingGuangzhouUserInfo.setCredentialstype(doc.select("td:contains(证件类型)+td").first().text());
			housingGuangzhouUserInfo.setHousingaccountnum(doc.select("td:contains(公积金账号)+td").first().text());
			housingGuangzhouUserInfo.setIdnum(doc.select("td:contains(证件号)+td").first().text());
			housingGuangzhouUserInfo.setLastbusinessdate(doc.select("td:contains(最后业务日期)+td").first().text());
			housingGuangzhouUserInfo.setLastyearbalance(doc.select("td:contains(上年结转余额)+td").first().text());
			housingGuangzhouUserInfo.setName(doc.select("td:contains(姓名)+td").first().text());
			housingGuangzhouUserInfo.setPerchargescale(doc.select("td:contains(个人缴存比例)+td").first().text());
			housingGuangzhouUserInfo.setPersonalnum(doc.select("td:contains(个人编号)+td").first().text());
			housingGuangzhouUserInfo.setTaskid(taskHousing.getTaskid());
			housingGuangzhouUserInfo.setThisyearbalance(doc.select("td:contains(当前余额)+td").first().text());
			housingGuangzhouUserInfo.setThisyearcharge(doc.select("td:contains(当年缴存金额)+td").first().text());
			housingGuangzhouUserInfo.setThisyeardrawmoney(doc.select("td:contains(当年提取金额)+td").first().text());
		}
		return housingGuangzhouUserInfo;
	}
}
