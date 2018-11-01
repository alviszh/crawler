package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.daqing.InsuranceDaQingMedical;
import com.microservice.dao.entity.crawler.insurance.daqing.InsuranceDaQingUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.InsuranceService;

/**
 * Created by root on 2017/9/27.
 */
@Component
@SuppressWarnings("all")
public class InsuranceDaQingParser {
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private TracerLog tracer;
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    /**
     * @Des 解析个人信息
     * @param html
     * @return
     */
    public InsuranceDaQingUserInfo userInfoParser(String taskid,String html) {
        Document doc = Jsoup.parse(html);
        InsuranceDaQingUserInfo insuranceDaQingUserInfo = new InsuranceDaQingUserInfo();

        String insurnum = getNextLabelByKeyword(doc, "社会保障卡卡号：");
        String idnum = getNextLabelByKeyword(doc, "身份证号码：");
        String name = getNextLabelByKeyword(doc, "姓名：");
        String gender = getNextLabelByKeyword(doc, "性别：");
        String unitname = getNextLabelByKeyword(doc, "单位名称：");
        String balance = getNextLabelByKeyword(doc, "医疗账户余额：");  //余额中有汉字“元”，此处处理掉
        balance=balance.split("元")[0];
        insuranceDaQingUserInfo.setBalance(balance);
        insuranceDaQingUserInfo.setGender(gender);
        insuranceDaQingUserInfo.setIdnum(idnum);
        insuranceDaQingUserInfo.setInsurnum(insurnum);
        insuranceDaQingUserInfo.setName(name);
        insuranceDaQingUserInfo.setTaskid(taskid);
        insuranceDaQingUserInfo.setUnitname(unitname);
        return insuranceDaQingUserInfo;
    }

    /**
     * 解析医疗信息
     * @param html
     * @return
     */
    public List<InsuranceDaQingMedical> medicalInfoParser(String html) {
		return null;
		
		
		
		//需求中没有要求爬取医疗消费明细，故此处不再爬取
    	/*List<InsuranceDaQingMedical> list=new ArrayList<InsuranceDaQingMedical>();
        Document doc = Jsoup.parse(html);
        Elements trs = doc.getElementById("sbox1").getElementsByTag("tbody").get(0).getElementsByTag("tr");
        int size = trs.size();
        InsuranceDaQingMedical insuranceDaQingMedical = null;
        if(size>1){  //有一行列名
        	Elements tds = null;
        	for(int i=1;i<size;i++){
        		tds=trs.get(i).getElementsByTag("td");
        		insuranceDaQingMedical = new InsuranceDaQingMedical();
        	}
        }else{
        	list=null;
        }
		return list;*/
    }
    public String getNextLabelByKeyword(Document document, String keyword){
        Elements es = document.select("th:contains("+keyword+")");
        if(null != es && es.size()>0){
            Element element = es.first();
            Element nextElement = element.nextElementSibling();
            if(null != nextElement){
                return nextElement.text();
            }
        }
        return null;
    }
}
