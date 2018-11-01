package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.lishui.HousingLiShuiDetailAccount;
import com.microservice.dao.entity.crawler.housing.lishui.HousingLiShuiUserInfo;
@Component
public class HousingFundLiShuiParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundLiShuiParser.class);
	public HousingLiShuiUserInfo userInfoParser(String html,TaskHousing taskHousing) {
		Document doc = Jsoup.parse(html);
		String taskid = taskHousing.getTaskid().trim();
		String name = doc.select("td:contains(姓名)+td").first().text();
		String accnum = doc.select("td:contains(公积金帐号)+td").first().text(); 
		String idnum = doc.select("td:contains(身份证号码)+td").first().text();
		String chargeunit = doc.select("td:contains(缴存单位)+td").first().text();
		String accbalance = doc.select("td:contains(帐户余额)+td").first().text();
		String accstatus = doc.select("td:contains(帐户状态)+td").first().text();
		String yearinterest = doc.select("td:contains(本年度利息)+td").first().text();
		HousingLiShuiUserInfo housingLiShuiUserInfo=new HousingLiShuiUserInfo(taskid, name, accnum, idnum, chargeunit, accbalance, accstatus, yearinterest);
		return housingLiShuiUserInfo;
	}

	public List<HousingLiShuiDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingLiShuiDetailAccount> list=new  ArrayList<HousingLiShuiDetailAccount>();
		HousingLiShuiDetailAccount housingLiShuiDetailAccount=null;
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementById("personinfo").getElementsByTag("table").get(2).getElementsByTag("tr");
		int size = trs.size();
		if(size>1){   //因为还有标题行，所以从1开始比较和爬取
			Element eachTr = null;
			for(int i=1;i<size;i++){
				eachTr =trs.get(i);
				housingLiShuiDetailAccount=new HousingLiShuiDetailAccount();
				housingLiShuiDetailAccount.setTaskid(taskHousing.getTaskid());
				housingLiShuiDetailAccount.setChargeamount(eachTr.getElementsByTag("td").get(2).text());
				housingLiShuiDetailAccount.setChargedate(eachTr.getElementsByTag("td").get(1).text());
				housingLiShuiDetailAccount.setChargemonth(eachTr.getElementsByTag("td").get(0).text());
				list.add(housingLiShuiDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
