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
import com.microservice.dao.entity.crawler.housing.liangshan.HousingLiangShanDetailAccount;
import com.microservice.dao.entity.crawler.housing.liangshan.HousingLiangShanUserInfo;
@Component
public class HousingFundLiangShanParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundLiangShanParser.class);
	public HousingLiangShanUserInfo userInfoParser(String html1, String html2, TaskHousing taskHousing) {
		//从html1中获取：单位名称,缴止年月,月缴款合计,借贷关系,贷款余额
		Document doc1 = Jsoup.parse(html1);
		String taskid = taskHousing.getTaskid().trim();
		String unitname = doc1.getElementById("labDWMC").text();
		String paytoyearmonth = doc1.getElementById("labJZNY").text();
		String monthtotalpay = doc1.getElementById("labYJHJ").text();
		String creditrelation = doc1.getElementById("labJDGX").text();
		String loanbalance = doc1.getElementById("labDKYE").text();
		
		//从html2中获取账户信息
		Document doc2 = Jsoup.parse(html2);
		String accnum = doc2.getElementById("ctl00_CP1_GRZH").val();
		String name = doc2.getElementById("ctl00_CP1_XINGMING").val();
		String gender = doc2.getElementById("ctl00_CP1_XINGBIEMC").val();
		String fixphone = doc2.getElementById("ctl00_CP1_GDDHHM").val();
		String phonenum = doc2.getElementById("ctl00_CP1_SJHM").val();
		String idtype = doc2.getElementById("ctl00_CP1_ZJLXMC").val();
		String idnum = doc2.getElementById("ctl00_CP1_ZJHM").val();
		String birthday = doc2.getElementById("ctl00_CP1_CSNY").val();
		String marriage = doc2.getElementById("ctl00_CP1_HYZKMC").val();
		String profession = doc2.getElementById("ctl00_CP1_ZHIYEMC").val();
		String professionalrank =  doc2.getElementById("ctl00_CP1_ZHICHENMC").val();
		String post = doc2.getElementById("ctl00_CP1_ZHIWUMC").val();
		String education = doc2.getElementById("ctl00_CP1_XUELIMC").val();
		String postalcode = doc2.getElementById("ctl00_CP1_YZBM").val();
		String homeaddress = doc2.getElementById("ctl00_CP1_JTZZ").val();
		String homemonthincome = doc2.getElementById("ctl00_CP1_JTYSR").val();
		String chargebasenum = doc2.getElementById("ctl00_CP1_GRJCJS").val();
		String accstatus = doc2.getElementById("ctl00_CP1_GRZHZTMC").val();
		String opendate = doc2.getElementById("ctl00_CP1_KHRQ").val();
		String balance = doc2.getElementById("ctl00_CP1_GRZHYE").val();
		String lastyearbalance = doc2.getElementById("ctl00_CP1_GRZHSNJZYE").val();
		String thisyeardownbalance = doc2.getElementById("ctl00_CP1_GRZHDNGJYE").val();
		String permonthcharge = doc2.getElementById("ctl00_CP1_GRYJCE").val();
		String unitmonthcharge = doc2.getElementById("ctl00_CP1_DWYJCE").val();
		String accountcanceldate = doc2.getElementById("ctl00_CP1_XHRQ").val();
		String accountcancelreason = doc2.getElementById("ctl00_CP1_XHYY").val();
		String persavingaccnum = doc2.getElementById("ctl00_CP1_GRCKZHHM").val();
		String persavingbank = doc2.getElementById("ctl00_CP1_GRCKZHKHYHMC").val();
		String persavingbankcode = doc2.getElementById("ctl00_CP1_GRCKZHKHYHDM").val();
		HousingLiangShanUserInfo housingLiangShanUserInfo=new HousingLiangShanUserInfo(taskid, unitname, paytoyearmonth, monthtotalpay, creditrelation, loanbalance, accnum, name, gender, fixphone, phonenum, idtype, idnum, birthday, marriage, profession, professionalrank, post, education, postalcode, homeaddress, homemonthincome, chargebasenum, accstatus, opendate, balance, lastyearbalance, thisyeardownbalance, permonthcharge, unitmonthcharge, accountcanceldate, accountcancelreason, persavingaccnum, persavingbank, persavingbankcode);
		return housingLiangShanUserInfo;
	}

	public List<HousingLiangShanDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingLiangShanDetailAccount> list=new  ArrayList<HousingLiangShanDetailAccount>();
		HousingLiangShanDetailAccount housingLiangShanDetailAccount=null;
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementById("ctl00_CP1_GridDataGRZHMXCX").getElementsByTag("tbody").get(0).getElementsByTag("tr");
		int size = trs.size();
		if(size>1){   //因为还有标题行，所以从1开始比较和爬取
			Element eachTr = null;
			for(int i=1;i<size;i++){
				eachTr =trs.get(i);
				housingLiangShanDetailAccount=new HousingLiangShanDetailAccount();
				housingLiangShanDetailAccount.setTaskid(taskHousing.getTaskid());
				housingLiangShanDetailAccount.setAccdate(eachTr.getElementsByTag("td").get(0).text());
				housingLiangShanDetailAccount.setBalance(eachTr.getElementsByTag("td").get(5).text());
				housingLiangShanDetailAccount.setBusinesstype(eachTr.getElementsByTag("td").get(1).text());
				housingLiangShanDetailAccount.setCapitalexpense(eachTr.getElementsByTag("td").get(3).text());
				housingLiangShanDetailAccount.setInterestexpense(eachTr.getElementsByTag("td").get(4).text());
				housingLiangShanDetailAccount.setIncome(eachTr.getElementsByTag("td").get(2).text());
				housingLiangShanDetailAccount.setSummary(eachTr.getElementsByTag("td").get(6).text());
				list.add(housingLiangShanDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
