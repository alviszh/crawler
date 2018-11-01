package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingAllChargeInfo;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



/**
 * @description:
 * @author: sln 
 * @date: 2017年9月26日 下午6:23:52 
 */
@Component
public class InsuranceNanjingParser {
	public static final Logger log = LoggerFactory.getLogger(InsuranceNanjingParser.class);
	/**
	 * 解析用户信息
	 * @param taskInsurance
	 * @param html 
	 * @return
	 */
	public InsuranceNanjingUserInfo userInfoParser(TaskInsurance taskInsurance, String html) {
		Document doc = Jsoup.parse(html);
		Elements lis = doc.getElementsByClass("first_li").get(0).getElementsByTag("li");
		InsuranceNanjingUserInfo insuranceNanjingUserInfo = new InsuranceNanjingUserInfo();
		insuranceNanjingUserInfo.setIdnum(lis.get(1).getElementsByTag("span").get(0).text());
		insuranceNanjingUserInfo.setLaborcardnum(lis.get(3).getElementsByTag("span").get(0).text());
		insuranceNanjingUserInfo.setName(doc.getElementsByClass("dot_per").get(0).text());
		insuranceNanjingUserInfo.setPerstatus(lis.get(2).getElementsByTag("span").get(0).text());
		insuranceNanjingUserInfo.setTaskid(taskInsurance.getTaskid());
		return insuranceNanjingUserInfo;
	}
	//解析所有的保险信息
	public List<InsuranceNanjingAllChargeInfo> insuranceParser(TaskInsurance taskInsurance, String html,String insurtype) {
		List<InsuranceNanjingAllChargeInfo> list=new ArrayList<InsuranceNanjingAllChargeInfo>();
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("data");
		int size = jsonArray.size();
		if(size>0){
			InsuranceNanjingAllChargeInfo insuranceNanjingAllChargeInfo=null;
			for(int i=0;i<size;i++){
				insuranceNanjingAllChargeInfo=new InsuranceNanjingAllChargeInfo();
				JSONObject jsob = jsonArray.getJSONObject(i);
				insuranceNanjingAllChargeInfo.setChargebasenum(jsob.getString("aae180"));
				insuranceNanjingAllChargeInfo.setChargemonth(jsob.getString("jsq"));
				insuranceNanjingAllChargeInfo.setCompcharge(jsob.getString("dwyj"));
				insuranceNanjingAllChargeInfo.setInsurancetype(insurtype);
				insuranceNanjingAllChargeInfo.setInterest(jsob.getString("lx"));
				insuranceNanjingAllChargeInfo.setOverduefine(jsob.getString("znj"));
				insuranceNanjingAllChargeInfo.setPersonalcharge(jsob.getString("gryj"));
				insuranceNanjingAllChargeInfo.setTaskid(taskInsurance.getTaskid().trim());
				insuranceNanjingAllChargeInfo.setUnitname(jsob.getString("dwmc"));
				insuranceNanjingAllChargeInfo.setUnitnum(jsob.getString("dwbh"));
				list.add(insuranceNanjingAllChargeInfo);
			}
		}else{
			list=null;
		}
		return list;
	}
}
