package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.shijiazhuang.HousingShiJiaZhuangDetailAccount;
import com.microservice.dao.entity.crawler.housing.shijiazhuang.HousingShiJiaZhuangUserInfo;

import app.service.common.HousingFundHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * @description:石家庄公积金信息解析parser
 * @author: sln 
 */
@Component
public class HousingFundShiJiaZhuangParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundShiJiaZhuangParser.class);
	public HousingShiJiaZhuangUserInfo userInfoParser(String html, String accName, String unitAccName, TaskHousing taskHousing) {
		HousingShiJiaZhuangUserInfo housingShiJiaZhuangUserInfo=new HousingShiJiaZhuangUserInfo();
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("data");
		housingShiJiaZhuangUserInfo.setAccname(accName);
		housingShiJiaZhuangUserInfo.setAccnum(jsob.getString("AccNum"));
		housingShiJiaZhuangUserInfo.setBalance(jsob.getString("amt1"));
		housingShiJiaZhuangUserInfo.setCertinum(jsob.getString("CertiNum"));
		housingShiJiaZhuangUserInfo.setChargebasenum(jsob.getString("prin"));
		housingShiJiaZhuangUserInfo.setChargemonth(jsob.getString("intamt"));
		housingShiJiaZhuangUserInfo.setChargetodate(jsob.getString("accname"));
		housingShiJiaZhuangUserInfo.setOpenaccountdate(jsob.getString("transdate"));
		housingShiJiaZhuangUserInfo.setStatuscode(jsob.getString("indiaccstate"));
		housingShiJiaZhuangUserInfo.setStatus(HousingFundHelperService.getStatus(jsob.getString("indiaccstate")));
		housingShiJiaZhuangUserInfo.setTaskid(taskHousing.getTaskid());
		housingShiJiaZhuangUserInfo.setUnitaccname(unitAccName);
		housingShiJiaZhuangUserInfo.setUnitaccnum(jsob.getString("UnitAccNum"));
		return housingShiJiaZhuangUserInfo;
	}

	public List<HousingShiJiaZhuangDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingShiJiaZhuangDetailAccount> list=new  ArrayList<HousingShiJiaZhuangDetailAccount>();
		HousingShiJiaZhuangDetailAccount housingShiJiaZhuangDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
		int size=jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject fromObject =JSONObject.fromObject(jsonArray.get(i));
				housingShiJiaZhuangDetailAccount=new HousingShiJiaZhuangDetailAccount();
				housingShiJiaZhuangDetailAccount.setTaskid(taskHousing.getTaskid());
				housingShiJiaZhuangDetailAccount.setAmount(fromObject.getString("amt1"));
				housingShiJiaZhuangDetailAccount.setBalance(fromObject.getString("basenum"));
				housingShiJiaZhuangDetailAccount.setChargeyearmonth(fromObject.getString("transdate"));
				housingShiJiaZhuangDetailAccount.setReceivemoney(fromObject.getString("amt2"));
				housingShiJiaZhuangDetailAccount.setRownum(i+1);
				//摘要(采集的是代码)
				housingShiJiaZhuangDetailAccount.setSummary(fromObject.getString("certinum"));
				list.add(housingShiJiaZhuangDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
