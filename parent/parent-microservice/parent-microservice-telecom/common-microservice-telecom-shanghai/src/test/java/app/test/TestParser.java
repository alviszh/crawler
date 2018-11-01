package app.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiMsg;

import app.bean.DataBean;
import app.bean.MsgBean;

public class TestParser {
	
	public static void main(String[] args) {
		String text = readTxtFile("D:\\img\\qwe.txt");
		parser(text);
	}
	
	private static void parser(String text) {
		Gson gson = new Gson();
		Type userListType = new TypeToken<DataBean<MsgBean>>(){}.getType();
		DataBean<MsgBean> data = gson.fromJson(text, userListType);
		List<TelecomShanghaiMsg> list = data.getRESULT().getPagedResult();
		if(null != list && list.size()>0){
			for(int i=1;i<list.size();i++){
				System.out.println(list.get(i).toString());
			}
		}
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static String readTxtFile(String filePath) {
		 String text = "";
	     try {
	         File file = new File(filePath);
	         if (file.isFile() && file.exists()) {
	             InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
	             BufferedReader br = new BufferedReader(isr);
	             String lineTxt;
				while ((lineTxt = br.readLine()) != null) {
					text = text+lineTxt;
	             }
	             br.close();
	         } else {
	             System.out.println("文件不存在!");
	         }
	     } catch (Exception e) {
	         System.out.println("文件读取错误!");
	     }
		return text;

	    }

}
