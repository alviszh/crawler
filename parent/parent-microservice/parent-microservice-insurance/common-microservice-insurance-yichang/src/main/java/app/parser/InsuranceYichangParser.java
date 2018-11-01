package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.yichang.InsuranceYichangGeneral;
import com.microservice.dao.entity.crawler.insurance.yichang.InsuranceYichangUserInfo;

import app.commontracerlog.TracerLog;

@Component
public class InsuranceYichangParser {

	@Autowired
	private TracerLog tracer;

	// 社保用户信息 101
	public InsuranceYichangUserInfo getuserinfo(String taskId, String html) {
		try {
			Document doc = Jsoup.parse(html);
			String dw_name = doc.getElementsByTag("aab004").text();
			String idcard = doc.getElementsByTag("aac002").text();
			String gr_num = doc.getElementsByTag("aac001").text();
			String name = doc.getElementsByTag("aac003").text();
			String sex = doc.getElementsByTag("aac004").text();
			String birthday = doc.getElementsByTag("aac006").text();
			String state = doc.getElementsByTag("aac008").text();
			String insurance_num = doc.getElementsByTag("akc020").text();
			String medical_category = doc.getElementsByTag("aac008").text();

			InsuranceYichangUserInfo insuranceYichangUserInfo = new InsuranceYichangUserInfo(taskId, dw_name, idcard,
					gr_num, name, sex, birthday, state, insurance_num, medical_category);
			return insuranceYichangUserInfo;
		} catch (Exception e) {
			tracer.addTag("insuranceYichangService.getuserinfo.error", e.getMessage());
		}
		return null;
	}

	// 社保流水 102
	public List<InsuranceYichangGeneral> getInsuranceGeneral(String html, String taskId) {

		List<InsuranceYichangGeneral> list = new ArrayList<InsuranceYichangGeneral>();
		try {
			html = html.toLowerCase();
			Document doc = Jsoup.parse(html);
			Elements ds = doc.getElementsByTag("ds");
			for (Element element : ds) {
				String period_of_payment = element.getElementsByTag("AAE003").text();
				String insurance_type = element.getElementsByTag("AAE140").text();
				String pay_num = element.getElementsByTag("AAC150").text();
				String payable = element.getElementsByTag("AAC123").text();
				String money = element.getElementsByTag("AAE210").text();
				String payment_type = element.getElementsByTag("AAE143").text();
				String pay_mark = element.getElementsByTag("AAE114").text();
				InsuranceYichangGeneral insuranceYichangGeneral = new InsuranceYichangGeneral(taskId, period_of_payment,
						insurance_type, pay_num, payable, money, payment_type, pay_mark);
				list.add(insuranceYichangGeneral);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("insuranceYichangService.getInsuranceGeneral.error", e.getMessage());
		}
		return null;
	}

}
