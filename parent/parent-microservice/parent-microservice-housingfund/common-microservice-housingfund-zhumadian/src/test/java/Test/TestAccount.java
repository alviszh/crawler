package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.microservice.dao.entity.crawler.housing.zhumadian.HousingFundZhuMaDianAccount;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestAccount {
	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/zmd.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    txt += lineTxt;
                }
               // System.out.println(txt);
                read.close();
            }else{
            	System.out.println("找不到指定的文件");
            }
		}catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		txt = txt.substring(4);
		System.out.println(txt);
		JSONObject fromObject = JSONObject.fromObject(txt);
		System.out.println(fromObject);
		HousingFundZhuMaDianAccount h = null;
		List<HousingFundZhuMaDianAccount> list = new ArrayList<HousingFundZhuMaDianAccount>();
		String string = fromObject.getString("results");
		System.out.println(string);
		JSONArray fromObject2 = JSONArray.fromObject(string);
		int a =0;
		for (int i = 0; i < fromObject2.size(); i++) {
			h = new HousingFundZhuMaDianAccount();
			String string2 = fromObject2.getString(i);
			JSONObject fromObject3 = JSONObject.fromObject(string2);
			h.setAccountNum(fromObject3.getString("ywlsh"));
			h.setDatea(fromObject3.getString("rq"));
			h.setGetMoney(fromObject3.getString("jfje"));
			h.setSetMoney(fromObject3.getString("dfje"));
			h.setFee(fromObject3.getString("ye"));
			h.setDescr(fromObject3.getString("zy"));
			list.add(h);
			a++;
		}
		
		System.out.println(list+"--"+a);
	}
}
