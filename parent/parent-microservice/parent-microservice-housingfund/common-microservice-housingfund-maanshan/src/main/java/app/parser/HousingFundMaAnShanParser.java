package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.maanshan.HousingMaAnShanDetailAccount;
import com.microservice.dao.entity.crawler.housing.maanshan.HousingMaAnShanUserInfo;

import app.service.common.HousingFundHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * @author: sln 
 */
@Component
public class HousingFundMaAnShanParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundMaAnShanParser.class);
	public HousingMaAnShanUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("data");
		String taskid = taskHousing.getTaskid().trim();
		String accnum = jsob.getString("grzh");
		String name = jsob.getString("xingming");
		String idnum = jsob.getString("zjhm");
		String accstatus = HousingFundHelperService.getAccStatus(jsob.getString("grzhzt"));
		String balance = jsob.getString("grzhye");
		String opendate = jsob.getString("khrq");
		String unitprop = jsob.getString("unitprop");
		String indiprop = jsob.getString("indiprop");
		String indichargebasenum = jsob.getString("grjcjs");
		String monthcharge = jsob.getString("yjce");
		String unitmonthcharge = jsob.getString("dwyjce");
		String indimonthcharge = jsob.getString("gryjce");
		String paytoyear = jsob.getString("jzny");
		String unitaccnum = jsob.getString("dwzh");
		String unitname = jsob.getString("unitaccname");
		String isloan = HousingFundHelperService.getIsloan(jsob.getString("isloanflag"));
		String phonenum = jsob.getString("sjhm");
		String borrownum = jsob.getString("jkhtbh");
		HousingMaAnShanUserInfo housingMaAnShanUserInfo=new HousingMaAnShanUserInfo(taskid, accnum, name, idnum, accstatus, balance, opendate, unitprop, indiprop, indichargebasenum, monthcharge, unitmonthcharge, indimonthcharge, paytoyear, unitaccnum, unitname, isloan, phonenum, borrownum);
		return housingMaAnShanUserInfo;
	}

	public List<HousingMaAnShanDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingMaAnShanDetailAccount> list=new  ArrayList<HousingMaAnShanDetailAccount>();
		HousingMaAnShanDetailAccount housingMaAnShanDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
		int size=jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject jsob =JSONObject.fromObject(jsonArray.get(i));
				housingMaAnShanDetailAccount=new HousingMaAnShanDetailAccount();
				housingMaAnShanDetailAccount.setTaskid(taskHousing.getTaskid());
				housingMaAnShanDetailAccount.setAmount(jsob.getString("amt1"));
				housingMaAnShanDetailAccount.setBusinesstype(jsob.getString("accname2"));
				housingMaAnShanDetailAccount.setEnddate(jsob.getString("enddate"));
				housingMaAnShanDetailAccount.setNote(jsob.getString("reason"));
				housingMaAnShanDetailAccount.setReachstatus(jsob.getString("freeuse2"));
				housingMaAnShanDetailAccount.setStartdate(jsob.getString("begindate"));
				housingMaAnShanDetailAccount.setTransdate(jsob.getString("transdate"));
				housingMaAnShanDetailAccount.setBalance(jsob.getString("amt2"));
				//备注
				list.add(housingMaAnShanDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
