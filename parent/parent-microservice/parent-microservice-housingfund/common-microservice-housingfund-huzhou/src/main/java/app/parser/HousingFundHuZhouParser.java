package app.parser;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.huzhou.HousingHuZhouDetailAccount;
import com.microservice.dao.entity.crawler.housing.huzhou.HousingHuZhouUserInfo;

import app.service.HousingFundHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class HousingFundHuZhouParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundHuZhouParser.class);
	public HousingHuZhouUserInfo userInfoParser(String html,TaskHousing taskHousing) {
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("obj");
		String taskid = taskHousing.getTaskid().trim();
		String name = jsob.getString("grxm");
		String peraccnum = jsob.getString("grzh");
		String idtype = HousingFundHelperService.getIdType(jsob.getString("zjlx")) ;
		String idnum = jsob.getString("zjhm");
		String opendate = jsob.getString("khrq");
		String perstate = HousingFundHelperService.getAccState(jsob.getString("grzhzt"));
		String unitaccnum = jsob.getString("dwzh");
		String unitname = jsob.getString("dwmc");
		String unitprop = jsob.getString("dwjcbl");
		String indiprop = jsob.getString("grjcbl");
		String subprop = jsob.getString("btbl");
		String wagebase = jsob.getString("grjcjs");
		String subbase = jsob.getString("btjs");
		String indimonthcharge = jsob.getString("gryjce");
		String unitmonthcharge = jsob.getString("dwyjce");
		String submonthcharge = jsob.getString("btyj");
//		String totalmonthcharge = jsob.getString("hjyj");
		String totalmonthcharge =(Double.parseDouble(jsob.getString("gryjce"))+Double.parseDouble(jsob.getString("dwyjce")))+"";
		String generalbalance = jsob.getString("grzhye_01");
		String housingfundsubalance = jsob.getString("grzhye_02");
		String housingsubalance = jsob.getString("grzhye_03");
		String totalbalance = jsob.getString("grzhye");
		String arrearsamount = jsob.getString("qjje");
		HousingHuZhouUserInfo housingHuZhouUserInfo=new HousingHuZhouUserInfo(taskid, name, peraccnum, idtype, idnum, opendate, perstate, unitaccnum, unitname, unitprop, indiprop, subprop, wagebase, subbase, indimonthcharge, unitmonthcharge, submonthcharge, totalmonthcharge, generalbalance, housingfundsubalance, housingsubalance, totalbalance, arrearsamount);
		return housingHuZhouUserInfo;
	}

	public List<HousingHuZhouDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingHuZhouDetailAccount> list=new  ArrayList<HousingHuZhouDetailAccount>();
		HousingHuZhouDetailAccount housingHuZhouDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("rows");
		int size = jsonArray.size();
		if(size>0){ 
			JSONObject jsob=null;
			for(int i=0;i<size;i++){
				jsob=jsonArray.getJSONObject(i);
				housingHuZhouDetailAccount=new HousingHuZhouDetailAccount();
				housingHuZhouDetailAccount.setTaskid(taskHousing.getTaskid());
				housingHuZhouDetailAccount.setAccbalance(jsob.getString("zhye"));
				housingHuZhouDetailAccount.setAcctype(HousingFundHelperService.getAccType(jsob.getString("zhlx")));
				housingHuZhouDetailAccount.setAmount(jsob.getString("fse"));
				housingHuZhouDetailAccount.setBorrowingmarks(HousingFundHelperService.getBorrowingMarks(jsob.getString("jdbj")));
				housingHuZhouDetailAccount.setUnitaccnum(jsob.getString("dwzh"));
				housingHuZhouDetailAccount.setChargedate(jsob.getString("jyrq"));
				list.add(housingHuZhouDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}
}
