package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.hengshui.HousingHengShuiDetailAccount;
import com.microservice.dao.entity.crawler.housing.hengshui.HousingHengShuiUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * @description:衡水市公积金信息解析parser
 * @author: sln 
 */
@Component
public class HousingFundHengShuiParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundHengShuiParser.class);
	public HousingHengShuiUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		HousingHengShuiUserInfo housingHengShuiUserInfo=new HousingHengShuiUserInfo();
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("data");
		housingHengShuiUserInfo.setAccnum(jsob.getString("AccNum"));
		housingHengShuiUserInfo.setBalance(jsob.getString("amt1"));
		housingHengShuiUserInfo.setCertinum(jsob.getString("certinum"));
		housingHengShuiUserInfo.setTaskid(taskHousing.getTaskid());
		housingHengShuiUserInfo.setUnitaccnum(jsob.getString("UnitAccNum"));
		housingHengShuiUserInfo.setAccname(jsob.getString("AccName"));
		housingHengShuiUserInfo.setOpendate(jsob.getString("transdate"));
		housingHengShuiUserInfo.setUnitaccname(jsob.getString("UnitAccName"));
		return housingHengShuiUserInfo;
	}

	public List<HousingHengShuiDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingHengShuiDetailAccount> list=new  ArrayList<HousingHengShuiDetailAccount>();
		HousingHengShuiDetailAccount housingHengShuiDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
		int size=jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject fromObject =JSONObject.fromObject(jsonArray.get(i));
				housingHengShuiDetailAccount=new HousingHengShuiDetailAccount();
				housingHengShuiDetailAccount.setTaskid(taskHousing.getTaskid());
				housingHengShuiDetailAccount.setRownum(i+1);
				housingHengShuiDetailAccount.setAmount(fromObject.getString("amt1"));
				housingHengShuiDetailAccount.setBalance(fromObject.getString("basenum"));
				housingHengShuiDetailAccount.setChargeyearmonth(fromObject.getString("transdate"));
				housingHengShuiDetailAccount.setReceivemoney(fromObject.getString("amt2"));
				list.add(housingHengShuiDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
