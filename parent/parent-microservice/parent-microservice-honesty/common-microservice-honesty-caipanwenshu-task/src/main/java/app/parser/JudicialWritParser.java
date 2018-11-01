package app.parser;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritList;
import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritTask;
import app.bean.HonestyJsonBean;
import app.service.JudicialWritStorageService;
import net.sf.json.JSONObject;

@Component
public class JudicialWritParser {

	@Autowired
	private JudicialWritStorageService judicialWritStorageService;
	public JudicialWritList getCrawler(JSONObject jsonObject, HonestyJsonBean honestyJsonBean, String runEval) throws Exception {
		JudicialWritList judicialWritList = null;
		String string = null;
		if(jsonObject.has("裁判要旨段原文")){
			string = jsonObject.getString("裁判要旨段原文");//裁判要旨段原文
		}

		String string2 = null;
		if(jsonObject.has("不公开理由")){
			string2 = jsonObject.getString("不公开理由");//不公开理由
		}

		String string3 = null;
		if(jsonObject.has("案件类型")){
			string3 = jsonObject.getString("案件类型");//案件类型
		}

		String string4 = null;
		if(jsonObject.has("裁判日期")){
			string4 = jsonObject.getString("裁判日期");//裁判日期
		}

		String docId = null;
		if(jsonObject.has("文书ID")){
			String string6 = jsonObject.getString("文书ID");//文书ID
			docId = encryptedPhone("20180822.js","Navi",string6,runEval);
		}


		String string7 = null;
		if(jsonObject.has("审判程序")){
			string7 = jsonObject.getString("审判程序");//审判程序
		}

		String string8 = null;
		if(jsonObject.has("案号")){
			string8 = jsonObject.getString("案号");//案号
		}

		String string9 = null;
		if(jsonObject.has("法院名称")){
			string9 = jsonObject.getString("法院名称");//法院名称
		}

		if(docId!=null){
			String writid = judicialWritStorageService.findByWritid(docId);
			if(StringUtils.isNotBlank(writid)){

				System.out.println("数据重复！"+writid);

			}else{
				judicialWritList = new JudicialWritList(honestyJsonBean.getTaskid(),
						string,string2,string3,string4,"",docId,string7,string8,string9,"");
			}
		}
		return judicialWritList;
	}
	public static String encryptedPhone(String jsname,String Navi,String writid, String runEval) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource(jsname, Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction(Navi,writid,runEval);
		return data.toString(); 
	}
	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}
}
