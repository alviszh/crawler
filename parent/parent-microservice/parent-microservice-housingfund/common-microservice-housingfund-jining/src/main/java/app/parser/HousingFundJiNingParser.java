package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jining.HousingJiNingDetailAccount;
import com.microservice.dao.entity.crawler.housing.jining.HousingJiNingUserInfo;

import app.service.common.HousingFundHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundJiNingParser {
	public HousingJiNingUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("data");
		String taskid = taskHousing.getTaskid().trim();
		String accname = jsob.getString("accname");
		String accnum = jsob.getString("accnum");
		String certinum = jsob.getString("certinum");
		String unitaccnum = jsob.getString("unitaccnum");
		String unitaccname = jsob.getString("unitaccname");
		String opendate = jsob.getString("opnaccdate");
		String balance = jsob.getString("bal");
		String lastpaydate = jsob.getString("lastdrawdate");
		String accountstate = HousingFundHelperService.getAccState(jsob.getString("indiaccstate"));
		String accountorganization = jsob.getString("instname");
		String paybasenum = jsob.getString("basenum");
		String unitprop = jsob.getString("unitprop");
		String indiprop = jsob.getString("indiprop");
		String permonthcharge = jsob.getString("gryjce");
		String isloan = jsob.getString("isloanflag");
		String lastextractdate = jsob.getString("lasttransdate");
		HousingJiNingUserInfo housingJiNingUserInfo=new HousingJiNingUserInfo(taskid, accname, accnum, certinum, unitaccnum, unitaccname, opendate, balance, lastpaydate, accountstate, accountorganization, paybasenum, unitprop, indiprop, permonthcharge, isloan, lastextractdate);
		return housingJiNingUserInfo;
	}
	
	public List<HousingJiNingDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingJiNingDetailAccount> list=new  ArrayList<HousingJiNingDetailAccount>();
		HousingJiNingDetailAccount housingJiNingDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
		int size=jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
				housingJiNingDetailAccount=new HousingJiNingDetailAccount();
				housingJiNingDetailAccount.setTaskid(taskHousing.getTaskid().trim());
				housingJiNingDetailAccount.setAmount(jsob.getString("amt1"));
				housingJiNingDetailAccount.setBalance(jsob.getString("amt2"));
				housingJiNingDetailAccount.setNote(jsob.getString("freeuse1"));
				housingJiNingDetailAccount.setSummary(HousingFundHelperService.getSummary(jsob.getString("reason")));
				housingJiNingDetailAccount.setTransdate(jsob.getString("transdate"));
				housingJiNingDetailAccount.setTreatganization(jsob.getString("accname2"));
				list.add(housingJiNingDetailAccount);
			}
		}else{
			list=null;
		}
		return list;
	}
}
