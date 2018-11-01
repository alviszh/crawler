package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.zhejiang.TelecomZhejiangCallRec;
import com.microservice.dao.entity.crawler.telecom.zhejiang.TelecomZhejiangMsg;
import com.microservice.dao.entity.crawler.telecom.zhejiang.TelecomZhejiangPayfee;

import app.commontracerlog.TracerLog;

@Component
public class TelecomZhejiangParser {
	
	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 判断短信是否发送成功
	 * @param html
	 * @return
	 */
	public boolean isSuccess(String html) {
		Document doc = Jsoup.parse(html);
		Elements es = doc.getElementsByTag("string");
		if(null != es && es.size()>0){
			String text = es.first().text();
			if("成功".equals(text)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @Des 获取星级信息或套餐信息
	 * @param page
	 * @return
	 */
	public String getIntegral(String page) throws Exception{
		
		Gson gson = new Gson();
		String integral = (String) gson.fromJson(page, Map.class).get("result_msg");
		tracer.addTag("cralwer.telecom.zhejiang.crawler.parser.getIntegral", integral);
		return integral;
	}

	/**
	 * @Des 解析缴费信息
	 * @param contentAsString
	 * @param taskMobile
	 * @return
	 */
	public List<TelecomZhejiangPayfee> parserPayRec(String contentAsString, TaskMobile taskMobile) {
		
		List<TelecomZhejiangPayfee> list = new ArrayList<TelecomZhejiangPayfee>();
		Document doc = Jsoup.parse(contentAsString);
		Elements tbodys = doc.getElementsByTag("tbody");
		if(null != tbodys && tbodys.size()>0){
			Element tbody = tbodys.first();
			Elements trs = tbody.select("tr");
			if(null != trs && trs.size()>0){
				for(Element tr : trs){
					String certificateType = tr.child(0).text();
					String feeType = tr.child(1).text();
					String payType = tr.child(2).text();
					String paySite = tr.child(3).text();
					String payTime = tr.child(4).text();
					String payMoney = tr.child(5).text();
					
					TelecomZhejiangPayfee telecomZhejiangPayfee = new TelecomZhejiangPayfee();
					telecomZhejiangPayfee.setCertificateType(certificateType);
					telecomZhejiangPayfee.setFeeType(feeType);
					telecomZhejiangPayfee.setPayMoney(payMoney);
					telecomZhejiangPayfee.setPaySite(paySite);
					telecomZhejiangPayfee.setPayType(payType);
					telecomZhejiangPayfee.setPayTime(payTime);
					telecomZhejiangPayfee.setTaskid(taskMobile.getTaskid());
					list.add(telecomZhejiangPayfee);					
				}
			}
			
		}		
		return list;
	}

	/**
	 * @Des 解析通话记录
	 * @param contentAsString
	 * @param task_id
	 * @return
	 */
	public List<TelecomZhejiangCallRec> parserCall(String contentAsString, String task_id) {
		
		List<TelecomZhejiangCallRec> list = new ArrayList<TelecomZhejiangCallRec>();
		
		Document doc = Jsoup.parse(contentAsString);
		Element div = doc.getElementById("Pzone_details_content_2");
		if(null != div){
			
			Elements trs = div.select("tr");
			if(null != trs && trs.size()>0){
				for(int i=1;i<trs.size();i++){
					if(trs.get(i).childNodeSize()>11){
						if(i==1){
							continue;
						}
						
						String otherNum = trs.get(i).child(1).text();
						String callType = trs.get(i).child(2).text();
						String beginTime = trs.get(i).child(3).text();
						String callDuriation = getParam(trs.get(i).child(4));
						String calledPartyVisitedCity = trs.get(i).child(5).text();
						String callType1 = getParam(trs.get(i).child(6));
						String callFee = getParam(trs.get(i).child(7));
						String remission = getParam(trs.get(i).child(8));
						String totalFee = getParam(trs.get(i).child(9));
						
						TelecomZhejiangCallRec telecomZhejiangCallRec = new TelecomZhejiangCallRec();
						telecomZhejiangCallRec.setBeginTime(beginTime);
						telecomZhejiangCallRec.setCallDuriation(callDuriation);
						telecomZhejiangCallRec.setCalledPartyVisitedCity(calledPartyVisitedCity);
						telecomZhejiangCallRec.setCallFee(callFee);
						telecomZhejiangCallRec.setCallType(callType);
						telecomZhejiangCallRec.setOtherNum(otherNum);
						telecomZhejiangCallRec.setRemission(remission);
						telecomZhejiangCallRec.setTotalFee(totalFee);
						telecomZhejiangCallRec.setTaskid(task_id);
						telecomZhejiangCallRec.setCallType1(callType1);
						list.add(telecomZhejiangCallRec);
						
					}
				}			
			}
		}
		return list;
	}
	
	public String getParam(Element e){
		Element script = e.getElementsByTag("script").first();
		String str = script.toString().substring(script.toString().indexOf("(")+2, script.toString().indexOf(")")-1);
		return str;
		
	}

	/**
	 * @Des 解析短信信息
	 * @param contentAsString
	 * @param task_id
	 * @return
	 */
	public List<TelecomZhejiangMsg> parserMsg(String contentAsString, String task_id) {
		
		List<TelecomZhejiangMsg> list = new ArrayList<TelecomZhejiangMsg>();
		
		Document doc = Jsoup.parse(contentAsString);
		Element div = doc.getElementById("Pzone_details_content_2");
		if(null != div){
			Elements trs = div.select("tr");
			if(null != trs && trs.size()>0){
				for(int i=1;i<trs.size();i++){
					if(trs.get(i).select("td").size()>8){
						if(i==1){
							continue;
						}
						String otherNum = trs.get(i).child(1).text();
						String businessType = trs.get(i).child(2).text();
						String beginTime = trs.get(i).child(3).text();
						String fee = getParam(trs.get(i).child(4));
						String remission = getParam(trs.get(i).child(5));
						String totalFee = getParam(trs.get(i).child(6));
						
						
						TelecomZhejiangMsg telecomZhejiangMsg = new TelecomZhejiangMsg();
						telecomZhejiangMsg.setBeginTime(beginTime);
						telecomZhejiangMsg.setBusinessType(businessType);
						telecomZhejiangMsg.setFee(fee);
						telecomZhejiangMsg.setOtherNum(otherNum);
						telecomZhejiangMsg.setRemission(remission);
						telecomZhejiangMsg.setTaskid(task_id);
						telecomZhejiangMsg.setTotalFee(totalFee);
						list.add(telecomZhejiangMsg);									
					}
				}			
			}		
		}
		return list;
	}
}
