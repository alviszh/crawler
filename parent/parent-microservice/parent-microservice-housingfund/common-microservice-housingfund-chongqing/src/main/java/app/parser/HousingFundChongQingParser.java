package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingAccountInfo;
import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingPaydetails;
import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingUserInfo;


@Component
public class HousingFundChongQingParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundChongQingParser.class);
	// 解析用户信息
	public HousingChongqingUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		Document doc = Jsoup.parse(html, "utf-8");
		HousingChongqingUserInfo userInfo = new HousingChongqingUserInfo();
		if (null != html && html.contains("listinfo")) {		
			Elements divs = doc.getElementsByAttributeValue("class", "listinfo");
			if (null != divs) {
				Elements tds = divs.select("tbody").select("td");
				String str_img = tds.get(1).toString();
				Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
				Matcher m_src = p_src.matcher(str_img);
				String imgurl = "";
				if (m_src.find()) {
					String str_src = m_src.group(3);
					imgurl = "http://www.cqgjj.cn" + str_src;
				}
				userInfo.setImgurl(imgurl);
				String username = tds.get(3).text();
				String loginName = tds.get(5).text();
				String userType = tds.get(7).text();
				String idnum = tds.get(9).text();
				String email = tds.get(11).text();
				String telephone = tds.get(13).text();
				String zipcode = tds.get(15).text();
				String address = tds.get(17).text();
				String registerTime = tds.get(19).text();
				String lastloginTime = tds.get(21).text();
				String totalNum = tds.get(23).text();
				userInfo.setUsername(username);
				userInfo.setLoginName(loginName);
				userInfo.setUserType(userType);
				userInfo.setIdnum(idnum);
				userInfo.setEmail(email);
				userInfo.setTelephone(telephone);
				userInfo.setZipcode(zipcode);
				userInfo.setAddress(address);
				userInfo.setRegisterTime(registerTime);
				userInfo.setLastloginTime(lastloginTime);
				userInfo.setTotalNum(totalNum);
				userInfo.setTaskid(taskHousing.getTaskid());
			}
		}
		return userInfo;
	}
		
		// 解析用户信息
	public HousingChongqingAccountInfo htmlAccountInfoParser(String html, TaskHousing taskHousing) {
		Document doc = Jsoup.parse(html, "utf-8");
		HousingChongqingAccountInfo accountInfo = new HousingChongqingAccountInfo();
		if (null != html && html.contains("listinfo")) {
		    Elements divs = doc.getElementsByAttributeValue("class", "listinfo");
			if (null != divs) {
				Elements tds = divs.select("tbody").select("td");
				String idnum = tds.get(1).text();
				String openTime = tds.get(3).text();
				String name = tds.get(5).text();
				String companyName = tds.get(7).text();
				String personalPay = tds.get(9).text();
				String companyPay = tds.get(11).text();
				String personAccount = tds.get(13).text();
				String personNum = tds.get(15).text();
				String currentBalance = tds.get(17).text();
				String status = tds.get(19).text();
				accountInfo.setIdnum(idnum);
				accountInfo.setOpenTime(openTime);
				accountInfo.setName(name);
				accountInfo.setCompanyName(companyName);
				accountInfo.setPersonalPay(personalPay);
				accountInfo.setCompanyPay(companyPay);
				accountInfo.setPersonAccount(personAccount);
				accountInfo.setPersonNum(personNum);
				accountInfo.setCurrentBalance(currentBalance);
				accountInfo.setStatus(status);
				accountInfo.setTaskid(taskHousing.getTaskid());
			}
		}
		return accountInfo;
	}
	// 解析缴存明细信息
	public  List<HousingChongqingPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		Document doc = Jsoup.parse(html, "utf-8"); 
		 List<HousingChongqingPaydetails> paydetails=new  ArrayList<HousingChongqingPaydetails>();
		 Elements divs= doc.getElementsByAttributeValue("class","listinfo");	
		 if (null !=divs) {
				Element  table=divs.select("table").get(0);
				if (null !=table) {
					Elements  trs=table.select("tbody").select("tr");
					int trs_size=trs.size();
					if (trs_size>0) {
						for (int i = 0; i < trs.size(); i++) {
							HousingChongqingPaydetails paydetail=new HousingChongqingPaydetails();
							Elements tds=trs.get(i).select("td");
							String paytime=tds.get(0).text();
							String remark=tds.get(1).text();
							String personalPay=tds.get(2).text();
							String companyPay=tds.get(3).text();
							String currentBalance=tds.get(4).text();
							paydetail.setPaytime(paytime);
							paydetail.setRemark(remark);
							paydetail.setPersonalPay(personalPay);
							paydetail.setCompanyPay(companyPay);
							paydetail.setCurrentBalance(currentBalance);
							paydetail.setTaskid(taskHousing.getTaskid());
							paydetails.add(paydetail);
						}
					}
				}
		}
		return paydetails;
	}
}
