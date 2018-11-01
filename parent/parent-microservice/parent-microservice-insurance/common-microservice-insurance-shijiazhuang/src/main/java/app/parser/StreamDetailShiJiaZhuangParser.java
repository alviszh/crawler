package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.BasicUserShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamAgedShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamLostWorkShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamMedicalShiJiaZhuang;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @Description: 详细信息的爬取中包括的爬取内容为：个人信息，医疗信息，失业信息，养老信息
 * @author sln
 * @date 2017年8月7日
 */
@Component
public class StreamDetailShiJiaZhuangParser {
	public static final Logger log = LoggerFactory.getLogger(StreamDetailShiJiaZhuangParser.class);
	/**
	 * @Des 根据获取的页面解析用户个人信息
	 * @param html
	 * @return
	 */
	public BasicUserShiJiaZhuang htmlUserInfoParser(String html,TaskInsurance taskInsurance){
		JSONObject jsonObj = JSONObject.fromObject(html);
		String jsonBasic = jsonObj.getJSONObject("body").getJSONObject("dataStores").getJSONObject("sbkxxStore").getJSONObject("rowSet")
				.getJSONArray("primary").getString(0);
		JSONObject jsonObjs = JSONObject.fromObject(jsonBasic);
		String basicuser_name=jsonObjs.getString("FACTNAME");  //姓名
		String basicuser_idnum=jsonObjs.getString("IDCARDNO"); //身份证号		
		String basicuser_social_insur_num=jsonObjs.getString("IDCARDNO"); //社会保障号  （这个网站用的是身份证号）
		String basicuser_nation=jsonObjs.getString("RACENAME");   //民族
		String basicuser_gender=jsonObjs.getString("SEX");  //性别
		String basicuser_card_num=jsonObjs.getString("AAZ500");     //卡号	
		String basicuser_mobile_phone_num=jsonObjs.getString("MOBILE");  //手机号
		String basicuser_fix_phone_num=jsonObjs.getString("TELEPHONE");   //座机号
		String basicuser_live_place=jsonObjs.getString("ADDRESS");   //居住地
		//暂时设置为null,因为返回的json串中没有这个数据或者是对应的位置编号，座机为null,但是有key
		//String basicuser_postalode=jsonObjs.getString();   //  居住地的邮政编码
		BasicUserShiJiaZhuang basicUserShiJiaZhuang=new BasicUserShiJiaZhuang(taskInsurance.getTaskid(), basicuser_name, basicuser_idnum, basicuser_social_insur_num, basicuser_nation, basicuser_gender, basicuser_card_num, basicuser_mobile_phone_num, basicuser_fix_phone_num, basicuser_live_place, null);
		return basicUserShiJiaZhuang;
	}	
	//===============================================================================
	/**
	 * @Des 根据获取的获取医疗保险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	public List<StreamMedicalShiJiaZhuang> htmlMedicalParser(String html,TaskInsurance taskInsurance){
		StreamMedicalShiJiaZhuang streamMedicalShiJiaZhuang=null;
		List<StreamMedicalShiJiaZhuang> medicalList=new ArrayList<StreamMedicalShiJiaZhuang>();
		JSONObject jsonObj = JSONObject.fromObject(html);
		JSONArray jsonArray = jsonObj.getJSONObject("body").getJSONObject("dataStores").getJSONObject("searchStore").getJSONObject("rowSet")
				.getJSONArray("primary");
		for (int i=0;i<jsonArray.size();i++) {
			streamMedicalShiJiaZhuang=new StreamMedicalShiJiaZhuang();
			String jsonInsur = jsonArray.getString(i);			
			JSONObject jsonInsurObjs = JSONObject.fromObject(jsonInsur);
			
			streamMedicalShiJiaZhuang.setTaskid(taskInsurance.getTaskid());
			streamMedicalShiJiaZhuang.setStreammedical_insur_name(jsonInsurObjs.getString("AC43_AAE140")); //  缴费类型（保险名称）
			streamMedicalShiJiaZhuang.setStreammedical_insur_pay_base(jsonInsurObjs.getString("AC43_AAE018"));  //	缴纳基数
			streamMedicalShiJiaZhuang.setStreammedical_insur_pay_per(jsonInsurObjs.getString("AC43_AAE021")); //	个人缴费金额
			streamMedicalShiJiaZhuang.setStreammedical_insur_pay_comp(jsonInsurObjs.getString("AC43_AAE022")); //	单位缴费金额
			streamMedicalShiJiaZhuang.setStreammedical_insur_pay_date(jsonInsurObjs.getString("AC43_AAE003")); //	缴费时间(结算期)
			streamMedicalShiJiaZhuang.setStreammedical_insur_belong_date(jsonInsurObjs.getString("AC43_AAE002")); //	缴费时间(结算期)
			medicalList.add(streamMedicalShiJiaZhuang);
		}
		return medicalList;
	}
	//==============================================================================
	/**
	 * @Des 根据获取的获取失业保险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	public List<StreamLostWorkShiJiaZhuang> htmlStreamLostWorkParser(String html,TaskInsurance taskInsurance){
		StreamLostWorkShiJiaZhuang streamLostWorkShiJiaZhuang=null;
		List<StreamLostWorkShiJiaZhuang> lostWorkList=new ArrayList<StreamLostWorkShiJiaZhuang>();
		JSONObject jsonObj = JSONObject.fromObject(html);
		JSONArray jsonArray = jsonObj.getJSONObject("body").getJSONObject("dataStores").getJSONObject("searchStore").getJSONObject("rowSet")
				.getJSONArray("primary");
		for (int i=0;i<jsonArray.size();i++) {
			streamLostWorkShiJiaZhuang=new StreamLostWorkShiJiaZhuang();
			String jsonInsur = jsonArray.getString(i);
			JSONObject jsonInsurObjs = JSONObject.fromObject(jsonInsur);	
			streamLostWorkShiJiaZhuang.setTaskid(taskInsurance.getTaskid());
			streamLostWorkShiJiaZhuang.setStreamlostwork_insur_name("失业保险"); //  缴费类型（保险名称）
			streamLostWorkShiJiaZhuang.setStreamlostwork_insur_pay_base(jsonInsurObjs.getString("AC43_AAE018"));   //缴费基数
			streamLostWorkShiJiaZhuang.setStreamlostwork_insur_pay_per(jsonInsurObjs.getString("AC43_AAE021")); //	个人缴费金额
			streamLostWorkShiJiaZhuang.setStreamlostwork_insur_pay_comp(jsonInsurObjs.getString("AC43_AAE022")); //	单位缴费金额
			streamLostWorkShiJiaZhuang.setStreamlostwork_insur_pay_date(jsonInsurObjs.getString("AC43_AAE003")); //	缴费时间(结算期)
			streamLostWorkShiJiaZhuang.setStreamlostwork_insur_belong_date(jsonInsurObjs.getString("AC43_AAE002"));  //费款所属期
			lostWorkList.add(streamLostWorkShiJiaZhuang);
		}
		return lostWorkList;
	}
	
	//==============================================================================
	/**
	 * @Des 根据获取的获取养老保险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	public StreamAgedShiJiaZhuang htmlStreamAgedParser(String html,TaskInsurance taskInsurance,int year){
		Document doc=Jsoup.parse(html);
		String aged_insur_name=doc.getElementById("dateTable").getElementsByTag("tr").get(1).getElementsByTag("td").get(0).text(); //  缴费类型（保险名称）
		String aged_insur_pay_base=doc.getElementById("dateTable").getElementsByTag("tr").get(8).getElementsByTag("td").get(1).text();	 //	缴纳基数(当年缴费基数)
		String aged_insur_pay_per=doc.getElementById("dateTable").getElementsByTag("tr").get(13).getElementsByTag("td").get(1).text(); //	个人缴费金额（无单位缴费明细）
		String aged_insur_thisyearmonth=doc.getElementById("dateTable").getElementsByTag("tr").get(10).getElementsByTag("td").get(1).text();  // 当年缴费月数
		String aged_insuer_allyearmonth=doc.getElementById("dateTable").getElementsByTag("tr").get(17).getElementsByTag("td").get(0).text();  //多年累计缴费月数
		String aged_insur_year=year+""; //	统计年份
		StreamAgedShiJiaZhuang streamAgedShiJiaZhuang=new StreamAgedShiJiaZhuang(taskInsurance.getTaskid(), aged_insur_name, aged_insur_pay_base, aged_insur_pay_per, aged_insur_thisyearmonth, aged_insuer_allyearmonth, aged_insur_year);
		return streamAgedShiJiaZhuang;
	}
}
