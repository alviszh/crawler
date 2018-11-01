package test;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\5555.html"), "UTF-8");
		Document doc = Jsoup.parse(html);
		Element  tt=doc.getElementById("zjhm");
		String idNum=tt.attr("value");
		System.out.println(idNum);
		JSONObject list1ArrayObjs = JSONObject.fromObject(html);
		String codeStr = list1ArrayObjs.getString("success");
		if ("true".endsWith(codeStr)) {
			String listStr = list1ArrayObjs.getString("grmxResult");
			JSONObject dateObjs = JSONObject.fromObject(listStr);
			String dataStr = dateObjs.getString("datalist");
			JSONArray listArray = JSONArray.fromObject(dataStr);
			for (int i = 0; i < listArray.size(); i++) {
				JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));				
				String zy = listArrayObjs.getString("zy");
				System.out.println(zy);
			}
		}
	}

	private static String regexStr(String  content){
		String text="";
		String regex = "(?<=\\().*(?=\\))";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
        	text=m.group();
        }
        return text;
	}
}
