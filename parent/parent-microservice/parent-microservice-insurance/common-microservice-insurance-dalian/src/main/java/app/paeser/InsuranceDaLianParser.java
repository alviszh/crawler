package app.paeser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDaLianMedical;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDaLianPension;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDaLianUnemployment;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDaLianUserInfo;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDaLiangInjury;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDalianMaternity;
@Component
public class InsuranceDaLianParser {
	//基本信息
	public InsuranceDaLianUserInfo  userInfo(String html){
		InsuranceDaLianUserInfo userInfo = new InsuranceDaLianUserInfo();
		Document doc = Jsoup.parse(html);
		//System.out.println(html);
		Elements element = doc.select("td.searchHeader2 > input.MyTextReadOnly");
		//System.out.println(element.size());
		if (element.size()>0){
			String number = element.get(0).attr("value").trim();
			String name = element.get(1).attr("value").trim();
			String sex = element.get(2).attr("value").trim();			//性别
			String nation = element.get(3).attr("value").trim();		//民族  
			String birthdate = element.get(4).attr("value").trim();		//出生日期
			String idNum = element.get(6).attr("value").trim();			//本人证件号码
			String phone = element.get(8).attr("value").trim();         //手机号码
			userInfo.setNumber(number);
			userInfo.setName(name);
			userInfo.setSex(sex);
			userInfo.setNation(nation);
			userInfo.setBirthdate(birthdate);
			userInfo.setIdNum(idNum);
			userInfo.setPhone(phone);
			return userInfo;
		}
		return null;
	}
	
	//养老
	public List<InsuranceDaLianPension> parser(String html,String taskid){
		List<InsuranceDaLianPension> list = new ArrayList<InsuranceDaLianPension>();
		Document doc = Jsoup.parse(html);
		Elements element = doc.select("#ec_table_body > tr");
		if (element.size()>0){
			for(int k = 0;k<element.size();k++){
				String unitName = element.get(k).select("td").eq(0).text().trim();                    //单位名称
				String year = element.get(k).select("td").eq(1).text().trim();                        //年度
				String beginMonth = element.get(k).select("td").eq(2).text().trim();                  //起始月份
				String terMonth = element.get(k).select("td").eq(3).text().trim();                    //终止月份
				String base = element.get(k).select("td").eq(4).text().trim();                        //缴费基数
				String unitRatio = element.get(k).select("td").eq(5).text().trim();                   //缴费比例单位
				String personalRatio = element.get(k).select("td").eq(6).text().trim();               //缴费比例个人
				String personalAmount = element.get(k).select("td").eq(7).text().trim();              //缴费金额个人
				String type = element.get(k).select("td").eq(8).text().trim();                        //缴费类型
				String sign = element.get(k).select("td").eq(9).text().trim();                        //缴费标记
				String mode = element.get(k).select("td").eq(10).text().trim();                        //缴费方式
				String payMonth = element.get(k).select("td").eq(11).text().trim(); 				  //缴费时间
				InsuranceDaLianPension pension = new InsuranceDaLianPension();
				pension.setUnitName(unitName);
				pension.setYear(year);
				pension.setBeginMonth(beginMonth);
				pension.setTerMonth(terMonth);
				pension.setBase(base);
				pension.setUnitRatio(unitRatio);
				pension.setPersonalRatio(personalRatio);
				pension.setPersonalAmount(personalAmount);
				pension.setType(type);
				pension.setSign(sign);
				pension.setMode(mode);
				pension.setPayMonth(payMonth);
				pension.setTaskid(taskid);
				list.add(pension);
			}
			return list;
		}
		return null;
		
	}
	
