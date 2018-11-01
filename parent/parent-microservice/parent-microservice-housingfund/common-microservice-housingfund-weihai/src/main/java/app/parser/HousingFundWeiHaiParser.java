package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.weihai.HousingWeiHaiDetailAccount;
import com.microservice.dao.entity.crawler.housing.weihai.HousingWeiHaiUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * @description:威海市市公积金信息解析parser
 * @author: sln 
 */
@Component
public class HousingFundWeiHaiParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundWeiHaiParser.class);
	public HousingWeiHaiUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		HousingWeiHaiUserInfo housingWeiHaiUserInfo=new HousingWeiHaiUserInfo();
		Document doc = Jsoup.parse(html);
		housingWeiHaiUserInfo.setAccnum(doc.getElementById("grzh").val());
		housingWeiHaiUserInfo.setBalance(doc.getElementById("grzhye").val());
		housingWeiHaiUserInfo.setCertinum(doc.getElementById("zjhm").val());
		housingWeiHaiUserInfo.setTaskid(taskHousing.getTaskid());
		housingWeiHaiUserInfo.setUnitaccnum(doc.getElementById("dwzh").val());
		housingWeiHaiUserInfo.setAccname(doc.getElementById("xingming").val());
		housingWeiHaiUserInfo.setOpendate(doc.getElementById("khrq").val());
		housingWeiHaiUserInfo.setUnitaccname(doc.getElementById("dwmc").val());
		housingWeiHaiUserInfo.setIndiprop(doc.getElementById("grjcbl").val());
		housingWeiHaiUserInfo.setBasenumber(doc.getElementById("grjcjs").val());
		return housingWeiHaiUserInfo;
	}

	public List<HousingWeiHaiDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingWeiHaiDetailAccount> list=new  ArrayList<HousingWeiHaiDetailAccount>();
		HousingWeiHaiDetailAccount housingWeiHaiDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
		int size=jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject fromObject =JSONObject.fromObject(jsonArray.get(i));
				housingWeiHaiDetailAccount=new HousingWeiHaiDetailAccount();
				housingWeiHaiDetailAccount.setTaskid(taskHousing.getTaskid());
				housingWeiHaiDetailAccount.setRownum(i+1);
				housingWeiHaiDetailAccount.setAmount(fromObject.getString("amt1"));
				housingWeiHaiDetailAccount.setBalance(fromObject.getString("amt2"));
				housingWeiHaiDetailAccount.setChargeyearmonth(fromObject.getString("birthday"));
				housingWeiHaiDetailAccount.setInterestdate(fromObject.getString("jzny"));
				housingWeiHaiDetailAccount.setNote(fromObject.getString("reason"));
				//备注
				list.add(housingWeiHaiDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
