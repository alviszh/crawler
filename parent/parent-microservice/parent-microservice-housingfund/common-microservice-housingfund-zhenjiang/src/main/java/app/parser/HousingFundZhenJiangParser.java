package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhenjiang.HousingZhenJiangDetailAccount;
import com.microservice.dao.entity.crawler.housing.zhenjiang.HousingZhenJiangUserInfo;



/**
 * @author: sln 
 */
@Component
public class HousingFundZhenJiangParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundZhenJiangParser.class);
	public HousingZhenJiangUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		HousingZhenJiangUserInfo housingZhenJiangUserInfo=new HousingZhenJiangUserInfo();
		Document doc = Jsoup.parse(html);
		Elements elementsByTag = doc.getElementsByClass("shuju1").get(0).getElementsByTag("td");
		System.out.println(elementsByTag.toString());
		housingZhenJiangUserInfo.setTaskid(taskHousing.getTaskid().trim());
		housingZhenJiangUserInfo.setBalance(elementsByTag.get(3).text().replaceAll(" 元", ""));
		housingZhenJiangUserInfo.setAccountstatus(elementsByTag.get(5).text().replaceAll(" ", ""));
		housingZhenJiangUserInfo.setBasenum(elementsByTag.get(1).text().replaceAll(" 元", ""));
		housingZhenJiangUserInfo.setIdnum(elementsByTag.get(0).text());
		housingZhenJiangUserInfo.setMonthcharge(elementsByTag.get(2).text().replaceAll(" 元", ""));
		housingZhenJiangUserInfo.setNewchargemonth(elementsByTag.get(4).text().replaceAll(" ", ""));
		return housingZhenJiangUserInfo;
	}

	public List<HousingZhenJiangDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingZhenJiangDetailAccount> list=new  ArrayList<HousingZhenJiangDetailAccount>();
		HousingZhenJiangDetailAccount housingZhenJiangDetailAccount=null;
		Document doc = Jsoup.parse(html);
		Elements elementsByTag = doc.getElementsByClass("shuju").get(0).getElementsByTag("tr");
		int size = elementsByTag.size();
		if(size>1){
			for(int i=1;i<size;i++){
				Elements tds = elementsByTag.get(i).getElementsByTag("td");
				housingZhenJiangDetailAccount=new HousingZhenJiangDetailAccount();
				housingZhenJiangDetailAccount.setTaskid(taskHousing.getTaskid());
				housingZhenJiangDetailAccount.setAmount(tds.get(5).text().replace(" ", ""));
				housingZhenJiangDetailAccount.setBalance(tds.get(6).text().replace(" ", ""));
				housingZhenJiangDetailAccount.setChargeyearmonth(tds.get(2).text().replace(" ", ""));
				housingZhenJiangDetailAccount.setReachdate(tds.get(1).text().replace(" ", ""));
				housingZhenJiangDetailAccount.setReceivemoney(tds.get(4).text().replace(" ", ""));
				housingZhenJiangDetailAccount.setSummary(tds.get(3).text().replace(" ", ""));
				//备注
				list.add(housingZhenJiangDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
