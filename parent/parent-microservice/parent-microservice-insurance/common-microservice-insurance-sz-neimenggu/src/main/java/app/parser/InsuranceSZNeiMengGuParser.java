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
import com.microservice.dao.entity.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuChargeDetail;
import com.microservice.dao.entity.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuInsurInfo;
import com.microservice.dao.entity.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuUserInfo;

import app.service.InsuranceSZNeiMengGuHelpService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceSZNeiMengGuParser {
	public static final Logger log = LoggerFactory.getLogger(InsuranceSZNeiMengGuParser.class);
	//个人基本信息解析
	public List<InsuranceSZNeiMengGuUserInfo> userInfoParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceSZNeiMengGuUserInfo> list=new ArrayList<InsuranceSZNeiMengGuUserInfo>();
		InsuranceSZNeiMengGuUserInfo insuranceSZNeiMengGuUserInfo=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("body")
				.getJSONObject("dataStores").getJSONObject("").getJSONObject("rowSet")  //返回的json串中有个地方key值是""，用这个方式取出没报错，my god！
				.getJSONArray("primary");
		int size = jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject jsob = jsonArray.getJSONObject(i);
				insuranceSZNeiMengGuUserInfo=new InsuranceSZNeiMengGuUserInfo();
				insuranceSZNeiMengGuUserInfo.setTaskid(taskInsurance.getTaskid());
				insuranceSZNeiMengGuUserInfo.setContactnum(jsob.getString("CCWEB_AC01_AAE005"));
				insuranceSZNeiMengGuUserInfo.setDetailaddr(jsob.getString("CCWEB_AC01_AAE006"));
				insuranceSZNeiMengGuUserInfo.setEmail((jsob.getString("CCWEB_AC01_AAE159")));
				insuranceSZNeiMengGuUserInfo.setGender(InsuranceSZNeiMengGuHelpService.getGender(jsob.getString("CCWEB_AC01_AAC004")));
				insuranceSZNeiMengGuUserInfo.setIdnum(jsob.getString("CCWEB_AC01_AAE135"));
				insuranceSZNeiMengGuUserInfo.setIdvalidate(InsuranceSZNeiMengGuHelpService.timeStampToDate(jsob.getString("CCWEB_AC01_AAC059")));
				insuranceSZNeiMengGuUserInfo.setMobilephone(jsob.getString("CCWEB_AC01_BAE017"));
				insuranceSZNeiMengGuUserInfo.setMunicipality(InsuranceSZNeiMengGuHelpService.getMunicipality(jsob.getString("CCWEB_AC01_AAE013")));
				insuranceSZNeiMengGuUserInfo.setName(jsob.getString("CCWEB_AC01_AAC003"));
				insuranceSZNeiMengGuUserInfo.setNation(InsuranceSZNeiMengGuHelpService.getNation(jsob.getString("CCWEB_AC01_AAC005")));
				insuranceSZNeiMengGuUserInfo.setNationality(InsuranceSZNeiMengGuHelpService.getNationality(jsob.getString("CCWEB_AC01_AAC163")));
				insuranceSZNeiMengGuUserInfo.setPostalcode(jsob.getString("CCWEB_AC01_AAE007"));
				insuranceSZNeiMengGuUserInfo.setUnioncity(InsuranceSZNeiMengGuHelpService.getUnioncity(jsob.getString("CCWEB_AC01_AAE014")));
				insuranceSZNeiMengGuUserInfo.setWorkphone(jsob.getString("CCWEB_AC01_AAE009"));
				list.add(insuranceSZNeiMengGuUserInfo);
			}
		}
		return list;
	}
	//参保信息解析
	public List<InsuranceSZNeiMengGuInsurInfo> insurInfoParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceSZNeiMengGuInsurInfo> list=new ArrayList<InsuranceSZNeiMengGuInsurInfo>();
		InsuranceSZNeiMengGuInsurInfo insuranceSZNeiMengGuInsurInfo=null;
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementById("_grid").getElementsByTag("tr");
		int size = trs.size();
		if(size>1){
			for(int i=1;i<size;i++){   //不要标题行
				insuranceSZNeiMengGuInsurInfo=new InsuranceSZNeiMengGuInsurInfo();
				insuranceSZNeiMengGuInsurInfo.setFirstinsurdate(trs.get(i).getElementsByTag("td").get(2).text());
				insuranceSZNeiMengGuInsurInfo.setInsuraddr(trs.get(i).getElementsByTag("td").get(5).text());
				insuranceSZNeiMengGuInsurInfo.setInsurstatus(trs.get(i).getElementsByTag("td").get(1).text());
				insuranceSZNeiMengGuInsurInfo.setInsurtype(trs.get(i).getElementsByTag("td").get(0).text());
				insuranceSZNeiMengGuInsurInfo.setInsurunit(trs.get(i).getElementsByTag("td").get(4).text());
				insuranceSZNeiMengGuInsurInfo.setTaskid(taskInsurance.getTaskid().trim());
				insuranceSZNeiMengGuInsurInfo.setThisinsurdate(trs.get(i).getElementsByTag("td").get(3).text());
				list.add(insuranceSZNeiMengGuInsurInfo);
			}
		}else{
			list=null;
		}
		return list;
	}
	//缴费信息解析
	public List<InsuranceSZNeiMengGuChargeDetail> chargeDetailParser(TaskInsurance taskInsurance, String html,String citycode, String insurCityName) {
		List<InsuranceSZNeiMengGuChargeDetail> list=new ArrayList<InsuranceSZNeiMengGuChargeDetail>();
		InsuranceSZNeiMengGuChargeDetail insuranceSZNeiMengGuChargeDetail=null;
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementById("_grid").getElementsByTag("tr");
		int size = trs.size();
		if(size>1){
			for(int i=1;i<size;i++){   //不要标题行
				insuranceSZNeiMengGuChargeDetail=new InsuranceSZNeiMengGuChargeDetail();
				insuranceSZNeiMengGuChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
				insuranceSZNeiMengGuChargeDetail.setAccountallocation(trs.get(i).getElementsByTag("td").get(6).text());
				insuranceSZNeiMengGuChargeDetail.setAccountdate(trs.get(i).getElementsByTag("td").get(0).text());
				insuranceSZNeiMengGuChargeDetail.setBelongdate(trs.get(i).getElementsByTag("td").get(1).text());
				insuranceSZNeiMengGuChargeDetail.setChargebasenum(trs.get(i).getElementsByTag("td").get(3).text());
				insuranceSZNeiMengGuChargeDetail.setCitycode(citycode);
				insuranceSZNeiMengGuChargeDetail.setInsurplace(insurCityName);
				insuranceSZNeiMengGuChargeDetail.setInsurtype(trs.get(i).getElementsByTag("td").get(2).text());
				insuranceSZNeiMengGuChargeDetail.setPercharge(trs.get(i).getElementsByTag("td").get(5).text());
				insuranceSZNeiMengGuChargeDetail.setUnitcharge(trs.get(i).getElementsByTag("td").get(4).text());
				list.add(insuranceSZNeiMengGuChargeDetail);
			}
		}else{
			list=null;
		}
		return list;
	}
}
