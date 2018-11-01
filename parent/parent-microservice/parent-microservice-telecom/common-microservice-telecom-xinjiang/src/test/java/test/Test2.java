package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangAddvalueItem;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Test2 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\222.html"),"UTF-8");
		if (null != html && html.contains("result")) {
			JSONArray jsonArray = JSONArray.fromObject(html);  
			JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(0));
			String result=list1ArrayObjs.getString("result");
			if ("1".equals(result)) {
				String list1InfoStr=list1ArrayObjs.getString("list1");
				String listInfoStr=list1ArrayObjs.getString("list");
				JSONArray list1InfoArray = JSONArray.fromObject(list1InfoStr);  
				JSONArray listInfoArray = JSONArray.fromObject(listInfoStr);  
				for(int i=0;i<list1InfoArray.size();i++){
					JSONObject listArrayObjs = JSONObject.fromObject(list1InfoArray.get(i));
					String serviceName=listArrayObjs.getString("zfmc");
					String serviceFee=listArrayObjs.getString("fylx");
					String effectiveDate=listArrayObjs.getString("sxsjj");
					String failureDate=listArrayObjs.getString("sxsj");				
					TelecomXinjiangAddvalueItem addvalueItem=new TelecomXinjiangAddvalueItem();
					addvalueItem.setServiceName(serviceName);
					addvalueItem.setServiceFee(serviceFee);
					addvalueItem.setEffectiveDate(effectiveDate);
					addvalueItem.setFailureDate(failureDate);
					addvalueItem.setType("1");
					System.out.println(addvalueItem.getServiceName());
				}
				for(int i=0;i<listInfoArray.size();i++){
					JSONObject listArrayObjs = JSONObject.fromObject(listInfoArray.get(i));
					String serviceName=listArrayObjs.getString("zfmc");
					String serviceFee=listArrayObjs.getString("fylx");
					String effectiveDate=listArrayObjs.getString("sxsjj");
					String failureDate=listArrayObjs.getString("sxsj");			
					TelecomXinjiangAddvalueItem addvalueItem=new TelecomXinjiangAddvalueItem();
					addvalueItem.setServiceName(serviceName);
					addvalueItem.setServiceFee(serviceFee);
					addvalueItem.setEffectiveDate(effectiveDate);
					addvalueItem.setFailureDate(failureDate);
					addvalueItem.setType("0");
				
				}
			}
		}
 	}

}
