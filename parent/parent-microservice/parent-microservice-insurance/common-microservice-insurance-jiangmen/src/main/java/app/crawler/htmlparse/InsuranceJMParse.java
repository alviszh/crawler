package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenInjury;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenMaternity;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenMedical;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenPersion;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenUnemployment;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenUserInfo;

public class InsuranceJMParse {
	//个人信息
	public static InsuranceJiangMenUserInfo userinfo_parse(String html){
		InsuranceJiangMenUserInfo userInfo = new InsuranceJiangMenUserInfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.sbjTable > tbody > tr > td[align='left'] > span");
		if(ele.size()>0){
			String insuredNumber = ele.get(0).text().trim();        //人员参保号
			String name = ele.get(1).text().trim();                 //姓名
			String idCard = ele.get(2).text().trim();               //身份证号
			String idNum = ele.get(3).text().trim();                //IC卡号
			String num = ele.get(4).text().trim();                  //单位参保号
			String company = ele.get(5).text().trim();              //单位名称
			String sex = ele.get(6).text().trim();                  //性别
			String birth = ele.get(7).text().trim();                //出生日期
			String addr = ele.get(8).text().trim();                 //通讯地址
			String postalCode = ele.get(9).text().trim();           //邮政编码
			String phone = ele.get(10).text().trim();                //联系电话
			String personnelType = ele.get(11).text().trim();        //医疗人员类别
			String state = ele.get(12).text().trim();                //人员状态
			String type = ele.get(13).text().trim();                 //户口类别
			userInfo.setInsuredNumber(insuredNumber);
			userInfo.setName(name);
			userInfo.setIdCard(idCard);
			userInfo.setIdNum(idNum);
			userInfo.setNum(num);
			userInfo.setCompany(company);
			userInfo.setSex(sex);
			userInfo.setBirth(birth);
			userInfo.setAddr(addr);
			userInfo.setPostalCode(postalCode);
			userInfo.setPhone(phone);
			userInfo.setPersonnelType(personnelType);
			userInfo.setState(state);
			userInfo.setType(type);
			return userInfo;
		}
		return null;
	}
	
	//养老缴费
	public static List<InsuranceJiangMenPersion> persion_parse(String html ,TaskInsurance taskInsurance){
		List<InsuranceJiangMenPersion> list = new ArrayList<InsuranceJiangMenPersion>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.axgview_body1 >tbody >tr");
		if(ele.size()>0){
			for(int i = 1;i<(ele.size()-1);i++){
				String type = ele.get(i).select("td").eq(1).text().trim();              //缴费记录类型
				String bureauName = ele.get(i).select("td").eq(2).text().trim();        //局名
				String num = ele.get(i).select("td").eq(3).text().trim();               //单位参保号
				String company = ele.get(i).select("td").eq(4).text().trim();           //单位名称
				String startMonth = ele.get(i).select("td").eq(5).text().trim();        //开始年月
				String endMonth = ele.get(i).select("td").eq(6).text().trim();          //终止年月
				String monthCount = ele.get(i).select("td").eq(7).text().trim();        //月数
				String unitPay = ele.get(i).select("td").eq(8).text().trim();           //单位缴纳
				String personalPay = ele.get(i).select("td").eq(9).text().trim();       //个人缴纳
				String base = ele.get(i).select("td").eq(10).text().trim();              //缴费工资
				InsuranceJiangMenPersion persion = new InsuranceJiangMenPersion();
				persion.setType(type);
				persion.setBureauName(bureauName);
				persion.setNum(num);
				persion.setCompany(company);
				persion.setStartMonth(startMonth);
				persion.setEndMonth(endMonth);
				persion.setMonthCount(monthCount);
				persion.setUnitPay(unitPay);
				persion.setPersonalPay(personalPay);
				persion.setBase(base);
				persion.setTaskid(taskInsurance.getTaskid());
				list.add(persion);
			}
			return list;
		}
		return null;
	}
	
	//医疗缴费
	public static List<InsuranceJiangMenMedical> medical_parse(String html ,TaskInsurance taskInsurance){
		List<InsuranceJiangMenMedical> list = new ArrayList<InsuranceJiangMenMedical>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.axgview_body1 >tbody >tr");
		if(ele.size()>0){
			for(int i = 1;i<(ele.size()-1);i++){
				String type = ele.get(i).select("td").eq(1).text().trim();              //缴费记录类型
				String bureauName = ele.get(i).select("td").eq(2).text().trim();        //局名
				String num = ele.get(i).select("td").eq(3).text().trim();               //单位参保号
				String company = ele.get(i).select("td").eq(4).text().trim();           //单位名称
				String startMonth = ele.get(i).select("td").eq(5).text().trim();        //开始年月
				String endMonth = ele.get(i).select("td").eq(6).text().trim();          //终止年月
				String monthCount = ele.get(i).select("td").eq(7).text().trim();        //月数
				String unitPay = ele.get(i).select("td").eq(8).text().trim();           //单位缴纳
				String personalPay = ele.get(i).select("td").eq(9).text().trim();       //个人缴纳
				String base = ele.get(i).select("td").eq(10).text().trim();              //缴费工资
				InsuranceJiangMenMedical medical = new InsuranceJiangMenMedical();
				medical.setType(type);
				medical.setBureauName(bureauName);
				medical.setNum(num);
				medical.setCompany(company);
				medical.setStartMonth(startMonth);
				medical.setEndMonth(endMonth);
				medical.setMonthCount(monthCount);
				medical.setUnitPay(unitPay);
				medical.setPersonalPay(personalPay);
				medical.setBase(base);
				medical.setTaskid(taskInsurance.getTaskid());
				list.add(medical);
			}
			return list;
		}
		return null;
	}
	
