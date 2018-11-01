package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamGeneralShiJiaZhuang;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class StreamGeneralShiJiaZhuangParser {
	public static final Logger log = LoggerFactory.getLogger(StreamGeneralShiJiaZhuangParser.class);

	public List<StreamGeneralShiJiaZhuang> htmlGeneralParser(String html,TaskInsurance taskInsurance){
		StreamGeneralShiJiaZhuang streamGeneralShiJiaZhuang=null;
		List<StreamGeneralShiJiaZhuang> infoList=new ArrayList<StreamGeneralShiJiaZhuang>();
		JSONObject jsonObj = JSONObject.fromObject(html);
		JSONArray jsonArray = jsonObj.getJSONObject("body").getJSONObject("dataStores").getJSONObject("xzStore").getJSONObject("rowSet")
				.getJSONArray("primary");
		for(int i=0;i<jsonArray.size();i++){
			streamGeneralShiJiaZhuang=new StreamGeneralShiJiaZhuang();
			String jsonInsur =jsonArray.getString(i);
			JSONObject jsonInsurObjs = JSONObject.fromObject(jsonInsur);
			streamGeneralShiJiaZhuang.setTaskid(taskInsurance.getTaskid());
			streamGeneralShiJiaZhuang.setStreamgeneral_company_name(jsonInsurObjs.getString("AAB004"));
			streamGeneralShiJiaZhuang.setStreamgeneral_company_num(jsonInsurObjs.getString("WORK"));
			streamGeneralShiJiaZhuang.setStreamgeneral_insur_start_date(jsonInsurObjs.getString("AAC049"));
			streamGeneralShiJiaZhuang.setStreamgeneral_insur_status(jsonInsurObjs.getString("AAC008"));
			streamGeneralShiJiaZhuang.setStreamgeneral_insur_type(jsonInsurObjs.getString("AAE140_S"));
			streamGeneralShiJiaZhuang.setStreamgeneral_per_status(jsonInsurObjs.getString("AAC084"));
			streamGeneralShiJiaZhuang.setStreamgeneral_insur＿type_num(jsonInsurObjs.getString("AAC001"));
			infoList.add(streamGeneralShiJiaZhuang);
		}
		return infoList;		
	}
}
