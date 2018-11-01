package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.yangzhou.InsuranceYangZhouMedical;
import com.microservice.dao.entity.crawler.insurance.yangzhou.InsuranceYangZhouUserInfo;

import app.commontracerlog.TracerLog;
import app.service.InsuranceService;
@Component
public class InsuranceYangZhouParser {
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 解析个人信息
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public InsuranceYangZhouUserInfo htmlParserForUserInfo(String html, TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("htmlParserForUserInfo.html","<xmp>"+html+"</xmp>");
		Document doc = Jsoup.parse(html,"UTF-8");
		String username = insuranceService.getNextLabelByKeyword(doc, "姓名");
		String idnum = insuranceService.getNextLabelByKeyword(doc, "身份证号码");
		String sex = insuranceService.getNextLabelByKeyword(doc, "性别");		
		String birthdate = insuranceService.getNextLabelByKeyword(doc, "出生年月");
		String useraccount = insuranceService.getNextLabelByKeyword(doc, "个人代码");
		String companyname = insuranceService.getNextLabelByKeyword(doc, "单位名称");
		InsuranceYangZhouUserInfo userInfo = new InsuranceYangZhouUserInfo(taskInsurance.getTaskid(), username, idnum,
				useraccount, birthdate, sex, companyname);
		return userInfo;
	}
	
	/**
	 * @Des 根据获取的获取缴费详情页面解析具体信息
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public List<InsuranceYangZhouMedical> htmlParserForMedicalList(String html, TaskInsurance taskInsurance)
			throws Exception {
		List<InsuranceYangZhouMedical> medicalList = new ArrayList<InsuranceYangZhouMedical>();
		if (null != html) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element  table=doc.select("table").last();
	     	if (null !=table) {
	     		Elements trs = table.select("tr");
	     		int trs_size = trs.size();
				if (trs_size >0) {
					for (int i = 1; i < trs_size; i++) {					
						Elements tds = trs.get(i).select("td");
						String amount =tds.get(0).text();//金额
					    String paymonth=tds.get(1).text();//所属期
					    String accountday=tds.get(2).text();//入账时间
					    String type=tds.get(3).text();//类型
					    InsuranceYangZhouMedical medical=new InsuranceYangZhouMedical(amount,paymonth,accountday,type,taskInsurance.getTaskid());
						medicalList.add(medical);
					}
				}
			}	
			
		}
		return medicalList;
	}

}
