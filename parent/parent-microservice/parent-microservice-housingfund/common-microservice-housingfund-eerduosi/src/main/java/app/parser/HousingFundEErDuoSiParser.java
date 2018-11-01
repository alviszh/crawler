package app.parser;


import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.eerduosi.HousingEErDuoSiFlowInfo;
import com.microservice.dao.entity.crawler.housing.eerduosi.HousingEErDuoSiUserInfo;

@Component
public class HousingFundEErDuoSiParser {

	public HousingEErDuoSiUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		Document doc = Jsoup.parse(html);
		String taskid = taskHousing.getTaskid().trim();
		String name = doc.getElementById("Label3").text();
		String idnum = doc.select("td:contains(身份证号)+td").first().text();
		String belongmanagedept = doc.select("td:contains(所属管理部)+td").first().text();
		String unitaccnum = doc.select("td:contains(单位账号)+td").first().text();
		String unitname = doc.select("td:contains(单位名称)+td").first().text();
		String peraccnum = doc.select("td:contains(个人帐号)+td").first().text();
		String employeestatus = doc.select("td:contains(职工状态)+td").first().text();
		String employeebalance = doc.select("td:contains(职工余额)+td").first().text();
		String yearextractimes = doc.select("td:contains(年提取次数)+td").first().text();
		String yearextractmoney = doc.select("td:contains(年提取金额)+td").first().text();
		String lastextracttime = doc.select("td:contains(最后一次提取时间)+td").first().text();
		String isloan = doc.select("td:contains(是否存在贷款)+td").first().text();
		String isfreeze = doc.select("td:contains(是否冻结)+td").first().text();
		String isassure = doc.select("td:contains(是否担保)+td").first().text();
		String assuretimes = doc.select("td:contains(担保次数)+td").first().text();
		String assistimes = doc.select("td:contains(辅助次数)+td").first().text();
		String wagebase = doc.select("td:contains(工资基数)+td").first().text();
		String isexistassureunit = doc.select("td:contains(是否存在担保公司)+td").first().text();
		String isspouseloan = doc.select("td:contains(配偶是否存在贷款)+td").first().text();
		String transferunit = doc.select("td:contains(转入单位)+td").first().text();
		String cancelaccountdate = doc.select("td:contains(转出销户日期)+td").first().text();
		String unitpaytoyear = doc.select("td:contains(单位缴至年月)+td").first().text();
		String perpaytoyear = doc.select("td:contains(个人缴至年月)+td").first().text();
		String financepaytoyear = doc.select("td:contains(财政缴至年月)+td").first().text();
		String supplementpaytoyear = doc.select("td:contains(补充缴至年月)+td").first().text();
		String opendate = doc.select("td:contains(开户日期)+td").first().text();
		String unitcharge = doc.select("td:contains(单位缴费)+td").first().text();
		String percharge = doc.select("td:contains(个人缴费)+td").first().text();
		String financecharge = doc.select("td:contains(财政缴费)+td").first().text();
		String supplementcharge = doc.select("td:contains(补充缴费)+td").first().text();
		String monthtotalcharge = doc.select("td:contains(月缴纳总额)+td").first().text();
		HousingEErDuoSiUserInfo HousingEErDuoSiUserInfo=new HousingEErDuoSiUserInfo(taskid, name, idnum, belongmanagedept, unitaccnum, unitname, peraccnum, employeestatus, employeebalance, yearextractimes, yearextractmoney, lastextracttime, isloan, isfreeze, isassure, assuretimes, assistimes, wagebase, isexistassureunit, isspouseloan, transferunit, cancelaccountdate, unitpaytoyear, perpaytoyear, financepaytoyear, supplementpaytoyear, opendate, unitcharge, percharge, financecharge, supplementcharge, monthtotalcharge);
		return HousingEErDuoSiUserInfo;
	}

	public List<HousingEErDuoSiFlowInfo> flowInfoParser(String html, TaskHousing taskHousing) {
		List<HousingEErDuoSiFlowInfo> list=new ArrayList<HousingEErDuoSiFlowInfo>();
		HousingEErDuoSiFlowInfo housingEErDuoSiFlowInfo=null;
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementById("table1").getElementById("table2").getElementsByTag("tr");
		int size = trs.size();
		if(size>0){
			for(int i=0;i<size;i++){
				String taskid = taskHousing.getTaskid().trim();
				String managedept = trs.get(i).getElementsByTag("td").get(3).text();
				String unitname = trs.get(i).getElementsByTag("td").get(4).text();
				String unitaccnum = trs.get(i).getElementsByTag("td").get(5).text();
				String businesstype = trs.get(i).getElementsByTag("td").get(6).text();
				String summary = trs.get(i).getElementsByTag("td").get(7).text();
				String date = trs.get(i).getElementsByTag("td").get(8).text();
				String amount = trs.get(i).getElementsByTag("td").get(9).text();
				String interest = trs.get(i).getElementsByTag("td").get(10).text();
				housingEErDuoSiFlowInfo=new HousingEErDuoSiFlowInfo(taskid, managedept, unitname, unitaccnum, businesstype, summary, date, amount, interest);
				list.add(housingEErDuoSiFlowInfo);
			}
		}else{
			list=null;
		}
		return list;
	}
}