	//失业
	public List<InsuranceDaLianUnemployment> unemployment(String html,String taskid){
		List<InsuranceDaLianUnemployment> list = new ArrayList<InsuranceDaLianUnemployment>();
		Document doc = Jsoup.parse(html);
		Elements element = doc.select("#ec_table_body > tr");
		if (element.size()>0){
			for(int k = 0;k<element.size();k++){
				String unitName = element.get(k).select("td").eq(0).text().trim();                    //单位名称
				String year = element.get(k).select("td").eq(1).text().trim();                        //年度
				String beginMonth = element.get(k).select("td").eq(2).text().trim();                  //缴费开始月份
				String terMonth = element.get(k).select("td").eq(3).text().trim();                    //缴费结止月份
				String personalbase = element.get(k).select("td").eq(4).text().trim();                //个人缴费基数
				String unitbase = element.get(k).select("td").eq(5).text().trim();                    //单位缴费基数
				String personalRatio = element.get(k).select("td").eq(6).text().trim();               //个人缴费比例
				String unitRatio = element.get(k).select("td").eq(7).text().trim();                   //单位缴费比例
				String personalAmount = element.get(k).select("td").eq(8).text().trim();              //个人缴费金额
				String unitAmount = element.get(k).select("td").eq(9).text().trim();                  //单位缴费金额
				String totalAmount = element.get(k).select("td").eq(10).text().trim();                 //缴费总金额
				String numOfMonths = element.get(k).select("td").eq(11).text().trim();                 //缴费月数累计
				InsuranceDaLianUnemployment unemployment = new InsuranceDaLianUnemployment();
				unemployment.setUnitName(unitName);
				unemployment.setYear(year);
				unemployment.setBeginMonth(beginMonth);
				unemployment.setTerMonth(terMonth);
				unemployment.setPersonalbase(personalbase);
				unemployment.setUnitbase(unitbase);
				unemployment.setPersonalRatio(personalRatio);
				unemployment.setUnitRatio(unitRatio);
				unemployment.setPersonalAmount(personalAmount);
				unemployment.setUnitAmount(unitAmount);
				unemployment.setTotalAmount(totalAmount);
				unemployment.setNumOfMonths(numOfMonths);
				unemployment.setTaskid(taskid);
				list.add(unemployment);
			}
			return list;
		}
		return null;
		
	}
	
	//医疗
	public List<InsuranceDaLianMedical> medical(String html,String taskid){
		List<InsuranceDaLianMedical> list = new ArrayList<InsuranceDaLianMedical>();
		Document doc = Jsoup.parse(html);
		Elements element = doc.select("#ec_table_body > tr");
		if (element.size()>0){
			for(int k = 0;k<element.size();k++){
				String unitName = element.get(k).select("td").eq(0).text().trim();                    //单位名称
				String year = element.get(k).select("td").eq(1).text().trim();                        //年度
				String beginMonth = element.get(k).select("td").eq(2).text().trim();                  //起始月份
				String terMonth = element.get(k).select("td").eq(3).text().trim();                    //终止月份
				String base = element.get(k).select("td").eq(4).text().trim();                        //缴费基数
				String unitRatio = element.get(k).select("td").eq(5).text().trim();                   //缴费比例单位
				String personalRatio = element.get(k).select("td").eq(6).text().trim();               //缴费比例个人
				String personalAmount = element.get(k).select("td").eq(7).text().trim();              //缴费金额个人
				String unitAmount = element.get(k).select("td").eq(8).text().trim();                  //缴费金额单位
				String type = element.get(k).select("td").eq(9).text().trim();                        //缴费类型
				String sign = element.get(k).select("td").eq(10).text().trim();                       //缴费标记
				String mode = element.get(k).select("td").eq(11).text().trim();                        //缴费方式
				String payMonth = element.get(k).select("td").eq(12).text().trim();					//缴费时间
				InsuranceDaLianMedical medical = new InsuranceDaLianMedical();
				medical.setUnitName(unitName);
				medical.setYear(year);
				medical.setBeginMonth(beginMonth);
				medical.setTerMonth(terMonth);
				medical.setBase(base);
				medical.setUnitRatio(unitRatio);
				medical.setPersonalRatio(personalRatio);
				medical.setPersonalAmount(personalAmount);
				medical.setUnitAmount(unitAmount);
				medical.setType(type);
				medical.setSign(sign);
				medical.setMode(mode);
				medical.setPayMonth(payMonth);
				medical.setTaskid(taskid);
				list.add(medical);
			}
			return list;
		}
		return null;
		
	}
	
