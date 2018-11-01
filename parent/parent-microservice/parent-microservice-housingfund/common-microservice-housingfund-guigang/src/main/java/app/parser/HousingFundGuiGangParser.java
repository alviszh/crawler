package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.guigang.HousingGuiGangDetailAccount;
import com.microservice.dao.entity.crawler.housing.guigang.HousingGuiGangUserInfo;

import app.service.common.HousingFundHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class HousingFundGuiGangParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundGuiGangParser.class);
	public HousingGuiGangUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("data");
		String taskid = taskHousing.getTaskid().trim();
		String accnum = jsob.getString("grzh");
		String name = jsob.getString("xingming");
		String idnum = jsob.getString("zjhm");
		String accstatus = HousingFundHelperService.getAccStatus(jsob.getString("grzhzt"));
		String balance = jsob.getString("grzhye");
		String opendate = jsob.getString("khrq");
		String unitprop = jsob.getString("unitprop");
		String indiprop = jsob.getString("unitprop");
		String indichargebasenum = jsob.getString("grjcjs");
		String monthcharge = jsob.getString("indipaysum");
		String unitmonthcharge = jsob.getString("dwyjce");
		String indimonthcharge = jsob.getString("gryjce");
		String paytoyear = jsob.getString("jzny");
		String unitaccnum = jsob.getString("dwzh");
		String unitname = jsob.getString("unitaccname");
		String isloan = HousingFundHelperService.getIsLoan(jsob.getString("isloanflag"));
		String iscommonloan = HousingFundHelperService.getIsCommonLoan(jsob.getString("flag"));
		String repaymentsign = jsob.getString("useflag");
		String cardno = jsob.getString("cardno");
		String loancontractnum = jsob.getString("jkhtbh");
		String phonenum = jsob.getString("sjhm");
		String isfreezed = HousingFundHelperService.getIsFreezed(jsob.getString("frzflag"));
		HousingGuiGangUserInfo HousingGuiGangUserInfo=new HousingGuiGangUserInfo(taskid, accnum, name, idnum, accstatus, balance, opendate, unitprop, indiprop, indichargebasenum, monthcharge, unitmonthcharge, indimonthcharge, paytoyear, unitaccnum, unitname, isloan, iscommonloan, repaymentsign, cardno, loancontractnum, phonenum, isfreezed);
		return HousingGuiGangUserInfo;
	}

	public List<HousingGuiGangDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingGuiGangDetailAccount> list=new  ArrayList<HousingGuiGangDetailAccount>();
		HousingGuiGangDetailAccount housingGuiGangDetailAccount=null;
		String totalCount = JSONObject.fromObject(html).getJSONObject("data").getString("totalCount");
		int size = Integer.parseInt(totalCount);
		if(size>0){
			JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
			JSONObject jsob =null;
			for(int i=0;i<size;i++){
				jsob =JSONObject.fromObject(jsonArray.get(i));
				housingGuiGangDetailAccount=new HousingGuiGangDetailAccount();
				housingGuiGangDetailAccount.setTaskid(taskHousing.getTaskid());
				housingGuiGangDetailAccount.setAmount(jsob.getString("amt1"));
				housingGuiGangDetailAccount.setBalance(jsob.getString("amt2"));
				housingGuiGangDetailAccount.setBusinesstype(jsob.getString("xingming2"));
				housingGuiGangDetailAccount.setEnddate(jsob.getString("endym"));
				housingGuiGangDetailAccount.setEntrystatus(jsob.getString("payvounum"));
				housingGuiGangDetailAccount.setOperator(jsob.getString("freeuse1"));
				housingGuiGangDetailAccount.setRemark(jsob.getString("reason"));
				housingGuiGangDetailAccount.setStartdate(jsob.getString("begym"));
				housingGuiGangDetailAccount.setTransdate(jsob.getString("transdate"));
				list.add(housingGuiGangDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
