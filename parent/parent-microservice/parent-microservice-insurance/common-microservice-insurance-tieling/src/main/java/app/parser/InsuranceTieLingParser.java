package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.tieling.InsuranceTieLingChargeDetail;
import com.microservice.dao.entity.crawler.insurance.tieling.InsuranceTieLingUserInfo;

/**
 * @description:
 * @author: sln 
 */
@Component
public class InsuranceTieLingParser {
	public static final Logger log = LoggerFactory.getLogger(InsuranceTieLingParser.class);
	//解析用户信息
	public InsuranceTieLingUserInfo userInfoParser(TaskInsurance taskInsurance, String html) {
		InsuranceTieLingUserInfo insuranceTieLingUserInfo=new InsuranceTieLingUserInfo();
		Document doc = Jsoup.parse(html);
		Elements tds =doc.getElementById("tab4").getElementsByClass("grid").get(0).getElementsByTag("tr").get(1).getElementsByTag("td");
		insuranceTieLingUserInfo.setBirthday(tds.get(7).text());
		insuranceTieLingUserInfo.setGender(tds.get(5).text());
		insuranceTieLingUserInfo.setInsurcardno(tds.get(1).text());
		insuranceTieLingUserInfo.setInsurno(tds.get(3).text());
		insuranceTieLingUserInfo.setMedicalretire(tds.get(9).text());
		insuranceTieLingUserInfo.setName(tds.get(4).text());
		insuranceTieLingUserInfo.setNation(tds.get(6).text());
		insuranceTieLingUserInfo.setPersonalnum(tds.get(2).text());
		insuranceTieLingUserInfo.setRetirestate(tds.get(8).text());
		insuranceTieLingUserInfo.setTaskid(taskInsurance.getTaskid());
		return insuranceTieLingUserInfo;
	}
	//解析个人缴费明细信息(返回的字段有很多，但是个人信息中有的此处不再存储)
	public List<InsuranceTieLingChargeDetail> chargeDetailParser(TaskInsurance taskInsurance, String html, String tabId) {
		List<InsuranceTieLingChargeDetail> list=new ArrayList<InsuranceTieLingChargeDetail>();
		InsuranceTieLingChargeDetail insuranceTieLingChargeDetail=null;
		Document doc = Jsoup.parse(html);
		//想要从隐藏域中获取本页个人缴费明细信息的总记录数，但是铁岭市获取的都是1
		Elements elementsByTag =doc.getElementById("tab"+tabId).getElementsByClass("grid").get(0).getElementsByTag("tr");
		for(int i=1;i<=10;i++){
			Elements tds = elementsByTag.get(i).getElementsByTag("td");
//			故决定根据解析的信息是不是""获者&nbsp;,若果是，就跳出当前for循环
//			String noDataFlag=tds.get(2).text(); 
//			if(noDataFlag.equals("&nbsp;")){  //这样判断无效
			//获取的是险种名称，要是有数据:<td class="line_right">机关事业养老保险&nbsp;</td>     长度是9
			//要是没有数据：<td class="line_right">&nbsp;</td>      长度是1
			//故决定根据长度判断
			int noDataLength=tds.get(2).text().length();
//			System.out.println("noDataLength的length是："+noDataLength);
			if(noDataLength==1){   //没有数据
				break;
			}
			insuranceTieLingChargeDetail=new InsuranceTieLingChargeDetail();
			insuranceTieLingChargeDetail.setAccountdate(tds.get(14).text());
			insuranceTieLingChargeDetail.setChargebasenum(tds.get(11).text());
			insuranceTieLingChargeDetail.setChargeflag(tds.get(10).text());
			insuranceTieLingChargeDetail.setChargetype(tds.get(8).text());
			insuranceTieLingChargeDetail.setChargeyearmonth(tds.get(9).text());
			insuranceTieLingChargeDetail.setInsurtype(tds.get(2).text());
			insuranceTieLingChargeDetail.setPercharge(tds.get(12).text());
			insuranceTieLingChargeDetail.setTaskid(taskInsurance.getTaskid());
			insuranceTieLingChargeDetail.setUnitcharge(tds.get(13).text());
			insuranceTieLingChargeDetail.setUnitname(tds.get(7).text());
			insuranceTieLingChargeDetail.setUnitnum(tds.get(6).text());
			list.add(insuranceTieLingChargeDetail);
		}
		return list;
	}
}
