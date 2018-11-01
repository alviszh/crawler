package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.kunming.HousingKunMingDetailAccount;
import com.microservice.dao.entity.crawler.housing.kunming.HousingKunMingUserInfo;

import app.service.common.HousingFundHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class HousingFundKunMingParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundKunMingParser.class);
	public HousingKunMingUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("data");
		String taskid = taskHousing.getTaskid().trim();
		String accnum = jsob.getString("accnum");
		String name = jsob.getString("accname");
		String idnum = jsob.getString("certinum");
		String accstatus = HousingFundHelperService.getAccStatus(jsob.getString("peraccstate"));
		String balance = jsob.getString("balance");
		String lastpaymonth = jsob.getString("lastpaydate");
		String lastextractday = jsob.getString("lastdrawdate");
		String chargebasenum = jsob.getString("basenumber");
		String monthcharge = jsob.getString("monpaysum");
		String indiprop = jsob.getString("indiprop");
		String unitprop = jsob.getString("unitprop");
		String unitaccnum = jsob.getString("unitaccnum");
		String unitname = jsob.getString("unitaccname");
		String organization =HousingFundHelperService.getOrganization(jsob.getString("accinstcode"));
		HousingKunMingUserInfo housingKunMingUserInfo=new HousingKunMingUserInfo(taskid, accnum, name, idnum, accstatus, balance, lastpaymonth, lastextractday, chargebasenum, monthcharge, indiprop, unitprop, unitaccnum, unitname, organization);
		return housingKunMingUserInfo;
	}

	public List<HousingKunMingDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingKunMingDetailAccount> list=new  ArrayList<HousingKunMingDetailAccount>();
		HousingKunMingDetailAccount housingKunMingDetailAccount=null;
		String totalCount = JSONObject.fromObject(html).getJSONObject("data").getString("totalCount");
		int size = Integer.parseInt(totalCount);
		if(size>0){
			JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
			JSONObject jsob =null;
			for(int i=0;i<size;i++){
				jsob =JSONObject.fromObject(jsonArray.get(i));
				housingKunMingDetailAccount=new HousingKunMingDetailAccount();
				housingKunMingDetailAccount.setTaskid(taskHousing.getTaskid());
				housingKunMingDetailAccount.setAgencies(HousingFundHelperService.getOrganization(jsob.getString("instcode")));
				housingKunMingDetailAccount.setBalance(jsob.getString("amt3"));
				housingKunMingDetailAccount.setCreditamount(jsob.getString("amt2"));
				housingKunMingDetailAccount.setDebitamount(jsob.getString("amt1"));
				housingKunMingDetailAccount.setEnddate(jsob.getString("enddate"));
				housingKunMingDetailAccount.setStartdate(jsob.getString("begindate"));
				housingKunMingDetailAccount.setTransdate(jsob.getString("transdate"));
				list.add(housingKunMingDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
