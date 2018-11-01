package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xingtai.InsuranceXingTaiInjury;
import com.microservice.dao.entity.crawler.insurance.xingtai.InsuranceXingTaiPension;
import com.microservice.dao.entity.crawler.insurance.xingtai.InsuranceXingTaiUserInfo;

public class InsuranceXTParse {
	//个人信息
	public static InsuranceXingTaiUserInfo userinfo_parse(String html){
		InsuranceXingTaiUserInfo userInfo = new InsuranceXingTaiUserInfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.result > tbody > tr > td > font");
		if(ele.size()>0){
			String name = ele.get(0).text().trim();				                 //姓名
			String accountNum = ele.get(1).text().trim();						//个人编号
			String idNum = ele.get(2).text().trim();							//身份证号
			String sex = ele.get(3).text().trim();								//性别
			String companyNum = ele.get(4).text().trim();                      //单位编号
			String company = ele.get(5).text().trim();                         //单位名称
			userInfo.setName(name);
			userInfo.setAccountNum(accountNum);
			userInfo.setIdNum(idNum);
			userInfo.setSex(sex);
			userInfo.setCompanyNum(companyNum);
			userInfo.setCompany(company);
			return userInfo;
		}
		return null;
	}
	
	//养老缴费
	public static List<InsuranceXingTaiPension> persion_parse(String html,TaskInsurance taskInsurance){
		List<InsuranceXingTaiPension> list = new ArrayList<InsuranceXingTaiPension>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.result > tbody > tr ");
		if(ele.size()>6){
			for(int i = 5;i<ele.size();i++){
				String year = ele.get(i).select("td").eq(0).text().trim();                            //缴费年月
				String paymentType = ele.get(i).select("td").eq(1).text().trim();				        //缴费类别
				String base = ele.get(i).select("td").eq(2).text().trim();                            //缴费月基数
				String sign = ele.get(i).select("td").eq(3).text().trim();                            //缴费标记
				String payMonth = ele.get(i).select("td").eq(4).text().trim();                        //到账日期
				InsuranceXingTaiPension pension=new InsuranceXingTaiPension();
				pension.setYear(year);
				pension.setPaymentType(paymentType);
				pension.setBase(base);
				pension.setSign(sign);
				pension.setPayMonth(payMonth);
				pension.setTaskid(taskInsurance.getTaskid());
				list.add(pension);
			}
			return list;
		}
		return null;
	}
	
	//工伤缴费
	public static List<InsuranceXingTaiInjury> injury_parse(String html,TaskInsurance taskInsurance){
		List<InsuranceXingTaiInjury> list = new ArrayList<InsuranceXingTaiInjury>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.result > tbody > tr ");
		if(ele.size()>6){
			for(int i = 5;i<ele.size();i++){
				String year = ele.get(i).select("td").eq(0).text().trim();                            //缴费年月
				String paymentType = ele.get(i).select("td").eq(1).text().trim();				        //缴费类别
				String base = ele.get(i).select("td").eq(2).text().trim();                            //缴费月基数
				String sign = ele.get(i).select("td").eq(3).text().trim();                            //缴费标记
				String payMonth = ele.get(i).select("td").eq(4).text().trim();                        //到账日期
				InsuranceXingTaiInjury injury=new InsuranceXingTaiInjury();
				injury.setYear(year);
				injury.setPaymentType(paymentType);
				injury.setBase(base);
				injury.setSign(sign);
				injury.setPayMonth(payMonth);
				injury.setTaskid(taskInsurance.getTaskid());
				list.add(injury);
			}
			return list;
		}
		return null;
	}
}
