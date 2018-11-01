package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xingtai.HousingXingTaiDetailAccount;
import com.microservice.dao.entity.crawler.housing.xingtai.HousingXingTaiUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * @description:邢台市公积金信息解析parser
 * @author: sln 
 */
@Component
public class HousingFundXingTaiParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundXingTaiParser.class);
	public HousingXingTaiUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		HousingXingTaiUserInfo housingXingTaiUserInfo=new HousingXingTaiUserInfo();
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("data");
		housingXingTaiUserInfo.setAccnum(jsob.getString("AccNum"));
		housingXingTaiUserInfo.setBalance(jsob.getString("amt1"));
		housingXingTaiUserInfo.setCertinum(jsob.getString("certinum"));
		housingXingTaiUserInfo.setTaskid(taskHousing.getTaskid());
		housingXingTaiUserInfo.setUnitaccnum(jsob.getString("UnitAccNum"));
		housingXingTaiUserInfo.setAccname(jsob.getString("AccName"));
		housingXingTaiUserInfo.setOpendate(jsob.getString("transdate"));
		housingXingTaiUserInfo.setUnitaccname(jsob.getString("UnitAccName"));
		return housingXingTaiUserInfo;
	}

	public List<HousingXingTaiDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingXingTaiDetailAccount> list=new  ArrayList<HousingXingTaiDetailAccount>();
		HousingXingTaiDetailAccount housingXingTaiDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
		int size=jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject fromObject =JSONObject.fromObject(jsonArray.get(i));
				housingXingTaiDetailAccount=new HousingXingTaiDetailAccount();
				housingXingTaiDetailAccount.setTaskid(taskHousing.getTaskid());
				housingXingTaiDetailAccount.setRownum(i+1);
				housingXingTaiDetailAccount.setAmount(fromObject.getString("amt1"));
				housingXingTaiDetailAccount.setBalance(fromObject.getString("basenum"));
				housingXingTaiDetailAccount.setChargeyearmonth(fromObject.getString("transdate"));
				housingXingTaiDetailAccount.setReceivemoney(fromObject.getString("amt2"));
				list.add(housingXingTaiDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
