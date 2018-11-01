package app.crawler.htmlparse;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.zhengzhou.HousingZhengZhouPay;


public class HousingZhengZhouParse {
	public static HousingZhengZhouPay userinfo_parse(String html) {

		HousingZhengZhouPay userinfo = new HousingZhengZhouPay();

		Document doc = Jsoup.parse( html);
		Elements ele = doc.select("div.cx p");
		String account = null;       //公积金账户
		String company = null;       //单位信息
		String openAccount = null;   //开户日期
		String name = null;         //缴存人姓名
		String base = null;          //缴存基数
		String amountPaid = null;    //月缴额
		String indDeposit = null;    //个人缴存比例
		String unitDeposit = null;   //单位缴存比例
		String balance = null;       //缴存余额
		String month = null;         //缴至月份
		String state = null;         //缴存状态
		for (int i = 0;i<11;i++){
			String str = ele.get(i).text().trim();
			if(str.contains("公积金账户")){
				account = str.substring(str.indexOf("：")+1);
				System.out.println("公积金账户"+account );
			}
			if(str.contains("单位信息")){
				company = str.substring(str.indexOf("：")+1);
				System.out.println("单位信息"+company );
			}
			if(str.contains("开户日期")){
				openAccount = str.substring(str.indexOf("：")+1);
			}
			if(str.contains("缴存人姓名")){
				name = str.substring(str.indexOf("：")+1);
			}
			if(str.contains("缴存基数")){
				base = str.substring(str.indexOf("：")+1);
			}
			if(str.contains("月缴额")){
				amountPaid = str.substring(str.indexOf("：")+1);
			}
			if(str.contains("个人缴存比例")){
				indDeposit = str.substring(str.indexOf("：")+1);
			}
			if(str.contains("单位缴存比例")){
				unitDeposit = str.substring(str.indexOf("：")+1);
			}
			if(str.contains("缴存余额")){
				balance = str.substring(str.indexOf("：")+1);
			}
			if(str.contains("缴至月份")){
				month = str.substring(str.indexOf("：")+1);
			}
			if(str.contains("缴存状态")){
				state = str.substring(str.indexOf("：")+1);
			}
		}
	
		userinfo.setAccount(account);
		userinfo.setCompany(company);
		userinfo.setOpenAccount(openAccount);
		userinfo.setName(name);
		userinfo.setBase(base);
		userinfo.setAmountPaid(amountPaid);
		userinfo.setIndDeposit(indDeposit);
		userinfo.setUnitDeposit(unitDeposit);
		userinfo.setBalance(balance);
		userinfo.setMonth(month);
		userinfo.setState(state);
		System.out.println(userinfo.toString());

		return userinfo;
	}

}
