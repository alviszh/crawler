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
import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangChargeDetail;
import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangInsurInfo;
import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@Component
public class InsuranceZhenJiangParser {
	public static final Logger log = LoggerFactory.getLogger(InsuranceZhenJiangParser.class);

	public InsuranceZhenJiangUserInfo userInfoParser(TaskInsurance taskInsurance, String html, String idNum) {
		JSONObject jsob = JSONObject.fromObject(html);
		String taskid = taskInsurance.getTaskid().trim();
		String name = jsob.getString("aac003");
		String gender = jsob.getString("aac004");
		String birthday = jsob.getString("aac006");
		String nationality = jsob.getString("aac163");
		String nation = jsob.getString("aac005");
		String phonenum = jsob.getString("aae005");
		String homeaddress = jsob.getString("aae006");
		String insurcardnum = jsob.getString("aae135");
		String identificationnum = idNum.trim();
		String cardservicedate = jsob.getString("aac007");
		String idnum = idNum;
		InsuranceZhenJiangUserInfo insuranceZhenJiangUserInfo=new InsuranceZhenJiangUserInfo(taskid, name, gender, birthday, nationality, nation, phonenum, homeaddress, insurcardnum, identificationnum, cardservicedate, idnum);
		return insuranceZhenJiangUserInfo;
	}
	public List<InsuranceZhenJiangInsurInfo> insurInfoParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceZhenJiangInsurInfo> list=new ArrayList<>();
		InsuranceZhenJiangInsurInfo insuranceZhenJiangInsurInfo=null;
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementsByClass("grcbxxcx-tab").get(0).getElementsByTag("tr");
		int size = trs.size();
		if(size>1){   //除了标题行
			Elements tds=null;
			for(int i=1;i<size;i++){
				tds = trs.get(i).getElementsByTag("td");
				insuranceZhenJiangInsurInfo=new InsuranceZhenJiangInsurInfo();
				//期初存储到数据库中如下，需要split
//				[个人缴费基数]2940.00	[缴费状态]参保缴费	[个人缴费比例]8.00%	[参保身份]企业一般人员	企业基本养老保险	[单位名称]南京易才镇江分公司（1）	[单位缴费比例]19.00%
				insuranceZhenJiangInsurInfo.setChargebasenum(tds.get(4).text().split("]")[1]);
				insuranceZhenJiangInsurInfo.setChargestate(tds.get(2).text().split("]")[1]);
				insuranceZhenJiangInsurInfo.setIndiprop(tds.get(6).text().split("]")[1]);
				insuranceZhenJiangInsurInfo.setInsuredidentity(tds.get(3).text().split("]")[1]);
				insuranceZhenJiangInsurInfo.setInsurtype(tds.get(0).text());
				insuranceZhenJiangInsurInfo.setTaskid(taskInsurance.getTaskid().trim());
				insuranceZhenJiangInsurInfo.setUnitname(tds.get(1).text().split("]")[1]);
				insuranceZhenJiangInsurInfo.setUnitprop(tds.get(5).text().split("]")[1]);
				list.add(insuranceZhenJiangInsurInfo);
			}
		}else{
			list=null;
		}
		return list;
	}
	public List<InsuranceZhenJiangChargeDetail> chargeDetailParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceZhenJiangChargeDetail> list=new ArrayList<InsuranceZhenJiangChargeDetail>();
		InsuranceZhenJiangChargeDetail insuranceZhenJiangChargeDetail=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("aaData");
		int size=jsonArray.size(); 
		if(size>0){
			JSONObject jsob =null;
			for(int i=0;i<size;i++){
				insuranceZhenJiangChargeDetail=new InsuranceZhenJiangChargeDetail();
				jsob = JSONObject.fromObject(jsonArray.get(i));
				insuranceZhenJiangChargeDetail.setBelongdate(jsob.getString("aae003"));
				insuranceZhenJiangChargeDetail.setChargebasenum(jsob.getString("aae180"));
				insuranceZhenJiangChargeDetail.setInsurtype(jsob.getString("aae140"));
				insuranceZhenJiangChargeDetail.setIsreachacc(jsob.getString("aae078"));
				insuranceZhenJiangChargeDetail.setPercharge(jsob.getString("aae022"));
				insuranceZhenJiangChargeDetail.setReachdate(jsob.getString("aae079"));
				insuranceZhenJiangChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
				insuranceZhenJiangChargeDetail.setUnitcharge(jsob.getString("aae020"));
				insuranceZhenJiangChargeDetail.setUnitname(jsob.getString("aae044"));
				list.add(insuranceZhenJiangChargeDetail);
			}
		}else{
			list=null;
		}
		return list;
	}

	
}
