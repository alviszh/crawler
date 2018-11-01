package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jilin.HousingJiLinDetailAccount;
import com.microservice.dao.entity.crawler.housing.jilin.HousingJiLinUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundJiLinParser {
	public HousingJiLinUserInfo userInfoParser(String html, TaskHousing taskHousing, String certinum) {
		HousingJiLinUserInfo housingJiLinUserInfo=new HousingJiLinUserInfo();
		JSONObject jsob = JSONArray.fromObject(html).getJSONObject(0);
		housingJiLinUserInfo.setTaskid(taskHousing.getTaskid());
		housingJiLinUserInfo.setAccname(jsob.getString("accname"));
		housingJiLinUserInfo.setAccnum(jsob.getString("accnum"));
		housingJiLinUserInfo.setBalance(jsob.getString("balance"));
		housingJiLinUserInfo.setBasenumber(jsob.getString("basenumber"));
		housingJiLinUserInfo.setCertinum(certinum);
		housingJiLinUserInfo.setLastpaydate(jsob.getString("lastpaydate"));
		housingJiLinUserInfo.setMonpaysum(jsob.getString("monpaysum"));
		housingJiLinUserInfo.setUnitaccname(jsob.getString("unitaccname"));
		return housingJiLinUserInfo;
	}
	
	public List<HousingJiLinDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingJiLinDetailAccount> list=new  ArrayList<HousingJiLinDetailAccount>();
		HousingJiLinDetailAccount housingJiLinDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
		int size=jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
				housingJiLinDetailAccount=new HousingJiLinDetailAccount();
				housingJiLinDetailAccount.setBalance(jsob.getString("basenumber"));
				housingJiLinDetailAccount.setBegindate(jsob.getString("begindate"));
				housingJiLinDetailAccount.setCreditamount(jsob.getString("amt2"));
				housingJiLinDetailAccount.setDebitamount(jsob.getString("amt1"));
				housingJiLinDetailAccount.setEnddate(jsob.getString("enddate"));
				housingJiLinDetailAccount.setOperator(jsob.getString("oper"));
				housingJiLinDetailAccount.setRownumber((i+1)+"");
				housingJiLinDetailAccount.setTaskid(taskHousing.getTaskid().trim());
				housingJiLinDetailAccount.setTransdate(jsob.getString("transdate"));
				housingJiLinDetailAccount.setUsedatec(jsob.getString("usedatec"));
				list.add(housingJiLinDetailAccount);
			}
		}else{
			list=null;
		}
		return list;
	}
}