	//工伤
	public List<InsuranceDaLiangInjury> injury(String html,String taskid){
		List<InsuranceDaLiangInjury> list = new ArrayList<InsuranceDaLiangInjury>();
		Document doc = Jsoup.parse(html);
		Elements element = doc.select("#ec_table_body > tr");
		if (element.size()>0){
			for(int k = 0;k<element.size();k++){
				String unitName = element.get(k).select("td").eq(0).text().trim();                    //单位名称
				String year = element.get(k).select("td").eq(1).text().trim();                        //年度
				String beginMonth = element.get(k).select("td").eq(2).text().trim();                  //起始月份
				String terMonth = element.get(k).select("td").eq(3).text().trim();                    //终止月份
				String base = element.get(k).select("td").eq(4).text().trim();                        //缴费基数
				String unitRatio = element.get(k).select("td").eq(5).text().trim();                   //缴费比例单位
				String personalRatio = element.get(k).select("td").eq(6).text().trim();               //缴费比例个人
				String personalAmount = element.get(k).select("td").eq(7).text().trim();              //缴费金额个人
				String unitAmount = element.get(k).select("td").eq(8).text().trim();                  //缴费金额单位
				String type = element.get(k).select("td").eq(9).text().trim();                        //缴费类型
				String sign = element.get(k).select("td").eq(10).text().trim();                       //缴费标记
				String mode = element.get(k).select("td").eq(11).text().trim();                        //缴费方式
				String payMonth = element.get(k).select("td").eq(12).text().trim();					//缴费时间
				InsuranceDaLiangInjury injury = new InsuranceDaLiangInjury();
				injury.setUnitName(unitName);
				injury.setYear(year);
				injury.setBeginMonth(beginMonth);
				injury.setTerMonth(terMonth);
				injury.setBase(base);
				injury.setUnitRatio(unitRatio);
				injury.setPersonalRatio(personalRatio);
				injury.setPersonalAmount(personalAmount);
				injury.setUnitAmount(unitAmount);
				injury.setType(type);
				injury.setSign(sign);
				injury.setMode(mode);
				injury.setPayMonth(payMonth);
				injury.setTaskid(taskid);
				list.add(injury);
			}
			return list;
		}
		return null;
		
	}	
	
	//生育
	public List<InsuranceDalianMaternity> maternity(String html,String taskid){
		List<InsuranceDalianMaternity> list = new ArrayList<InsuranceDalianMaternity>();
		Document doc = Jsoup.parse(html);
		Elements element = doc.select("#ec_table_body > tr");
		if (element.size()>0){
			for(int k = 0;k<element.size();k++){
				String unitName = element.get(k).select("td").eq(0).text().trim();                    //单位名称
				String year = element.get(k).select("td").eq(1).text().trim();                        //年度
				String beginMonth = element.get(k).select("td").eq(2).text().trim();                  //起始月份
				String terMonth = element.get(k).select("td").eq(3).text().trim();                    //终止月份
				String base = element.get(k).select("td").eq(4).text().trim();                        //缴费基数
				String unitRatio = element.get(k).select("td").eq(5).text().trim();                   //缴费比例单位
				String personalRatio = element.get(k).select("td").eq(6).text().trim();               //缴费比例个人
				String personalAmount = element.get(k).select("td").eq(7).text().trim();              //缴费金额个人
				String unitAmount = element.get(k).select("td").eq(8).text().trim();                  //缴费金额单位
				String type = element.get(k).select("td").eq(9).text().trim();                        //缴费类型
				String sign = element.get(k).select("td").eq(10).text().trim();                       //缴费标记
				String mode = element.get(k).select("td").eq(11).text().trim();                        //缴费方式
				String payMonth = element.get(k).select("td").eq(12).text().trim();					//缴费时间
				InsuranceDalianMaternity maternity = new InsuranceDalianMaternity();
				maternity.setUnitName(unitName);
				maternity.setYear(year);
				maternity.setBeginMonth(beginMonth);
				maternity.setTerMonth(terMonth);
				maternity.setBase(base);
				maternity.setUnitRatio(unitRatio);
				maternity.setPersonalRatio(personalRatio);
				maternity.setPersonalAmount(personalAmount);
				maternity.setUnitAmount(unitAmount);
				maternity.setType(type);
				maternity.setSign(sign);
				maternity.setMode(mode);
				maternity.setPayMonth(payMonth);
				maternity.setTaskid(taskid);
				list.add(maternity);
			}
			return list;
		}
		return null;
		
	}	
}
