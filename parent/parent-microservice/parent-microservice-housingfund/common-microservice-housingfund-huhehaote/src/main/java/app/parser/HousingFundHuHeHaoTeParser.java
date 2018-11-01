package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.huhehaote.HousingHuHeHaoTeDetailAccount;
import com.microservice.dao.entity.crawler.housing.huhehaote.HousingHuHeHaoTeUserInfo;

import app.service.common.HousingFundHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundHuHeHaoTeParser {
	public HousingHuHeHaoTeUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		HousingHuHeHaoTeUserInfo housingHuHeHaoTeUserInfo=new HousingHuHeHaoTeUserInfo();
		Document doc = Jsoup.parse(html);
		housingHuHeHaoTeUserInfo.setAccname(doc.getElementById("accname").val());
		housingHuHeHaoTeUserInfo.setAccnum(doc.getElementById("accnum").val());
		housingHuHeHaoTeUserInfo.setBalance(doc.getElementById("balance").val());
		housingHuHeHaoTeUserInfo.setBasenumber(doc.getElementById("basenumber").val());
		housingHuHeHaoTeUserInfo.setBeginpaydate(doc.getElementById("beginpaydate").val());
		housingHuHeHaoTeUserInfo.setCardno(doc.getElementById("cardno").val());
		housingHuHeHaoTeUserInfo.setCertinum(doc.getElementById("certinum").val());
		housingHuHeHaoTeUserInfo.setIndiprop(doc.getElementById("indiprop").val());
		housingHuHeHaoTeUserInfo.setIsassure(doc.getElementById("isassure").val());
		housingHuHeHaoTeUserInfo.setIsloan(doc.getElementById("isloan").val());
		housingHuHeHaoTeUserInfo.setLastpaydate(doc.getElementById("lastpaydate").val());
		housingHuHeHaoTeUserInfo.setMonpaysum(doc.getElementById("monpaysum").val());
		housingHuHeHaoTeUserInfo.setPeraccstate(doc.getElementById("peraccstate").getElementsByAttribute("selected").get(0).text());
		housingHuHeHaoTeUserInfo.setProp(doc.getElementById("prop").val());
		housingHuHeHaoTeUserInfo.setTaskid(taskHousing.getTaskid().trim());
		housingHuHeHaoTeUserInfo.setUnitaccname(doc.getElementById("unitaccname").val());
		housingHuHeHaoTeUserInfo.setUnitaccnum(doc.getElementById("unitaccnum").val());
		housingHuHeHaoTeUserInfo.setUnitprop(doc.getElementById("unitprop").val());
		return housingHuHeHaoTeUserInfo;
	}
	
	public List<HousingHuHeHaoTeDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingHuHeHaoTeDetailAccount> list=new  ArrayList<HousingHuHeHaoTeDetailAccount>();
		HousingHuHeHaoTeDetailAccount housingHuHeHaoTeDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
		int size=jsonArray.size();
		if(size>0){
			JSONObject jsob = null;
			for(int i=0;i<size;i++){
				jsob = JSONObject.fromObject(jsonArray.get(i));
				housingHuHeHaoTeDetailAccount=new HousingHuHeHaoTeDetailAccount();
				housingHuHeHaoTeDetailAccount.setTaskid(taskHousing.getTaskid().trim());
				housingHuHeHaoTeDetailAccount.setBalance(jsob.getString("basenumber"));
				housingHuHeHaoTeDetailAccount.setBegindate(jsob.getString("begindate"));
				housingHuHeHaoTeDetailAccount.setCreditamount(jsob.getString("amt2"));
				housingHuHeHaoTeDetailAccount.setDebitamount(jsob.getString("amt1"));
				housingHuHeHaoTeDetailAccount.setEnddate(jsob.getString("enddate"));
				housingHuHeHaoTeDetailAccount.setTransdate(jsob.getString("transdate"));
				housingHuHeHaoTeDetailAccount.setTranstatus(HousingFundHelperService.getTransStatus(jsob.getString("sex")));   //返回的是个代号
				housingHuHeHaoTeDetailAccount.setUnitaccname(jsob.getString("unitaccname"));
				housingHuHeHaoTeDetailAccount.setUnitaccnum(jsob.getString("unitaccnum1"));
				list.add(housingHuHeHaoTeDetailAccount);
			}
		}else{
			list=null;
		}
		return list;
	}
}