	//生育缴费
	public static List<InsuranceJiangMenMaternity> maternity_parse(String html,TaskInsurance taskInsurance){
		List<InsuranceJiangMenMaternity> list = new ArrayList<InsuranceJiangMenMaternity>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.axgview_body1 >tbody >tr");
		if(ele.size()>0){
			for(int i = 1;i<(ele.size()-1);i++){
				String type = ele.get(i).select("td").eq(1).text().trim();              //缴费记录类型
				String bureauName = ele.get(i).select("td").eq(2).text().trim();        //局名
				String num = ele.get(i).select("td").eq(3).text().trim();               //单位参保号
				String company = ele.get(i).select("td").eq(4).text().trim();           //单位名称
				String startMonth = ele.get(i).select("td").eq(5).text().trim();        //开始年月
				String endMonth = ele.get(i).select("td").eq(6).text().trim();          //终止年月
				String monthCount = ele.get(i).select("td").eq(7).text().trim();        //月数
				String unitPay = ele.get(i).select("td").eq(8).text().trim();           //单位缴纳
				String personalPay = ele.get(i).select("td").eq(9).text().trim();       //个人缴纳
				String base = ele.get(i).select("td").eq(10).text().trim();              //缴费工资
				InsuranceJiangMenMaternity maternity = new InsuranceJiangMenMaternity();
				maternity.setType(type);
				maternity.setBureauName(bureauName);
				maternity.setNum(num);
				maternity.setCompany(company);
				maternity.setStartMonth(startMonth);
				maternity.setEndMonth(endMonth);
				maternity.setMonthCount(monthCount);
				maternity.setUnitPay(unitPay);
				maternity.setPersonalPay(personalPay);
				maternity.setBase(base);
				maternity.setTaskid(taskInsurance.getTaskid());
				list.add(maternity);
			}
			return list;
		}
		return null;
	}
	
	//工伤缴费
	public static List<InsuranceJiangMenInjury> injury_parse(String html,TaskInsurance taskInsurance){
		List<InsuranceJiangMenInjury> list = new ArrayList<InsuranceJiangMenInjury>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.axgview_body1 >tbody >tr");
		if(ele.size()>0){
			for(int i = 1;i<(ele.size()-1);i++){
				String type = ele.get(i).select("td").eq(1).text().trim();              //缴费记录类型
				String bureauName = ele.get(i).select("td").eq(2).text().trim();        //局名
				String num = ele.get(i).select("td").eq(3).text().trim();               //单位参保号
				String company = ele.get(i).select("td").eq(4).text().trim();           //单位名称
				String startMonth = ele.get(i).select("td").eq(5).text().trim();        //开始年月
				String endMonth = ele.get(i).select("td").eq(6).text().trim();          //终止年月
				String monthCount = ele.get(i).select("td").eq(7).text().trim();        //月数
				String unitPay = ele.get(i).select("td").eq(8).text().trim();           //单位缴纳
				String personalPay = ele.get(i).select("td").eq(9).text().trim();       //个人缴纳
				String base = ele.get(i).select("td").eq(10).text().trim();              //缴费工资
				InsuranceJiangMenInjury injury = new InsuranceJiangMenInjury();
				injury.setType(type);
				injury.setBureauName(bureauName);
				injury.setNum(num);
				injury.setCompany(company);
				injury.setStartMonth(startMonth);
				injury.setEndMonth(endMonth);
				injury.setMonthCount(monthCount);
				injury.setUnitPay(unitPay);
				injury.setPersonalPay(personalPay);
				injury.setBase(base);
				injury.setTaskid(taskInsurance.getTaskid());
				list.add(injury);
			}
			return list;
		}
		return null;
	}
	
	//失业缴费
	public static List<InsuranceJiangMenUnemployment> unemployment_parse(String html,TaskInsurance taskInsurance){
		List<InsuranceJiangMenUnemployment> list = new ArrayList<InsuranceJiangMenUnemployment>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.axgview_body1 >tbody >tr");
		if(ele.size()>0){
			for(int i = 1;i<(ele.size()-1);i++){
				String type = ele.get(i).select("td").eq(1).text().trim();              //缴费记录类型
				String bureauName = ele.get(i).select("td").eq(2).text().trim();        //局名
				String num = ele.get(i).select("td").eq(3).text().trim();               //单位参保号
				String company = ele.get(i).select("td").eq(4).text().trim();           //单位名称
				String startMonth = ele.get(i).select("td").eq(5).text().trim();        //开始年月
				String endMonth = ele.get(i).select("td").eq(6).text().trim();          //终止年月
				String monthCount = ele.get(i).select("td").eq(7).text().trim();        //月数
				String unitPay = ele.get(i).select("td").eq(8).text().trim();           //单位缴纳
				String personalPay = ele.get(i).select("td").eq(9).text().trim();       //个人缴纳
				String base = ele.get(i).select("td").eq(10).text().trim();              //缴费工资
				InsuranceJiangMenUnemployment unemployment = new InsuranceJiangMenUnemployment();
				unemployment.setType(type);
				unemployment.setBureauName(bureauName);
				unemployment.setNum(num);
				unemployment.setCompany(company);
				unemployment.setStartMonth(startMonth);
				unemployment.setEndMonth(endMonth);
				unemployment.setMonthCount(monthCount);
				unemployment.setUnitPay(unitPay);
				unemployment.setPersonalPay(personalPay);
				unemployment.setBase(base);
				unemployment.setTaskid(taskInsurance.getTaskid());
				list.add(unemployment);
			}
			return list;
		}
		return null;
	}
}
