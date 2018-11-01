package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.sichuan.InsuranceSZSichuanInformation;

import app.commontracerlog.TracerLog;

@Component
public class SZSichuanInsuranceParser {
	
	@Autowired
	private TracerLog tracer;	

	/**
	 * 解析参保信息
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public List<InsuranceSZSichuanInformation> parserMessage(String html, TaskInsurance taskInsurance) {
		List<InsuranceSZSichuanInformation> list = new ArrayList<InsuranceSZSichuanInformation>();
		
		Pattern r = Pattern.compile("list\\\":[^]]*");
		Matcher m = r.matcher(html);
		if(m.find()){
			tracer.addTag("需要提取的一段信息： ",m.group(0));			
			String json = m.group(0).replace("list\":[", "");
			tracer.addTag("处理字符格式后，转成json ：",json);
			
			Gson gson = new Gson();
			InsuranceSZSichuanInformation[] jsonBeans = gson.fromJson("["+json+"]", InsuranceSZSichuanInformation[].class);
			for(InsuranceSZSichuanInformation jsonBean : jsonBeans){
				list.add(jsonBean);
			}
		}else{
			tracer.addTag("没有参保信息", "null");
			return null;
		}
		return list;
	}

}
